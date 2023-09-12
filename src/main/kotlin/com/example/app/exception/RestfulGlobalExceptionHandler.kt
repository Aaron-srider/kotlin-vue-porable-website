package com.example.app.exception

import com.example.app.utils.ClassUtils
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException
import javax.validation.Path
import kotlin.collections.set


interface ServerResultProducer {
    fun toResponse(code: Int, data: Any?): ResponseEntity<Any?>
}

@Component
class TraditionalHttp : ServerResultProducer {

    @Autowired
    lateinit var objectMapper: ObjectMapper
    override fun toResponse(code: Int, data: Any?): ResponseEntity<Any?> {
        return ResponseEntity.status(code).contentType(MediaType.APPLICATION_JSON)
            .body(objectMapper.writeValueAsString(data))
    }
}

@Component
class Always200 : ServerResultProducer {

    @Autowired
    lateinit var objectMapper: ObjectMapper
    override fun toResponse(code: Int, data: Any?): ResponseEntity<Any?> {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(
            objectMapper.writeValueAsString(object {
                val code = code
                val data = data
            })
        )
    }
}

// Define error codes
enum class ErrorCode(val code: Int) {
    SUCCESS(HttpStatus.OK.value()),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value()),
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE.value()),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value())
}

// Custom exception for the backend
class BackendException(code: Int, data: Any?, msg: String?, cause: Throwable?) : RuntimeException(msg, cause) {
    var code: Int;
    var data: Any?;

    init {
        this.code = code
        this.data = data
    }
}


/**
 * This class centralizes exception handling. The behavior of the handler methods in this class is described as follows:
 *
 * - Log the error, if necessary, record them into storage. The information of the log should include at least:
 *   - Timestamp
 *   - Comprehensive name
 *   - Class and method
 *   - Message
 *   - Stacktrace
 * - Get the error information from the exception object and build the HTTP response.
 */
@ControllerAdvice
class RestfulGlobalExceptionHandler {

    private val log = KotlinLogging.logger {}


    @Autowired
    @Qualifier("traditionalHttp")
    lateinit var serverResultProducer: ServerResultProducer

    @Value("\${spring.servlet.multipart.max-file-size}")
    private lateinit var uploadLimit: String

    private fun doReturnResponse(code: Int, data: Any?, ex: Throwable): ResponseEntity<Any?> {
        log.error("ERROR", ex)
        return serverResultProducer.toResponse(code, data)
    }

    @ResponseBody
    @ExceptionHandler(value = [Throwable::class])
    fun otherException(req: HttpServletResponse?, ex: Throwable): ResponseEntity<Any?> {
        if (ex is HttpMessageNotReadableException) {
            return doReturnResponse(
                ErrorCode.BAD_REQUEST.code,
                "Request body format error: " + ex.message,
                ex
            )
        }
        if (ex is MissingServletRequestParameterException || ex is MissingServletRequestPartException) {
            return doReturnResponse(
                ErrorCode.BAD_REQUEST.code,
                "Required request body is missing",
                ex
            )
        }
        if (ex is MaxUploadSizeExceededException) {
            return doReturnResponse(
                ErrorCode.BAD_REQUEST.code,
                "Upload file too large, Limitation: $uploadLimit",
                ex
            )
        }
        return doReturnResponse(
            ErrorCode.INTERNAL_SERVER_ERROR.code,
            "Unknown Error",
            ex
        )
    }


    // Error code handler
    @ResponseBody
    @ExceptionHandler(value = [BackendException::class])
    fun backendException(req: HttpServletResponse?, ex: BackendException): ResponseEntity<Any?> {
        return doReturnResponse(ex.code, ex.data, ex)
    }

    // This handler processes validation errors, collecting invalid parameters and redirecting to backendException
    @ResponseBody
    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun constraintViolationException(ex: ConstraintViolationException): ResponseEntity<Any?> {
        val maps = buildInvalidParameterReport(ex)
        return doReturnResponse(ErrorCode.BAD_REQUEST.code, maps, ex)
    }

    // Handler for parameter validation errors
    @ResponseBody
    @ExceptionHandler(value = [BindException::class, MethodArgumentNotValidException::class])
    fun paramValidateException(ex: Exception): ResponseEntity<Any?> {
        val bindingResult = ClassUtils.getFieldValue(ex, "bindingResult", BindingResult::class.java)
        val maps = buildInvalidParameterReport(bindingResult)
        return doReturnResponse(ErrorCode.BAD_REQUEST.code, maps, ex)
    }

    // region: private
    private fun buildInvalidParameterReport(bindingResult: BindingResult): List<Map<String, String>> {
        val list = ArrayList<Map<String, String>>()
        for (objectError in bindingResult.allErrors) {
            val fieldError = objectError as? FieldError
            val errorParameterReport = HashMap<String, String>()
            errorParameterReport["name"] = fieldError?.field ?: ""
            errorParameterReport["reason"] = fieldError?.defaultMessage ?: ""
            list.add(errorParameterReport)
        }
        return list
    }

    private fun buildInvalidParameterReport(ex: ConstraintViolationException): List<Map<String, String>> {
        val list = ArrayList<Map<String, String>>()
        val constraintViolations = ex.constraintViolations
        if (constraintViolations == null) {
            return list
        }
        for (constraintViolation in constraintViolations) {
            val errorParameterReport = HashMap<String, String>()
            val propertyPath = constraintViolation.propertyPath
            errorParameterReport["name"] = getLastPathNode(propertyPath)
            errorParameterReport["reason"] = constraintViolation.message
            list.add(errorParameterReport)
        }
        return list
    }

    private fun getLastPathNode(path: Path): String {
        val wholePath = path.toString()
        val i = wholePath.lastIndexOf(".")
        return if (i != -1) {
            wholePath.substring(i + 1)
        } else {
            wholePath
        }
    }
    // endregion


}


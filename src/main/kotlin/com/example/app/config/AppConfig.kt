package com.example.app.config

import com.example.app.exception.Always200
import com.example.app.exception.ServerResultProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.ErrorPageRegistrar
import org.springframework.boot.web.server.ErrorPageRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.*


class AppConfig {
}


@Configuration
@ConfigurationProperties(prefix = "response")
class CustomProperties {
    var mode: String = "traditionalHttp"
}

@Configuration
class BeanFactory{

    @Autowired
    lateinit var customProperties: CustomProperties

    @Bean
    fun serverResultProducer(): ServerResultProducer {
        println(customProperties.mode)
        return Always200()
    }
}

@Configuration
class RequestCorsFilter {
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.setAllowedOriginPatterns(
            listOf(
                "http://*",
                "http://*.*.*:[*]",
                "http://*.*:[*]",
                "http://*:[*]",
                "https://*.*.*:[*]",
                "https://*.*:[*]",
                "https://*:[*]",
            )
        );
        config.allowedHeaders = Arrays.asList("Origin", "Content-Type", "Accept", "responseType", "Authorization")
        config.allowedMethods = Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}

@Component
class StaticServerErrorPageConfig : ErrorPageRegistrar {
    override fun registerErrorPages(registry: ErrorPageRegistry) {
        val error404Page = ErrorPage(HttpStatus.NOT_FOUND, "/index.html")
        registry.addErrorPages(error404Page)
    }
}

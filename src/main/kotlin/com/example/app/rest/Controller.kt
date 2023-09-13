package com.example.app.rest

import com.example.app.dao.RecordPO
import com.example.app.dao.UserDao
import com.example.app.dao.UserPO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.io.Serializable
import java.math.BigDecimal
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


@RestController
@RequestMapping("/api")
@Validated
class UserController {

    @Autowired
    lateinit var userDao:UserDao;

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Serializable): UserPO? {
        return userDao.getUser(id)
    }

    @GetMapping("/users")
    fun getUsers(): MutableList<UserPO> {
        return userDao.getUsers()
    }

    @GetMapping("/users/{userId}/records")
    fun getUserRecords(@PathVariable userId: java.io.Serializable): MutableList<RecordPO> {
        return userDao.getUserRecords(userId)
    }

    @PostMapping("/users/{userId}/records")
    fun addRecord(
        @PathVariable userId: Int,
        @RequestBody recordDto: RecordDto
    ) {
        userDao.addRecord(userId, recordDto.amount!!)
    }



    @DeleteMapping("/users/{userId}/records/{recordId}")
    fun removeRecord(
        @PathVariable userId: Int,
        @PathVariable recordId: Int
    ) {
        userDao.removeRecord(userId, recordId)
    }

    @PutMapping("/users/{userId}/records/{recordId}")
    fun updateRecord(
        @PathVariable userId: Int,
        @PathVariable recordId: Int,
        @RequestBody recordDto: RecordDto
    ) {
        userDao.updateRecord(userId, recordId, recordDto.amount!!)
    }



}
class RecordDto {
    @NotNull
    var amount: BigDecimal? = null
}
class Dto  {
    @NotEmpty
    var name: String? = null

    @Max(100) @Min(0)
    var age: Int? = null
}
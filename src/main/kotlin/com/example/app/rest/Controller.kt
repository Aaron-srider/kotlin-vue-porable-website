package com.example.app.rest

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty

@RestController
@Validated
class Controller {

    @GetMapping("/test")
    fun test(): String {
        var i = 10 /0
        return "test"
    }

    @PostMapping("/test2")
    fun test2(@RequestBody @Valid dto: Dto): String {
        return "test2"
    }
}

class Dto  {
    @NotEmpty
    var name: String? = null

    @Max(100) @Min(0)
    var age: Int? = null
}
package com.example.app

import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}

@Component
class TestRunner: CommandLineRunner {


	private val log = KotlinLogging.logger {}

	override fun run(vararg args: String?) {
		// var i = 10;
		// for (i1 in 0..i) {
		// 	userDao.save(UserPO(
		// 		id = null,
		// 		username = "test${i1}",
		// 		password = "test"
		// 	))
		// }
		// println(userDao.list())
	}

}
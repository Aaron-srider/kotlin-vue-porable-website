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
		log.info { "write some here" }
	}

}
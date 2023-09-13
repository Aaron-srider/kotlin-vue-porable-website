package com.example.app.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

enum class Season { SPRING, SUMMER, AUTUMN, WINTER }

enum class Month { JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER }

class DateTime(private val date: Date) {

    companion object {
        fun now(): DateTime {
            return DateTime(Date())
        }

        fun of(date: Date): DateTime {
            return DateTime(date)
        }

        fun parse(dateTimeStr: String, pattern: String): DateTime {
            val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
            try {
                val parsedDate = sdf.parse(dateTimeStr)
                return DateTime(parsedDate)
            } catch (e: ParseException) {
                throw IllegalArgumentException("Invalid date-time format: $dateTimeStr")
            }
        }
    }

    fun year(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.YEAR)
    }

    fun seasonEnum(): Season {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val month = calendar.get(Calendar.MONTH)
        return when (month) {
            Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH -> Season.SPRING
            Calendar.APRIL, Calendar.MAY, Calendar.JUNE -> Season.SUMMER
            Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER -> Season.AUTUMN
            else -> Season.WINTER
        }
    }

    fun monthEnum(): Month {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val month = calendar.get(Calendar.MONTH)
        return Month.values()[month]
    }

    fun dayOfMonth(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    override fun toString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return sdf.format(date)
    }
}


fun main() {
    val date = Date()

    // Creating a DateTime object using 'new' constructor
    val time = DateTime(date)
    println(time)

    // Creating a DateTime object using 'of' method
    val now = DateTime.now()
    val dt = DateTime.of(date)
    println(now)
    println(dt)

    // Creating a DateTime object by parsing a date-time string
    val dateTimeStr = "2017-01-05 12:34:23"
    val dateTime = DateTime.parse(dateTimeStr, "yyyy-MM-dd HH:mm:ss")
    println(dateTime)

    // Retrieving various date-time components
    val year = dateTime.year()
    val season = dateTime.seasonEnum()
    val month = dateTime.monthEnum()
    val day = dateTime.dayOfMonth()

    println("Year: $year")
    println("Season: $season")
    println("Month: $month")
    println("Day of Month: $day")
}
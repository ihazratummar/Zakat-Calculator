package com.practice.zakatcalculator.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @author Hazrat Ummar Shaikh
 */

fun getDateFromLong(dateLong: Long, format: String = "dd/MM/yyyy"): String {
    val date = Date(dateLong)
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(date)

}
package com.practice.zakatcalculator.util

import android.content.Context
import com.practice.zakatcalculator.presentation.DateType
import com.practice.zakatcalculator.util.Constants.KEY_SORT_BY
import com.practice.zakatcalculator.util.Constants.PREF_NAME

object PreferenceUtil {

    fun setSortType(context: Context, sortType: DateType){
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putString(KEY_SORT_BY, sortType.name).apply()
    }

    fun getSortType(context: Context): DateType{
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val sortType = pref.getString(KEY_SORT_BY, DateType.DATE_DESC.name)
        return DateType.valueOf(sortType!!)

    }


}
package com.ccc.listadecompracriolla.Core.formatterdata

import java.text.DateFormat.getDateInstance
import java.util.Date

fun dateFormattert():String{
    val formato = getDateInstance()
    val date = Date()
    val current = formato.format(date)
    return current
}
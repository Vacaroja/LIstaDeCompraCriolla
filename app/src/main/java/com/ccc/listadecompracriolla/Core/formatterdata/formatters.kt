package com.ccc.listadecompracriolla.Core.formatterdata

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun dateFormattert(actualDate:String?): LocalDate? {
    val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    try {
        val current = LocalDate.parse(actualDate,formato)
        return current
    }catch (_: Exception){
        return null
    }

}
fun formatNumber(number: Float): String {
    // Puedes especificar la configuración regional (Locale) aquí.
    // Locale.US usa comas para miles y puntos para decimales (ej. 1,234.56).
    // Locale.getDefault() usa la configuración regional del dispositivo.
    val venezuelaLocale = Locale.Builder()
        .setLanguage("es")
        .setRegion("VE")
        .build()
    val formatter = NumberFormat.getNumberInstance(venezuelaLocale)

    // Si quisieras que el formato usara puntos para miles y comas para decimales
    // (común en muchos países de Europa y América Latina), usarías una Locale diferente,
    // por ejemplo, Locale("es", "ES") para España o Locale("es", "VE") para Venezuela.
    // val formatter = NumberFormat.getNumberInstance(Locale("es", "ES")) // Ejemplo para España
    // val formatter = NumberFormat.getNumberInstance(Locale("es", "VE")) // Ejemplo para Venezuela

    return formatter.format(number)
}

fun formatNumberText(value: String): String {

    val sanitizedText = value.replace("\\D".toRegex(), "")

    if (sanitizedText.isEmpty()) {
        return ""
    }
    val number = sanitizedText.toLongOrNull() ?: 0
    val venezuelaLocale = Locale.Builder()
        .setLanguage("es")
        .setRegion("VE")
        .build()
    val symbols = DecimalFormatSymbols(venezuelaLocale)
    val formatter = DecimalFormat("#,##0", symbols)

    return formatter.format(number)
}
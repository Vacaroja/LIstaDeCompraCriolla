package com.ccc.listadecompracriolla.Core.formatterdata

import java.math.BigDecimal
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
    formatter.maximumFractionDigits = 3

    // Si quisieras que el formato usara puntos para miles y comas para decimales
    // (común en muchos países de Europa y América Latina), usarías una Locale diferente,
    // por ejemplo, Locale("es", "ES") para España o Locale("es", "VE") para Venezuela.
    // val formatter = NumberFormat.getNumberInstance(Locale("es", "ES")) // Ejemplo para España
    // val formatter = NumberFormat.getNumberInstance(Locale("es", "VE")) // Ejemplo para Venezuela

    return formatter.format(number)
}

fun formatFloatToDecimals(value: Float): String {
    // 1. Convertir el Float a Double para tener una mejor base, luego a BigDecimal
    val bigDecimal = BigDecimal.valueOf(value.toDouble())

    // 2. Definir el patrón de formato:
    //    "#" para dígitos opcionales (elimina ceros a la izquierda)
    //    "0" para dígitos obligatorios (fuerza al menos un cero antes del punto)
    //    ".########" permite hasta 8 decimales. Los # evitan ceros innecesarios al final.
    val pattern = "0.#####"

    // 3. Configurar el formato para usar el punto (.) como separador decimal
    val symbols = DecimalFormatSymbols(Locale.ROOT)
    symbols.decimalSeparator = '.'

    val formatter = DecimalFormat(pattern, symbols)

    // 4. Aplicar el formato
    return formatter.format(bigDecimal)
}
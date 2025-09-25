package com.ccc.listadecompracriolla.Core.clientlist.drawer

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

fun sendFeedback(context : Context){
    val feedbackEmail = "infoappccc@gmail.com"
    val asunto = "Sugerencias para la app"
    val cuerpo = "Buenos dias, "

    val uriString = "mailto:$feedbackEmail?subject=${Uri.encode(asunto)}&body=${Uri.encode(cuerpo)}"

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = uriString.toUri()
    }
        try {
            context.startActivity(intent)
        }catch (_: Exception){

        }

}
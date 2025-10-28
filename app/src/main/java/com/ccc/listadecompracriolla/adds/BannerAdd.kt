package com.ccc.listadecompracriolla.adds

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.ccc.listadecompracriolla.adds.Constants.AD_ID_BANNER
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun AdBanner() {
    AndroidView( factory = { context ->
        val adView = AdView(context)
        adView.setAdSize(AdSize.LARGE_BANNER)
        adView.apply {
            adUnitId = AD_ID_BANNER
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    // 2. Aquí puedes confirmar que el anuncio se cargó
                    // Por ejemplo, loggear el evento o cambiar un estado en Compose
                    Log.d("AdMob", "Banner Ad cargado con éxito.")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // 3. Aquí puedes manejar el error de carga
                    // Por ejemplo, loggear el error o mostrar un placeholder
                    val errorMessage = "Fallo al cargar el Banner Ad. Error: ${loadAdError.message}, Dominio: ${loadAdError.domain}, Código: ${loadAdError.code}"
                    Log.e("AdMob", errorMessage)
                }}
            loadAd(AdRequest.Builder().build())
        }
    })

}



package com.ccc.listadecompracriolla.adds

import android.app.Activity
import android.content.Context
import android.util.Log
import com.ccc.listadecompracriolla.adds.Constants.AD_ID_FULLSCREEN
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

private var mInterstitialAd: InterstitialAd? = null

fun loadInterstitialAd(context: Context) {
    val adRequest = AdRequest.Builder().build()

    InterstitialAd.load(
        context,
        AD_ID_FULLSCREEN, // ID de prueba para intersticial
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("AdMob", "Error al cargar anuncio: ${adError.message}")
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("AdMob", "Anuncio intersticial cargado con éxito.")
                mInterstitialAd = interstitialAd
            }


        })
}

fun showInterstitialAd(activity: Activity) {
    if (mInterstitialAd != null) {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                // El anuncio ha sido cerrado, aquí puedes navegar o hacer algo más
                Log.d("AdMob", "Anuncio cerrado.")
                mInterstitialAd = null
            }
        }
        mInterstitialAd?.show(activity)
    } else {
        Log.d("AdMob", "El anuncio no está listo.")
    }
}
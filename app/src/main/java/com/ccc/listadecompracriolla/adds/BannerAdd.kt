package com.ccc.listadecompracriolla.adds

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.ccc.listadecompracriolla.adds.Constants.AD_ID_BANNER
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdBanner( modifier: Modifier = Modifier) {
    AndroidView( factory = { context ->
        val adView = AdView(context)
        adView.setAdSize(AdSize.LARGE_BANNER)
        adView.apply {
            adUnitId = AD_ID_BANNER
            loadAd(AdRequest.Builder().build())
        }
    })

}



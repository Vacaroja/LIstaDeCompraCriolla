package com.ccc.listadecompracriolla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.adds.loadInterstitialAd
import com.ccc.listadecompracriolla.ui.theme.ListaDeCompraCriollaTheme
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this){}

        loadInterstitialAd(this)
        enableEdgeToEdge()
        setContent {
            ListaDeCompraCriollaTheme {
                val viewModel: ProductViewModel by viewModels()
                viewModel.searchDolarBcv()
                NavegationControl(viewModel)

            }
        }
    }
}

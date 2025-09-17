package com.ccc.listadecompracriolla.Core.clases

import android.content.Context
import android.os.Build
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class VersionManager(private val context: Context) {
    private val remoteConfig = Firebase.remoteConfig

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf("forced_app_version" to 1))
    }

    fun checkForUpdates(onUpdateNeeded:() -> Unit){
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val forcedVersion = remoteConfig.getLong("forced_app_version")
                val currentVersionCode = try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                        context.packageManager.getPackageInfo(context.packageName,0).longVersionCode
                    }else{
                        @Suppress("DEPRECATION")
                        context.packageManager.getPackageInfo(context.packageName,0).versionCode.toLong()
                    }
                }catch (_:Exception){
                    1L
                }
                if (currentVersionCode < forcedVersion) {
                    onUpdateNeeded()
                }

            }

        }
    }
}
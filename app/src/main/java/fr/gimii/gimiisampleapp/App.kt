package fr.gimii.gimiisampleapp

import android.app.Application
import com.google.android.gms.ads.MobileAds
import io.didomi.sdk.Didomi
import io.didomi.sdk.DidomiInitializeParameters

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        val parameters = DidomiInitializeParameters(
            apiKey = "API_KEY",
            noticeId = "NOTICE_ID"
        )

        MobileAds.initialize(this)

        Didomi.getInstance().initialize(this, parameters)
    }
}
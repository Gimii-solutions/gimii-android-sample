package fr.gimii.gimiisampleapp

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import fr.gimii.GimiiManager
import fr.gimii.gimiisampleapp.ui.theme.GimiiSampleAppTheme
import io.didomi.sdk.Didomi
import io.didomi.sdk.events.EventListener
import io.didomi.sdk.events.NoticeClickDisagreeEvent

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        Didomi.getInstance().setupUI(this)

        val gimiiManager = GimiiManager.getInstance()

        // Gimii is executed when the user refuse the CMP
        Didomi.getInstance().addEventListener(object : EventListener() {
            override fun noticeClickDisagree(event: NoticeClickDisagreeEvent) {
                gimiiManager.execute("RAISER_ID", this@MainActivity)
            }
        })

        var adRequest = AdManagerAdRequest
            .Builder()

        setContent {
            GimiiSampleAppTheme {
                Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                    Image(
                        painter = painterResource(id = R.drawable.lbc),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                    AndroidViewAdView(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        adRequest = gimiiManager.applyAdTargeting(adRequest).build()
                    )
                }
            }
        }
    }
}


@Composable
fun AndroidViewAdView(modifier: Modifier, adRequest: AdRequest) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            AdView(context).apply {
                setAdSize(com.google.android.gms.ads.AdSize.BANNER)
                adUnitId = "/6499/example/banner"
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                loadAd(adRequest)
            }
        }
    )
}


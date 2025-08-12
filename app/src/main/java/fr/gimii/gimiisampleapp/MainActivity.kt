package fr.gimii.gimiisampleapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.FragmentActivity
import fr.gimii.GimiiEnvironment
import fr.gimii.GimiiManager
import fr.gimii.gimiisampleapp.ui.theme.GimiiSampleAppTheme
import io.didomi.sdk.Didomi

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        Didomi.getInstance().setupUI(this)

        val gimiiManager = GimiiManager.getInstance(environment = GimiiEnvironment.PRODUCTION)

        gimiiManager.execute("RAISER_ID", this) // TODO: rename en execute

        setContent {
            GimiiSampleAppTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.lbc),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
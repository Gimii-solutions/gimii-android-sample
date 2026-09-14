# Demo Android App for Gimii SDK


## Integrate Gimii SDK in your app

This guide explains how to add the Gimii Android SDK to your application, configure environments, enable logging, and optionally apply ad targeting to Google Ad Manager/AdMob requests.

### 1) Add the dependency
In your app module `build.gradle.kts`:

```kotlin
dependencies {
    implementation("fr.gimii:sdk:1.1.0-beta5")
}
```

Make sure your project repositories include Maven Central (usually already present):

```kotlin
// settings.gradle.kts or top-level build.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

The Gimii SDK depends on:
- Didomi SDK
- Google Mobile Ads (for ad targeting)

### 2) Replace Didomi IDs, inside the App.kt.
```kotlin
val parameters = DidomiInitializeParameters(
      apiKey = "API_KEY",
      noticeId = "NOTICE_ID"
 )
```

### 3) Add your AdMob application ID to the manifest
The SDK's manifest already declares an AdMob application ID. Override it with yours using `tools:replace`, otherwise the manifest merge fails.
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <application>
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="###########"
            tools:replace="android:value" />
    </application>
</manifest>
```



### 4) Initialize and start Gimii
Call Gimii from an `FragmentActivity` (e.g., your main Activity) after your CMP is ready:

```kotlin
import android.util.Log
import fr.gimii.GimiiEnvironment
import fr.gimii.GimiiEventListener
import fr.gimii.GimiiManager
import fr.gimii.utils.GimiiError
import fr.gimii.utils.Logger
import io.didomi.sdk.Didomi
import io.didomi.sdk.events.EventListener
import io.didomi.sdk.events.NoticeClickDisagreeEvent

// ... inside an Activity (FragmentActivity)
val gimiiManager = GimiiManager.getInstance(
    environment = GimiiEnvironment.PRODUCTION, // QA | STAGING | PRODUCTION
    logMode = Logger.Mode.INFO                 // DEBUG for verbose logs, INFO for standard
)

gimiiManager.execute(raiserId, this)

// Gimii is executed when the user refuse the CMP
Didomi.getInstance().addEventListener(object : EventListener() {
   override fun noticeClickDisagree(event: NoticeClickDisagreeEvent) {
      gimiiManager.execute(raiserId, this@MainActivity)
   }
})

// events listener

gimiiManager.setEventListener(object : GimiiEventListener {
    override fun onDisplayed() {
        Log.d("GimiiDemoApp", "Gimii onDisplayed")
    }

    override fun onAccepted() {
        Log.d("GimiiDemoApp", "Gimii onAccepted")
    }

    override fun onRefused() {
        Log.d("GimiiDemoApp", "Gimii onRefused")
    }

    override fun onError(error: GimiiError) {
        Log.d("GimiiDemoApp", "Gimii onError $error")
     }
})
```

`onError` is only called for network, configuration, consent and interaction errors. A display delay that has not elapsed yet is not reported.

### 5) Environments
Available environments:
- `GimiiEnvironment.QA` → `https://qa.api.gimii.dev` / `https://qa.static.gimii.dev/gimii-embedder.html`
- `GimiiEnvironment.STAGING` → `https://api.gimii.dev` / `https://static.gimii.dev/gimii-embedder.html`
- `GimiiEnvironment.PRODUCTION` → `https://api.gimii.fr` / `https://static.gimii.fr/gimii-embedder.html`

Select the environment when calling `GimiiManager.getInstance(...)`.

### 6) Logging
`Logger.Mode` options:
- `Logger.Mode.DEBUG` → prints DEBUG, INFO, ERROR, CRITICAL
- `Logger.Mode.INFO` → prints INFO, ERROR, CRITICAL
- `null` → disables all logs

Set it via `GimiiManager.getInstance(logMode = Logger.Mode.DEBUG)` during initialization.

### 7) Ad targeting (optional)
If you use Google Ad Manager/AdMob, you can apply Gimii custom targeting to an `AdManagerAdRequest.Builder`:

```kotlin
import com.google.android.gms.ads.admanager.AdManagerAdRequest

val builder = AdManagerAdRequest.Builder()
val targetedBuilder = gimiiManager.applyAdTargeting(builder)
val adRequest = targetedBuilder.build()
```

When an association has been selected, the SDK will add the following custom targeting:
- `gimii` → your `raiserId`
- `gimii-asso` → selected association id
- `gimii-cr` → composite key `${raiserId}-${associationId}`

If no association is available yet, no tags are applied.

### 8) Permissions
Add the following to your app `AndroidManifest.xml` if not already present:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### 9) R8 / ProGuard
The SDK does not ship keep rules yet. On a minified build, add to your `proguard-rules.pro`:

```proguard
-keep class fr.gimii.** { *; }
-keep class androidx.work.** { *; }
```

### 10) Troubleshooting
- Set `logMode = Logger.Mode.DEBUG` to get detailed logs in Logcat with tag `Gimii`
- Verify `raiserId` is correct
- Ensure Didomi is initialized and consent is accessible
- Confirm network access and environment selection

---

That's it! Gimii will handle the display and logic.

## License

© 2025 Gimii. All rights reserved.

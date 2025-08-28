![Demo Screenshot](https://github.com/Gimii-solutions/gimii-ios-demo/blob/bdefa8240d38efb1aac2bd8ca5ab1a6196771d59/demo.png)

# Demo Android App for Gimii SDK

This is a simple demo application showcasing the integration of the **Gimii iOS SDK**.

It demonstrates:
- A full-screen **transparent WebView** displaying the Gimii consent raiser pop-in.
- The WebView allows touch passthrough: users can interact with underlying UI elements like a visible **UISwitch**, proving transparency works.
- Integration of **Didomi v1.87.0** to handle GDPR consent.
- Integration of **Google Mobile Ads SDK v11.10.0**, with a demo **GAM banner ad**.
- A static image mimicking the LeBonCoin interface to simulate a real-world use case.

---



# Gimii SDK for Android

Gimii is an Android library that provides a consent-raiser pop-in for Android applications. This pop-in encourages users who initially refuse cookie consent to reconsider their decision in exchange for supporting a chosen charity.

## Features

- Displays a consent pop-in with user interaction
- Interoperates with Didomi for consent tracking
- Supports Google Ads custom targeting
- Easy integration into any Android app with minimal setup

### Environments

By default, the SDK uses the **production** environment.

If you wish to run against the **dev** or **staging** or **qa** environments for testing, you can pass an optional `environment` parameter to the `execute` function:

```swift
GimiiManager.getInstance(environment = GimiiEnvironment.PRODUCTION)
```

## Installation

1. Add the Gimii package to your project:
 ```gradle
implementation("fr.gimii:sdk:1.1.0-beta1")
```

2. Import the module in your code:
```kotlin
import fr.gimii.GimiiManager
```

3. Replace Didomi IDs, inside the App.kt.
```kotlin
val parameters = DidomiInitializeParameters(
      apiKey = "API_KEY",
      noticeId = "NOTICE_ID"
 )
```

4. If using Google Ads, add your application id inside the manifest.
```kotlin
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="###########"/>
```

## Usage

### MainActivity

The library need a `AppCompactActivity` or `ActivityFragment` in order to display the Gimii dialog.
You can start the execution of the Gimii SDK as follow : 

```kotlin
Didomi.getInstance().addEventListener(object : EventListener() {
      override fun noticeClickDisagree(event: NoticeClickDisagreeEvent) {
           val gimiiManager = GimiiManager.getInstance(environment = GimiiEnvironment.PRODUCTION)
           gimiiManager.execute("RAISER_ID", this@MainActivity)
      }
})
```

Replace the RAISER_ID with the one provided by the Gimii Team.

### Tagging Google Ads (GAMRequest)

If your app uses Google Ads (AdMob or GAM), you must apply Gimii's targeting tags to your `AdManagerAdRequest`:

```kotlin
var adRequest = AdManagerAdRequest.Builder()
adRequest = GimiiManager.getInstance().applyAdTargeting(adRequest)
bannerView.loadAd(adRequest.build())
```

This will automatically include the following targeting keys in your ad requests:

- `"gimii"`: The current raiser ID
- `"gimii-asso"`: The selected association ID
- `"gimii-cr"`: The concatenation between current raiser ID and selected association ID

These values are added to both `addCustomTargeting` and registered via `addNetworkExtrasBundle`, ensuring they are included in all ad requests.

That's it! Gimii will handle the display and logic.

## License

© 2025 Gimii. All rights reserved.

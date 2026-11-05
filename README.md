# Gimii Android sample

A native Android application demonstrating Didomi, the Gimii pop-in, and a
Google banner. For production integration and the current API, use the
[Android SDK guide](https://github.com/Gimii-solutions/gimii-android/blob/main/gimii-android/README.md).

## Dependency source and requirements

The [app build file](app/build.gradle.kts) pins the published artifact
`fr.gimii:sdk:1.1.0-beta6`. It also declares Didomi/Google dependencies. This is a
consumer pin, not a statement of the latest available release or a connection
to a sibling source checkout.

Use JDK 17, the included Gradle wrapper, Android platform 35, and a device or
emulator running API 26+. Configure `ANDROID_HOME` or an ignored local SDK path.
If an artifact cannot be resolved, check the pin against the intended available
release instead of guessing a newer version.

For unreleased native SDK changes, use the SDK repository's demo and
[development tests](https://github.com/Gimii-solutions/gimii-android/blob/main/README.md).

## Configure and run

1. Open this repository in Android Studio and sync Gradle.
2. Supply matching staging configuration in the files below.
3. Run `app` on an emulator/device, answer the Didomi notice, and inspect Gimii's
   eligibility flow. The sample executes once initially and again after refusal.

| File | Configuration |
| --- | --- |
| [App.kt](app/src/main/java/fr/gimii/gimiisampleapp/App.kt) | Didomi `API_KEY` and `NOTICE_ID`; application initialization |
| [MainActivity.kt](app/src/main/java/fr/gimii/gimiisampleapp/MainActivity.kt) | `RAISER_ID`, environment, and banner integration |
| [AndroidManifest.xml](app/src/main/AndroidManifest.xml) | Google's test application ID is included; use your own for production |

An application ID is not a banner ad unit ID. Keep test ad units during
integration and do not commit real configuration.

From this repository root:

```sh
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

Installation needs a booted emulator/device. Build success does not exercise a
real Didomi notice, customer configuration, or ad attribution.

## Environments

| Kotlin value | API origin | Static origin |
| --- | --- | --- |
| `DEV` | `https://api.gimii.dev` | `https://static.gimii.dev` |
| `STAGING` | `https://api.staging.gimii.fr` | `https://static.staging.gimii.fr` |
| `PRODUCTION` | `https://api.gimii.fr` | `https://static.gimii.fr` |

The sample selects staging. These are the current source SDK mappings; verify
that the published artifact selected by the sample includes the required
embedder/environment changes.

## What to verify

- After refusal, display requires enabled services, valid configuration, and
  elapsed capping. First-notice presentation alone does not prove eligibility.
- Full consent collected directly by the CMP bypasses the initial pop-in.
- After charity persistence, rebuild each Google Ad Manager request through the
  same configured manager's `applyAdTargeting` method. Verify `gimii`,
  `gimii-asso`, and `gimii-cr`; an existing request is not updated automatically.
- Verify thank-you/reminder modes, privacy preferences, dismissal, and touch
  pass-through on the intended device layout.
- Test a minified release with host/vendor ProGuard requirements before shipping.

Use `setEventListener` before execution when adapting the sample. SDK errors
selected for callbacks differ from expected capping/disabled states: inspect
`currentError` and native logs as described in the SDK guide. Keep the manager
for the activity's flow and remove host-owned CMP listeners when appropriate.

## Troubleshooting

| Problem | Check |
| --- | --- |
| No pop-in | Collected CMP answer, matching raiser/environment, service flags, and capping |
| Ad app ID manifest conflict | Supply your own value; older SDK artifacts may require `tools:replace="android:value"` |
| Missing attribution | New requests must be targeted after charity persistence |
| Release-only failure | Host/vendor keep rules and real-device release validation |
| Source change has no effect | This sample uses a published Maven artifact; choose the intended source consumer for unreleased work |

Use a dedicated test app install when clearing data to test fresh consent and
capping. Select production values and disable debug logs for a production host.

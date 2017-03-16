# AndroidTampering
[![Release](https://jitpack.io/v/SandroMachado/AndroidTampering.svg)](https://jitpack.io/#SandroMachado/AndroidTampering)

AndroidTampering is a library that provides an extra layer of security to your Android application. This library protects your application against simple tampering attacks. **Please note that this protection methods can also be hacked.** So, besides the tampering protection, don't forget to add all the other security recommendations like [ProGuard](http://developer.android.com/tools/help/proguard.html), [patching the Security Provider with ProviderInstaller](http://developer.android.com/training/articles/security-gms-provider.html#patching)...

Check this [talk](https://youtu.be/18tn_mF4XRg) from Scott Alexander-Bown to learn more about android security for applications.

# Gradle Dependency

## Repository

First, add the following to your app's `build.gradle` file:

```Gradle
repositories {
    maven { url "https://jitpack.io" }
}
```

Them include the openalpr-android dependency:

```gradle
dependencies {

    // ... other dependencies here.    	
    compile 'com.github.SandroMachado:AndroidTampering:1.0.3'

}
```

# Usage

## Code

### Certificate signature

Get your certificate signature:

```Java
// This method will print your certificate signature to the logcat.
AndroidTamperingProtectionUtils.getCertificateSignature(context);
```

Check your logcat for something like:
```
01-12 01:16:15.965 32487-32487/com.sandro.test D/TAMPERING_PROTECTION:**yweraaaaaaaaggggDfsa6egkjjI=
**
```
Please note that this certificate signature contains a `\n` at the end.

### Add tampering protection

```Java
AndroidTamperingProtection androidTamperingProtection = new AndroidTamperingProtection.Builder(context, "yweraaaaaaaaggggDfsa6egkjjI\n")
    .installOnlyFromPlayStore(true) // By default is set to false.
    .build();

if (!androidTamperingProtection.validate()) {
    Toast.makeText(this, "The application is compromised! Contact the application provider.", Toast.LENGTH_LONG).show();

    finish();
}
```

# Applications that use AndroidTampering

* [Aerial](https://play.google.com/store/apps/details?id=com.sandro.aerial)

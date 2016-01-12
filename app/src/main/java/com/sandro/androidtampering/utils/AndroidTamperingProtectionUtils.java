package com.sandro.androidtampering.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;

/**
 * A set of utils for the Android Tampering Protection library.
 */

public class AndroidTamperingProtectionUtils {

    /**
     * Prints your current certificate signature to the Logcat. Use this method to obtain your certificate signature.
     *
     * @param context The application context.
     */

    public static void getCertificateSignature(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            // The APK is signed with multiple signatures, probably it was tampered.
            if (packageInfo.signatures.length > 1) {
                return ;
            }

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                Log.d("TAMPERING_PROTECTION", "\n\n___________\n" +  Base64.encodeToString(md.digest(), Base64.DEFAULT) + "\n___________\n\n");
            }
        } catch (Exception exception) {
            Log.d("TAMPERING_PROTECTION", exception.getStackTrace().toString());
        }
    }
}

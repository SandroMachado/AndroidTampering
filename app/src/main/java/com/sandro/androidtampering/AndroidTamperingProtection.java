package com.sandro.androidtampering;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;

/**
 * Android Tampering Protection have the objective to help the developers to add an extra protection layer to their android applications,
 * validating the application signature and the installer.
 */

public class AndroidTamperingProtection {

    private static final String PLAY_STORE_PACKAGE = "com.android.vending";

    private final Context context;
    private final Boolean playStoreOnly;
    private final String certificateSignature;

    private AndroidTamperingProtection(Builder builder) {
        this.context = builder.context;
        this.playStoreOnly = builder.playStoreOnly;
        this.certificateSignature = builder.certificateSignature;
    }

    /**
     * Validates the APK. This method should return true if the apk is not tampered.
     *
     * @return a boolean indicating if the APK is valid. It returns true if the APK is valid, not tampered.
     */

    public Boolean validate() {
        // Check the application installer.
        if (this.playStoreOnly && !wasInstalledFromPlayStore(this.context)) {
            return false;
        }

        return isAValidSignature(context, certificateSignature);
    }

    /**
     * Checks if the apk signature is valid.
     *
     * @param context The context.
     * @param certificateSignature The certificate signature.
     *
     * @return a boolean indicating if the signature is valid.
     */

    private static boolean isAValidSignature(Context context, String certificateSignature) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            // The APK is signed with multiple signatures, probably it was tampered.
            if (packageInfo.signatures.length > 1) {
                return false;
            }

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                if (certificateSignature.compareToIgnoreCase(Base64.encodeToString(md.digest(), Base64.DEFAULT)) == 0) {
                    return true;
                }
            }
        } catch (Exception exception) {
            Log.d("TAMPERING_PROTECTION", exception.getStackTrace().toString());
        }

        return false;
    }

    /**
     * Verifies if the application was installed using the Google Play Store.
     *
     * @param context The application context.
     *
     * @return returns a boolean indicating if the application was installed using the Google Play Store.
     */

    private static boolean wasInstalledFromPlayStore(final Context context) {
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        return installer != null && installer.startsWith(PLAY_STORE_PACKAGE);
    }

    /**
     * Android Tampering protection builder.
     */

    public static class Builder {

        private final Context context;
        private Boolean playStoreOnly = false;
        private final String certificateSignature;

        /**
         * Constructor.
         *
         * @param context The application context.
         * @param certificateSignature The certificate signature.
         */

        public Builder(Context context, String certificateSignature) {
            this.context = context;
            this.certificateSignature = certificateSignature;
        }

        /**
         * Configures the library to check against installations from outside the Google Play Store. The default is false.
         *
         * @param installOnlyFromPlayStore A boolean indicating if is to validate the application installer.
         *
         * @return the builder.
         */

        public Builder installOnlyFromPlayStore(Boolean installOnlyFromPlayStore) {
            this.playStoreOnly = installOnlyFromPlayStore;

            return this;
        }

        /**
         * Builds the Android Tampering Protection.
         *
         * @return the Android Tampering Protection.
         */

        public AndroidTamperingProtection build() {
            AndroidTamperingProtection androidTamperingProtection = new AndroidTamperingProtection(this);

            return androidTamperingProtection;
        }

    }

}

package com.example.zr.gxapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.shandianshua.base.config.GlobalConfig;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author yaoyi@shandianshua.com (Yi Yao)
 */
public class NetUtils {

    public static final String TAG = "NetUtils";

    private static final String PIKACHU_SIGN_HEADER_KEY = "Entei-Content";

    private static final String NETWORK_TYPE_WIFI = "WIFI";
    private static final String NETWORK_TYPE_UNKNOWN = "UNKNOWN";
    private static final String NETWORK_TYPE_NO_NETWORK = "NO_NETWORK";

    public static final int CONNECTION_TIME_OUT = 15 * 1000;
    public static final int SOCKET_TIME_OUT = 15 * 1000;
    private static final Uri PREFERRED_APN_URI = Uri
            .parse("content://telephony/carriers/preferapn");
    private static final String USER_AGENT =
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F)";

    private static Context getContext() {
        return GlobalConfig.getAppContext();
    }

        public static String getNetwork() {
            try {
                if (isWifiConnected()) {
                    return NETWORK_TYPE_WIFI;
                } else if (isMobileNetConnected()) {
                    return getMobileNetworkName();
                } else if (isConnected()) {
                    return NETWORK_TYPE_UNKNOWN;
                } else {
                    return NETWORK_TYPE_NO_NETWORK;
                }
            } catch (SecurityException e) {
                if (LogUtils.isLogEnabled()) {
                    e.printStackTrace();
                }
                return NETWORK_TYPE_NO_NETWORK;
            }
        }

        public static boolean isConnected() {
            ConnectivityManager cm = (ConnectivityManager) getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                LogUtils.i(TAG, "connected!");
                return true;
            }
            return false;
        }

        public static boolean isWifiConnected() {
            ConnectivityManager cm = (ConnectivityManager) getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo != null && wifiInfo.isConnected();
        }

        public static boolean isMobileNetConnected() {
            ConnectivityManager connManager = (ConnectivityManager) getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo =
                    connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return networkInfo != null && networkInfo.isConnected();
        }

        public static String getMobileNetworkName() {
            ConnectivityManager connManager = (ConnectivityManager) getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo =
                    connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null) {
                return networkInfo.getSubtypeName();
            } else {
                return NETWORK_TYPE_UNKNOWN;
            }
        }

}
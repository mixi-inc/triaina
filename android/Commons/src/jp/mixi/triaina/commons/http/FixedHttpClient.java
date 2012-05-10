package jp.mixi.triaina.commons.http;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;

public class FixedHttpClient {
    private static String sUserAgent = "android";

    private static Context mApplicationContext = null;

    public static HttpClient newInstance() {
        HttpClient client = AndroidHttpClient.newInstance(getUserAgentString());
        HttpConnectionParams.setStaleCheckingEnabled(client.getParams(), true);
        HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
        
        // and we need SSL connection bug workaround before Ice Cream Sandwich
        final int HONEYCOMB_MR2 = 13;
        if (Build.VERSION.SDK_INT <= HONEYCOMB_MR2) 
            workAroundReverseDnsBugInHoneycombAndEarlier(client);

        return client;
    }
    
    private static void workAroundReverseDnsBugInHoneycombAndEarlier(HttpClient client) {
        // Android had a bug where HTTPS made reverse DNS lookups (fixed in Ice Cream Sandwich) 
        // http://code.google.com/p/android/issues/detail?id=13117
        SocketFactory socketFactory = new LayeredSocketFactory() {
            SSLSocketFactory delegate = SSLSocketFactory.getSocketFactory();
            @Override public Socket createSocket() throws IOException {
                return delegate.createSocket();
            }
            @Override public Socket connectSocket(Socket sock, String host, int port,
                    InetAddress localAddress, int localPort, HttpParams params) throws IOException {
                return delegate.connectSocket(sock, host, port, localAddress, localPort, params);
            }
            @Override public boolean isSecure(Socket sock) throws IllegalArgumentException {
                return delegate.isSecure(sock);
            }
            @Override public Socket createSocket(Socket socket, String host, int port,
                    boolean autoClose) throws IOException {
                injectHostname(socket, host);
                return delegate.createSocket(socket, host, port, autoClose);
            }
            private void injectHostname(Socket socket, String host) {
                try {
                    Field field = InetAddress.class.getDeclaredField("hostName");
                    field.setAccessible(true);
                    field.set(socket.getInetAddress(), host);
                } catch (Exception ignored) {
                }
            }
        };
        client.getConnectionManager().getSchemeRegistry()
                .register(new Scheme("https", socketFactory, 443));
    }
    
    public static void setApplicationContext(Context applicationContext) {
        if (mApplicationContext == null) {
            mApplicationContext = applicationContext;
            try {
                sUserAgent += "/"
                        + applicationContext.getPackageManager().getPackageInfo(
                                applicationContext.getPackageName(), 0).versionCode;
            } catch (NameNotFoundException ignored) {
                // this isn't much important, just ignore if the version could
                // not be retrieved.
            }
        }
    }

    /**
     * To make finalize the HTTP session, call this function and pass
     * {@link HttpClient} intance for an argument. Otherwise,
     * {@link ClientConnectionManager} will be leaked.
     * 
     * @param instance HttpClient
     */
    public static void closeInstance(HttpClient instance) {
        if (instance instanceof AndroidHttpClient) {
            AndroidHttpClient client = (AndroidHttpClient) instance;
            client.close();
        } else {
            ClientConnectionManager mgr = instance.getConnectionManager();
            if (mgr != null)
                mgr.shutdown();
        }
    }

    public static void setUserAgentString(String userAgent) {
    	sUserAgent = userAgent;
    }
    
    public static String getUserAgentString() {
        return sUserAgent;
    }
}


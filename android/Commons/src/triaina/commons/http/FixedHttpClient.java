package triaina.commons.http;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FixedHttpClient {
    private static String sUserAgent = "android";
    private static final int CONNECTION_TIMEOUT_SECONDS = 20;

    private static Context mApplicationContext = null;

    public static OkHttpClient newInstance() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        client.setReadTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        client.setWriteTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        client.networkInterceptors().add(new UserAgentInterceptor(getUserAgentString()));
        return client;
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

    public static void closeInstance(OkHttpClient instance) {
        // do nothing
    }

    public static void setUserAgentString(String userAgent) {
    	sUserAgent = userAgent;
    }
    
    public static String getUserAgentString() {
        return sUserAgent;
    }

    private static class UserAgentInterceptor implements Interceptor {
        private static final String HEADER_NAME_USER_AGENT = "User-Agent";
        private final String mUserAgent;

        public UserAgentInterceptor(String mUserAgent) {
            this.mUserAgent = mUserAgent;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            if (mUserAgent == null) return chain.proceed(chain.request());

            Request original = chain.request();

            Request newOne = original.newBuilder()
                    .removeHeader(HEADER_NAME_USER_AGENT)
                    .addHeader(HEADER_NAME_USER_AGENT, mUserAgent)
                    .build();

            return chain.proceed(newOne);
        }
    }
}


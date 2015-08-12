package triaina.webview.worker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import triaina.commons.http.CommonHttpClient;
import triaina.commons.utils.BundleUtils;
import triaina.commons.workerservice.AbstractNetworkWorker;
import triaina.commons.workerservice.WorkerService;
import triaina.webview.entity.Result;
import triaina.webview.entity.device.NetHttpSendParams;
import triaina.webview.entity.device.NetHttpSendResult;
import triaina.webview.entity.device.SendNotificationParams;
import triaina.webview.job.HttpRequestJob;
import triaina.webview.receiver.CallbackReceiver;

public class HttpRequestWorker extends AbstractNetworkWorker<HttpRequestJob> {
    private static final String TAG = HttpRequestWorker.class.getSimpleName();

    private static final int MAX_RETRY = 3;

    private static final String POST_METHOD = "POST";
    private static final String GET_METHOD = "GET";
    private static final String PUT_METHOD = "PUT";
    private static final String DELETE_METHOD = "DELETE";
    private static final String MULTIPART = "multipart/form-data";
    private static final String CONTENT_TYPE = "content-type";

    public static final String FILE_TYPE = "File";

    private Context mContext;

    private NetHttpSendParams mParams;

    @Override
    public boolean process(HttpRequestJob job, int retry, int delayAmount, ResultReceiver receiver, Context context,
            Handler handler) throws Exception {

        mContext = context;
        mParams = job.getParams();

        waitForNetworkAvailable();

        // 300 seconds
        if (delayAmount > 300 || retry > MAX_RETRY) {
            fail(job, receiver, CallbackReceiver.TIMEOUT_ERROR_CODE, null);
            return false;
        }

        String method = mParams.getMethod();
        Request request = null;

        // XXX post only
        // TODO other methods
        if (POST_METHOD.equals(method))
            request = createPostRequest(mParams, job.getCookie());
        else
            Log.d(TAG, "sorry " + method + " method is not implemented yet");

        showProgressNotification(job.getNotificationId(), mParams.getNotification());
        OkHttpClient client = null;
        try {
            client = CommonHttpClient.newInstance();
            Response response = client.newCall(request).execute();

            NetHttpSendResult result = new NetHttpSendResult();
            buildResult(result, response);

            if (receiver != null)
                succeed(job, receiver, result, mParams);
        } catch (IOException exp) {
            return false;
        } finally {
            CommonHttpClient.closeInstance(client);
        }

        return true;
    }

    private void showProgressNotification(Integer id, SendNotificationParams params) {
        if (id == null || params == null)
            return;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(
                WorkerService.ACTION_CANCEL_TASK), 0);
        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(mContext)
                .setContentTitle(params.getProgress())
                .setContentText(params.getSummary())
                .setSmallIcon(android.R.drawable.stat_sys_upload).setTicker(params.getProgress())
                .setContentIntent(pendingIntent)
                .setOngoing(true).setWhen(System.currentTimeMillis()).getNotification();

        notificationManager.notify(id, notification);
    }

    private void showCompletedNotification(Integer id, SendNotificationParams params) {
        if (id == null || params == null)
            return;

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(),
                Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(mContext).setContentTitle(params.getCompleted())
                .setSmallIcon(android.R.drawable.stat_sys_upload_done).setContentIntent(pendingIntent)
                .getNotification();
        notificationManager.notify(id, notification);
    }

    private void showFailedNotification(Integer id, SendNotificationParams params) {
        if (id == null || params == null)
            return;

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(),
                Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(mContext).setContentTitle(params.getFailed())
                .setSmallIcon(android.R.drawable.stat_sys_warning).setContentIntent(pendingIntent).getNotification();
        notificationManager.notify(id, notification);
    }

    public void succeed(HttpRequestJob job, ResultReceiver receiver, Result result, NetHttpSendParams params) {
        showCompletedNotification(job.getNotificationId(), params.getNotification());

        Bundle bundle = new Bundle();
        bundle.putParcelable(CallbackReceiver.RESULT, result);
        receiver.send(CallbackReceiver.SUCCESS_CODE, bundle);
    }

    public void fail(HttpRequestJob job, ResultReceiver receiver, int code, String msg) {
        showFailedNotification(job.getNotificationId(), mParams.getNotification());
        receiver.send(code, null);
    }

    private void buildResult(NetHttpSendResult result, Response response) throws IllegalStateException, IOException {
        result.setCode(response.code() + "");

        Headers headers = response.headers();
        Bundle bundle = new Bundle();
        for (int i = 0; i < headers.size(); i++)
            bundle.putString(headers.name(i), headers.value(i));

        result.setHeaders(bundle);

        ResponseBody responseBody = response.body();
        String res = responseBody != null ? responseBody.string() : null;
        result.setResponseText(res);
    }

    private Request createPostRequest(NetHttpSendParams params, String cookie) {
        Bundle headers = params.getHeaders();
        RequestBody requestBody;

        if (headers != null && MULTIPART.equals(BundleUtils.getStringByCaseInsensitive(headers, CONTENT_TYPE))) {
            requestBody = createMultipartRequestBody(params);
        } else {
            String contentType = null;
            if (headers != null) {
                contentType = BundleUtils.getStringByCaseInsensitive(headers, CONTENT_TYPE);
            }
            requestBody = createRequestBody(params, contentType);
        }

        Request.Builder builder = new Request.Builder()
                .url(params.getUrl())
                .post(requestBody);

        if (headers != null && !MULTIPART.equals(BundleUtils.getStringByCaseInsensitive(headers, CONTENT_TYPE))) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                builder.addHeader(key, headers.getString(key));
            }
        }

        if (cookie != null) {
            builder.addHeader("Cookie", cookie);
        }

        return builder.build();
    }

    private RequestBody createMultipartRequestBody(NetHttpSendParams params) {
        Bundle body = params.getBody();

        if (body == null)
            return null;

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        Set<String> keys = body.keySet();

        for (String key : keys) {
            String rawBody = body.getString(key);
            if (rawBody != null) {
                builder.addFormDataPart(key, rawBody);
                continue;
            }

            Bundle part = body.getBundle(key);
            if (part != null) {
                if (FILE_TYPE.equals(part.getString("type"))) {
                    File file = new File(part.getString("value"));
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    builder.addFormDataPart(key, file.getName(), requestBody);
                }
                continue;
            }
        }

        return builder.build();
    }

    private RequestBody createRequestBody(NetHttpSendParams params, String contentType) {
        MediaType mediaType = MediaType.parse(contentType == null ? "text/plain" : contentType);
        return params.getRawBody() == null ? null : RequestBody.create(mediaType, params.getRawBody());
    }
}

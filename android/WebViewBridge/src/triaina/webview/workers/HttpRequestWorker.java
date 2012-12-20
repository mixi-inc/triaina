package triaina.webview.workers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.MultipartEntity;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import triaina.commons.http.CommonHttpClient;
import triaina.commons.utils.BundleUtils;
import triaina.commons.utils.InputStreamUtils;
import triaina.commons.workerservice.AbstractNetworkWorker;
import triaina.webview.entity.Result;
import triaina.webview.entity.device.NetHttpSendParams;
import triaina.webview.entity.device.NetHttpSendResult;
import triaina.webview.jobs.HttpRequestJob;
import triaina.webview.receiver.CallbackReceiver;

public class HttpRequestWorker extends AbstractNetworkWorker<HttpRequestJob> {
	private static final String TAG = "HttpRequestWorker";
	
	private static final String POST_METHOD = "POST";
	private static final String GET_METHOD = "GET";
	private static final String PUT_METHOD = "PUT";
	private static final String DELETE_METHOD = "DELETE";
	private static final String MULTIPART = "multipart/form-data";
	private static final String CONTENT_TYPE = "content-type";
	
	public static final String FILE_TYPE = "File";
	
	@Override
	public boolean process(HttpRequestJob job, int retry, int delayAmount, ResultReceiver receiver, Context context, Handler handler) throws Exception {
		waitForNetworkAvailable();

		//300 seconds
		if (delayAmount > 300) {
			fail(receiver, CallbackReceiver.TIMEOUT_ERROR_CODE, null);
			return false;
		}
		
		NetHttpSendParams params = job.getParams();
		String method = params.getMethod();
		Bundle headers = params.getHeaders();
		HttpRequest request = null;
		
		//XXX post only
		//TODO other methods		
		if (POST_METHOD.equals(method))
			request = createHttpPost(params);
		else
			Log.d(TAG, "sorry " + method + " method is not implemented yet");
			
		if (headers != null)
			buildHeader(request, headers);
				
		String cookie = job.getCookie();
		if (cookie != null)
			buildCookie(request, cookie);

		HttpClient client = null;
		try {
			Uri uri = Uri.parse(params.getUrl());
			HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
			client = CommonHttpClient.newInstance();
			HttpResponse response = client.execute(host, request);
			
			NetHttpSendResult result = new NetHttpSendResult();
			buildResult(result, response);
			
			if (receiver != null)
				succeed(receiver, result);
		} catch (IOException exp) {
			return false;
		} finally {
			CommonHttpClient.closeInstance(client);
		}
		
		return true;
	}

	public void succeed(ResultReceiver receiver, Result result) {
		Bundle bundle = new Bundle();
		bundle.putParcelable(CallbackReceiver.RESULT, result);
		receiver.send(CallbackReceiver.SUCCESS_CODE, bundle);
	}
	
	public void fail(ResultReceiver receiver, int code, String msg) {
		receiver.send(code, null);
	}
	
	private void buildResult(NetHttpSendResult result, HttpResponse response) throws IllegalStateException, IOException {
		result.setCode(response.getStatusLine().getStatusCode() + "");
		
		Header[] headers = response.getAllHeaders();
		Bundle bundle = new Bundle();
		for (Header header : headers)
			bundle.putString(header.getName(), header.getValue());
		
		result.setHeaders(bundle);
		
		HttpEntity entity = response.getEntity();
		String res = entity != null ? InputStreamUtils.toString(entity.getContent()) : null; 			
		result.setResponseText(res);
	}
	
	private void buildHeader(HttpRequest request, Bundle headers) {
		if (MULTIPART.equals(BundleUtils.getStringByCaseInsensitive(headers, CONTENT_TYPE)))
			return;

		Set<String> keys = headers.keySet();
		for (String key : keys) {
			request.setHeader(key, headers.getString(key));
		}
	}
	
	private void buildCookie(HttpRequest request, String cookie) {
		request.setHeader("Cookie", cookie);
	}
	
	private HttpPost createHttpPost(NetHttpSendParams params) throws UnsupportedEncodingException, FileNotFoundException {
		HttpPost post = new HttpPost(params.getUrl());
		Bundle headers = params.getHeaders();
		HttpEntity entity;

		if (headers != null && MULTIPART.equals(BundleUtils.getStringByCaseInsensitive(headers, CONTENT_TYPE))) {
			entity = createMultipartEntity(params);
			post.setHeader(entity.getContentType());
		} else
			entity = createEntity(params);
		
		if (entity != null)
			post.setEntity(entity);
		
		return post;
	}
	
	private HttpEntity createMultipartEntity(NetHttpSendParams params) throws FileNotFoundException {
		MultipartEntity entity = null;
		Bundle body = params.getBody();
		
		if (body == null)
			return null;
		
		Set<String> keys = body.keySet();
		List<Part> parts = new ArrayList<Part>();
		
		for (String key : keys) {
			String rawBody = body.getString(key);
			if (rawBody != null) {
				parts.add(new StringPart(key, rawBody, HTTP.UTF_8));
				continue;
			}
			
			Bundle part = body.getBundle(key);
			if (part != null) {
				if (FILE_TYPE.equals(part.getString("type")))
					parts.add(new FilePart(key, new File(part.getString("value"))));
				continue;
			}
		}
		
		if (parts.size() > 0)
			entity = new MultipartEntity(parts.toArray(new Part[parts.size()]));
		
		return entity;
	}
	
	private HttpEntity createEntity(NetHttpSendParams params) throws UnsupportedEncodingException {
		return params.getRawBody() == null ? null : new StringEntity(params.getRawBody(), HTTP.UTF_8);		
	}
}

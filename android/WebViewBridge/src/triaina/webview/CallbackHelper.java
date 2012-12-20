package triaina.webview;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import triaina.commons.exception.JSONConvertException;
import triaina.commons.exception.CommonRuntimeException;
import triaina.commons.json.JSONConverter;
import triaina.commons.utils.ClassUtils;
import triaina.commons.utils.JSONObjectUtils;
import triaina.webview.entity.Result;

import org.json.JSONObject;

public class CallbackHelper {
	
	public void invokeSucceed(WebViewBridge bridge, Callback<Result> callback, JSONObject json) throws JSONConvertException  {
		Class<?> resultClass = getResultClass(callback);
		Result result = (Result)JSONConverter.toObject(json, resultClass);
		callback.succeed(bridge, result);
	}
	
	public void invokeFail(WebViewBridge bridge, Callback<Result> callback, JSONObject json) {
		callback.fail(bridge, JSONObjectUtils.getString(json, "code"), JSONObjectUtils.getString(json, "message"));
	}
	
	public Class<?> getResultClass(Callback<?> callback) {
		Method[] methods = ClassUtils.getMethodsByName(callback.getClass(), "succeed");
		for (Method method : methods) {
			Type[] geneTypes = method.getGenericParameterTypes();
			Class<?>[] params = method.getParameterTypes();
			
			if (geneTypes.length == 2 && params.length == 2 &&
					!Result.class.equals(geneTypes[1]) && Result.class.isAssignableFrom(params[1]))
				return params[1];
		}
		
		throw new CommonRuntimeException("BUG!! : Maybe result type is Illegal");
	}
}

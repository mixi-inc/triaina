package triaina.webview;

public class WebViewBridgeHelper {
	
	public String makeJavaScript(String func, String... args) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("javascript: ");
		builder.append(func);
		builder.append('(');
		
		for (int i = 0; i < args.length; ++i) {
			builder.append('\'');
			builder.append(args[i]);
			builder.append('\'');
			
			if (args.length - 1 != i) {
				builder.append(',');
			}
		}
		
		builder.append(");");
		
		return builder.toString();
	}
}

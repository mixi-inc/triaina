package triaina.injector.binder;

public class DynamicBinder {
	public static final String DEFAULT_VALUE = "default";
	
	private String mName;
	private String mValue;
	private Class<?> mBindClass;
	private Class<?> mImplementClass;
	
	public DynamicBinder(String name, String value) {
		mName = name;
		mValue = value;
	}
	
	public void bind(Class<?> bindClass) {
		mBindClass = bindClass;
	}
	
	public void to(Class<?> toClass) {
		mImplementClass = toClass;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getValue() {
		return mValue;
	}
	
	public Class<?> getBindClass() {
		return mBindClass;
	}
	
	public Class<?> getImplementClass() {
		return mImplementClass;
	}
}

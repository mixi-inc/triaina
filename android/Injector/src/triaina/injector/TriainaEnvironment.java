package triaina.injector;

import java.util.concurrent.ConcurrentHashMap;

public class TriainaEnvironment {
	private ConcurrentHashMap<String, String> mMap = new ConcurrentHashMap<String, String>();
	
	public String get(String name) {
		return mMap.get(name);
	}
	
	public void set(String name, String value) {
		mMap.put(name, value);
	}
}

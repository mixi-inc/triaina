package jp.mixi.triaina.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.mixi.triaina.commons.exception.IORuntimeException;


public final class InputStreamUtils {
	private InputStreamUtils() {}
	
    public static byte[] toByteArray(InputStream is) {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	byte[] buffer = new byte[4096];
    	int len = 0;
    	
    	try {
    		while ((len = is.read(buffer)) > 0) {
    			bos.write(buffer, 0, len);
    		}
    	} catch (IOException exp) {
    		throw new IORuntimeException(exp);
    	} finally {
        	CloseableUtils.close(is);    		
    	}
    	
    	return bos.toByteArray();
    }
    
    public static String toString(InputStream is) {
    	return new String(toByteArray(is));
    }
}

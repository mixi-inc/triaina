package triaina.commons.test.utils;

import java.io.File;
import java.io.FileInputStream;

import triaina.commons.exception.IORuntimeException;
import triaina.commons.utils.CloseableUtils;
import triaina.commons.utils.FileInputStreamUtils;
import android.test.AndroidTestCase;

public class FileInputStreamUtilsTest extends AndroidTestCase {

	public void testOpenOnException() {
		try {
			FileInputStreamUtils.open(new File("/a"));
			fail();
		} catch (IORuntimeException exp){
		}		
	}
	
	public void testOpen() throws Exception {
		File file = new File(getContext().getCacheDir() + "/" + "a");
		FileInputStream in = null;
		try {
			file.createNewFile();
			in = FileInputStreamUtils.open(file);
		} finally {
			CloseableUtils.close(in);
			file.delete();
		}
	}
}

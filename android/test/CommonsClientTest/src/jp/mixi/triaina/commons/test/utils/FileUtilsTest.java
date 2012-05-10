package jp.mixi.triaina.commons.test.utils;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.net.Uri;
import android.test.AndroidTestCase;
import jp.mixi.triaina.commons.utils.CloseableUtils;
import jp.mixi.triaina.commons.utils.FileUtils;


public class FileUtilsTest extends AndroidTestCase {
	private File dst;
	private FileInputStream srcInput;
	private FileOutputStream dstOutput;
	
	public void testCreateNewFile() throws Exception {
		File file = new File(getContext().getCacheDir() + "/a");
		FileUtils.createNewFile(file);
		assertEquals(true, file.exists());
		file.delete();
	}
	
	public void testCopyFile() throws Exception {
		//TODO
	}

	public void testCopyFileStream() throws Exception {	
		FileUtils.copyFileStream(srcInput, dstOutput);
		readTextFromFile(dst);
		//TODO
	}
	
	public void testGetFileNameByString() {
		assertEquals("ccc.c", FileUtils.getName("/aaa/bbb/ccc.c"));
	}
	
	public void testGetFileNameByUri() {
		assertEquals("ccc.c", FileUtils.getName(Uri.parse("/aaa/bbb/ccc.c")));
	}
	
	private String readTextFromFile(File file) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			return readTextFromStream(in);
		} finally {
			CloseableUtils.close(in);
		}
	}
	
	private String readTextFromStream(InputStream in) throws IOException {
		byte[] buf = new byte[4];
		int len = in.read(buf);
		return new String(buf, 0, len);		
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		AssetManager am = this.getContext().getResources().getAssets();
		srcInput = am.openFd("test.txt").createInputStream();
		dst = new File(this.getContext().getCacheDir(), "dst.txt");
		dstOutput = new FileOutputStream(dst);
	}

	@Override
	protected void tearDown() throws Exception {
		dst.delete();
		super.tearDown();
	}
}

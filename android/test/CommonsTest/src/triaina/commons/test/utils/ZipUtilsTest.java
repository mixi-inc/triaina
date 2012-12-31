package triaina.commons.test.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.test.AndroidTestCase;
import triaina.commons.utils.CloseableUtils;
import triaina.commons.utils.ZipUtils;

public class ZipUtilsTest extends AndroidTestCase {
	private File mTestZip;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		ZipOutputStream out = null;
		try {
			mTestZip = new File(getContext().getCacheDir(), "test.zip");
			out = new ZipOutputStream(new FileOutputStream(mTestZip));
			out.setComment("aaa");
			out.putNextEntry(new ZipEntry("text.txt"));
			out.write("bbb".getBytes());
		} finally {
			CloseableUtils.close(out);
		}
	}

	@Override
	protected void tearDown() throws Exception {
		mTestZip.delete();
		super.tearDown();
	}

	public void testReadCommentString() {
		String comment = ZipUtils.readComment(getContext().getCacheDir() + "/"
		        + "test.zip");
		assertEquals("aaa", comment);
	}

	public void testEndSignatureIndex() {
		int index = ZipUtils.endSignatureIndex(getContext().getCacheDir() + "/"
		        + "test.zip");
		assertEquals(0x71, index);
	}
}

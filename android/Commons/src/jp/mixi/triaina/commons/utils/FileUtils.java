package jp.mixi.triaina.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import jp.mixi.triaina.commons.exception.IORuntimeException;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public final class FileUtils {

    private FileUtils() {}

    /**
     * Copy a file from source to destination
     * 
     * @param srcPath the source
     * @param destPath the destination
     * @return the number of bytes that were transferred
     * @throws IOException
     */
    public static long copyFile(File srcPath, File destPath) throws IOException {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(srcPath);
            os = new FileOutputStream(destPath);
            return copyFileStream(is, os);
        } finally {
            CloseableUtils.close(is);
            CloseableUtils.close(os);
        }
    }

    /**
     * Transfer between streams
     * 
     * @param is the input stream
     * @param os the output stream
     * @return the number of bytes that were transferred
     * @throws IOException
     */
    public static long copyFileStream(FileInputStream is, FileOutputStream os)
            throws IOException {
        FileChannel srcChannel = null;
        FileChannel destChannel = null;
        try {
            srcChannel = is.getChannel();
            destChannel = os.getChannel();
            return srcChannel.transferTo(0, srcChannel.size(), destChannel);
        } finally {
            CloseableUtils.close(srcChannel);
            CloseableUtils.close(destChannel);
        }
    }

    public static String getName(String path) {
        return new File(path).getName();
    }

    public static String getName(Uri uri) {
        return new File(uri.toString()).getName();
    }

    public static boolean createNewFile(File file) {
        try {
            return file.createNewFile();
        } catch (IOException exp) {
            throw new IORuntimeException(exp);
        }
    }

    /**
     * Retrieve real filename from Content Provider.
     *
     * @param contentUri Uri to be checked.
     * @return Full path filename of the resource pointed by contentUri if look up successfully finished. null otherwise.
     */
    public static String getPathFromUri(ContentResolver resolver, Uri contentUri) {
        final String dataColumn = MediaStore.MediaColumns.DATA;
        Cursor cursor = null;
        try {
            cursor = resolver.query(contentUri, new String[] { dataColumn }, null, null, null);
            if (cursor == null || !cursor.moveToFirst())
                return null;
            final int index = cursor.getColumnIndex(dataColumn);
            return cursor.getString(index);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }
}

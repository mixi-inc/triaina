package jp.mixi.triaina.commons.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

import jp.mixi.triaina.commons.exception.IORuntimeException;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public final class FileUtils {

    private static final String TAG = FileUtils.class.getCanonicalName();

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

    /**
     * Guess the MIME type of a file from its contents (data) if possible, or its filename extension if it fails.
     * @param filePath the path of the file to get the MIME type of
     * @return the file MIME type of null if it couldn't be guessed just from its data
     * @throws IOException if an I/O error happened while reading the file
     * @throws FileNotFoundException if the {@link file} couldn't be accessed
     * @see FileUtils#guessMimeType(File)
     */
    public static String guessMimeType (final String filePath) throws FileNotFoundException, IOException {
        if (filePath == null)
            return null;
        return guessMimeType(new File(filePath));
    }

    /**
     * Guess the MIME type of a file from its contents (data) if possible, or its filename extension if it fails.
     * @param file the file to get the MIME type of
     * @return the file MIME type of null if it couldn't be guessed just from its data
     * @throws IOException if an I/O error happened while reading the file
     * @throws FileNotFoundException if the {@link file} couldn't be accessed
     * @see FileUtils#guessMimeType(String)
     */
    public static String guessMimeType (final File file) throws FileNotFoundException, IOException {
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        String mimeType = null;
        //try from file data
        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);

            mimeType = URLConnection.guessContentTypeFromStream(bufferedInputStream);
            //although in implementations such as the openJDK the URLConnection method looks for a lot of filetypes such as JPEG, the Dalvik implementation only looks for ZIP, GIF, HTML and XML...
            //so we try another way to detect image file types :
            if (mimeType == null) {
                //try as an image
                final BitmapFactory.Options imageDecodeOptions = new BitmapFactory.Options();
                imageDecodeOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(bufferedInputStream, null, imageDecodeOptions);
                mimeType = imageDecodeOptions.outMimeType;
            }
        } finally {
            try {
                if (bufferedInputStream != null)
                    bufferedInputStream.close();
            } finally {
                if (fileInputStream != null)
                    fileInputStream.close();
            }
        }
        //if failed, try from file name extension :
        if (mimeType == null) {
            final String fileUrl = file.toURI().toURL().toExternalForm();
            mimeType = URLConnection.guessContentTypeFromName(fileUrl);
        }

        return mimeType;
    }

    /**
     * Guess MIME type of the content.
     * It is first guessed from the {@link resolver}, if this fails then from the content file data, and if this also fails finally from the content file extension.
     * @param resolver a {@link ContentResolver} to access the bitmap data
     * @param uri the URI of the bitmap to guess the MIME type of
     * @return MIME type or null if it couldn't be guessed
     * @throws FileNotFoundException if the specified {@link uri} couldn't be accessed
     * @throws IOException if an I/O error occured while trying to access the file corresponding to the specified {@link uri}
     */
    public static String guessMimeType (final ContentResolver resolver, final Uri uri) throws FileNotFoundException, IOException {
        if ((resolver == null) || (uri == null))
            return null;

        String contentType = null;
        try {
            //try from the ContentResolver
            contentType = resolver.getType(uri);
        } catch (final IllegalStateException illegalStateException) {
            Log.w(TAG, "ContentResolver#getType() failed on " + uri, illegalStateException);
            contentType = null;
        }
        if (contentType == null) {
            //try from the file / file name
            Cursor cursor = null;
            String fileName = null;
            try {
                cursor = resolver.query(uri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                }
            } finally {
                if (cursor != null) cursor.close();
            }

            if (fileName != null) {
                contentType = guessMimeType(fileName);
            }
        }

        return contentType;
    }

}

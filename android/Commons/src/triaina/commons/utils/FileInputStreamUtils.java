package triaina.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import triaina.commons.exception.IORuntimeException;


public final class FileInputStreamUtils {

    public static FileInputStream open(File file) {
        try {
            return new FileInputStream(file);
        } catch (IOException exp) {
            throw new IORuntimeException(exp);
        }
    }
}

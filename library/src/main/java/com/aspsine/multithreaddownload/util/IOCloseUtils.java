package com.aspsine.multithreaddownload.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by luchongbin on 2018/10/30.
 */
public class IOCloseUtils {

    public static final void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            synchronized (IOCloseUtils.class) {
                closeable.close();
            }
        }
    }
}

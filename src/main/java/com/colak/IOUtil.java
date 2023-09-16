package com.colak;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class IOUtil {


    private IOUtil() {
    }

    public static byte[] toByteArray(InputStream is) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            is.transferTo(os);
            return os.toByteArray();
        }
    }

}

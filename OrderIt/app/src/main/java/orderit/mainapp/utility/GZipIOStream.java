package orderit.mainapp.utility;

/**
 * Created by Blackcool on 1/9/16.
 */

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.util.Base64;
import org.apache.commons.io.IOUtils;

public class GZipIOStream {
    /** Constructor */
    public GZipIOStream() {

    }

    public static String compressString(String srcTxt) throws IOException {
        ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
        GZIPOutputStream zos = new GZIPOutputStream(rstBao);
        zos.write(srcTxt.getBytes());
        IOUtils.closeQuietly(zos);

        byte[] bytes = rstBao.toByteArray();
        return Base64.encodeToString(bytes, Base64.URL_SAFE);
    }

    public static String uncompressString(String zippedBase64Str) throws IOException {
        String result = null;
        byte[] bytes = Base64.decode(zippedBase64Str, Base64.URL_SAFE);
        GZIPInputStream zi = null;
        try {
            zi = new GZIPInputStream(new ByteArrayInputStream(bytes));
            result = IOUtils.toString(zi);
        } finally {
            IOUtils.closeQuietly(zi);
        }
        return result;
    }

}

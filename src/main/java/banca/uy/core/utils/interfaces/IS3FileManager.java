package banca.uy.core.utils.interfaces;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public interface IS3FileManager {

    public InputStream getFile(String url);

    public String uploadFile(String contentType, ByteArrayInputStream byteArray, long contentLength, String url);
}
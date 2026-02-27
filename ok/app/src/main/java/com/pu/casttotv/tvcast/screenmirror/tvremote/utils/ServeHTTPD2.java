package com.pu.casttotv.tvcast.screenmirror.tvremote.utils;

import android.util.Log;

import com.amazon.whisperlink.util.NanoHTTPD;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.getMimeTypeForFile;

public class ServeHTTPD2 extends NanoHTTPD {

    public ServeHTTPD2(int i) {
        super(i);
    }

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession iHTTPSession) {
        Map<String, String> headers = iHTTPSession.getHeaders();
        iHTTPSession.getParms();
        NanoHTTPD.Method method = iHTTPSession.getMethod();
        String uri = iHTTPSession.getUri();
        HashMap hashMap = new HashMap();
        File file = new File(uri);
        if (NanoHTTPD.Method.POST.equals(method) || NanoHTTPD.Method.PUT.equals(method)) {
            try {
                iHTTPSession.parseBody(hashMap);
            } catch (IOException e2) {
                Log.e("###TAG", "serve iHTTPSession 2: " + e2.getMessage());
                return getResponse("Internal Error IO Exception: " + e2.getMessage());
            } catch (NanoHTTPD.ResponseException e3) {
                Log.e("###TAG", "serve iHTTPSession 3: " + e3.getMessage());
                return new NanoHTTPD.Response(e3.getStatus(), "text/plain", e3.getMessage());
            }
        }
        String replace = uri.trim().replace(File.separatorChar, '/');
        Log.e("###TAG", "serve: " + replace);
        if (replace.indexOf(63) >= 0) {
            replace = replace.substring(0, replace.indexOf(63));
        }
        return serveFile(replace, headers, file);
    }

    private Response serveFile(String uri, Map<String, String> header, File file) {
        Response res;
        String mime = getMimeTypeForFile(uri);
        try {
            // Calculate etag
            String etag = Integer.toHexString((file.getAbsolutePath() +
                    file.lastModified() + "" + file.length()).hashCode());

            // Support (simple) skipping:
            long startFrom = 0;
            long endAt = -1;
            String range = header.get("range");
            if (range != null) {
                if (range.startsWith("bytes=")) {
                    range = range.substring("bytes=".length());
                    int minus = range.indexOf('-');
                    try {
                        if (minus > 0) {
                            startFrom = Long.parseLong(range.substring(0, minus));
                            endAt = Long.parseLong(range.substring(minus + 1));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            // Change return code and add Content-Range header when skipping is requested
            long fileLen = file.length();
            if (range != null && startFrom >= 0) {
                if (startFrom >= fileLen) {
                    res = createResponse(Response.Status.RANGE_NOT_SATISFIABLE, MIME_PLAINTEXT, "");
                    res.addHeader("Content-Range", "bytes 0-0/" + fileLen);
                    res.addHeader("ETag", etag);
                } else {
                    if (endAt < 0) {
                        endAt = fileLen - 1;
                    }
                    long newLen = endAt - startFrom + 1;
                    if (newLen < 0) {
                        newLen = 0;
                    }

                    final long dataLen = newLen;
                    FileInputStream fis = new FileInputStream(file) {
                        @Override
                        public int available() throws IOException {
                            return (int) dataLen;
                        }
                    };
                    fis.skip(startFrom);

                    res = createResponse(Response.Status.PARTIAL_CONTENT, mime, fis);
                    res.addHeader("Content-Length", "" + dataLen);
                    res.addHeader("Content-Range", "bytes " + startFrom + "-" +
                            endAt + "/" + fileLen);
                    res.addHeader("ETag", etag);
                }
            } else {
                if (etag.equals(header.get("if-none-match")))
                    res = createResponse(Response.Status.NOT_MODIFIED, mime, "");
                else {
                    res = createResponse(Response.Status.OK, mime, new FileInputStream(file));
                    res.addHeader("Content-Length", "" + fileLen);
                    res.addHeader("ETag", etag);
                }
            }
        } catch (IOException ioe) {
            res = getResponse("Forbidden: Reading file failed");
        }

        return (res == null) ? getResponse("Error 404: File not found") : res;
    }

    private NanoHTTPD.Response createResponse(NanoHTTPD.Response.Status status, String str, InputStream inputStream) {
        NanoHTTPD.Response response = new NanoHTTPD.Response(status, str, inputStream);
        response.addHeader("Accept-Ranges", "bytes");
        return response;
    }

    private NanoHTTPD.Response createResponse(NanoHTTPD.Response.Status status, String str, String str2) {
        NanoHTTPD.Response response = new NanoHTTPD.Response(status, str, str2);
        response.addHeader("Accept-Ranges", "bytes");
        return response;
    }

    private NanoHTTPD.Response getResponse(String str) {
        return createResponse(NanoHTTPD.Response.Status.OK, "text/plain", str);
    }
}

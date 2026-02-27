package com.yanzhenjie.andserver.website;

import android.content.res.AssetManager;
import android.os.SystemClock;
import com.yanzhenjie.andserver.exception.NotFoundException;
import com.yanzhenjie.andserver.protocol.ETag;
import com.yanzhenjie.andserver.protocol.LastModified;
import com.yanzhenjie.andserver.util.AssetsReader;
import com.yanzhenjie.andserver.util.FileUtils;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import com.yanzhenjie.andserver.view.View;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.entity.ContentType;
import org.apache.httpcore.entity.InputStreamEntity;
import org.apache.httpcore.protocol.HttpContext;

public class AssetsWebsite extends SimpleWebsite implements LastModified, ETag {
    private boolean isScanned;
    private final AssetsReader mAssetsReader;
    private final Map<String, String> mPatternMap = new LinkedHashMap();
    private final String mRootPath;

    public AssetsWebsite(AssetManager assetManager, String str) {
        this.mAssetsReader = new AssetsReader(assetManager);
        this.mRootPath = str;
    }

    @Override // com.yanzhenjie.andserver.website.WebSite
    public boolean intercept(HttpRequest httpRequest, HttpContext httpContext) {
        tryScanFile();
        return this.mPatternMap.containsKey(HttpRequestParser.getRequestPath(httpRequest));
    }

    private void tryScanFile() {
        if (!this.isScanned) {
            synchronized (AssetsWebsite.class) {
                if (!this.isScanned) {
                    onScanFile(this.mRootPath, this.mAssetsReader, this.mPatternMap);
                    this.isScanned = true;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onScanFile(String str, AssetsReader assetsReader, Map<String, String> map) {
        List<String> scanFile = assetsReader.scanFile(str);
        if (scanFile.size() > 0) {
            for (String str2 : scanFile) {
                String trimStartSlash = trimStartSlash(str2);
                String addStartSlash = addStartSlash(trimStartSlash.substring(str.length(), trimStartSlash.length()));
                map.put(addStartSlash, str2);
                if (str2.endsWith("/index.html")) {
                    String substring = addStartSlash.substring(0, addStartSlash.indexOf("/index.html"));
                    map.put(substring, str2);
                    map.put(addEndSlash(substring), str2);
                }
            }
        }
    }

    @Override // com.yanzhenjie.andserver.website.SimpleWebsite
    public View handle(HttpRequest httpRequest) throws HttpException, IOException {
        String requestPath = HttpRequestParser.getRequestPath(httpRequest);
        String str = this.mPatternMap.get(requestPath);
        InputStream inputStream = this.mAssetsReader.getInputStream(str);
        if (inputStream != null) {
            return new View(200, new InputStreamEntity(inputStream, (long) inputStream.available(), ContentType.create(FileUtils.getMimeType(str), Charset.defaultCharset())));
        }
        throw new NotFoundException(requestPath);
    }

    @Override // com.yanzhenjie.andserver.protocol.LastModified
    public long getLastModified(HttpRequest httpRequest) throws IOException {
        String trimEndSlash = trimEndSlash(HttpRequestParser.getRequestPath(httpRequest));
        if (this.mAssetsReader.getInputStream(this.mPatternMap.get(trimEndSlash)) != null) {
            return System.currentTimeMillis() - SystemClock.currentThreadTimeMillis();
        }
        return -1;
    }

    @Override // com.yanzhenjie.andserver.protocol.ETag
    public String getETag(HttpRequest httpRequest) throws IOException {
        String str = this.mPatternMap.get(trimEndSlash(HttpRequestParser.getRequestPath(httpRequest)));
        InputStream inputStream = this.mAssetsReader.getInputStream(str);
        if (inputStream == null) {
            return null;
        }
        return ((long) inputStream.available()) + str;
    }
}

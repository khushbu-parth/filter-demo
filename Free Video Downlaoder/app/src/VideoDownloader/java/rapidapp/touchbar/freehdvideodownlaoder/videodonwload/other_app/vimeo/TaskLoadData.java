package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.vimeo;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class TaskLoadData {
    static String response = null;

    public String makeServiceCall(String url, int method) {
        return makeServiceCall(url, method, null);
    }

    public String makeServiceCall(String url, int method, List<NameValuePair> params) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;
            if (method == 1) {
                if (params != null) {
                    url = url + "?" + URLEncodedUtils.format(params, "utf-8");
                }
                httpResponse = httpClient.execute(new HttpGet(url));
            } else if (method == 2) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    try {
                        httpPost.setEntity(new UrlEncodedFormEntity(params));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                httpResponse = httpClient.execute(httpPost);
                Log.i("HTTP POST:", httpResponse.toString());
            }
            response = EntityUtils.toString(httpResponse.getEntity());
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return response;
    }
}
package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class HttpJsonParser {
    private static JSONObject jObj;

    public JSONObject makeHttpRequest(String str, String str2, Map<String, String> map) {
        HttpURLConnection httpURLConnection;
        try {
            Uri.Builder builder = new Uri.Builder();
            String str3 = "";
            if (map != null) {
                for (Map.Entry next : map.entrySet()) {
                    builder.appendQueryParameter((String) next.getKey(), (String) next.getValue());
                }
            }
            if (builder.build().getEncodedQuery() != null) {
                str3 = builder.build().getEncodedQuery();
            }
            if ("GET".equals(str2)) {
                httpURLConnection = (HttpURLConnection) new URL(str + "?" + str3).openConnection();
                httpURLConnection.setRequestMethod(str2);
            } else {
                httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                httpURLConnection.setRequestMethod(str2);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(str3.getBytes().length));
                httpURLConnection.getOutputStream().write(str3.getBytes());
            }
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
                sb.append("\n");
            }
            inputStream.close();
            jObj = new JSONObject(sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        } catch (JSONException e4) {
            Log.e("JSON Parser", "Error parsing data " + e4.toString());
        } catch (Exception e5) {
            Log.e("Exception", "Error parsing data " + e5.toString());
        }
        return jObj;
    }
}

package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.Splashexit10.TokanData;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SendAppToken extends AsyncTask<String, Void, Void> {

    Context c;
    int Scode;
    InputStream in;
    JSONObject jmainobj;
    private String url_g = Glob.GSM_link;

    public SendAppToken(Context c) {
        super();
        this.c = c;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Void doInBackground(String... arg0) {
        String token = (String) arg0[0];
        Log.i("Token", token);
        HttpClient hc = new DefaultHttpClient();
        HttpPost hp = new HttpPost(url_g);
        HttpResponse res;
        try {
            List<NameValuePair> mylist = new ArrayList<NameValuePair>();
            mylist.add(new BasicNameValuePair("app_id", Glob.appID + ""));
            mylist.add(new BasicNameValuePair("device_token", token));

            hp.setEntity(new UrlEncodedFormEntity(mylist));
            res = hc.execute(hp);
            Scode = res.getStatusLine().getStatusCode();
            try {
                HttpEntity httpEntity = res.getEntity();
                in = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String str = null;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                    System.out.println("Tokan Successfullyy..........!!!!" + sb);
                }
                in.close();
                jmainobj = new JSONObject(sb.toString());

            } catch (Exception e) {

            }
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

}
package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.api;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static final RestClient restClient = new RestClient();
    private static Activity mActivity;
    private static Retrofit retrofit;

    private RestClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient build = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).writeTimeout(2, TimeUnit.MINUTES).addInterceptor(new Interceptor() {

            @Override
            public final Response intercept(Chain chain) throws IOException {
                return RestClient.this.lambda$new$0$RestClient(chain);
            }
        }).addInterceptor(httpLoggingInterceptor).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl("https://www.instagram.com/").addConverterFactory(GsonConverterFactory.create(new Gson())).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(build).build();
        }
    }

    public static RestClient getInstance(Activity activity) {
        mActivity = activity;
        return restClient;
    }

    public Response lambda$new$0$RestClient(Interceptor.Chain chain) throws IOException {
        Response response = null;
        try {
            response = chain.proceed(chain.request());
            if (response.code() == 200) {
                try {
                    String jSONObject = new JSONObject(response.body().string()).toString();
                    printMsg(jSONObject + "");
                    return response.newBuilder().body(ResponseBody.create(response.body().contentType(), jSONObject)).build();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketTimeoutException e2) {
            e2.printStackTrace();
        }
        return response;
    }

    public APIServices getService() {
        return (APIServices) retrofit.create(APIServices.class);
    }

    private void printMsg(String str) {
        int length = str.length() / 4050;
        int i = 0;
        while (i <= length) {
            int i2 = i + 1;
            int i3 = i2 * 4050;
            if (i3 >= str.length()) {
                Log.d("Response::", str.substring(i * 4050));
            } else {
                Log.d("Response::", str.substring(i * 4050, i3));
            }
            i = i2;
        }
    }
}
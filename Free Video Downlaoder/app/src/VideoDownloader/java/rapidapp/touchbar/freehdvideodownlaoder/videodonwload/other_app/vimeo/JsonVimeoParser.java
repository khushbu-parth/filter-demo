package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.other_app.vimeo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonVimeoParser {
    private static JsonVimeoParser INSTANCE;
    private JSONObject mRequest;
    private JSONObject mjsonObject;

    private JsonVimeoParser(JSONObject jSONObject) {
        this.mjsonObject = jSONObject;
    }

    public static JsonVimeoParser getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        throw new IllegalAccessError("JsonPaser has null data. Must call prepare() first");
    }

    public static void prepare(JSONObject jSONObject) {
        INSTANCE = new JsonVimeoParser(jSONObject);
    }

    public ArrayList<HashMap<String, String>> getAllVideo() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList();
        try {
            this.mRequest = this.mjsonObject.getJSONObject("request");
            JSONArray jSONArray = this.mRequest.getJSONObject("files").getJSONArray("progressive");
            for (int i = 0; i < jSONArray.length(); i++) {
                HashMap hashMap = new HashMap();
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                String string = jSONObject.getString("url");
                String string2 = jSONObject.getString("width");
                String string3 = jSONObject.getString("height");
                hashMap.put("url", string);
                hashMap.put("width", string2);
                hashMap.put("height", string3);
                arrayList.add(hashMap);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getVideoTime() {
        String str = "";
        try {
            str = this.mjsonObject.getJSONObject("request").getString("timestamp");
        } catch (Exception e) {
        }
        return str;
    }

    public String getVideoTitle() {
        String str = "";
        String str2 = "";
        try {
            str = this.mjsonObject.getJSONObject("video").getString("title");
        } catch (Exception e) {
        }
        return str;
    }
}
package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.webcast;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* loaded from: classes4.dex */
public class CustomSearch {
    public static List<CustomGrabberModel> Search(Context context, String str, String str2) {
        String nextString;
        ArrayList arrayList = new ArrayList();
        CustomGrabberModel customGrabberModel = new CustomGrabberModel();
        String[] stringArray = context.getResources().getStringArray(R.array.customised_searches);
        JsonReader jsonReader = new JsonReader(new StringReader(str2));
        jsonReader.setLenient(true);
        try {
            if (jsonReader.peek() == JsonToken.STRING && (nextString = jsonReader.nextString()) != null) {
                Document parse = Jsoup.parse(nextString);
                if (str.contains(stringArray[0])) {
                    Elements select = parse.select("link[rel=\"preload\"]");
                    Iterator<Element> it = select.iterator();
                    boolean z = false;
                    while (it.hasNext()) {
                        Element next = it.next();
                        if (next.attr("href").endsWith(".mp4.m3u8")) {
                            customGrabberModel.setVideoUrl(next.attr("href"));
                            customGrabberModel.set_file_type(file_type.VIDEO);
                            customGrabberModel.setM3u8(Boolean.TRUE);
                            arrayList.add(customGrabberModel);
                            z = true;
                        }
                    }
                    if (!z) {
                        Iterator<Element> it2 = select.iterator();
                        while (it2.hasNext()) {
                            Element next2 = it2.next();
                            if (next2.attr("href").contains(".m3u8")) {
                                customGrabberModel.setVideoUrl(next2.attr("href"));
                                customGrabberModel.set_file_type(file_type.VIDEO);
                                customGrabberModel.setM3u8(Boolean.TRUE);
                                arrayList.add(customGrabberModel);
                            }
                        }
                    }
                }
                if (str.contains(stringArray[1])) {
                    Iterator<Element> it3 = parse.select("script").iterator();
                    while (it3.hasNext()) {
                        String trim = it3.next().html().trim();
                        if (trim.startsWith("var flashvars_")) {
                            JSONArray jSONArray = new JSONArray(Utils.removeBackSlash(trim.split("qualityItems")[1].split(" = ")[1].split("playerObjList")[0]).substring(1).trim().substring(0, -2));
                            for (int i = 0; i < jSONArray.length(); i++) {
                                JSONObject jSONObject = jSONArray.getJSONObject(i);
                                CustomGrabberModel customGrabberModel2 = new CustomGrabberModel();
                                customGrabberModel2.setVideoUrl(jSONObject.getString("url"));
                                customGrabberModel2.set_file_type(file_type.VIDEO);
                                customGrabberModel2.setM3u8(Boolean.FALSE);
                                arrayList.add(customGrabberModel2);
                            }
                        }
                    }
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (JSONException e3) {
            e3.printStackTrace();
        }
        return arrayList;
    }
}

package com.colorcallscreen.colorphone.callscreen.calltheme.singleton;

import android.content.Context;
import android.text.TextUtils;

import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Constants;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.database.SpeakUpKeywords;
import com.colorcallscreen.colorphone.callscreen.calltheme.database.helper.DatabaseHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.notification.NotificationWear;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class BoloSingleTon {
    private static BoloSingleTon mInstance;
    private List<String> acceptList;
    private List<List<String>> acceptLists;
    private List<String> acceptTitles;
    private ArrayList<String> autoReplyList;
    private ArrayList<List<String>> autoReplyLists;
    private ArrayList<String> autoReplyTitles;
    private ArrayList<String> blockList;
    private ArrayList<List<String>> blockLists;
    private ArrayList<String> blockTitles;
    private List<String> declineList;
    private List<List<String>> declineLists;
    private List<String> declineTitles;
    private ArrayList<String> muteList;
    private ArrayList<List<String>> muteLists;
    private ArrayList<String> muteTitles;
    private List<String> speakerList;
    private List<List<String>> speakerLists;
    private List<String> speakerTitles;
    private ArrayList<String> whoList;
    private ArrayList<List<String>> whoLists;
    private ArrayList<String> whoTitles;
    private HashMap<String, HashMap<String, NotificationWear>> wearableHashMap = null;
    public List<String> supportedLanguages = new ArrayList();

    public BoloSingleTon(Context context) {
        initKeysList(context);
    }

    public static BoloSingleTon getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BoloSingleTon(context);
        }
        return mInstance;
    }

    private void initKeysList(Context context) {
        updateAcceptList(true, context);
        updateDeclineList(true, context);
        updateSpeakerList(true, context);
        updateAutoReplyList(true, context);
        updateMuteList(true, context);
        updateBlockList(true, context);
        updateWhoList(true, context);
    }

    private void storeAcceptDefaultKeywords(Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            SpeakUpKeywords speakUpKeywords = new SpeakUpKeywords();
            speakUpKeywords.setTag(Constants.acceptTag);
            speakUpKeywords.setTitle(context.getResources().getString(R.string.accept_key));
            speakUpKeywords.setWords(TextUtils.join(",", context.getResources().getStringArray(R.array.accept_key_values)));
            speakUpKeywordsDao.createOrUpdate(speakUpKeywords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void storeAutoReplyDefaultKeywords(Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            SpeakUpKeywords speakUpKeywords = new SpeakUpKeywords();
            speakUpKeywords.setTag(Constants.autoReplyTag);
            speakUpKeywords.setTitle(context.getResources().getString(R.string.auto_reply_key));
            speakUpKeywords.setWords(TextUtils.join(",", context.getResources().getStringArray(R.array.auto_reply_key_values)));
            speakUpKeywordsDao.createOrUpdate(speakUpKeywords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void storeBlockDefaultKeywords(Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            SpeakUpKeywords speakUpKeywords = new SpeakUpKeywords();
            speakUpKeywords.setTag(Constants.blockTag);
            speakUpKeywords.setTitle(context.getResources().getString(R.string.block_key));
            speakUpKeywords.setWords(TextUtils.join(",", context.getResources().getStringArray(R.array.block_key_values)));
            speakUpKeywordsDao.createOrUpdate(speakUpKeywords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void storeMuteDefaultKeywords(Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            SpeakUpKeywords speakUpKeywords = new SpeakUpKeywords();
            speakUpKeywords.setTag(Constants.muteTag);
            speakUpKeywords.setTitle(context.getResources().getString(R.string.mute_key));
            speakUpKeywords.setWords(TextUtils.join(",", context.getResources().getStringArray(R.array.mute_key_values)));
            speakUpKeywordsDao.createOrUpdate(speakUpKeywords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void storeNoDefaultKeywords(Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            SpeakUpKeywords speakUpKeywords = new SpeakUpKeywords();
            speakUpKeywords.setTag(Constants.declineTag);
            speakUpKeywords.setTitle(context.getResources().getString(R.string.decline_key));
            speakUpKeywords.setWords(TextUtils.join(",", context.getResources().getStringArray(R.array.decline_key_values)));
            speakUpKeywordsDao.createOrUpdate(speakUpKeywords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void storeSpeakerDefaultKeywords(Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            SpeakUpKeywords speakUpKeywords = new SpeakUpKeywords();
            speakUpKeywords.setTag(Constants.speakerTag);
            speakUpKeywords.setTitle(context.getResources().getString(R.string.speaker_key));
            speakUpKeywords.setWords(TextUtils.join(",", context.getResources().getStringArray(R.array.speaker_key_values)));
            speakUpKeywordsDao.createOrUpdate(speakUpKeywords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void storeWhoDefaultKeywords(Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            SpeakUpKeywords speakUpKeywords = new SpeakUpKeywords();
            speakUpKeywords.setTag(Constants.whoTag);
            speakUpKeywords.setTitle(context.getResources().getString(R.string.who_key));
            speakUpKeywords.setWords(TextUtils.join(",", context.getResources().getStringArray(R.array.who_key_values)));
            speakUpKeywordsDao.createOrUpdate(speakUpKeywords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateAcceptList(boolean z, Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            List<SpeakUpKeywords> query = speakUpKeywordsDao.query(speakUpKeywordsDao.queryBuilder().where().eq("tag", Constants.acceptTag).prepare());
            this.acceptTitles = new ArrayList();
            this.acceptList = new ArrayList();
            this.acceptLists = new ArrayList();
            if (query != null && !query.isEmpty()) {
                for (SpeakUpKeywords speakUpKeywords : query) {
                    this.acceptTitles.add(speakUpKeywords.getTitle());
                    List<String> convertToLowerCase = Utility.convertToLowerCase(Arrays.asList(speakUpKeywords.getWords().split("\\s*,\\s*")));
                    this.acceptList.addAll(convertToLowerCase);
                    this.acceptLists.add(convertToLowerCase);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateAutoReplyList(boolean z, Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            List<SpeakUpKeywords> query = speakUpKeywordsDao.query(speakUpKeywordsDao.queryBuilder().where().eq("tag", Constants.autoReplyTag).prepare());
            this.autoReplyList = new ArrayList<>();
            this.autoReplyTitles = new ArrayList<>();
            this.autoReplyLists = new ArrayList<>();
            if (query != null && !query.isEmpty()) {
                for (SpeakUpKeywords speakUpKeywords : query) {
                    this.autoReplyTitles.add(speakUpKeywords.getTitle());
                    List<String> convertToLowerCase = Utility.convertToLowerCase(Arrays.asList(speakUpKeywords.getWords().split("\\s*,\\s*")));
                    this.autoReplyList.addAll(convertToLowerCase);
                    this.autoReplyLists.add(convertToLowerCase);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateBlockList(boolean z, Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            List<SpeakUpKeywords> query = speakUpKeywordsDao.query(speakUpKeywordsDao.queryBuilder().where().eq("tag", Constants.blockTag).prepare());
            this.blockList = new ArrayList<>();
            this.blockTitles = new ArrayList<>();
            this.blockLists = new ArrayList<>();
            if (query != null && !query.isEmpty()) {
                for (SpeakUpKeywords speakUpKeywords : query) {
                    this.blockTitles.add(speakUpKeywords.getTitle());
                    List<String> convertToLowerCase = Utility.convertToLowerCase(Arrays.asList(speakUpKeywords.getWords().split("\\s*,\\s*")));
                    this.blockList.addAll(convertToLowerCase);
                    this.blockLists.add(convertToLowerCase);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDeclineList(boolean z, Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            List<SpeakUpKeywords> query = speakUpKeywordsDao.query(speakUpKeywordsDao.queryBuilder().where().eq("tag", Constants.declineTag).prepare());
            this.declineList = new ArrayList();
            this.declineTitles = new ArrayList();
            this.declineLists = new ArrayList();
            if (query != null && !query.isEmpty()) {
                for (SpeakUpKeywords speakUpKeywords : query) {
                    this.declineTitles.add(speakUpKeywords.getTitle());
                    List<String> convertToLowerCase = Utility.convertToLowerCase(Arrays.asList(speakUpKeywords.getWords().split("\\s*,\\s*")));
                    this.declineList.addAll(convertToLowerCase);
                    this.declineLists.add(convertToLowerCase);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMuteList(boolean z, Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            List<SpeakUpKeywords> query = speakUpKeywordsDao.query(speakUpKeywordsDao.queryBuilder().where().eq("tag", Constants.muteTag).prepare());
            this.muteList = new ArrayList<>();
            this.muteTitles = new ArrayList<>();
            this.muteLists = new ArrayList<>();
            if (query != null && !query.isEmpty()) {
                for (SpeakUpKeywords speakUpKeywords : query) {
                    this.muteTitles.add(speakUpKeywords.getTitle());
                    List<String> convertToLowerCase = Utility.convertToLowerCase(Arrays.asList(speakUpKeywords.getWords().split("\\s*,\\s*")));
                    this.muteList.addAll(convertToLowerCase);
                    this.muteLists.add(convertToLowerCase);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSpeakerList(boolean z, Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            List<SpeakUpKeywords> query = speakUpKeywordsDao.query(speakUpKeywordsDao.queryBuilder().where().eq("tag", Constants.speakerTag).prepare());
            this.speakerList = new ArrayList();
            this.speakerTitles = new ArrayList();
            this.speakerLists = new ArrayList();
            if (query != null && !query.isEmpty()) {
                for (SpeakUpKeywords speakUpKeywords : query) {
                    this.speakerTitles.add(speakUpKeywords.getTitle());
                    List<String> convertToLowerCase = Utility.convertToLowerCase(Arrays.asList(speakUpKeywords.getWords().split("\\s*,\\s*")));
                    this.speakerList.addAll(convertToLowerCase);
                    this.speakerLists.add(convertToLowerCase);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateWhoList(boolean z, Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            List<SpeakUpKeywords> query = speakUpKeywordsDao.query(speakUpKeywordsDao.queryBuilder().where().eq("tag", Constants.whoTag).prepare());
            this.whoList = new ArrayList<>();
            this.whoTitles = new ArrayList<>();
            this.whoLists = new ArrayList<>();
            if (query != null && !query.isEmpty()) {
                for (SpeakUpKeywords speakUpKeywords : query) {
                    this.whoTitles.add(speakUpKeywords.getTitle());
                    List<String> convertToLowerCase = Utility.convertToLowerCase(Arrays.asList(speakUpKeywords.getWords().split("\\s*,\\s*")));
                    this.whoList.addAll(convertToLowerCase);
                    this.whoLists.add(convertToLowerCase);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteKeyword(String str, String str2, Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            speakUpKeywordsDao.delete(speakUpKeywordsDao.query(speakUpKeywordsDao.queryBuilder().where().eq("tag", str).and().eq("title", str2).prepare()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (str.equalsIgnoreCase(Constants.acceptTag)) {
            updateAcceptList(true, context);
        } else if (str.equalsIgnoreCase(Constants.declineTag)) {
            updateDeclineList(true, context);
        } else if (str.equalsIgnoreCase(Constants.speakerTag)) {
            updateSpeakerList(true, context);
        } else if (str.equalsIgnoreCase(Constants.muteTag)) {
            updateMuteList(true, context);
        } else if (str.equalsIgnoreCase(Constants.blockTag)) {
            updateBlockList(true, context);
        } else if (str.equalsIgnoreCase(Constants.whoTag)) {
            updateWhoList(true, context);
        }
    }

    public List<String> getAcceptList() {
        List<String> list = this.acceptList;
        return list == null ? new ArrayList() : list;
    }

    public List<List<String>> getAcceptLists() {
        List<List<String>> list = this.acceptLists;
        return list == null ? new ArrayList() : list;
    }

    public List<String> getAcceptTitles() {
        List<String> list = this.acceptTitles;
        return list == null ? new ArrayList() : list;
    }

    public List<String> getAutoMessageList() {
        ArrayList<String> arrayList = this.autoReplyList;
        return arrayList == null ? new ArrayList() : arrayList;
    }

    public List<List<String>> getAutoReplyLists() {
        ArrayList<List<String>> arrayList = this.autoReplyLists;
        return arrayList == null ? new ArrayList() : arrayList;
    }

    public List<String> getAutoReplyTitles() {
        ArrayList<String> arrayList = this.autoReplyTitles;
        return arrayList == null ? new ArrayList() : arrayList;
    }

    public List<String> getBlockList() {
        ArrayList<String> arrayList = this.blockList;
        return arrayList == null ? new ArrayList() : arrayList;
    }

    public List<String> getBlockTitles() {
        ArrayList<String> arrayList = this.blockTitles;
        return arrayList == null ? new ArrayList() : arrayList;
    }

    public List<String> getDeclineList() {
        List<String> list = this.declineList;
        return list == null ? new ArrayList() : list;
    }

    public List<List<String>> getDeclineLists() {
        List<List<String>> list = this.declineLists;
        return list == null ? new ArrayList() : list;
    }

    public List<String> getDeclineTitles() {
        List<String> list = this.declineTitles;
        return list == null ? new ArrayList() : list;
    }

    public List<String> getMuteList() {
        ArrayList<String> arrayList = this.muteList;
        return arrayList == null ? new ArrayList() : arrayList;
    }

    public List<String> getMuteTitles() {
        ArrayList<String> arrayList = this.muteTitles;
        return arrayList == null ? new ArrayList() : arrayList;
    }

    public List<String> getSpeakerList() {
        List<String> list = this.speakerList;
        return list == null ? new ArrayList() : list;
    }

    public List<List<String>> getSpeakerLists() {
        List<List<String>> list = this.speakerLists;
        return list == null ? new ArrayList() : list;
    }

    public List<String> getSpeakerTitles() {
        List<String> list = this.speakerTitles;
        return list == null ? new ArrayList() : list;
    }

    public HashMap<String, HashMap<String, NotificationWear>> getWearableHashMap() {
        if (this.wearableHashMap == null) {
            this.wearableHashMap = new HashMap<>();
        }
        return this.wearableHashMap;
    }

    public void storeKeywords(String str, List<String> list, String str2, Context context) {
        try {
            Dao<SpeakUpKeywords, Integer> speakUpKeywordsDao = new DatabaseHelper(context).getSpeakUpKeywordsDao();
            ArrayList arrayList = new ArrayList();
            for (String str3 : list) {
                String[] split = str3.split(" ");
                if (split.length > 0) {
                    for (String str4 : split) {
                        if (!str4.trim().isEmpty()) {
                            arrayList.add(str4.trim());
                        }
                    }
                } else if (!str3.trim().isEmpty()) {
                    arrayList.add(str3.trim());
                }
            }
            SpeakUpKeywords speakUpKeywords = new SpeakUpKeywords();
            speakUpKeywords.setTag(str);
            speakUpKeywords.setTitle(str2);
            speakUpKeywords.setWords(TextUtils.join(",", arrayList));
            speakUpKeywordsDao.createOrUpdate(speakUpKeywords);
            if (str.equalsIgnoreCase(Constants.acceptTag)) {
                updateAcceptList(true, context);
            } else if (str.equalsIgnoreCase(Constants.declineTag)) {
                updateDeclineList(true, context);
            } else if (str.equalsIgnoreCase(Constants.speakerTag)) {
                updateSpeakerList(true, context);
            } else if (str.equalsIgnoreCase(Constants.autoReplyTag)) {
                updateAutoReplyList(true, context);
            } else if (str.equalsIgnoreCase(Constants.muteTag)) {
                updateMuteList(true, context);
            } else if (str.equalsIgnoreCase(Constants.blockTag)) {
                updateBlockList(true, context);
            } else if (str.equalsIgnoreCase(Constants.whoTag)) {
                updateWhoList(true, context);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

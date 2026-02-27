package com.pu.casttotv.tvcast.screenmirror.tvremote.utils;

import android.accounts.Account;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pu.casttotv.tvcast.screenmirror.tvremote.MyApplication;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.ModelSaleAll;

public class SharedPrefsUtil {
    private static SharedPrefsUtil mInstance;
    private SharedPreferences mSharedPreferences = MyApplication.getInstance().getSharedPreferences("cast_86", 0);

    private SharedPrefsUtil() {
    }

    public static SharedPrefsUtil getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPrefsUtil();
        }
        return mInstance;
    }

    public <T> T get(String str, Class<T> cls) {
        if (cls == String.class) {
            return (T) this.mSharedPreferences.getString(str, "");
        }
        if (cls == Boolean.class) {
            return (T) Boolean.valueOf(this.mSharedPreferences.getBoolean(str, false));
        }
        if (cls == Float.class) {
            return (T) Float.valueOf(this.mSharedPreferences.getFloat(str, 0.0f));
        }
        if (cls == Integer.class) {
            return (T) Integer.valueOf(this.mSharedPreferences.getInt(str, 0));
        }
        if (cls == Long.class) {
            return (T) Long.valueOf(this.mSharedPreferences.getLong(str, 0));
        }
        return null;
    }

    public <T> void put(String str, T t) {
        SharedPreferences.Editor edit = this.mSharedPreferences.edit();
        if (t instanceof String) {
            edit.putString(str, (String) t);
        } else if (t instanceof Boolean) {
            edit.putBoolean(str, ((Boolean) t).booleanValue());
        } else if (t instanceof Float) {
            edit.putFloat(str, ((Float) t).floatValue());
        } else if (t instanceof Integer) {
            edit.putInt(str, ((Integer) t).intValue());
        } else if (t instanceof Long) {
            edit.putLong(str, ((Long) t).longValue());
        }
        edit.apply();
    }

    public ModelSaleAll getSupperSale() {
        String string = this.mSharedPreferences.getString("list_sale", "");
        if (string == null || string.equalsIgnoreCase("")) {
            return null;
        }
        return (ModelSaleAll) new Gson().fromJson(string, new TypeToken<ModelSaleAll>() {
        }.getType());
    }

    public void setAccount(Account account) {
        put("ACCOUNT_DRIVER", new Gson().toJson(account));
    }

    public Account getAccount() {
        String str = (String) get("ACCOUNT_DRIVER", String.class);
        if (str == null || str.equalsIgnoreCase("")) {
            return null;
        }
        return (Account) new Gson().fromJson(str, new TypeToken<Account>() {
        }.getType());
    }
}

package cn.rygel.gd.utils.sp;

import com.google.gson.Gson;

import java.util.Set;

public class CustomSharedPreferences {

    SPUtils.PreferenceKeys mPreferences = null;

    public CustomSharedPreferences(SPUtils.PreferenceKeys preferences) {
        mPreferences = preferences;
    }

    public CustomSharedPreferences putValue(String key, String value) {
        mPreferences.getPreferences().edit().putString(key,value).apply();
        return this;
    }

    public CustomSharedPreferences putValue(String key, Boolean value) {
        mPreferences.getPreferences().edit().putBoolean(key,value).apply();
        return this;
    }

    public CustomSharedPreferences putValue(String key, Float value) {
        mPreferences.getPreferences().edit().putFloat(key,value).apply();
        return this;
    }

    public CustomSharedPreferences putValue(String key, int value) {
        mPreferences.getPreferences().edit().putInt(key,value).apply();
        return this;
    }

    public CustomSharedPreferences putValue(String key, long value) {
        mPreferences.getPreferences().edit().putLong(key,value).apply();
        return this;
    }

    public CustomSharedPreferences putValue(String key, Set<String> values) {
        mPreferences.getPreferences().edit().putStringSet(key,values).apply();
        return this;
    }

    public CustomSharedPreferences putValue(String key, Object obj){
        mPreferences.getPreferences().edit().putString(key,new Gson().toJson(obj)).apply();
        return this;
    }

    public String getString(String key,String defValue) {
        return mPreferences.getPreferences().getString(key,defValue);
    }

    public int getInt(String key,int defValue) {
        return mPreferences.getPreferences().getInt(key,defValue);
    }

    public boolean getBoolean(String key,boolean defValue) {
        return mPreferences.getPreferences().getBoolean(key,defValue);
    }

    public float getFloat(String key,float defValue) {
        return mPreferences.getPreferences().getFloat(key,defValue);
    }

    public long getLong(String key, long defValue) {
        return mPreferences.getPreferences().getLong(key,defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return mPreferences.getPreferences().getStringSet(key,defValue);
    }

    public Object getObject(String key, Object defValue){
        return new Gson().fromJson(getString(key,"{}"),defValue.getClass());
    }

}

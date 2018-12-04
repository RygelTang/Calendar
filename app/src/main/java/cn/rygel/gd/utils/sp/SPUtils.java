package cn.rygel.gd.utils.sp;

import android.content.Context;

public class SPUtils {

    private Context mContext = null;

    private SPUtils(){}

    public static SPUtils getInstance(){
        return Instance.sInstance;
    }

    public SPUtils init(Context context) {
        mContext = context;
        for(PreferenceKeys preferences : PreferenceKeys.values()){
            preferences.setPreferences(mContext.getSharedPreferences(preferences.getName(), Context.MODE_PRIVATE));
        }
        return this;
    }

    public CustomSharedPreferences getCustomSharedPreferences(PreferenceKeys preferences) {
        return new CustomSharedPreferences(preferences);
    }

    private static class Instance {
        private static SPUtils sInstance = new SPUtils();
    }

    public enum PreferenceKeys {

        SETTING("setting");

        String mName = null;
        android.content.SharedPreferences mPreferences = null;

        PreferenceKeys(String name){
            mName = name;
        }

        public String getName() {
            return mName;
        }

        protected android.content.SharedPreferences getPreferences() {
            return mPreferences;
        }

        private void setPreferences(android.content.SharedPreferences preferences) {
            mPreferences = preferences;
        }
    }
}



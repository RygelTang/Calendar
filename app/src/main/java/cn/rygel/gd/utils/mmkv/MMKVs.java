package cn.rygel.gd.utils.mmkv;

import com.tencent.mmkv.MMKV;

public enum MMKVs {

    SETTING("setting"),
    WIDGET("widget");

    private String mDir = "";

    MMKVs(String dir){
        mDir = dir;
    }

    public MMKV getMMKV(){
        return MMKV.mmkvWithID(mDir);
    }

}

package cn.rygel.gd.db;

import io.objectbox.BoxStore;

public class BoxStoreHolder {

    private BoxStore mBoxStore = null;

    private BoxStoreHolder(){}

    public void init(BoxStore boxStore){
        mBoxStore = boxStore;
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }

    public static BoxStoreHolder getInstance() {
        return Instance.sInstance;
    }

    private static class Instance {
        private static BoxStoreHolder sInstance = new BoxStoreHolder();
    }

}

package cn.rygel.gd.db.boxstore;

import cn.rygel.gd.db.model.EventModel;
import cn.rygel.gd.db.model.UserModel;
import io.objectbox.BoxStore;

public class BoxStoreHolder {

    private BoxStore mBoxStore = null;

    private BoxStoreHolder(){}

    public void init(BoxStore boxStore){
        mBoxStore = boxStore;
        UserModel.getInstance().onNewStore(mBoxStore);
        EventModel.getInstance().onNewStore(mBoxStore);
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

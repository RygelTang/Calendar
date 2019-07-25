package cn.rygel.gd.db.model;

import com.orhanobut.logger.Logger;

import java.util.List;

import cn.rygel.gd.db.boxstore.BoxStoreHolder;
import cn.rygel.gd.db.entity.User;
import cn.rygel.gd.db.entity.User_;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.exception.UniqueViolationException;

public class UserModel {

    private BoxStore mBoxStore = BoxStoreHolder.getInstance().getBoxStore();
    private Box<User> mUserBox = mBoxStore.boxFor(User.class);

    private UserModel() { }

    public static UserModel getInstance() {
        return Instance.INSTANCE;
    }

    /**
     * 删除用户
     * @param userName
     */
    public void deleteByName(String userName) {
        Logger.i("object delete user : " + userName);
        User user = getUserByName(userName);
        Logger.i("user found is null? " + (user == null));
        if (user != null) {
            mUserBox.remove(user);
        }
    }

    /**
     * 查询用户
     * @param userName
     * @return
     */
    public User getUserByName(String userName) {
        return mUserBox.query()
                .equal(User_.mUserName, userName)
                .build()
                .findUnique();
    }

    /**
     * 创建用户
     * @param userName
     * @return
     */
    public boolean putUser(String userName) {
        boolean flag;
        try{
            flag = mUserBox.put(new User().setUserName(userName)) >= 0;
        } catch (UniqueViolationException ex){
            Logger.e(ex,ex.getMessage());
            flag = false;
        }
        Logger.i("object box put",userName,flag ? "success" : "fail");
        return flag;

    }

    /**
     * 查询所有用户
     * @return
     */
    public List<User> queryUsers() {
        return mUserBox.query()
                .build()
                .find();
    }

    private static class Instance{
        private static final UserModel INSTANCE = new UserModel();
    }

}

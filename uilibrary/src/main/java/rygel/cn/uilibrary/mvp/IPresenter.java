package rygel.cn.uilibrary.mvp;

public interface IPresenter<V>{

   void bindView(V view);
   void dropView();

}

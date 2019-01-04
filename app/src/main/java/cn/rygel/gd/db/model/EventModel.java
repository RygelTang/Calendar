package cn.rygel.gd.db.model;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.base.DefaultEvent;
import cn.rygel.gd.db.boxstore.BoxStoreHolder;
import cn.rygel.gd.db.entity.Alert;
import cn.rygel.gd.db.entity.Description;
import cn.rygel.gd.db.entity.Event;
import cn.rygel.gd.db.entity.Time;
import cn.rygel.gd.db.entity.Time_;
import cn.rygel.gd.db.entity.User;
import cn.rygel.gd.db.entity.User_;
import cn.rygel.gd.db.filter.TimeFilter;
import cn.rygel.gd.utils.calendar.LunarUtils;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.exception.UniqueViolationException;
import io.objectbox.query.Query;

public class EventModel {

    private BoxStore mBoxStore = BoxStoreHolder.getInstance().getBoxStore();

    private TimeFilter mTimeFilter = new TimeFilter();

    private Gson mGson = new Gson();

    private Box<Event> mEventBox = mBoxStore.boxFor(Event.class);
    private Box<Time> mTimeBox = mBoxStore.boxFor(Time.class);
    private Box<User> mUserBox = mBoxStore.boxFor(User.class);

    private EventModel(){}

    public static EventModel getInstance(){
        return Instance.sInstance;
    }

    /**
     * 查询指定用户的所有日程
     * @param userName
     * @return
     */
    public List<BaseEvent> queryByUser(String userName){
        List<BaseEvent> events = new ArrayList<>();
        final User user = queryUser(userName).findUnique();
        if(user == null){
            Logger.e("query user fail, user not exist");
            return new ArrayList<>();
        }
        for(Event event : user.getEvents()){
            events.add(format2BaseEvent(event));
        }
        return events;
    }

    /**
     * 根据时间范围来查询事件
     * start必须在end之前
     * @param start
     * @param end
     * @return
     */
    public List<BaseEvent> queryInRange(LunarUtils.Solar start, LunarUtils.Solar end){
        List<BaseEvent> events = new ArrayList<>();
        List<Time> times = mTimeBox.query()
                .filter(mTimeFilter
                        .setStartSolar(start)
                        .setEndSolar(end)
                )
                .eager(Time_.mEvent)
                .build()
                .find();
        for(Time time : times){
            events.add(format2BaseEvent(time.getEvent().getTarget()));
        }
        return events;
    }

    /**
     * 删除事件
     * @param event
     */
    public void delete(BaseEvent event){
        Logger.i("object delete event, event id : " + event.getId());
        if(event.getId() >= 0){
            mEventBox.remove(event.getId());
        }
    }

    /**
     * 删除用户
     * @param userName
     */
    public void delete(String userName){
        Logger.i("object delete user : " + userName);
        User user = queryUser(userName).findUnique();
        Logger.i("user found is null? " + (user == null));
        if(user != null){
            mUserBox.remove(user);
        }
    }

    /**
     * 保存集合内所有的事件
     * @param events
     */
    public void putEvents(Collection<BaseEvent> events){
        if(events == null || events.size() == 0){
            return;
        }
        for (BaseEvent event : events){
            putEvent(event);
        }
    }

    /**
     * 创建用户
     * @param userName
     * @return
     */
    public boolean putUser(String userName){
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

    public Query<User> queryUser(String userName){
        return mUserBox.query().equal(User_.mUserName,userName).build();
    }

    /**
     * 保存单个日程
     * @param baseEvent
     * @return
     */
    public boolean putEvent(BaseEvent baseEvent){
        boolean flag = false;
        if(baseEvent == null){
            Logger.e("event to put is null");
            return flag;
        }
        long eventId = mEventBox.put(format2Event(baseEvent));
        flag = eventId >= 0;
        Logger.i("object box put",mGson.toJson(baseEvent),flag ? "success" : "fail");
        return flag;
    }

    private BaseEvent format2BaseEvent(Event event){
        if(event == null){
            return null;
        }
        Time time = event.getTime().getTarget();
        Alert alert = time.getAlert().getTarget();
        BaseEvent baseEvent = time.getDuration() > 0 ? new DefaultEvent() : new BaseEvent();
        if(time.getDuration() > 0){
            ((DefaultEvent) baseEvent).setDuration(time.getDuration());
        }
        baseEvent.setShowNotification(event.isShowNotification());
        baseEvent.setEventType(event.getEventType());
        baseEvent.setName(event.getName());
        baseEvent.setDelayTime(alert.getDelayTime());
        baseEvent.setAdvanceTime(alert.getAdvanceTime());
        baseEvent.setDescription(event.getDescription().getTarget().getDescription());
        baseEvent.setLunarEvent(time.isLunar());
        baseEvent.setEventLunarDate(time.getLunar());
        baseEvent.setEventSolarDate(time.getSolar());
        baseEvent.setTimeZone(time.getTimeZone());
        baseEvent.setStart(time.getStart());
        baseEvent.setUser(event.getUser().get(0).getUserName());
        return baseEvent;
    }

    private Event format2Event(BaseEvent baseEvent){
        Alert alert = new Alert()
                .setAdvanceTime(baseEvent.getAdvanceTime())
                .setDelayTime(baseEvent.getDelayTime());
        Event event = new Event()
                .setEventType(baseEvent.getEventType())
                .setName(baseEvent.getName())
                .setShowNotification(baseEvent.isShowNotification());
        Description description = new Description()
                .setDescription(baseEvent.getDescription());
        Time time = new Time()
                .setLunar(baseEvent.isLunarEvent())
                .setRepeatType(baseEvent.getEventType().mRepeatType)
                .setSolar(baseEvent.getEventSolarDate())
                .setLunar(baseEvent.getEventLunarDate())
                .setStart(baseEvent.getStart())
                .setTimeZone(baseEvent.getTimeZone())
                .setDuration(baseEvent instanceof DefaultEvent ? ((DefaultEvent) baseEvent).getDuration() : -1);
        time.getAlert().setTarget(alert);
        event.getTime().setTarget(time);
        event.getDescription().setTarget(description);
        event.getUser().add(queryUser(baseEvent.getUser()).findUnique());
        return event;
    }

    private static class Instance {
        private static final EventModel sInstance = new EventModel();
    }

}

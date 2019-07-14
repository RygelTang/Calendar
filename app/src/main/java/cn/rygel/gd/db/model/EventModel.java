package cn.rygel.gd.db.model;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.rygel.gd.bean.event.AppointmentEvent;
import cn.rygel.gd.bean.event.BirthdayEvent;
import cn.rygel.gd.bean.event.MeetingEvent;
import cn.rygel.gd.bean.event.MemorialEvent;
import cn.rygel.gd.bean.event.base.BaseEvent;
import cn.rygel.gd.bean.event.base.DefaultEvent;
import cn.rygel.gd.bean.event.base.LocationEvent;
import cn.rygel.gd.bean.event.constants.EventType;
import cn.rygel.gd.db.boxstore.BoxStoreHolder;
import cn.rygel.gd.db.entity.Alert;
import cn.rygel.gd.db.entity.Description;
import cn.rygel.gd.db.entity.Event;
import cn.rygel.gd.db.entity.Location;
import cn.rygel.gd.db.entity.Time;
import cn.rygel.gd.db.entity.Time_;
import cn.rygel.gd.db.entity.User;
import cn.rygel.gd.db.entity.User_;
import cn.rygel.gd.db.filter.RangeTimeFilter;
import cn.rygel.gd.db.filter.TimeFilter;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.exception.UniqueViolationException;
import io.objectbox.query.Query;
import rygel.cn.calendar.bean.Solar;

public class EventModel {

    private BoxStore mBoxStore = BoxStoreHolder.getInstance().getBoxStore();

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
    public List<BaseEvent> queryInRange(Solar start, Solar end){
        List<BaseEvent> events = new ArrayList<>();
        List<Time> times = mTimeBox.query()
                .filter(new RangeTimeFilter(start, end))
                .eager(Time_.mEvent)
                .build()
                .find();
        return queryByTimes(times);
    }

    public List<BaseEvent> queryInDay(Solar date) {
        List<Time> times = mTimeBox.query()
                .filter(new TimeFilter(date))
                .eager(Time_.mEvent)
                .build()
                .find();
        return queryByTimes(times);
    }

    public List<BaseEvent> queryByTimes(List<Time> times) {
        List<BaseEvent> events = new ArrayList<>();
        for(Time time : times){
            Event event = time.getEvent().getTarget();
            if(event != null) {
                events.add(format2BaseEvent(event));
            }
        }
        return events;
    }

    /**
     * 删除事件
     * @param event
     */
    public void delete(BaseEvent event){
        deleteEventByID(event.getId());
    }

    /**
     * 删除用户
     * @param userName
     */
    public void delete(String userName) {
        Logger.i("object delete user : " + userName);
        User user = queryUser(userName).findUnique();
        Logger.i("user found is null? " + (user == null));
        if (user != null) {
            mUserBox.remove(user);
        }
    }

    /**
     * 根据id删除Event
     * @param id
     */
    public void deleteEventByID(long id) {
        Logger.i("object delete event, event id : " + id);
        if(id >= 0){
            mEventBox.remove(id);
        }
    }

    public boolean update(BaseEvent event) {
        deleteEventByID(event.getId());
        return putEvent(event);
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

    public List<User> getUser(){
        return mUserBox.query().build().find();
    }

    public Query<User> queryUser(String userName){
        return mUserBox.query()
                .equal(User_.mUserName,userName)
                .build();
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
        Logger.i("object box put " + baseEvent.getName() + (flag ? " success" : " fail") + " id : " + eventId);
        return flag;
    }

    private BaseEvent format2BaseEvent(Event event){
        if(event == null){
            return null;
        }
        Time time = event.getTime().getTarget();
        Alert alert = time.getAlert().getTarget();
        int eventTypeIndex = EventType.EVENT_TYPE_SUPPORT.indexOf(event.getEventType());
        BaseEvent baseEvent = null;
        switch (eventTypeIndex) {
            case EventType.TYPE_APPOINTMENT:
                AppointmentEvent appointmentEvent = new AppointmentEvent();
                appointmentEvent.setDuration(time.getDuration());
                appointmentEvent.setLocation(event.getLocation().getTarget().getLocation());
                baseEvent = appointmentEvent;
                break;
            case EventType.TYPE_BIRTHDAY:
                baseEvent = new BirthdayEvent();
                break;
            case EventType.TYPE_DEFAULT:
                DefaultEvent defaultEvent = new DefaultEvent();
                defaultEvent.setDuration(time.getDuration());
                baseEvent = defaultEvent;
                break;
            case EventType.TYPE_MEETING:
                MeetingEvent meetingEvent = new MeetingEvent();
                meetingEvent.setDuration(time.getDuration());
                meetingEvent.setLocation(event.getLocation().getTarget().getLocation());
                baseEvent = meetingEvent;
                break;
            case EventType.TYPE_MEMORIAL:
                baseEvent = new MemorialEvent();
                break;
            default:
                baseEvent = new BaseEvent();
                break;
        }
        baseEvent.setId(event.getId());
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
        Event event = new Event()
                .setEventType(baseEvent.getEventType())
                .setName(baseEvent.getName())
                .setShowNotification(baseEvent.isShowNotification());
        Time time = formatTime(baseEvent);
        time.getEvent().setTarget(event);
        event.getTime().setTarget(time);
        Location location = formatLocation(baseEvent);
        if(location != null) {
            event.getLocation().setTarget(location);
        }
        event.getDescription().setTarget(formatDescription(baseEvent));
        User user = queryUser(baseEvent.getUser()).findUnique();
        if(user == null) {
            putUser(baseEvent.getUser());
            user = queryUser(baseEvent.getUser()).findUnique();
            if(user != null) {
                user.getEvents().add(event);
            }
        }
        event.getUser().add(user);
        return event;
    }

    private static Time formatTime(BaseEvent event) {
        Time time = new Time().setLunar(event.isLunarEvent())
                .setRepeatType(event.getEventType().mRepeatType)
                .setSolar(event.getEventSolarDate())
                .setLunar(event.getEventLunarDate())
                .setStart(event.getStart())
                .setTimeZone(event.getTimeZone());
        time.getAlert().setTarget(formatAlert(event));
        int eventTypeIndex = EventType.EVENT_TYPE_SUPPORT.indexOf(event.getEventType());
        switch (eventTypeIndex) {
            case EventType.TYPE_MEETING:
            case EventType.TYPE_APPOINTMENT:
            case EventType.TYPE_DEFAULT:
                DefaultEvent e = (DefaultEvent) event;
                time.setDuration(e.getDuration());
                break;
        }
        return time;
    }

    private static Description formatDescription(BaseEvent event) {
        return new Description().setDescription(event.getDescription());
    }

    private static Location formatLocation(BaseEvent event) {
        if(event instanceof LocationEvent) {
            return new Location().setLocation(((LocationEvent) event).getLocation());
        }
        return null;
    }

    private static Alert formatAlert(BaseEvent event) {
        return new Alert()
                .setAdvanceTime(event.getAdvanceTime())
                .setDelayTime(event.getDelayTime());
    }

    private static class Instance {
        private static final EventModel sInstance = new EventModel();
    }

}

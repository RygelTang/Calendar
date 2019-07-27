package cn.rygel.gd.utils.provider;

import rygel.cn.calendar.bean.Solar;

/**
 * 日程信息提供者
 */
public interface EventInfoProvider {

    boolean hasEvent(Solar solar);

}

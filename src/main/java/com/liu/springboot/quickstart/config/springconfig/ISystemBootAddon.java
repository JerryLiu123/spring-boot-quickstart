package com.liu.springboot.quickstart.config.springconfig;

import org.springframework.core.Ordered;

/**
 * 当需要系启动后执行的方法，在实现此接口后会自动执行
 * @author lgh
 *
 */
public interface ISystemBootAddon extends Ordered{

    /**
     * 在系统准备好后调用
     */
    void onReady();
}

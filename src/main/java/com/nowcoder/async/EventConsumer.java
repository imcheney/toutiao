package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.controller.MessageController;
import com.nowcoder.util.JedisAdaptor;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Consumer在一开始就得初始化好一个event的映射表/路由表, 这样好分配event
 * Created by Chen on 10/05/2017.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private ApplicationContext applicationContext;
    //mapping eventType to a list of due handlers for this type of event.
    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();

    @Autowired
    JedisAdaptor jedisAdaptor;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        //fill config mapping
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry:beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type:eventTypes) {
                    config.computeIfAbsent(type, k -> new ArrayList<EventHandler>());
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> messages = jedisAdaptor.brpop(0, key);
                    for (String msg:messages) {
                        if (msg.equals(key)) {continue;}  //first element is the key
                        EventModel event = JSON.parseObject(msg, EventModel.class);  //memorize this parseObject method
                        EventType eventType = event.getEventType();
                        if (config.containsKey(eventType)==false) {
                            logger.error("No such eventType found!");
                            continue;
                        }
                        for (EventHandler handler:config.get(eventType)) {
                            handler.doHandle(event);
                        }
                    }
                }
            }
        });

        thread.start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

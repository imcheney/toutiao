package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.controller.MessageController;
import com.nowcoder.util.JedisAdaptor;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * producer for the queue
 * Created by Chen on 10/05/2017.
 */
@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    JedisAdaptor jedisAdaptor;

    public boolean fireEvent(EventModel model) {
        try {
            String json = JSONObject.toJSONString(model);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdaptor.lpush(key, json);
            return true;
        } catch (Exception e) {
            logger.error("fire Event error" + e.getMessage());
            return false;
        }
    }
}

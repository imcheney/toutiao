package com.nowcoder.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * 运用Jedis的类
 * Created by Chen on 08/05/2017.
 */
@Service
public class JedisAdaptor implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(JedisAdaptor.class);
    private JedisPool pool = null;  //线程池

    public JedisAdaptor() {
        try {
            afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*配置线程池*/
    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379);
    }

    /**
     * 获取jedis线程
     * @return
     */
    public Jedis getJedis() {
        return pool.getResource();
    }

    public long sadd(String set, String member) {
        Jedis jedis = null;  //函数栈中的jedis数据库线程
        try {
            jedis = getJedis();
            return jedis.sadd(set, member);
        } catch (Exception e) {
            logger.error("sadd发生异常" + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            if (jedis!=null) {
                jedis.close();
            }
        }
    }

    public long srem(String set, String member) {
        Jedis jedis = null;  //函数栈中的jedis数据库线程
        try {
            jedis = getJedis();
            return jedis.srem(set, member);
        } catch (Exception e) {
            logger.error("srem发生异常" + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            if (jedis!=null) {
                jedis.close();
            }
        }
    }

    public boolean sismember(String set, String member) {
        Jedis jedis = null;  //函数栈中的jedis数据库线程
        try {
            jedis = getJedis();
            return jedis.sismember(set, member);
        } catch (Exception e) {
            logger.error("srem发生异常" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (jedis!=null) {
                jedis.close();
            }
        }
    }

    public long scard(String set) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.scard(set);
        } catch (Exception e) {
            logger.error("scard发生异常" + e.getMessage());
            return -1;
        } finally {
            if (jedis!=null) {
                jedis.close();
            }
        }
    }

    /*两个测试方法*/
    public static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, obj));
    }
    public static void main(String[] args) {
        System.out.println("========================BASIC=======================");
        Jedis jedis = new Jedis();
        jedis.flushAll();  //delete all data
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "yello");
        print(1, jedis.get("yello"));

        //验证码等时效性临时数据场景, 可以使用setex, ttl等来做
        jedis.setex("valid", 15, "test123");
        print(2, jedis.ttl("valid"));
        jedis.set("pv","100");
        jedis.incr("pv");
        print(2, jedis.get("pv"));
        jedis.incrBy("pv", 10);
        print(2, jedis.get("pv"));
        //pv等的高并发策略
        //pv的更新放在内存里面做, 每次来一个请求就pv++, 或者用jedis.incr("pv")
        //每隔一秒update一次关系型数据库进行持久化. 用户读取PV从关系数据库中读.

        /*list*/
        System.out.println("====================LIST=========================");
        String lista = "lista";
        for (int i = 0; i < 10; i++) {
            jedis.rpush(lista, "a" + i);
        }
        print(3, jedis.lrange(lista, 0, -1));
        print(4, jedis.llen(lista));
        print(5, jedis.lpop(lista));
        print(6,jedis.lindex(lista, 3));
        print(7, jedis.linsert(lista, BinaryClient.LIST_POSITION.AFTER, "a7", "test1"));
        print(8, jedis.linsert(lista, BinaryClient.LIST_POSITION.AFTER, "a7", "test0"));
        print(9, jedis.lrange(lista, 0, -1));

        /*hash*/
        System.out.println("-------hash--------------------");
        String myUser = "user01";
        jedis.hset(myUser, "username", "TomBlue");
        jedis.hset(myUser, "password", "123456");
        jedis.hset(myUser, "age", "23");
        jedis.hset(myUser, "addr", "Beijing Haidian");
        print(1, jedis.hgetAll(myUser));
        print(2, jedis.hexists(myUser, "addr"));
        print(2, jedis.hexists(myUser, "phone"));
        print(3, jedis.hdel(myUser, "age"));
        print(4, jedis.hkeys(myUser));
        print(4, jedis.hvals(myUser));
        print(5, jedis.hsetnx(myUser, "addr", "Shanghai"));
        print(6, jedis.hsetnx(myUser, "sex", "Male"));
        print(7, jedis.hgetAll(myUser));

        /*set*/
        System.out.println("================SET===============");
        String s1 = "set01", s2 = "set02";
        for (int i = 1; i <= 5 ; i++) {
            jedis.sadd(s1, i+"");
            jedis.sadd(s2, i*i+"");
        }
        print(1, jedis.smembers(s1));
        print(1, jedis.smembers(s2));
        print(2, jedis.sinter(s1, s2));
        print(2, jedis.sunion(s1, s2));
        print(2, jedis.sdiff(s1, s2));  //s1-s2
        print(2, jedis.sdiff(s2, s1));  //s2-s1
        print(3, jedis.sismember(s1, "5"));
        print(3,jedis.scard(s1));
        print(4, jedis.srem(s1, "5"));
        print(5,jedis.scard(s1));
        print(5, jedis.sismember(s1, "5"));
        print(6, jedis.smembers(s1));

        /*优先队列*/
        System.out.println("=========sorted set=============");
        String pq = "pq";
        jedis.zadd(pq, 80, "Liuling");
        jedis.zadd(pq, 75, "Haifeng");
        jedis.zadd(pq, 60, "Qingqing");
        jedis.zadd(pq, 85, "Chenwei");
        jedis.zadd(pq, 82, "Alison");
        print(1, jedis.zcard(pq));
        print(2, jedis.zcount(pq, 80,90));
        print(3, jedis.zscore(pq,"Alison"));
        print(4, jedis.zincrby(pq, 2,"Alison"));
        print(5, jedis.zrange(pq, 0, 2));
        print(5, jedis.zrevrange(pq,1,2));

        print(5, jedis.zrank(pq, "Alison"));
        print(5, jedis.zrevrank(pq, "Alison"));
        //THIS IS SPECIAL
        for (Tuple tuple:jedis.zrangeByScoreWithScores(pq, 80,90)) {
            print(6, tuple.getElement() + ": " + tuple.getScore());
        }

        /* jedis pool*/
        System.out.println("================POOL===============");
        JedisPool pool = new JedisPool();
        for (int i = 0; i < 100; i++) {
            Jedis j = pool.getResource();
            j.get("a");
            System.out.println("POOL" + i);
            j.close();
        }
    }
}

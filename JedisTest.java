package com.icis.jedispg;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

// Jedis 测试类
public class JedisTest {
    private Jedis jedis;
    // 初始化一个链接  客户端 操作Redis
    @Before
    public void initJedis(){
        // 从jedispool中获得jedis 连接多项
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(50);
        jedisPoolConfig.setMaxIdle(10);
        JedisPool jedisPool=new JedisPool(jedisPoolConfig,"127.0.0.1",6379);
        //从连接池获得一个连接
        this.jedis=jedisPool.getResource();

    }
    // jedis 操作  字符串  set get
    @Test
    public void setStringKeyToRedis(){
        jedis.set("username","admin");
        jedis.set("password","123456");
    }

    @Test
    public void setExStringKeyToRedis(){
       this.jedis.setex("13521348721",60,"23gf");
    }
    @Test
    public void getStringKetyFromRedis(){
        String usernameValue = this.jedis.get("username");
        String passwordValue = this.jedis.get("password");
        System.out.println(usernameValue+"----"+passwordValue);
    }

    // 操作list  nginx+redis  高并发   高可用  集群
    @Test
    public void setListKeyToRedis(){
        List<String> myList=new ArrayList<String>();
        myList.add("赵四");
        myList.add("刘能");
        myList.add("宋小宝");
        myList.add("宋晓锋");
        String[] strArr=new String[myList.size()];
        String[] stringArrays = myList.toArray(strArr);
        // 使用jedis 存储一个list集合
        jedis.rpush("mylist",stringArrays);

    }
    @Test
    public void getListKeyFromRedis(){
        List<String> mylist = this.jedis.lrange("mylist", 0, -1);
        System.out.println(mylist);

    }

    //存储map集合到  redis
    @Test
    public void setMapKeyToRedis(){
        Map<String,String> myMap=new HashMap<String, String>();
        myMap.put("name","tom");
        myMap.put("age","23");
        myMap.put("sex","男");
        for (Map.Entry<String, String> entry : myMap.entrySet()) {
            this.jedis.hset("myMap",entry.getKey(),entry.getValue());
        }
    }
    // 获取数据
    @Test
    public void getMapKeyFromRedis(){
        Map<String, String> map = this.jedis.hgetAll("myMap");
        String name = this.jedis.hget("myMap", "name");
        System.out.println(name);

        System.out.println(map);

    }

    // set 集合 存储
    @Test
    public void setKetToRedis(){
        // 定义一个Set 集合
        Set<String> mySet=new HashSet<String>();
        mySet.add("aa");
        mySet.add("bb");
        mySet.add("cc");
        String[] strArr=new String[mySet.size()];
        String[] stringArrays = mySet.toArray(strArr);
        //存储到redis
        this.jedis.sadd("myset",stringArrays);
    }
    // 获取set 数据
    @Test
    public void setKeyFromRedis(){
        Set<String> myset = this.jedis.smembers("girls");
        System.out.println(myset);

    }

    // 排序的set
    @Test
    public void sortedSetTest(){
        // 定义一个Set 集合
       this.jedis.zadd("mysortedSet",3,"貂蝉");
       this.jedis.zadd("mysortedSet",1,"王昭君");
       this.jedis.zadd("mysortedSet",2,"西施");

    }

    @Test
    public void sortedSetGetTest(){
        // 定义一个Set 集合
        Set<String> mysortedSet = this.jedis.zrange("mysortedSet", 0, -1);

        System.out.println(mysortedSet);

    }



    //关闭链接
    @After
    public void closeResources(){
        //归还  给连接池
        this.jedis.close();
    }

}

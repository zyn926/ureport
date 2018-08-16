package com.mfd.controller.chat;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/chatMessage/{userId}")
public class ChartServe {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");	// 日期格式化
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<ChartServe> webSocketSet = new CopyOnWriteArraySet<ChartServe>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String userId;

    //----------单聊---------用户id和websocket的session绑定的路由表
    //@SuppressWarnings("rawtypes")
    //private static Map routeTable = new HashMap<>();

    @OnOpen
    public void open(@PathParam("userId") String userId, Session session){

        // 添加初始化操作
        System.out.println("---初始化----userId:"+userId);
        this.session = session;
        //获取当前登录用户的id
        this.userId = userId;

        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1

        Map<String,String> joinMap = new HashMap<>();
        joinMap.put("date", DATE_FORMAT.format(new Date()));
        joinMap.put("joinInfo","用户"+userId+"加入");
        joinMap.put("count",getOnlineCount()+"");
        for(ChartServe item: webSocketSet) {
            try {
                item.sendMessage(JSON.toJSONString(joinMap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        //---------单聊-----------将用户id和session绑定到路由表
        //绑定之后就可以在其它地方根据id来获取session，这时两个用户私聊就可以实现了
        //routeTable.put(userId, session);
    }

    /**
     * 接受客户端的消息，并把消息发送给所有连接的会话
     * @param message 客户端发来的消息
     * @param session 客户端的会话
     */
    @OnMessage
    public void OnMessage(String message, Session session) {
       Map<String,Object> jsonObject = new HashMap<>();
       jsonObject.put("message",message);
        // 在消息中添加发送日期
        jsonObject.put("date", DATE_FORMAT.format(new Date()));
        // -----------------------把消息发送给所有连接的会话--------------------------------
        //System.out.println("来自客户端的消息"+this.userId+":" + message);
        for(ChartServe item: webSocketSet){
            try {
                //当前用户右侧显示，非本用户左侧显示
                if(this.userId.equals(item.userId)){
                    jsonObject.put("isSelf", true);
                } else{
                    jsonObject.put("isSelf", false);
                }
                // 发送JSON格式的消息
                item.sendMessage(JSON.toJSONString(jsonObject));
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    @OnClose
    public void close() {
        // 添加关闭会话时的操作
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }


    @OnError
    public void error(Throwable t) {
        // 添加处理错误的操作
        System.err.println("发生错误");
        t.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message json消息
     * @throws IOException
     */
    private synchronized  void  sendMessage(String message) throws IOException{
        //getAsyncRemote()和getBasicRemote()确实是异步与同步的区别，大部分情况下，推荐使用getAsyncRemote()
        //this.session.getBasicRemote().sendText(message);//阻塞式的
        this.session.getAsyncRemote().sendText(message);//非阻塞式的
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        ChartServe.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        ChartServe.onlineCount--;
    }

}

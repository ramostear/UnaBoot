package com.ramostear.unaboot.web.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName LoggerWsHandler
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/17 0017 6:38
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Component
public class LoggerWsHandler extends TextWebSocketHandler {

    @Value("${spring.application.name}")
    private String applicationName;

    private static Map<String,WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private static Map<String,Integer> lengthMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionMap.put(session.getId(),session);
        lengthMap.put(session.getId(),1);//默认从第一行开始读取

        //获取日志信息
        new Thread(()->{
           boolean first = true;
           while (sessionMap.get(session.getId()) != null){
               BufferedReader reader = null;
               try{
                   String filePath = System.getProperty("user.home")+"/.una/log/"+new SimpleDateFormat("yyyyMMdd").format(new Date())+"/"+applicationName+".log";
                   reader = new BufferedReader(new FileReader(filePath));
                   Object[] lines = reader.lines().toArray();
                   Object[] copyOfRange = Arrays.copyOfRange(lines,lengthMap.get(session.getId()),lines.length);
                   //对日志进行作色
                   for(int i = 0;i<copyOfRange.length;i++){
                       String line = (String) copyOfRange[i];
                       line = line.replaceAll("&","&amp;")
                               .replaceAll("<","&lg;")
                               .replaceAll(">","&gt;")
                               .replaceAll("\"","&quot;");
                       line = line.replace("DEBUG","<span class='text-blue' style='font-weight:600;'>DEBUG</span>");
                       line = line.replace("INFO","<span class='text-green' style='font-weight:600;'>INFO</span>");
                       line = line.replace("WARN","<span class='text-yellow' style='font-weight:600;'>WARN</span>");
                       line = line.replace("ERROR","<span class='text-danger' style='font-weight:600;'>ERROR</span>");

                       String[] split = line.split("]");
                       if(split.length >=2){
                           String[] subSplit = split[1].split("-");
                           if(subSplit.length>=2){
                               line = split[0]+"]"+"<span style='color:#26e1ff;'>"+subSplit[0]+"</span>"+"-"+subSplit[1];
                           }
                       }
                       Pattern pattern = Pattern.compile("[\\d+][\\d+][\\d+][\\d+]-[\\d+][\\d+]-[\\d+][\\d+] [\\d+][\\d+]:[\\d+][\\d+]:[\\d+][\\d+]");
                       Matcher matcher = pattern.matcher(line);
                       if(matcher.find()){
                           int start = matcher.start();
                           StringBuilder sb = new StringBuilder(line);
                           sb.insert(start,"<br/>");
                           line = sb.toString();
                       }
                       copyOfRange[i] = line;
                   }

                   //存储最新一行的位置
                   lengthMap.put(session.getId(),lines.length);

                   //判断数据量，如果数据量过大，截取数据，只显示200行
                   if(first && copyOfRange.length > 500){
                       copyOfRange = Arrays.copyOfRange(copyOfRange,copyOfRange.length-500,copyOfRange.length);
                       first = false;
                   }

                   String result = StringUtils.join(copyOfRange,"<br/>");

                   send(session,result);
                   Thread.sleep(1000);
               }catch (Exception e){
                   e.printStackTrace();
               }finally {
                   try {
                       reader.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
        }).start();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
       TextMessage msg = new TextMessage(message.getPayload());
       session.sendMessage(msg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.remove(session.getId());
        lengthMap.remove(session.getId());
    }

    private void send(WebSocketSession session, String message){
        TextMessage msg = new TextMessage(message);
        try {
            session.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

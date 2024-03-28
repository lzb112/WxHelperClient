package com.example.wxhk.msg.impl;

import com.example.wxhk.model.PrivateChatMsg;
import com.example.wxhk.msg.IMsgHandler;
import com.example.wxhk.util.HttpAsyncUtil;
import com.example.wxhk.util.HttpSendUtil;
import com.example.wxhk.util.JfUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lzb
 * @date: 2024-03-26
 */
@Slf4j
public class ChatMsgHandler implements IMsgHandler {
    @Override
    public Object handle(PrivateChatMsg chatMsg) {
        log.info("收到私聊：{}，来源:{}", chatMsg.getContent(), chatMsg.getFromUser());
        String msg = chatMsg.getContent();
//        if(chatMsg.getFromUser().endsWith("@chatroom")){
//            String chatUser = msg.substring(0,msg.indexOf(":\n"));
//            String chatmsg = msg.substring(msg.indexOf(":\n")+2);
//            String chatUserName = HttpSendUtil.GetContactByWxid(chatUser).getString("nickname");
//            String roomName = HttpSendUtil.GetContactByWxid(chatMsg.getFromUser()).getString("nickname");
//            HttpSendUtil.发送文本("junL1470" ,"收到来自群("+roomName+")["+chatUserName+"]的消息:\n"+chatmsg);
//        }else{
//            HttpSendUtil.发送文本("junL1470" ,chatMsg.getContent());
//        }
        if(chatMsg.getFromUser().equals("49310072276@chatroom")){
//            String chatUser = msg.substring(0,msg.indexOf(":\n"));
            String chatmsg = msg.substring(msg.indexOf(":\n")+2);
            if(chatmsg.indexOf("jd.com")!=-1){
                chatmsg = JfUtils.changeUrl(chatmsg);
            }
//            String chatUserName = HttpSendUtil.GetContactByWxid(chatUser).getString("nickname");
//            String roomName = HttpSendUtil.GetContactByWxid(chatMsg.getFromUser()).getString("nickname");
            HttpSendUtil.发送文本("48139976383@chatroom" ,chatmsg);
        }

        if(chatMsg.getFromUser().equals("weijun_126516105") || chatMsg.getFromUser().equals("lzblove")){
//            String chatUser = msg.substring(0,msg.indexOf(":\n"));
            String chatmsg = msg;
            if(chatmsg.indexOf("jd.com")!=-1){
                chatmsg = JfUtils.changeUrl(chatmsg);
            }
//            String chatUserName = HttpSendUtil.GetContactByWxid(chatUser).getString("nickname");
//            String roomName = HttpSendUtil.GetContactByWxid(chatMsg.getFromUser()).getString("nickname");
            HttpSendUtil.发送文本("44548320951@chatroom" ,chatmsg);
        }
        return null;
    }
}

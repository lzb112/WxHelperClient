package com.example.wxhk.msg.impl;

import com.example.wxhk.model.PrivateChatMsg;
import com.example.wxhk.msg.IMsgHandler;
import com.example.wxhk.util.HttpSendUtil;
import com.example.wxhk.util.JfUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: wxlinzebin
 * @create: 2024-04-07
 */
@Slf4j
public class MsgRecordHandler implements IMsgHandler {
    @Override
    public Object handle(PrivateChatMsg chatMsg) {
        if(chatMsg.getFromUser().equals("49310072276@chatroom")){
            HttpSendUtil.ForwardMsg(chatMsg.getMsgId(), "48139976383@chatroom");
        }

        if(chatMsg.getFromUser().equals("weijun_126516105") || chatMsg.getFromUser().equals("lzblove")){
            HttpSendUtil.ForwardMsg(chatMsg.getMsgId(), "44548320951@chatroom");
        }
        return null;
    }
}

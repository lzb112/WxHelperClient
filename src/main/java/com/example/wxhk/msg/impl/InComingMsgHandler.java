package com.example.wxhk.msg.impl;

import com.example.wxhk.model.PrivateChatMsg;
import com.example.wxhk.msg.IMsgHandler;
import com.example.wxhk.util.HttpSendUtil;
import com.example.wxhk.util.UtilTools;

import java.util.regex.Pattern;

/**
 * @author: wxlinzebin
 * @create: 2024-03-29
 */
public class InComingMsgHandler  implements IMsgHandler {
    @Override
    public Object handle(PrivateChatMsg chatMsg) {
        if(chatMsg.getFromUser().equals("48139976383@chatroom") || chatMsg.getFromUser().equals("44548320951@chatroom")){
            String msg = "欢迎{}加入暮色团队，让我们一起开启撸货旅程，赚取零花钱。群内走咸鱼结算，到货当天确认收货。群内方案以一号店&京东为主，群管理员为君君，有事可以找她处理";
            String userName = chatMsg.getContent().split("邀请")[1]
                    .replaceAll(Pattern.quote("加入了群聊"), "")
                    .replaceAll(Pattern.quote("\""),"");
            HttpSendUtil.发送文本(chatMsg.getFromUser() , UtilTools.formatString(msg, userName));
        }
        return null;
    }
}

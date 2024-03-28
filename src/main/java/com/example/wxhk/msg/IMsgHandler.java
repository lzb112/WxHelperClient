package com.example.wxhk.msg;

import com.example.wxhk.model.PrivateChatMsg;

public interface IMsgHandler {
    Object handle(PrivateChatMsg chatMsg);
}
package com.example.wxhk.msg.impl;

import com.example.wxhk.model.PrivateChatMsg;
import com.example.wxhk.msg.IMsgHandler;
import com.example.wxhk.util.HttpSendUtil;
import io.netty.handler.codec.base64.Base64Decoder;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.codec.binary.Base64;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: lzb
 * @date: 2024-03-26
 */
@Slf4j
public class ImageMsgHandler implements IMsgHandler {
    @Override
    public Object handle(PrivateChatMsg chatMsg) {
        log.info("收到图片：{}，来源:{}", chatMsg.getContent(), chatMsg.getFromUser());
        if(!chatMsg.getFromUser().equals("49310072276@chatroom"))return null;
        log.info("收到方案群图片，转发开始...");
        JsonObject dbInfo = HttpSendUtil.getDbInfo();
        log.info(dbInfo.encode());
        JsonArray data = dbInfo.getJsonArray("data");
        List<Long> dbhandles = new ArrayList<>(16);
        HttpSendUtil.downloadAttach(chatMsg.getMsgId());
        for (Object datum : data) {
            JsonObject jo = (JsonObject) datum;
            String databaseName = jo.getString("databaseName");
            if(databaseName.startsWith("MSG") && databaseName.endsWith(".db")){
                dbhandles.add(jo.getLong("handle"));
            }
        }
        CompletableFuture.runAsync(()->{
            try {
                for (Long dbhandle : dbhandles) {
                    JsonObject pjson = HttpSendUtil.execSql(dbhandle, "select BytesExtra from MSG where MsgSvrID=" + chatMsg.getMsgId());
                    if(pjson.getJsonArray("data").size()>0){
                        String ency = pjson.getJsonArray("data").getJsonArray(1).getString(0);
                        String imgPath = Base64.decodeStr(ency);
                        log.info(imgPath);
                        imgPath = imgPath.substring(imgPath.indexOf("FileStorage"), imgPath.indexOf(".dat")+4);
                        log.info("imaPath:{}", imgPath);
                        String dataSavePath = HttpSendUtil.getUserInfo().getString("dataSavePath");
                        File image = new File(dataSavePath+imgPath);
                        while (!image.exists())Thread.sleep(500);
                        HttpSendUtil.发送图片("48139976383@chatroom", image.getAbsolutePath());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        return null;
    }
}

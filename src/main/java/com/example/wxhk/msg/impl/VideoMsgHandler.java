package com.example.wxhk.msg.impl;

import com.example.wxhk.model.PrivateChatMsg;
import com.example.wxhk.msg.IMsgHandler;
import com.example.wxhk.util.HttpSendUtil;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.codec.binary.Base64;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

/**
 * @author: lzb
 * @date: 2024-04-06
 */
@Slf4j
public class VideoMsgHandler implements IMsgHandler {
    @SneakyThrows
    @Override
    public Object handle(PrivateChatMsg chatMsg) {
        if(chatMsg.getFromUser().equals("weijun_126516105") || chatMsg.getFromUser().equals("lzblove")){
            this.forward(chatMsg, "44548320951@chatroom");
        }

        if(chatMsg.getFromUser().equals("49310072276@chatroom")){
            this.forward(chatMsg, "48139976383@chatroom");
        }
        return null;
    }

    private void forward(PrivateChatMsg chatMsg, String wxid){
        JsonObject dbInfo = HttpSendUtil.getDbInfo();
        JsonArray data = dbInfo.getJsonArray("data");
        List<Long> dbhandles = new ArrayList<>(16);
        log.info("转发视频msgID:{},wxid:{}",chatMsg.getMsgId(), wxid);
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
                        String videoPath = "";
                        int i =0;
                        do {
                            String ency = pjson.getJsonArray("data").getJsonArray(1).getString(0);
                            videoPath = Base64.decodeStr(ency);
                            if (videoPath.endsWith(".mp4")) {
                                HttpSendUtil.ForwardMsg(chatMsg.getMsgId(), wxid);
                                break;
                            }else{
                                i++;
                                Thread.sleep(500);
                                pjson = HttpSendUtil.execSql(dbhandle, "select BytesExtra from MSG where MsgSvrID=" + chatMsg.getMsgId());
                            }
                        }while(!videoPath.endsWith(".mp4") && i<10);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }


    public static void main(String[] args) {
        String videoPath = Base64.decodeStr("CgQIEBAACgQIBhAAGgsIARIHbHpibG92ZRrOAwgHEskDPG1zZ3NvdXJjZT4KICAgIDxzZWNfbXNnX25vZGU+CiAgICAgICAgPHV1aWQ+ZGIzNmJkNzk3MWNlYjMwNWM2ZTM5MWJmOThmM2IzMDdfPC91dWlkPgogICAgICAgIDxhbG5vZGU+CiAgICAgICAgICAgIDxmcj4xPC9mcj4KICAgICAgICA8L2Fsbm9kZT4KICAgIDwvc2VjX21zZ19ub2RlPgogICAgPHZpZGVvbXNnX3BkIGNkbnZpZGVvdXJsX3NpemU9IjQ1Njk0NTQiIGNkbnZpZGVvdXJsX3Njb3JlX2FsbD0iIiBjZG52aWRlb3VybF9wZF9wcmk9IjMwIiBjZG52aWRlb3VybF9wZD0iMCIgLz4KICAgIDxzaWxlbmNlPjA8L3NpbGVuY2U+CiAgICA8bWVtYmVyY291bnQ+NDwvbWVtYmVyY291bnQ+CiAgICA8c2lnbmF0dXJlPlYxX1prTkRRdHdpfHYxX2VRTXFYME1uPC9zaWduYXR1cmU+CiAgICA8dG1wX25vZGU+CiAgICAgICAgPHB1Ymxpc2hlci1pZCAvPgogICAgPC90bXBfbm9kZT4KPC9tc2dzb3VyY2U+ChokCAISIDEwODIwZjUyYjIzYTM4ZDFlYTU4NDVlMjZkZDVlOGEwGlMIAxJPd2VpanVuXzEyNjUxNjEwNVxGaWxlU3RvcmFnZVxWaWRlb1wyMDI0LTA0XDg0MjZjN2UyYTc0ZTc5NDBkNTMwNzVkOWQ4YWVmOTQ2LmpwZxpTCAQST3dlaWp1bl8xMjY1MTYxMDVcRmlsZVN0b3JhZ2VcVmlkZW9cMjAyNC0wNFw4NDI2YzdlMmE3NGU3OTQwZDUzMDc1ZDlkOGFlZjk0Ni5tcDQ=");
        if(videoPath.endsWith(".mp4")){
            System.out.println(videoPath.substring(videoPath.lastIndexOf("FileStorage")));
        }

    }
}

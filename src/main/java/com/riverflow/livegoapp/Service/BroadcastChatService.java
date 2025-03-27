package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.BroadcastChat;
import com.riverflow.livegoapp.Repository.BroadcastChatRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;


@Service
public class BroadcastChatService {



    @Autowired
    BroadcastChatRepository broadcastChatRepository;


    /*
     *@  채팅 정보
     */
    public BroadcastChat getBroadcastChatByParentId(Long parentId) {

        return broadcastChatRepository.getByParentId(parentId);
    }

    /*
     *@  채팅 정보 저장
     */
    @Transactional
    public HashMap<String, Object> updateBroadcastChat(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String parentIdString  = params.get("parentId");
        String msg  = params.get("message");

        if (params.get("parentId") == null || parentIdString.isEmpty()) {
            empty = true;
        }
        if (params.get("message") == null || msg.isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }

        Long parentId = Long.parseLong(parentIdString);
        BroadcastChat chatInfo = broadcastChatRepository.getByParentId(parentId);
        String actType = "insert";
        Long actId = 0L;
        String message = "";


        if(chatInfo!=null && chatInfo.getId()>0) {
            actId = chatInfo.getId();
            actType = "update";

            String isMessage = chatInfo.getMessage();
            JSONArray jObject = new JSONArray(isMessage);
            jObject.put(msg);
            message = jObject.toString();

        } else {
            JSONArray jObject = new JSONArray();
            jObject.put(msg);
            message = jObject.toString();
        }

        BroadcastChat broadcastChat = BroadcastChat.builder()
                .parentId(parentId)
                .message(message)
                .actType(actType)
                .actId(actId)
                .build();
        broadcastChatRepository.save(broadcastChat);

        result.put("status", "success");

        return result;
    }




}

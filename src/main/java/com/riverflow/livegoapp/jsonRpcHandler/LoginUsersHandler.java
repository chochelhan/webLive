package com.riverflow.livegoapp.jsonRpcHandler;


import java.util.HashMap;
import java.util.Map;

public class LoginUsersHandler {
    public Map<String, String> users = new HashMap<>();


    public HashMap<String, String> addUser(String sessId, String uid) {

        HashMap<String, String> resultMap = new HashMap<>();
        if(users.get(uid)==null) {
            users.put(uid,sessId);
            resultMap.put("status","success");
        } else {
            resultMap.put("status","double");
        }
     
        return resultMap;
    }

    public void deleteUser(String sessId) {
        boolean doubleUser = false;
        String uid = "";
        for(String userId : users.keySet()) {
            String sessionId = users.get(userId);
            if(sessionId.equals(sessId)) {
                doubleUser = true;
                uid = userId;
                break;
            }
        }

        if(doubleUser && !uid.isEmpty()) {
            users.remove(uid);
        }

    }

    public void deleteUserByUid(String uid) {
        users.remove(uid);

    }
    public  HashMap<String, String>  forceDeleteUserAndAddUser(String sessId,String uid) {

        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("logoutSessionId",users.get(uid));

        users.remove(uid);
        users.put(uid,sessId);

        return resultMap;
    }
}

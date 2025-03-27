package com.riverflow.livegoapp.jsonRpcHandler;


import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class StreamRoomHandler {

    public Map<String, Object> rooms = new HashMap<>();
    private String roomHostSessionId = "";

    /**
     * @param method
     * @param datas
     * @param uid
     * @param sessionId
     * @return
     * @ 호스트 참여자 들의 접속시 핸들러
     */
    public HashMap<String, Object> actionHandler(String method, JsonObject datas, String uid, String sessionId) {
        HashMap<String, Object> resultMap = new HashMap<>();
        if (uid.isEmpty()) {
            resultMap.put("status", "fail");
            return resultMap;
        }
        if (sessionId.isEmpty()) {
            resultMap.put("status", "fail");
            return resultMap;
        }
        if (datas.get("roomId") == null) {
            resultMap.put("status", "fail");
            return resultMap;
        }
        String roomId = datas.get("roomId").getAsString();
        if (roomId.isEmpty()) {
            resultMap.put("status", "fail");
            return resultMap;
        }
        String status = "";
        switch (method) {
            case "makeLiveRoom":
                HashMap<String, Object> resultRoom = addRoom(roomId, uid, sessionId);
                status = (String) resultRoom.get("status");
                if (status.equals("isRoom")) {
                    HashMap<String, String> waitUsers = (HashMap<String, String>) resultRoom.get("waitUsers");
                    resultMap.put("waitUsers", waitUsers);
                }
                resultMap.put("status", status);
                break;
            case "joinRoom":
                String accessType = datas.get("accessType").getAsString();
                if (accessType.equals("yes")) {
                    status = joinWaitRoom(roomId, uid, sessionId);
                } else {
                    status = joinRoom(roomId, uid, sessionId);
                }
                resultMap.put("status", status);
                resultMap.put("roomHostSessionId", roomHostSessionId);

                break;
            case "liveClose":
                String userType = datas.get("userType").getAsString();
                if (userType.equals("host")) {
                    HashMap<String, Object> userResult = getUsers(roomId);
                    resultMap.put("users", userResult.get("users"));
                }
                resultMap.put("status", closeRoom(roomId, uid, userType));
                break;

        }
        return resultMap;
    }

    /**
     * @param method
     * @param datas
     * @param uid
     * @return
     * @ 호스트와 참여자들의 정보교환 핸들러
     */
    public HashMap<String, Object> dataActionHandler(String method, JsonObject datas, String uid) {

        HashMap<String, Object> resultMap = new HashMap<>();
        if (uid.isEmpty()) {
            resultMap.put("status", "fail");
            return resultMap;
        }
        if (datas.get("roomId") == null) {
            resultMap.put("status", "fail");
            return resultMap;
        }
        String roomId = datas.get("roomId").getAsString();
        if (roomId.isEmpty()) {
            resultMap.put("status", "fail");
            return resultMap;
        }
        String receiveId = "";
        String userSessionId = "";

        switch (method) {
            /// 호스트가 참여자 입장 여부 결정
            case "userJoinPermit":
                receiveId = datas.get("receiveId").getAsString();
                userSessionId = getUserSessionId(roomId, receiveId);
                if (userSessionId.equals("none")) {
                    resultMap.put("status", "fail");
                } else {
                    String cmd = datas.get("cmd").getAsString();
                    if (cmd.equals("yes")) {
                        changeUserWait(roomId, receiveId);
                    }
                    resultMap.put("userSessionId", userSessionId);
                    resultMap.put("status", "success");
                }


                break;
            case "hostForceOut":
                receiveId = datas.get("sendUid").getAsString();
                userSessionId = getUserSessionId(roomId, receiveId);
                if (userSessionId.equals("none")) {
                    resultMap.put("status", "fail");
                } else {
                    resultMap.put("userSessionId", userSessionId);
                    resultMap.put("status", "success");
                }
                break;
            /// 참여자 전체 정보 가져옴
            case "insertProblem":
            case "insertFdata":
            case "insertQuiz":
            case "insertPoll":
            case "forceResponseEnd":
            case "insertVote":
            case "getJoinUsers":

                HashMap<String, Object> result = getUsers(roomId);
                String status = (String) result.get("status");
                resultMap.put("status", status);
                if (status.equals("success")) {
                    resultMap.put("users", result.get("users"));
                }
                break;
            // 참여자가 호스트에게 정보를 보냄
            case "replayProblem":
            case "replayPoll":
            case "replayVote":
            case "replayQuiz":
                if (rooms.get(roomId) != null) {
                    HashMap<String, Object> roomData = (HashMap<String, Object>) rooms.get(roomId);
                    HashMap<String, String> host = (HashMap<String, String>) roomData.get("host");
                    resultMap.put("hostSessionId", host.get("sessionId"));
                    resultMap.put("status", "success");
                } else {
                    resultMap.put("status", "fail");
                }

                break;

        }
        return resultMap;
    }

    /**
     * @ 방 생성 (roomId 가 존재할시에 host sessionId 정보를 변경함
     */
    public HashMap<String, Object> addRoom(String roomId, String uid, String sessionId) {
        HashMap<String, Object> resultMap = new HashMap<>();
        String otherRoomId = "";
        for (String rId : rooms.keySet()) {
            HashMap<String, Object> roomData = (HashMap<String, Object>) rooms.get(rId);
            HashMap<String, String> host = (HashMap<String, String>) roomData.get("host");
            if (host.get("uid").equals(uid) && !rId.equals(roomId)) {
                otherRoomId = rId;
                break;
            }
        }
        if (!otherRoomId.isEmpty()) {
            rooms.remove(otherRoomId);
        }
        if (rooms.get(roomId) == null) {
            HashMap<String, Object> roomData = new HashMap<>();
            HashMap<String, String> host = new HashMap<>();
            host.put("uid", uid);
            host.put("sessionId", sessionId);
            roomData.put("host", host);

            HashMap<String, String> users = new HashMap<>();
            roomData.put("users", users);

            HashMap<String, String> waitUsers = new HashMap<>();
            roomData.put("waitUsers", waitUsers);
            rooms.put(roomId, roomData);
            resultMap.put("status", "create");
        } else {
            HashMap<String, Object> roomData = (HashMap<String, Object>) rooms.get(roomId);
            HashMap<String, String> host = (HashMap<String, String>) roomData.get("host");
            HashMap<String, String> waitUsers = (HashMap<String, String>) roomData.get("waitUsers");
            resultMap.put("waitUsers", waitUsers);


            host.put("sessionId", sessionId);
            roomData.put("host", host);
            rooms.put(roomId, roomData);
            resultMap.put("status", "isRoom");

        }

        System.out.println(rooms);
        return resultMap;

    }

    /**
     * @ 방에 저장
     */
    public String joinRoom(String roomId, String uid, String sessionId) {
        if (rooms.get(roomId) != null) {
            HashMap<String, Object> roomData = (HashMap<String, Object>) rooms.get(roomId);
            HashMap<String, String> users = (HashMap<String, String>) roomData.get("users");
            users.put(uid, sessionId);
            roomData.put("users", users);
            rooms.put(roomId, roomData);
            HashMap<String, String> host = (HashMap<String, String>) roomData.get("host");
            roomHostSessionId = host.get("sessionId");
            return "success";

        } else {
            return "fail";
        }


    }

    /**
     * @ 대기실에 저장
     */
    public String joinWaitRoom(String roomId, String uid, String sessionId) {
        if (rooms.get(roomId) != null) {
            HashMap<String, Object> roomData = (HashMap<String, Object>) rooms.get(roomId);
            HashMap<String, String> waitUsers = (HashMap<String, String>) roomData.get("waitUsers");
            waitUsers.put(uid, sessionId);
            roomData.put("waitUsers", waitUsers);
            rooms.put(roomId, roomData);

            HashMap<String, String> host = (HashMap<String, String>) roomData.get("host");
            roomHostSessionId = host.get("sessionId");
            return "success";
        } else {
            return "fail";
        }


    }

    /**
     * @ 대기실 정보를 사용자 정보로 변경함
     */
    public void changeUserWait(String roomId, String uid) {
        if (rooms.get(roomId) != null) {
            HashMap<String, Object> roomData = (HashMap<String, Object>) rooms.get(roomId);
            HashMap<String, String> waitUsers = (HashMap<String, String>) roomData.get("waitUsers");
            if (waitUsers.get(uid) != null) {
                String sessionId = waitUsers.get(uid);
                waitUsers.remove(uid);
                HashMap<String, String> users = (HashMap<String, String>) roomData.get("users");
                users.put(uid, sessionId);
                roomData.put("users", users);
                rooms.put(roomId, roomData);

            }
        }
    }

    public String closeRoom(String roomId, String uid, String userType) {

        if (rooms.get(roomId) != null) {
            HashMap<String, Object> roomData = (HashMap<String, Object>) rooms.get(roomId);
            if (userType.equals("host")) {
                HashMap<String, String> host = (HashMap<String, String>) roomData.get("host");
                if (host.get("uid").equals(uid)) {
                    rooms.remove(roomId);
                }
            } else {
                HashMap<String, String> users = (HashMap<String, String>) roomData.get("users");
                if (users.get(uid) != null) {
                    users.remove(uid);
                }
                HashMap<String, String> waitUsers = (HashMap<String, String>) roomData.get("waitUsers");
                if (waitUsers.get(uid) != null) {
                    waitUsers.remove(uid);
                }

            }
            return "success";
        } else {
            return "fail";
        }

    }

    public void deleteRoom(String sessionId) {
        for (String roomId : rooms.keySet()) {

            HashMap<String, Object> roomData = (HashMap<String, Object>) rooms.get(roomId);
            //HashMap<String, String> host = (HashMap<String, String>) roomData.get("host");
            //if(host.get("sessionId").equals(sessionId)) {
            //  rooms.remove(roomId);
            //break;
            //}
            HashMap<String, String> users = (HashMap<String, String>) roomData.get("users");
            Boolean check = false;
            for (String uid : users.keySet()) {
                String sessId = users.get(uid);
                if (sessId.equals(sessionId)) {
                    users.remove(uid);
                    check = true;
                    break;
                }
            }
            HashMap<String, String> waitUsers = (HashMap<String, String>) roomData.get("waitUsers");
            for (String wuid : waitUsers.keySet()) {
                String wsessId = users.get(wuid);
                if (wsessId.equals(sessionId)) {
                    waitUsers.remove(wuid);
                    check = true;
                    break;
                }
            }
            if (check) break;
        }
    }

    /**
     * @param roomId
     * @ 호트스가 참여자 에게 정보 보낼때
     */
    public HashMap<String, Object> getUsers(String roomId) {

        HashMap<String, Object> result = new HashMap<>();
        if (rooms.get(roomId) != null) {
            HashMap<String, Object> roomData = (HashMap<String, Object>) rooms.get(roomId);
            HashMap<String, String> users = (HashMap<String, String>) roomData.get("users");

            if (users != null) {
                result.put("users", users);
                result.put("status", "success");
            } else {
                result.put("status", "empty");
            }
        } else {
            result.put("status", "fail");
        }
        return result;
    }

    /**
     * @ 참여자 세션 아이디 획득
     */
    public String getUserSessionId(String roomId, String receiveId) {
        String result = "none";
        if (rooms.get(roomId) != null) {
            HashMap<String, Object> roomData = (HashMap<String, Object>) rooms.get(roomId);
            HashMap<String, String> users = (HashMap<String, String>) roomData.get("users");
            Boolean check = false;
            if (users.get(receiveId) != null) {
                result = users.get(receiveId);
                check = true;
            }
            if (!check) {
                HashMap<String, String> waitUsers = (HashMap<String, String>) roomData.get("waitUsers");
                if (waitUsers.get(receiveId) != null) {
                    result = waitUsers.get(receiveId);
                }
            }

        }
        return result;
    }
}

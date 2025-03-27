package com.riverflow.livegoapp.jsonRpcHandler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.kurento.jsonrpc.DefaultJsonRpcHandler;
import org.kurento.jsonrpc.Session;
import org.kurento.jsonrpc.Transaction;
import org.kurento.jsonrpc.message.Request;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


//import org.json.JSONObject;

public class StreamJsonRpcHandler extends DefaultJsonRpcHandler<JsonObject> {

    LoginUsersHandler loginUsersHandler = new LoginUsersHandler();
    StreamRoomHandler streamRoomHandler = new StreamRoomHandler();

    private String clientId;
    public Map<String, Session> sessions = new HashMap<>();


    public Map<String, Object> rooms = new HashMap<String, Object>();
    public Map<String, String> roomHosts = new HashMap<String, String>();

    @Override
    public void handleRequest(Transaction transaction, Request<JsonObject> request) throws Exception {

        JsonObject datas = request.getParams();
        String sessId = request.getSessionId();
        String method = request.getMethod();
        String roomNo;
        String uid = "";
        if (datas.get("uid") != null) {
            uid = datas.get("uid").getAsString();
        }
        JSONObject jsonObject = new JSONObject();
        System.out.println(method+"    host--------------------host");

        switch (method) {
            /** 로그인한 유저들 정보 저장 **/
            case "login":// 회원 접근시
                if (!uid.isEmpty() && !clientId.isEmpty()) {

                    HashMap<String, String> userResult = loginUsersHandler.addUser(clientId, uid);
                    jsonObject.put("result", userResult.get("status"));

                } else {
                    jsonObject.put("result", "fail");
                }

                break;

            /** 로그인한 유저 접속 끊을때**/
            case "loginClose":
                if (!uid.isEmpty()) {
                    loginUsersHandler.deleteUserByUid(uid);
                    jsonObject.put("result", "success");
                } else {
                    jsonObject.put("result", "fail");
                }

                break;
            /** 다른곳에서 로그인한 유저 강제 로그아웃 **/
            case "forceLogOut":
                if (!uid.isEmpty() && !sessId.isEmpty()) {
                    HashMap<String, String> userResult = loginUsersHandler.forceDeleteUserAndAddUser(sessId, uid);
                    if (userResult.get("logoutSessionId") != null) {
                        String logoutSessionId = userResult.get("logoutSessionId");
                        if (!logoutSessionId.isEmpty()) {
                            sendNotification(logoutSessionId, "sendResponse", request.getParams());
                        }
                    }
                    jsonObject.put("result", "success");
                } else {
                    jsonObject.put("result", "fail");
                }
                break;

            /**
             *@ 라이브 방송 일대
             **/

            /** 라이브방송 유저 접속 끊을때**/
            case "liveClose":
                String userType = datas.get("userType").getAsString();
                if (!uid.isEmpty()) {
                    HashMap<String, Object> roomResult = streamRoomHandler.actionHandler(method, datas, uid, sessId);
                    if (userType.equals("host")) {
                        HashMap<String, String> userList = (HashMap<String, String>) roomResult.get("users");
                        for (String userId : userList.keySet()) {
                            String sendSessionId = userList.get(userId);
                            if (!sendSessionId.isEmpty()) {
                                System.out.println(sendSessionId);
                                sendNotification(sendSessionId, "sendResponse", request.getParams());
                            }
                        }
                    }
                    jsonObject.put("result", (String) roomResult.get("status"));
                } else {
                    jsonObject.put("result", "fail");
                }
                break;
            case "makeLiveRoom":
            case "joinRoom":
                if (!uid.isEmpty() && !clientId.isEmpty()) {
                    HashMap<String, Object> roomResult = streamRoomHandler.actionHandler(method, datas, uid, clientId);
                    String status = (String) roomResult.get("status");

                    if (method.equals("joinRoom")) {
                        if (status.equals("success")) {
                            String roomHostSessionId = (String) roomResult.get("roomHostSessionId");
                            String accessType = datas.get("accessType").getAsString();
                            if (accessType.equals("yes")) {
                                sendNotification(roomHostSessionId, "sendResponse", request.getParams());
                            }
                        }
                        jsonObject.put("result", status);
                    } else {
                        if (status.equals("isRoom")) {
                            jsonObject.put("waitUsers", roomResult.get("waitUsers"));
                        }
                        jsonObject.put("result", "success");
                    }
                } else {
                    jsonObject.put("result", "fail");
                }
                break;
            case "userJoinPermit": // 관리자가 입장 승인 or 거부
                if (!uid.isEmpty()) {
                    HashMap<String, Object> roomResult = streamRoomHandler.dataActionHandler(method, datas, uid);
                    String status = (String) roomResult.get("status");
                    if (status.equals("success")) {
                        if (roomResult.get("userSessionId") != null) {
                            String sendSessionId = (String) roomResult.get("userSessionId");
                            if (!sendSessionId.isEmpty()) {
                                sendNotification(sendSessionId, "sendResponse", request.getParams());
                            }
                        }
                    }
                    jsonObject.put("result", status);
                } else {
                    jsonObject.put("result", "fail");
                }
                break;
            case "insertProblem":
            case "insertQuiz":
            case "insertFdata":
            case "insertPoll":
            case "insertVote":
            case "forceResponseEnd":
            case "replayProblem":
            case "replayPoll":
            case "replayVote":
            case "replayQuiz":
                if (!uid.isEmpty()) {
                    HashMap<String, Object> roomResult = streamRoomHandler.dataActionHandler(method, datas, uid);
                    String status = (String) roomResult.get("status");
                    if (status.equals("success")) {
                        if (roomResult.get("hostSessionId") != null) {
                            String sendSessionId = (String) roomResult.get("hostSessionId");
                            if (!sendSessionId.isEmpty()) {
                                sendNotification(sendSessionId, "sendResponse", request.getParams());
                            }
                        } else {
                            HashMap<String, String> userList = (HashMap<String, String>) roomResult.get("users");
                            for (String userId : userList.keySet()) {
                                String sendSessionId = userList.get(userId);
                                if (!sendSessionId.isEmpty()) {
                                    sendNotification(sendSessionId, "sendResponse", request.getParams());
                                }
                            }
                            jsonObject.put("userList", userList);
                        }
                    }
                    jsonObject.put("result", status);
                } else {
                    jsonObject.put("result", "fail");
                }
                break;
            case "hostForceOut": // 호스트가 참여자를 강제 퇴장시킴
                System.out.println("--------------- --------------------");
                if (!uid.isEmpty()) {
                    HashMap<String, Object> roomResult = streamRoomHandler.dataActionHandler(method, datas, uid);
                    String status = (String) roomResult.get("status");
                    if (status.equals("success")) {
                        String sendSessionId = (String) roomResult.get("userSessionId");
                        if (!sendSessionId.isEmpty()) {
                            sendNotification(sendSessionId, "sendResponse", request.getParams());
                        }

                    }
                    jsonObject.put("result", status);
                } else {
                    jsonObject.put("result", "fail");
                }
                break;
            case "getJoinUsers":
                if (!uid.isEmpty()) {
                    HashMap<String, Object> roomResult = streamRoomHandler.dataActionHandler(method, datas, uid);
                    String status = (String) roomResult.get("status");
                    if (status.equals("success")) {
                       HashMap<String, String> userList = (HashMap<String, String>) roomResult.get("users");
                        jsonObject.put("userList", userList);
                    }
                    jsonObject.put("result", status);
                } else {
                    jsonObject.put("result", "fail");
                }
                break;

        }
        transaction.sendResponse(jsonObject);
    }

    @Override
    public void afterConnectionEstablished(Session session) throws Exception {
        clientId = (String) session.getSessionId();
        sessions.put(clientId, session);
    }

    @Override
    public void afterConnectionClosed(Session session, String status) throws Exception {

        clientId = (String) session.getSessionId();

        loginUsersHandler.deleteUser(clientId);
        streamRoomHandler.deleteRoom(clientId);
        sessions.remove(clientId);

        // System.out.println(users);

    }

    @Override
    public void handleTransportError(Session session, Throwable exception) throws Exception {
        // Do something useful here
    }

    @Override
    public void handleUncaughtException(Session session, Exception exception) {
        // Do something useful here
    }

    public void sendNotification(String clientId, String method, Object params) throws IOException {
        this.sessions.get(clientId).sendNotification(method, params);
    }
}

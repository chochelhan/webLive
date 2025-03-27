package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.Member;
import com.riverflow.livegoapp.Repository.MemberRepository;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ZoomApiService {

    @Autowired
    MemberRepository memberRepository;

    private String sdkKey = "EuiShpeZ4ARJYrD4tjXnbd6JSBcjPCcNWyvl";
    private String sdkPassword = "wwy54QhGM5SEImyqlIfZbazER2oxt8hrwfp8";


    private String apiKey = "S6GcPGBYT2S6t0zNdDxXeQ";
    private String apiPassword = "XVfdCulmiieSIA8qqlH7em98mKDWCmQA";
    private String redirectUrl = "https://dev1.testpeso.com/zoomApi/redirect";

    private String accessToken = "";

    private String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6IlZNRU01clpOUzVDQVowUXpCU3N0V3ciLCJleHAiOjE2NzI4MDI3MTAsImlhdCI6MTY3MjE5NzkxMH0.WzCrby5mC15V2a-GdFwZcCLUqe0tdA-ijPPRYwqzfMk";

    /**
     * @ 줌 api 를 통해서 미팅 사용자 목록 가져오기
     * @param sessionId
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> getMeetingUserList(String sessionId) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();

        String userSessionURL = "https://api.zoom.us/v2/videosdk/sessions?type=live&from=2022-12-01&to=2022-12-30";
        accessToken = jwtToken;
        String userSessonBody = getMethod(userSessionURL);
        JSONObject sessionObj = new JSONObject(userSessonBody);
        JSONArray sessionList = sessionObj.getJSONArray("sessions");
        String selectSessionId = "";
        for(int i=0; i<sessionList.length(); i++) {
            JSONObject sess = sessionList.getJSONObject(i);
            if(sess.getString("session_key").equals(sessionId)) {
                selectSessionId = sess.getString("id");
            }
        }
        if(!selectSessionId.isEmpty()) {
            String userURL = "https://api.zoom.us/v2/videosdk/sessions/"+selectSessionId+"/users?type=live&page_size=300";
            String body = getMethod(userURL);
            resultMap.put("status","success");
            resultMap.put("userResult",body);

        } else {

            resultMap.put("status","message");
        }


        return resultMap;
    }

    /**
     * @param code
     * @throws Exception
     * @ 줌 로그인
     */
    public HashMap<String, Object> setZoomMemberLogin(String code) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        try {
            String apitoken = apiKey + ":" + apiPassword;
            String basictoken = Base64.getEncoder().encodeToString(apitoken.getBytes());
            String requestURL = "https://zoom.us/oauth/token?grant_type=authorization_code&code=" + code + "&redirect_uri=" + redirectUrl;

            HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
            HttpPost postRequest = new HttpPost(requestURL); //POST 메소드 URL 새성
            postRequest.addHeader("Authorization", "Basic " + basictoken);
            HttpResponse response = client.execute(postRequest);
            //Response 출력
            int resultCode = Math.abs((response.getStatusLine().getStatusCode()) / 100);

            if (resultCode == 2) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);
                HashMap<String, Object> map = setMemberInfoByZoom(body);
                String status = (String) map.get("status");
                if (status.equals("success")) {
                    resultMap.put("zoomMemberInfo", map);
                }
                resultMap.put("status", status);
            } else {
                System.out.println("response is error : " + response.getStatusLine().getStatusCode() + " message " + response.getStatusLine().getReasonPhrase());
                resultMap.put("status", "fail");
            }
        } catch (Exception e) {
            resultMap.put("status", "fail");
        }
        return resultMap;

    }

    /**
     * @param
     * @throws Exception
     * @ 줌 미팅 생성
     */
    public HashMap<String, Object> createMeeting(String subject, Member member) throws Exception {

        HashMap<String, Object> resultMap = new HashMap<>();

        setAccessToken(member);

        String zoomUid = member.getZid();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("topic", subject);
        jsonObject.put("timezone", "Asia/Seoul");
        jsonObject.put("password", "1111");
        jsonObject.put("type", 1); // 1 instant 2 schedule
        // jsonObject.put("agenda", params.getAgenda());

        String paramsString = jsonObject.toString();
        String requestURL = "https://api.zoom.us/v2/users/" + zoomUid + "/meetings";
        String Result = postMethod(requestURL, paramsString);

        JSONObject mObj = new JSONObject(Result);
        Long meetingNo = mObj.getLong("id");


        String zakURL = "https://api.zoom.us/v2/users/me/token?type=zak";
        String zakResult = getMethod(zakURL);

        JSONObject zakObject = new JSONObject(zakResult);
        String zak = zakObject.getString("token");

        resultMap.put("meetingNo", meetingNo);
        resultMap.put("zak", zak);

        return resultMap;

    }


    /**
     * @ zoom live 생성시 필요한 signkey
     */
    public HashMap<String, String> getSdkKeyPasswd() {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("sdkKey", sdkKey);
        resultMap.put("sdkPassword", sdkPassword);

        return resultMap;

    }

    // 토큰 셋팅 또는 만료시에 새토큰 발급
    public void setAccessToken(Member member) {

        accessToken = member.getAccessToken();
        int expires = member.getExpires();
        LocalDateTime baseDate = member.getUpdateAt();
        Long baseTime = Timestamp.valueOf(baseDate).getTime() / 1000;
        Long tokenTime = baseTime + (expires - 600);
        long nowTime = new Date().getTime();
        nowTime = nowTime / 1000;

        if (nowTime > tokenTime) {
            String requestURL = "https://zoom.us/oauth/token";
            String apitoken = apiKey + ":" + apiPassword;
            String basictoken = Base64.getEncoder().encodeToString(apitoken.getBytes());
            String refreshToken = member.getRefreshToken();

            try {
                List<NameValuePair> nameValues = new ArrayList<NameValuePair>(2);
                nameValues.add(new BasicNameValuePair("grant_type", "refresh_token"));
                nameValues.add(new BasicNameValuePair("refresh_token", refreshToken));

                HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
                HttpPost postRequest = new HttpPost(requestURL); //POST 메소드 URL 새성
                postRequest.addHeader("Authorization", "Basic " + basictoken);

                postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
                postRequest.setEntity(new UrlEncodedFormEntity(nameValues)); //json 메시지 입력

                HttpResponse response = client.execute(postRequest);
                //Response 출력
                int resultCode = Math.abs((response.getStatusLine().getStatusCode()) / 100);

                if (resultCode == 2) {
                    ResponseHandler<String> handler = new BasicResponseHandler();
                    String body = handler.handleResponse(response);

                    JSONObject jObject = new JSONObject(body);
                    accessToken = jObject.getString("access_token");
                    String newRefreshToken = jObject.getString("refresh_token");
                    int newExpires = jObject.getInt("expires_in");
                    LocalDateTime now = LocalDateTime.now();

                    memberRepository.updateSQLMemberZoomToken(accessToken, newRefreshToken, newExpires, now, member.getId());

                } else {
                    System.out.println("response is error : " + response.getStatusLine().getStatusCode() + " message " + response.getStatusLine().getReasonPhrase());
                    //return "error";
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }


    }

    // 줌에서 회원정보 가져오기
    private HashMap<String, Object> setMemberInfoByZoom(String tokenInfo) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject jObject = new JSONObject(tokenInfo);
            accessToken = jObject.getString("access_token");
            String refreshToken = jObject.getString("refresh_token");
            int expires = jObject.getInt("expires_in");

            String requestURL = "https://api.zoom.us/v2/users/me";
            HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
            HttpGet getRequest = new HttpGet(requestURL); //GET 메소드 URL 생성
            getRequest.addHeader("Authorization", "Bearer " + accessToken);

            HttpResponse response = client.execute(getRequest);
            //Response 출력
            int resultCode = Math.abs((response.getStatusLine().getStatusCode()) / 100);
            if (resultCode == 2) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);
                JSONObject jobj = new JSONObject(body);


                String zid = jobj.getString("id");
                String fName = jobj.getString("first_name");
                String lName = jobj.getString("last_name");
                String email = jobj.getString("email");


                map.put("accessToken", accessToken);
                map.put("refreshToken", refreshToken);
                map.put("expires", expires);
                map.put("zid", zid);
                map.put("name", fName + lName);
                map.put("email", email);
                map.put("status", "success");

            } else {
                System.out.println("response is error : " + response.getStatusLine().getStatusCode());
                map.put("status", "error");
            }
        } catch (Exception e) {
            System.out.println("response is error : " + e.toString());
            map.put("status", "error");
        }
        return map;
    }


    private String getMethod(String requestURL) {
        try {
            HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
            HttpGet getRequest = new HttpGet(requestURL); //GET 메소드 URL 생성
            if(!accessToken.isEmpty()) {
                getRequest.addHeader("Authorization", "Bearer " + accessToken);
            }


            HttpResponse response = client.execute(getRequest);

            //Response 출력
            int resultCode = Math.abs((response.getStatusLine().getStatusCode()) / 100);

            if (resultCode == 2) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);
                return body;
            } else {
                System.out.println("response is error : " + response.getStatusLine().getStatusCode());
                return "error";
                //return "error";

            }

        } catch (Exception e) {
            System.out.println("error " + e.toString());
            return "error";
        }
    }

    private String postMethod(String requestURL, String jsonMessage) throws Exception {

        try {
            HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
            HttpPost postRequest = new HttpPost(requestURL); //POST 메소드 URL 새성
            postRequest.addHeader("Authorization", "Bearer " + accessToken);
            postRequest.setHeader("Content-Type", "application/json; charset=UTF-8");
            postRequest.setEntity(new StringEntity(jsonMessage, "UTF-8")); //json 메시지 입력

            HttpResponse response = client.execute(postRequest);
            //Response 출력
            int resultCode = Math.abs((response.getStatusLine().getStatusCode()) / 100);

            if (resultCode == 2) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);
                return body;
            } else {
                return "response is error : " + response.getStatusLine().getStatusCode() + " message " + response.getStatusLine().getReasonPhrase();
                //return "error";
            }
        } catch (Exception e) {
            return "error";
            //System.err.println(e.toString());
        }
    }

    private String putMethod(String requestURL, String jsonMessage) {

        try {
            HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
            HttpPut putRequest = new HttpPut(requestURL); //POST 메소드 URL 새성
            putRequest.addHeader("Authorization", "Bearer " + accessToken);
            putRequest.setHeader("Content-Type", "application/json; charset=UTF-8");
            putRequest.setEntity(new StringEntity(jsonMessage, "UTF-8")); //json 메시지 입력

            HttpResponse response = client.execute(putRequest);
            //Response 출력
            int resultCode = Math.abs((response.getStatusLine().getStatusCode()) / 100);

            if (resultCode == 2) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);
                return body;
            } else {
                return "response is error : " + response.getStatusLine().getStatusCode() + " message " + response.getStatusLine().getReasonPhrase();
                //return "error";
            }
        } catch (Exception e) {
            return "error";
            //System.err.println(e.toString());
        }
    }

    private String postMethodWithoutToken(String requestURL, String jsonMessage) {

        try {
            String apitoken = apiKey + ":" + apiPassword;
            String basictoken = Base64.getEncoder().encodeToString(apitoken.getBytes());

            HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
            HttpPost postRequest = new HttpPost(requestURL); //POST 메소드 URL 새성
            postRequest.addHeader("Authorization", "Basic " + basictoken);
            postRequest.setHeader("Content-Type", "application/json; charset=UTF-8");
            postRequest.setEntity(new StringEntity(jsonMessage, "UTF-8")); //json 메시지 입력

            HttpResponse response = client.execute(postRequest);
            //Response 출력
            int resultCode = Math.abs((response.getStatusLine().getStatusCode()) / 100);

            if (resultCode == 2) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);
                return body;
            } else {
                return "response is error : " + response.getStatusLine().getStatusCode() + " message " + response.getStatusLine().getReasonPhrase();
                //return "error";
            }
        } catch (Exception e) {
            return "error";
            //System.err.println(e.toString());
        }
    }

    private String deleteMethod(String requestURL) {
        try {
            HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
            HttpDelete deleteRequest = new HttpDelete(requestURL); //GET 메소드 URL 생성
            deleteRequest.addHeader("Authorization", "Bearer " + accessToken);

            HttpResponse response = client.execute(deleteRequest);

            //Response 출력
            int resultCode = Math.abs((response.getStatusLine().getStatusCode()) / 100);

            if (resultCode == 2) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);

                return body;
            } else {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);


                return "response is error : " + response.getStatusLine().getStatusCode();
                //return "error";

            }

        } catch (Exception e) {
            return "error";
        }
    }
/*
    private String getAccessToken(Member member) throws ParseException {
        if (!StringUtils.isEmpty(member.getZoomId())) {
            String dbAccessToken = member.getZoomToken();
            String expires = member.getZoomTokenExpire();
            String refreshToken = member.getZoomRefreshToken();
            Date date = new Date();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date tokenDate = transFormat.parse(expires);
            long diff = date.getTime() - tokenDate.getTime();
            long sec = diff / 1000;
            if (sec > 0) { // 토큰 만료됨
                try {
                    String apitoken = apiKey + ":" + apiSecret;
                    String basictoken = Base64.getEncoder().encodeToString(apitoken.getBytes());
                    String requestURL = "https://zoom.us/oauth/token?grant_type=refresh_token&refresh_token=" + refreshToken;
                    HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
                    HttpPost postRequest = new HttpPost(requestURL); //POST 메소드 URL 새성
                    postRequest.addHeader("Authorization", "Basic " + basictoken);
                    HttpResponse response = client.execute(postRequest);
                    //Response 출력
                    int resultCode = Math.abs((response.getStatusLine().getStatusCode()) / 100);

                    if (resultCode == 2) {
                        ResponseHandler<String> handler = new BasicResponseHandler();
                        String body = handler.handleResponse(response);
                        JSONObject jObject = new JSONObject(body);

                        String refreshAccessToken = jObject.getString("access_token");
                        String refreshRefreshToken = jObject.getString("refresh_token");
                        int refreshExpires = jObject.getInt("expires_in");

                        accessToken = refreshAccessToken;

                        Member m = new Member();
                        m.setZoomId(member.getZoomId());
                        m.setZoomToken(refreshAccessToken);
                        m.setZoomRefreshToken(refreshRefreshToken);
                        Date d = new Date();
                        String expireDate = transFormat.format(date.getTime() + ((refreshExpires - 60) * 1000));
                        m.setZoomTokenExpire(expireDate);
                        m.setUserId(member.getUserId());
                        memberSerivce.updateZoomData(m);
                        return "succ";
                    } else {
                        System.out.println("response is error : " + response.getStatusLine().getStatusCode() + " message " + response.getStatusLine().getReasonPhrase());
                        return "unusedtoken";
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                    return "fail";
                    //System.err.println(e.toString());
                }

            } else {
                accessToken = dbAccessToken;
                return "succ";
            }
        } else {
            accessToken = null;
            return "noLogin";
        }
    }
*/

}

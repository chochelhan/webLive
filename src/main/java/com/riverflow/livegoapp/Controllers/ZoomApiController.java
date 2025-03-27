package com.riverflow.livegoapp.Controllers;


import com.riverflow.livegoapp.Entity.Member;
import com.riverflow.livegoapp.Service.MemberService;
import com.riverflow.livegoapp.Service.ZoomApiService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;

@RestController
@RequestMapping("/zoomApi/")
public class ZoomApiController {

    @Autowired
    protected ZoomApiService zoomApiService;

    @Autowired
    protected MemberService memberService;

    /**
     * 줌 리다이렉트 (사용자 로그인 및 회원가입 시킨다)
     */
    @GetMapping("redirect")
    public String redirect(@RequestParam(value = "code", required = true) String code, @RequestParam(value = "state", required = false) String userId) throws Exception {

        HashMap<String,Object> resultMap = new HashMap<>();
        HashMap<String,Object> result =  zoomApiService.setZoomMemberLogin(code);


        String status = (String) result.get("status");
        JSONObject jsonObject = new JSONObject();
        if(status.equals("success")) {

            HashMap<String,Object> zoomResult = (HashMap<String,Object>) result.get("zoomMemberInfo");
            String zid = (String) zoomResult.get("zid");
            Member isMember = memberService.getMemberByZid(zid);
            if (isMember != null && !isMember.getUid().isEmpty()) {
                zoomResult.put("updateType", "zoom");
                resultMap = memberService.loginByZoom(isMember, zoomResult);
            } else {
                resultMap = memberService.insertMemberByZoom(zoomResult);
            }
            jsonObject.put("result",resultMap);
        }
        jsonObject.put("status",status);
        return "<script>window.opener.setLoginResult('"+jsonObject+"');window.close();</script>";
    }


}

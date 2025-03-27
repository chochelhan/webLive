package com.riverflow.livegoapp.Controllers;


import com.riverflow.livegoapp.Entity.Member;
import com.riverflow.livegoapp.Service.MemberService;
import com.riverflow.livegoapp.Service.ZoomApiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/controller/zoomApi/")
public class BroadcastZoomApiController {

    @Autowired
    protected ZoomApiService zoomApiService;

    @Autowired
    protected MemberService memberService;

    /**
     * 줌 미팅 사용자의 목록
     */
    @PostMapping("getUserList")
    public ResponseEntity<HashMap<String, Object>> getUserList(@RequestBody HashMap<String, String> params) throws Exception {

        String sessionId = params.get("sessionKey");
        HashMap<String, Object> resultMap = zoomApiService.getMeetingUserList(sessionId);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


}

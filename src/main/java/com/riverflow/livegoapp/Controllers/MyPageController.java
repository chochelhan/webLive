package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Service.BroadcastService;
import com.riverflow.livegoapp.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
@RestController
@RequestMapping("/api/controller/myPage/")
public class MyPageController {

    @Autowired
    protected MemberService memberService;

    @Autowired
    protected BroadcastService broadcastService;


    /**
     * 방송 내역
     */
    @PostMapping("getMyBroadcastList")
    public ResponseEntity<HashMap<String, Object>> getMyBroadcastList(@RequestBody HashMap<String, String> params, Principal principal)  {

        String uid = principal.getName();
        HashMap<String, Object> result = broadcastService.getMyBroadcastList(uid);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }



}

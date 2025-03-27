package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;

@RestController
@RequestMapping("/api/controller/member/")
public class MemberController {

    @Autowired
    MemberService memberService;

    @PostMapping("login")
    public ResponseEntity<HashMap<String, Object>> memberLogin(@RequestBody HashMap<String, String> params) {
        String uid = params.get("uid");
        String upass = params.get("upass");
        HashMap<String, Object> resultMap = memberService.memberLogin(uid,upass);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }
}

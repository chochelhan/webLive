package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Service.BroadcastChatService;
import com.riverflow.livegoapp.Service.BroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

@RestController
@RequestMapping("/api/controller/broadcast/")
public class BroadcastController {

    @Autowired
    BroadcastService broadcastService;



    @Autowired
    BroadcastChatService broadcastChatService;
    /**
     * 로고 이미지 가져오기
     * @param logo
     * @return
     */
    @GetMapping("getLogo")
    public  @ResponseBody byte[] getLogo(@RequestParam(name = "logo") String logo) throws IOException {

        byte[] imageUrl = broadcastService.getLogo(logo);
        return imageUrl;
    }

    /*
     *@ 채팅 기록
     */
    @PostMapping("updateChat")
    public ResponseEntity<HashMap<String, Object>> updateChat(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastChatService.updateBroadcastChat(params);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /*
     *@ 방송신고 => 답변 받는자 (관리자)
     */
    @PostMapping("insertBadReport")
    public ResponseEntity<HashMap<String, Object>> insertBadReport(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}

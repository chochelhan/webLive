package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Service.BroadcastCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/controller/broadcastComment/")
public class BroadcastCommentController {

    @Autowired
    BroadcastCommentService broadcastCommentService;


    /**
     * @ 댓글 목록
     * @param params
     * @return
     */
    @PostMapping("getRepleList")
    public ResponseEntity<HashMap<String, Object>> getRepleList(@RequestBody HashMap<String, String> params) {

        Long pid = Long.parseLong(params.get("pid"));
        HashMap<String, Object> resultMap = broadcastCommentService.getBroadcastCommentList(pid);
      
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    
    /**
     * @ 댓글  등록/수정
     * @param params
     * @return
     */
    @PostMapping("updateReple")
    public ResponseEntity<HashMap<String, Object>> updateReple(@RequestBody HashMap<String, String> params) {
        HashMap<String, Object> resultMap = broadcastCommentService.updateBroadcastComment(params);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @ 댓글 삭제
     * @param params
     * @return
     */
    @PostMapping("deleteReple")
    public ResponseEntity<HashMap<String, Object>> deleteReple(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastCommentService.deleteBroadcastComment(params);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


}

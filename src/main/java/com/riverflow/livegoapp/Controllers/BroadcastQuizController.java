package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Entity.BroadcastUserResponse;
import com.riverflow.livegoapp.Service.BroadcastQuizService;
import com.riverflow.livegoapp.Service.BroadcastUserResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/controller/broadcastQuiz/")
public class BroadcastQuizController {

    @Autowired
    BroadcastQuizService broadcastQuizService;

    @Autowired
    BroadcastUserResponseService broadcastUserResponseService;

    /**
     * 퀴즈등록(수기등록)
     */
    @PostMapping("insertQuiz")
    public ResponseEntity<HashMap<String, Object>> insertQuiz(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = broadcastQuizService.insertBroadcastQuiz(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     * 퀴즈정보
     */
    @PostMapping("getQuiz")
    public ResponseEntity<HashMap<String, Object>> getQuizData(@RequestBody HashMap<String, String> params) {

        String idString = params.get("id");
        Long id = Long.parseLong(idString);
        HashMap<String, Object> resultMap = broadcastQuizService.getBroadcastQuizId(id);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * 퀴즈 상태변경
     */
    @PostMapping("updateQuizStatus")
    public ResponseEntity<HashMap<String, Object>> updateQuizStatus(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastQuizService.updateBroadcastQuizByStatus(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     * 참여자의 설문 답변
     */
    @PostMapping("insertQuizReply")
    public ResponseEntity<HashMap<String, Object>> insertQuizReply(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = broadcastUserResponseService.insertQuizReply(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     *  방송 종료후 퀴즈목록
     */
    @PostMapping("getQuizAfterList")
    public ResponseEntity<HashMap<String, Object>> getQuizAfterList(@RequestBody HashMap<String, String> params) {

        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        HashMap<String, Object> resultMap = broadcastQuizService.getBroadcastQuizByParentId(parentId);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * 퀴즈 삭제
     */
    @PostMapping("deleteQuizAfter")
    public ResponseEntity<HashMap<String, Object>> deleteQuizAfter(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastQuizService.deleteBroadcastQuiz(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }
    /**
     *  퀴즈 상세정보
    */
    @PostMapping("getQuizWithUserReport")
    public ResponseEntity<HashMap<String, Object>> getQuizWithUserReport(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();

        String idString = params.get("id");
        Long id = Long.parseLong(idString);
        HashMap<String, Object> examMap  = broadcastQuizService.getBroadcastQuizId(id);
        resultMap.put("info",examMap.get("info"));
        List<BroadcastUserResponse> dataList = broadcastUserResponseService.getReplyList(id,"quiz");
        resultMap.put("dataList",dataList);
        resultMap.put("status","success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}

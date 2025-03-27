package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Entity.BroadcastUserResponse;
import com.riverflow.livegoapp.Service.BroadcastExamService;
import com.riverflow.livegoapp.Service.BroadcastUserResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/controller/broadcastExam/")
public class BroadcastExamController {

    @Autowired
    BroadcastExamService broadcastExamService;

    @Autowired
    BroadcastUserResponseService broadcastUserResponseService;

    /**
     * 시험등록(수기등록)
     */
    @PostMapping("insertDirect")
    public ResponseEntity<HashMap<String, Object>> insertLive(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = broadcastExamService.inseartBroadcastExam(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * 시험수정
     */
    @PostMapping("updateDirect")
    public ResponseEntity<HashMap<String, Object>> updateLive(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = broadcastExamService.updateBroadcastExam(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * 시험목록
     */
    @PostMapping("getExamList")
    public ResponseEntity<HashMap<String, Object>> getExamList(@RequestBody HashMap<String, String> params) {

        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        HashMap<String, Object> resultMap = broadcastExamService.getBroadcastExamByParentId(parentId);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * 시험정보
     */
    @PostMapping("getExamData")
    public ResponseEntity<HashMap<String, Object>> getExamData(@RequestBody HashMap<String, String> params) {

        String idString = params.get("id");
        Long id = Long.parseLong(idString);
        HashMap<String, Object> resultMap = broadcastExamService.getBroadcastExamId(id);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * 시험정보 상태변경
     */
    @PostMapping("updateExamStatus")
    public ResponseEntity<HashMap<String, Object>> updateExamStatus(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastExamService.updateBroadcastExamByStatus(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     * 참여자의 시험 답변
     */
    @PostMapping("insertExamReply")
    public ResponseEntity<HashMap<String, Object>> insertExamReply(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = broadcastUserResponseService.insertExamReply(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * 시험정보 삭제
     */
    @PostMapping("deleteExamAfter")
    public ResponseEntity<HashMap<String, Object>> deleteExamAfter(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastExamService.deleteBroadcastExam(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     * 방소 종료후에 시험 목록
     */
    @PostMapping("getExamAfterList")
    public ResponseEntity<HashMap<String, Object>> getExamAfterList(@RequestBody HashMap<String, String> params) {

        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        HashMap<String, Object> resultMap = broadcastExamService.getBroadcastExamByParentId(parentId);


        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * 방소 종료후에 시험 목록 상세
     */
    @PostMapping("getExamWithUser")
    public ResponseEntity<HashMap<String, Object>> getExamWithUser(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();
        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);

        String idString = params.get("id");
        Long id = Long.parseLong(idString);
        HashMap<String, Object> examMap  = broadcastExamService.getBroadcastExamId(id);
        resultMap.put("info",examMap.get("info"));
        List<BroadcastUserResponse> dataList = broadcastUserResponseService.getReplyList(id,"exam");
        resultMap.put("dataList",dataList);
        resultMap.put("status","success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * 방소 종료후에 사용자 시험 결과
     */
    @PostMapping("getExamWithUserReport")
    public ResponseEntity<HashMap<String, Object>> getExamWithUserReport(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();
        String pidString = params.get("pid");
        Long pid = Long.parseLong(pidString);

        String idString = params.get("id");
        Long id = Long.parseLong(idString);

        HashMap<String, Object> examMap  = broadcastExamService.getBroadcastExamId(pid);
        resultMap.put("info",examMap.get("info"));
        BroadcastUserResponse replyInfo = broadcastUserResponseService.getReplyInfo(id);
        resultMap.put("replyInfo",replyInfo);
        resultMap.put("status","success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     * xlxs 샘플파일 가져오기
     *
     * @return
     */
    @GetMapping("getSample")
    public void getSample(HttpServletResponse response) throws IOException {

        broadcastExamService.getSample(response);
    }
}

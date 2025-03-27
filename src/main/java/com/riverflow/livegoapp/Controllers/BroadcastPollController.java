package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Entity.Broadcast;
import com.riverflow.livegoapp.Entity.BroadcastJoinUsers;
import com.riverflow.livegoapp.Entity.BroadcastPoll;
import com.riverflow.livegoapp.Entity.BroadcastUserResponse;
import com.riverflow.livegoapp.Service.BroadcastJoinUserService;
import com.riverflow.livegoapp.Service.BroadcastPollService;
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
@RequestMapping("/api/controller/broadcastPoll/")
public class BroadcastPollController {

    @Autowired
    BroadcastPollService broadcastPollService;

    @Autowired
    BroadcastUserResponseService broadcastUserResponseService;

    @Autowired
    BroadcastJoinUserService broadcastJoinUserService;


    @PostMapping("insertPoll")
    public ResponseEntity<HashMap<String, Object>> insertPoll(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = broadcastPollService.inseartBroadcastPoll(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @ 설문조사 정보 가져오기
     * @param params
     * @return
     */
    @PostMapping("getPollListByParentId")
    public ResponseEntity<HashMap<String, Object>> getPollListByParentId(@RequestBody HashMap<String, String> params) {

        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        HashMap<String, Object> resultMap = broadcastPollService.getBroadcastPollByParentId(parentId);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @ 설문조사 정보 상태변경
     * @param params
     * @return
     */
    @PostMapping("updatePollStatus")
    public ResponseEntity<HashMap<String, Object>> updatePollStatus(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastPollService.updatePollStatus(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * 참여자의 설문 답변
     */
    @PostMapping("insertPollReply")
    public ResponseEntity<HashMap<String, Object>> insertPollReply(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = broadcastUserResponseService.insertPollReply(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }



    /**
     *  방송 종료후 설문 답변내역
     */
    @PostMapping("getPollAfterInfo")
    public ResponseEntity<HashMap<String, Object>> getPollAfterInfo(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();
        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        HashMap<String, Object> pollMap = broadcastPollService.getBroadcastPollByParentId(parentId);

        BroadcastPoll info = (BroadcastPoll) pollMap.get("info");
        resultMap.put("info",info);

        List<BroadcastUserResponse> dataList = broadcastUserResponseService.getReplyList(info.getId(),"poll");
        resultMap.put("dataList",dataList);
        resultMap.put("status","success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     *  방송 종료후 설문 개인별 목록
     */
    @PostMapping("getPollWithUserList")
    public ResponseEntity<HashMap<String, Object>> getPollWithUserList(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();
        String idString = params.get("id");
        Long id = Long.parseLong(idString);
        List<BroadcastUserResponse> dataList = broadcastUserResponseService.getReplyList(id,"poll");
        resultMap.put("dataList",dataList);

        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        HashMap<String, Object> userMap  = broadcastJoinUserService.getBroadcastJoinUsersByParentId(parentId);
        resultMap.put("userList",userMap.get("list"));
        resultMap.put("status","success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     *  방송 종료후 개인별 상세 내역
     */
    @PostMapping("getPollWithUserInfo")
    public ResponseEntity<HashMap<String, Object>> getPollWithUserInfo(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();

        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        HashMap<String, Object> pollMap = broadcastPollService.getBroadcastPollByParentId(parentId);

        BroadcastPoll info = (BroadcastPoll) pollMap.get("info");
        resultMap.put("info",info);


        String replyIdString = params.get("replyId");
        Long id = Long.parseLong(replyIdString);
        BroadcastUserResponse data = broadcastUserResponseService.getReplyInfoWithUserEmail(id,parentId);
        resultMap.put("responseInfo",data);

        resultMap.put("status","success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}

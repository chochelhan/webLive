package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Entity.BroadcastPoll;
import com.riverflow.livegoapp.Entity.BroadcastUserResponse;
import com.riverflow.livegoapp.Entity.BroadcastVote;
import com.riverflow.livegoapp.Service.BroadcastUserResponseService;
import com.riverflow.livegoapp.Service.BroadcastVoteService;
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
@RequestMapping("/api/controller/broadcastVote/")
public class BroadcastVoteController {

    @Autowired
    BroadcastVoteService broadcastVoteService;

    @Autowired
    BroadcastUserResponseService broadcastUserResponseService;


    @PostMapping("insertVote")
    public ResponseEntity<HashMap<String, Object>> insertVote(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = broadcastVoteService.inseartBroadcastVote(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @ 투표 정보 가져오기
     * @param params
     * @return
     */
    @PostMapping("getVoteListByParentId")
    public ResponseEntity<HashMap<String, Object>> getVoteListByParentId(@RequestBody HashMap<String, String> params) {

        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        HashMap<String, Object> resultMap = broadcastVoteService.getBroadcastVoteByParentId(parentId);


        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @ 투표 정보 상태변경
     * @param params
     * @return
     */
    @PostMapping("updateVoteStatus")
    public ResponseEntity<HashMap<String, Object>> updateVoteStatus(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastVoteService.updateVoteStatus(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     * 참여자의 투표 답변
     */
    @PostMapping("insertVoteReply")
    public ResponseEntity<HashMap<String, Object>> insertVoteReply(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = broadcastUserResponseService.insertVoteReply(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     * 방송 종료후 투표 답변
     */
    @PostMapping("getVoteWithUserInfo")
    public ResponseEntity<HashMap<String, Object>> getVoteWithUserInfo(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();
        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        HashMap<String, Object> voteMap = broadcastVoteService.getBroadcastVoteByParentId(parentId);

        BroadcastVote info = (BroadcastVote) voteMap.get("info");
        resultMap.put("info",info);

        List<BroadcastUserResponse> dataList = broadcastUserResponseService.getReplyList(info.getId(),"vote");
        resultMap.put("dataList",dataList);
        resultMap.put("status","success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}

package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Entity.BroadcastBoard;
import com.riverflow.livegoapp.Entity.BroadcastChat;
import com.riverflow.livegoapp.Entity.BroadcastFile;
import com.riverflow.livegoapp.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/controller/broadcastUserLive/")
public class BroadcastUserLiveController {


    @Autowired
    BroadcastService broadcastService;


    @Autowired
    BroadcastJoinUserService broadcastJoinUserService;

    @Autowired
    ZoomApiService zoomApiService;

    @Autowired
    BroadcastChatService broadcastChatService;

    @Autowired
    BroadcastExamService broadcastExamService;

    @Autowired
    BroadcastPollService broadcastPollService;

    @Autowired
    BroadcastVoteService broadcastVoteService;

    @Autowired
    BroadcastFileService broadcastFileService;

    @Autowired
    BroadcastBoardService broadcastBoardService;

    @Autowired
    BroadcastBoardArticleService broadcastBoardArticleService;

    @Autowired
    BroadcastUserResponseService broadcastUserResponseService;


    /*
     *@ 방송중인 정보 가져오기
     */
    @PostMapping("getAllLive")
    public ResponseEntity<HashMap<String, Object>> getAllLive(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();

        String idString = (String) params.get("id");
        Long id = Long.parseLong(idString);
        Map<String, Object> broadcastInfo = broadcastService.getBroadcastAllNotComplete(id);
        resultMap.put("info",broadcastInfo);

        String uid = (String) params.get("uid");
        resultMap.put("userInfo",broadcastUserResponseService.getUserReply(id,uid));


        boolean pollInfo = broadcastPollService.getBoolPoll(id);
        resultMap.put("pollInfo", pollInfo);

        boolean voteInfo = broadcastVoteService.getBoolVote(id);
        resultMap.put("voteInfo", voteInfo);

        BroadcastBoard boardInfo = broadcastBoardService.getBroadcastBoardInfo(id);
        resultMap.put("boardInfo", boardInfo);

        List<BroadcastFile> fileList = broadcastFileService.getFileList(id);
        resultMap.put("fileList", fileList);

        if (boardInfo != null) {
            boolean articleInfo = broadcastBoardArticleService.getBoolArticle(id);
            resultMap.put("articleInfo", articleInfo);
        } else {
            resultMap.put("articleInfo", false);
        }


        //BroadcastChat chatInfo = broadcastChatService.getBroadcastChatByParentId(id);
        //resultMap.put("chatInfo", chatInfo);

        HashMap<String, String> sdkInfo = zoomApiService.getSdkKeyPasswd();
        resultMap.put("sdkKey",sdkInfo.get("sdkKey"));
        resultMap.put("sdkPassword",sdkInfo.get("sdkPassword"));


        resultMap.put("status","success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    @PostMapping("getLiveInfo")
    public ResponseEntity<HashMap<String, Object>> getLiveInfo(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();

        String idString = (String) params.get("id");
        Long id = Long.parseLong(idString);
        Map<String, Object> broadcastInfo = broadcastService.getBroadcastAllNotComplete(id);
        resultMap.put("info",broadcastInfo);


        resultMap.put("status","success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     *@ 참여자 정보등록
     */
    @PostMapping("insertLiveUser")
    public ResponseEntity<HashMap<String, Object>> insertLiveUser(@RequestBody HashMap<String, Object> params) {


        HashMap<String, Object> resultMap = broadcastJoinUserService.inseartBroadcastJoinUser(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     *@ 참여자 상태변경
     */
    @PostMapping("updateLiveUserStatus")
    public ResponseEntity<HashMap<String, Object>> updateLiveUserStatus(@RequestBody HashMap<String, String> params) {


        HashMap<String, Object> resultMap = broadcastJoinUserService.updateBroadcastJoinUserByStatus(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     *@ 참여자 방송 종료
     */
    @PostMapping("outLiveUser")
    public ResponseEntity<HashMap<String, Object>> outLiveUser(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastJoinUserService.updateBroadcastJoinUserByStatus(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}

package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Entity.BroadcastBoard;
import com.riverflow.livegoapp.Service.BroadcastBoardArticleService;
import com.riverflow.livegoapp.Service.BroadcastBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/controller/broadcastBoard/")
public class BroadcastBoardController {

    @Autowired
    BroadcastBoardService broadcastBoardService;

    @Autowired
    BroadcastBoardArticleService broadcastBoardArticleService;

    @PostMapping("getBoardByParentId")
    public ResponseEntity<HashMap<String, Object>> getBoardByParentId(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastBoardService.getBroadcastBoard(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    @PostMapping("updateBoard")
    public ResponseEntity<HashMap<String, Object>> updateBoard(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();
        if(params.get("no")==null || params.get("no").isEmpty()) {
            resultMap = broadcastBoardService.insertBroadcastBoard(params);
        } else {
            resultMap = broadcastBoardService.updateBroadcastBoard(params);
        }


        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    @PostMapping("getArticleListWithBoardInfo")
    public ResponseEntity<HashMap<String, Object>> getArticleListWithBoardInfo(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastBoardArticleService.getBroadcastBoardArticleList(params);

        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        BroadcastBoard boardInfo = broadcastBoardService.getBroadcastBoardInfo(parentId);
        resultMap.put("zoomBoardInfo",boardInfo);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


}

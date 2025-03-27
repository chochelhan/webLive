package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.BroadcastBoard;
import com.riverflow.livegoapp.Repository.BroadcastBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class BroadcastBoardService {



    @Autowired
    BroadcastBoardRepository broadcastBoardRepository;


    /*
     *@  게시판 정보
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastBoard(HashMap<String, String> params) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String parentIdString  = params.get("parentId");

        if (parentIdString == null || parentIdString.isEmpty()) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Long parentId  = Long.parseLong(parentIdString);
        Map<String,Object> boardInfo = broadcastBoardRepository.getSQLByParentId(parentId);

        result.put("status", "success");
        result.put("info", boardInfo);

        return result;
    }

    /*
     *@  게시판 정보
     * return : {status: -> message,success.fail} access_token
     */
    public BroadcastBoard getBroadcastBoardInfo(Long parentId) {
        BroadcastBoard boardInfo = broadcastBoardRepository.getByParentId(parentId);

        return boardInfo;
    }

    /*
     *@  게시판 생성
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> insertBroadcastBoard(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String parentIdString  = params.get("parentId");
        String boardName  = params.get("boardName");
        String writeAuth  = params.get("writeAuth");
        String ufileAuth = params.get("ufileAuth");
        String repleUse = params.get("repleUse");
        String downLimit = params.get("downLimit");

        if (parentIdString == null || parentIdString.isEmpty()) {
            empty = true;
        }

        if (boardName == null || boardName.isEmpty()) {
            empty = true;
        }
        if (writeAuth == null || writeAuth.isEmpty()) {
            empty = true;
        }
        if (ufileAuth == null || ufileAuth.isEmpty()) {
            empty = true;
        }
        if (repleUse == null || repleUse.isEmpty()) {
            empty = true;
        }
        if (downLimit == null || downLimit.isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Long parentId  = Long.parseLong(parentIdString);
        BroadcastBoard broadcastBoard = BroadcastBoard.builder()
                .parentId(parentId)
                .boardName(boardName)
                .writeAuth(writeAuth)
                .ufileAuth(ufileAuth)
                .repleUse(repleUse)
                .downLimit(downLimit)
                .actType("insert")
                .build();

        BroadcastBoard info = broadcastBoardRepository.save(broadcastBoard);

        result.put("status", "success");
        result.put("info", info);

        return result;
    }

    /*
     *@  게시판 수정
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> updateBroadcastBoard(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String no  = params.get("no");
        String boardName  = params.get("boardName");
        String writeAuth  = params.get("writeAuth");
        String ufileAuth = params.get("ufileAuth");
        String repleUse = params.get("repleUse");
        String downLimit = params.get("downLimit");

        if (no == null || no.isEmpty()) {
            empty = true;
        }

        if (boardName == null || boardName.isEmpty()) {
            empty = true;
        }
        if (writeAuth == null || writeAuth.isEmpty()) {
            empty = true;
        }
        if (ufileAuth == null || ufileAuth.isEmpty()) {
            empty = true;
        }
        if (repleUse == null || repleUse.isEmpty()) {
            empty = true;
        }
        if (downLimit == null || downLimit.isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        
        Long id  = Long.parseLong(no);
        BroadcastBoard boardInfo = broadcastBoardRepository.getById(id);
        
                
        BroadcastBoard broadcastBoard = BroadcastBoard.builder()
                .parentId(boardInfo.getParentId())
                .boardName(boardName)
                .writeAuth(writeAuth)
                .ufileAuth(ufileAuth)
                .repleUse(repleUse)
                .downLimit(downLimit)
                .actType("update")
                .actId(boardInfo.getId())
                .build();
        broadcastBoardRepository.save(broadcastBoard);

        result.put("status", "success");

        return result;
    }




}

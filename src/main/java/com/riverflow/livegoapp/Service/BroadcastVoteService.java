package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.BroadcastPoll;
import com.riverflow.livegoapp.Entity.BroadcastVote;
import com.riverflow.livegoapp.Repository.BroadcastRepository;
import com.riverflow.livegoapp.Repository.BroadcastVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;


@Service
public class BroadcastVoteService {


    @Autowired
    BroadcastRepository broadcastRepository;

    @Autowired
    BroadcastVoteRepository broadcastVoteRepository;

    /*
     *@  투표 생성
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> inseartBroadcastVote(HashMap<String, Object> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String parentIdString  = (String) params.get("parentId");
        String subject  = (String) params.get("subject");
        String items  = (String) params.get("items");
        String multigood = (String) params.get("multigood");

        if (parentIdString == null || parentIdString.isEmpty()) {
            empty = true;
        }

        if (subject == null || subject.isEmpty()) {
            empty = true;
        }
        if (items == null || items.isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Long parentId  = Long.parseLong(parentIdString);

        if(params.get("id") != null) {
            String idString = (String) params.get("id");
            Long id = Long.parseLong(idString);
            BroadcastVote broadcastVote = BroadcastVote.builder()
                    .parentId(parentId)
                    .subject(subject)
                    .items(items)
                    .multigood(multigood)
                    .status("ready")
                    .actType("update")
                    .actId(id)
                    .build();
            broadcastVoteRepository.save(broadcastVote);
        } else {

            BroadcastVote broadcastVote = BroadcastVote.builder()
                    .parentId(parentId)
                    .subject(subject)
                    .items(items)
                    .multigood(multigood)
                    .status("ready")
                    .actType("insert")
                    .build();
            broadcastVoteRepository.save(broadcastVote);
        }



        result.put("status", "success");

        return result;
    }
    public boolean getBoolVote(Long parentId) {
        BroadcastVote info = broadcastVoteRepository.getByParentId(parentId);
        if(info==null) {
            return false;
        } else {
            return true;
        }
    }

    /*
     *@  설문 정보
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastVoteId(Long id) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        BroadcastVote info = broadcastVoteRepository.getById(id);

        result.put("info", info);
        result.put("status", "success");

        return result;
    }


    /*
     *@  설문 정보
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastVoteByParentId(Long parentId) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        BroadcastVote info = broadcastVoteRepository.getByParentId(parentId);

        result.put("info", info);
        result.put("status", "success");

        return result;
    }

    /*
     *@  정보상태변경
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> updateVoteStatus(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        String idString   = params.get("id");
        Long id = Long.parseLong(idString);
        String status = params.get("status");
        broadcastVoteRepository.updateSQLStatus(id,status);
        result.put("status", "success");

        return result;
    }
}

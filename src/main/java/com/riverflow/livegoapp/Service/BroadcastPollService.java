package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.BroadcastPoll;
import com.riverflow.livegoapp.Repository.BroadcastPollRepository;
import com.riverflow.livegoapp.Repository.BroadcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;


@Service
public class BroadcastPollService {


    @Autowired
    BroadcastRepository broadcastRepository;

    @Autowired
    BroadcastPollRepository broadcastPollRepository;

    /*
     *@  시험 생성
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> inseartBroadcastPoll(HashMap<String, Object> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String parentIdString  = (String) params.get("parentId");
        String subject  = (String) params.get("subject");
        String problems  = (String) params.get("problems");

        if (parentIdString == null || parentIdString.isEmpty()) {
            empty = true;
        }

        if (subject == null || subject.isEmpty()) {
            empty = true;
        }
        if (problems == null || problems.isEmpty()) {
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

            BroadcastPoll broadcastPoll = BroadcastPoll.builder()
                    .parentId(parentId)
                    .subject(subject)
                    .problems(problems)
                    .status("ready")
                    .actType("update")
                    .actId(id)
                    .build();
            broadcastPollRepository.save(broadcastPoll);

        } else {

            BroadcastPoll broadcastPoll = BroadcastPoll.builder()
                    .parentId(parentId)
                    .subject(subject)
                    .problems(problems)
                    .status("ready")
                    .actType("insert")
                    .build();
            broadcastPollRepository.save(broadcastPoll);
        }

        result.put("status", "success");

        return result;
    }

    public boolean getBoolPoll(Long parentId) {
        BroadcastPoll info = broadcastPollRepository.getByParentId(parentId);
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
    public HashMap<String, Object> getBroadcastPollByParentId(Long parentId) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        BroadcastPoll info = broadcastPollRepository.getByParentId(parentId);

        result.put("info", info);
        result.put("status", "success");

        return result;
    }

    /*
     *@  설문 정보
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastPollId(Long id) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        BroadcastPoll info = broadcastPollRepository.getById(id);

        result.put("info", info);
        result.put("status", "success");

        return result;
    }


    /*
     *@  정보상태변경
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> updatePollStatus(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        String idString   = params.get("id");
        Long id = Long.parseLong(idString);
        String status = params.get("status");
        broadcastPollRepository.updateSQLStatus(id,status);
        result.put("status", "success");

        return result;
    }
}

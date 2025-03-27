package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.BroadcastUserResponse;
import com.riverflow.livegoapp.Repository.BroadcastExamRepository;
import com.riverflow.livegoapp.Repository.BroadcastUserResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;


@Service
public class BroadcastUserResponseService {



    @Autowired
    BroadcastExamRepository broadcastExamRepository;

    @Autowired
    BroadcastUserResponseRepository broadcastUserResponseRepository;

    /*
     *@  시험 답변 생성
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> insertExamReply(HashMap<String, Object> params) {


        params.put("rtype","exam");
        HashMap<String, Object> result  = insertReply(params);
        return result;
    }

    /*
     *@  설문 답변 생성
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> insertPollReply(HashMap<String, Object> params) {


        params.put("rtype","poll");
        HashMap<String, Object> result  = insertReply(params);
        return result;
    }

    /*
     *@  투표 답변 생성
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> insertVoteReply(HashMap<String, Object> params) {


        params.put("rtype","vote");
        HashMap<String, Object> result  = insertReply(params);
        return result;
    }

    /*
     *@  퀴즈 답변 생성
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> insertQuizReply(HashMap<String, Object> params) {


        params.put("rtype","quiz");
        HashMap<String, Object> result  = insertReply(params);
        return result;
    }


    private HashMap<String, Object>  insertReply(HashMap<String, Object> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String pidString  = (String) params.get("pid");
        String uid  = (String) params.get("uid");
        String rtype  = (String) params.get("rtype");
        String name  = (String) params.get("name");



        if (pidString == null || pidString.isEmpty()) {
            empty = true;
        }

        if (uid == null || uid.isEmpty()) {
            empty = true;
        }
        if (rtype == null || rtype.isEmpty()) {
            empty = true;
        }
        if (name == null || name.isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }

        Long pid  = Long.parseLong(pidString);

        BroadcastUserResponse isInfo = broadcastUserResponseRepository.getByPidAndRtypeAndUid(pid,rtype,uid);
        if(isInfo !=null && !isInfo.getUid().isEmpty()) {
            result.put("code", "is");
            result.put("status", "message");
            return result;
        }
        String answers  = "";
        if(params.get("answers") !=null) {
            answers  = (String) params.get("answers");
        }
        int ptotal = 0;
        if(params.get("ptotal") !=null) {
            ptotal = (int) params.get("ptotal");
        }
        int gtotal = 0;
        if(params.get("gtotal") !=null) {
            gtotal = (int) params.get("gtotal");
        }

        int jumsu = 0;
        if(params.get("jumsu") !=null) {
            jumsu  = (int) params.get("jumsu");
        }


        BroadcastUserResponse broadcastResponse = BroadcastUserResponse.builder()
                .pid(pid)
                .uid(uid)
                .rtype(rtype)
                .name(name)
                .answers(answers)
                .ptotal(ptotal)
                .gtotal(gtotal)
                .jumsu(jumsu)
                .actType("insert")
                .build();

        BroadcastUserResponse info = broadcastUserResponseRepository.save(broadcastResponse);

        result.put("info", info);
        result.put("status", "success");
        return result;
    }

    /*
     *@  참여자의 응답 여부 가져오기
     */
    public List<BroadcastUserResponse> getReplyList(Long pid,String rtype) {


        return broadcastUserResponseRepository.getByPidAndRtypeOrderByIdDesc(pid, rtype);
    }

    public BroadcastUserResponse getReplyInfo(Long id) {


        return broadcastUserResponseRepository.getById(id);
    }


    /*
     *@  참여자의 1인의 응답
     */
    public   HashMap<String, Object>  getUserReply(Long parentId,String uid) {

        HashMap<String, Object> result = new HashMap<>();
        result.put("voteInfo",broadcastUserResponseRepository.getSQLByVote(parentId,uid));
        result.put("pollInfo",broadcastUserResponseRepository.getSQLByPoll(parentId,uid));

        return  result;
    }


    /*
     *@  참여자의 1인의 응답
     */
    public BroadcastUserResponse getReplyInfoWithUserEmail(Long id,Long parentId) {

        return broadcastUserResponseRepository.getSQLByIdWithUserEmail(parentId,id);

    }
}

package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.BroadcastComment;
import com.riverflow.livegoapp.Repository.BroadcastCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service
public class BroadcastCommentService {



    @Autowired
    BroadcastCommentRepository broadcastCommentRepository;


    /*
     *@  댓글 목록
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastCommentList(Long pid) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        List<BroadcastComment> articleList = broadcastCommentRepository.getByPidOrderByIdDesc(pid);
        result.put("status", "success");
        result.put("list", articleList);

        return result;
    }

    /*
     *@  댓글 생성/수정
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> updateBroadcastComment(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();

        boolean empty = false;
        String pidString = params.get("pid");
        String uid = params.get("uid");
        String name = params.get("nae");
        String content = params.get("content");
        String ctype = "brboard";

        if (pidString == null || pidString.isEmpty()) {
            empty = true;
        }

        if (content == null || content.isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Long pid = Long.parseLong(pidString);
        if(params.get("id") != null) {
            Long id = Long.parseLong(params.get("id"));
            BroadcastComment isInfo = broadcastCommentRepository.getById(id);
            BroadcastComment broadcastComment = BroadcastComment.builder()
                    .pid(isInfo.getPid())
                    .uid(isInfo.getUid())
                    .name(isInfo.getName())
                    .content(content)
                    .ctype(ctype)
                    .createAt(isInfo.getCreateAt())
                    .actType("update")
                    .actId(id)
                    .build();

            broadcastCommentRepository.save(broadcastComment);
        } else {
            BroadcastComment broadcastComment = BroadcastComment.builder()
                    .pid(pid)
                    .uid(uid)
                    .name(name)
                    .content(content)
                    .ctype(ctype)
                    .actType("insert")
                    .build();

            broadcastCommentRepository.save(broadcastComment);
        }
        result.put("status", "success");

        return result;
    }

    /*@  댓글 삭제
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> deleteBroadcastComment(HashMap<String, String> params) {

        Long id = Long.parseLong(params.get("id"));
        broadcastCommentRepository.deleteById(id);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("status", "success");
        return result;
    }
}

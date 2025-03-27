package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.BroadcastJoinUsers;
import com.riverflow.livegoapp.Repository.BroadcastJoinUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;


@Service
public class BroadcastJoinUserService {


    @Autowired
    BroadcastJoinUsersRepository broadcastJoinUsersRepository;

    /*
     *@  라이브방송 참여자 등록
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> inseartBroadcastJoinUser(HashMap<String, Object> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String parentIdString  = (String) params.get("parentId");
        String uid = (String) params.get("uid");
        String name  = (String) params.get("name");
        String email  = (String) params.get("email");
        String job  = (String) params.get("job");
        String part  = (String) params.get("part");
        String posi  = (String) params.get("posi");
        String status  = (String) params.get("status");

        if (parentIdString == null || parentIdString.isEmpty()) {
            empty = true;
        }
        if (name == null || name.isEmpty()) {
            empty = true;
        }
        if (email == null || email.isEmpty()) {
            empty = true;
        }
        if (uid == null || uid.isEmpty()) {
            empty = true;
        }
        if(status==null || status.isEmpty()) {
            status = "ing";
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Long parentId  = Long.parseLong(parentIdString);


        BroadcastJoinUsers checkInfo = broadcastJoinUsersRepository.getByParentIdAndUid(parentId,uid);
        if(checkInfo == null) {
            BroadcastJoinUsers broadcastJoinUsers = BroadcastJoinUsers.builder()
                    .parentId(parentId)
                    .uid(uid)
                    .name(name)
                    .email(email)
                    .job(job)
                    .part(part)
                    .posi(posi)
                    .status(status)
                    .actType("insert")
                    .build();

            BroadcastJoinUsers info = broadcastJoinUsersRepository.save(broadcastJoinUsers);
            result.put("info", info);
        } else {
            result.put("info", checkInfo);
        }
        result.put("status", "success");


        return result;
    }

    /*
     *@  참여자 정보상태변경
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> updateBroadcastJoinUserByStatus(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        String parentIdString   = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);

        String uid   = params.get("uid");
        String status = params.get("status");

        BroadcastJoinUsers checkInfo = broadcastJoinUsersRepository.getByParentIdAndUid(parentId,uid);
        if(checkInfo != null && checkInfo.getId()>0) {
            BroadcastJoinUsers broadcastJoinUsers = BroadcastJoinUsers.builder()
                    .parentId(parentId)
                    .uid(uid)
                    .name(checkInfo.getName())
                    .email(checkInfo.getEmail())
                    .job(checkInfo.getJob())
                    .part(checkInfo.getPart())
                    .posi(checkInfo.getPosi())
                    .status(status)
                    .createAt(checkInfo.getCreateAt())
                    .actType("update")
                    .actId(checkInfo.getId())
                    .build();
            broadcastJoinUsersRepository.save(broadcastJoinUsers);
        }
        result.put("status", "success");

        return result;
    }


    /*
     *@  참여자 정보
     */
    public BroadcastJoinUsers getBroadcastJoinUser(Long parentId,String uid) {

        BroadcastJoinUsers info = broadcastJoinUsersRepository.getByParentIdAndUid(parentId,uid);
        return info;
    }

    /*
     *@  참여자 목록 (parentId)
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastJoinUsersByParentId(Long parentId) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        List<BroadcastJoinUsers> list = broadcastJoinUsersRepository.getByParentIdOrderByNameDesc(parentId);

        result.put("list", list);
        result.put("status", "success");

        return result;
    }

    /*
     *@  참여자 목록 (parentId, status)
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastJoinUsersByParentIdWithStatus(Long parentId, String status) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        List<BroadcastJoinUsers> list = broadcastJoinUsersRepository.getByParentIdAndStatusOrderByNameDesc(parentId,status);

        result.put("list", list);
        result.put("status", "success");

        return result;
    }
}

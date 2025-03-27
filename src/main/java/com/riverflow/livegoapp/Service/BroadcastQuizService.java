package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.BroadcastExam;
import com.riverflow.livegoapp.Entity.BroadcastQuiz;
import com.riverflow.livegoapp.Repository.BroadcastQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;


@Service
public class BroadcastQuizService {


    @Autowired
    BroadcastQuizRepository broadcastQuizRepository;

    /*
     *@  퀴즈 생성
     */
    @Transactional
    public HashMap<String, Object> insertBroadcastQuiz(HashMap<String, Object> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String parentIdString  = (String) params.get("parentId");
        String question  = (String) params.get("question");
        String good  = (String) params.get("good");
        String items  = (String) params.get("items");
        String prbType  = (String) params.get("prbType");
        String subjectGood  = (String) params.get("subjectGood");

        if (parentIdString == null || parentIdString.isEmpty()) {
            empty = true;
        }

        if (question == null || question.isEmpty()) {
            empty = true;
        }
        if (prbType == null || prbType.isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Long parentId  = Long.parseLong(parentIdString);

        BroadcastQuiz broadcastQuiz = BroadcastQuiz.builder()
                .parentId(parentId)
                .question(question)
                .good(good)
                .items(items)
                .prbType(prbType)
                .subjectGood(subjectGood)
                .status("ready")
                .actType("insert")
                .build();
        BroadcastQuiz info = broadcastQuizRepository.save(broadcastQuiz);

        result.put("status", "success");
        result.put("info", info);
        return result;
    }

    /*
     *@  퀴즈 정보상태변경
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> updateBroadcastQuizByStatus(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        String idString   = params.get("id");
        Long id = Long.parseLong(idString);
        String status = params.get("status");
        broadcastQuizRepository.updateSQLStatus(id,status);
        result.put("status", "success");

        return result;
    }

    /*
     *@  퀴즈 정보
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastQuizId(Long id) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        BroadcastQuiz info = broadcastQuizRepository.getById(id);

        result.put("info", info);
        result.put("status", "success");

        return result;
    }

    /*
     *@  퀴즈 정보
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastQuizByParentId(Long parentId) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        List<BroadcastQuiz> examList = broadcastQuizRepository.getByParentIdOrderByIdDesc(parentId);

        result.put("examList", examList);
        result.put("status", "success");

        return result;
    }

    /*
     *@  삭제
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> deleteBroadcastQuiz(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        String idString = params.get("id");
        Long id = Long.parseLong(idString);
        BroadcastQuiz info = broadcastQuizRepository.getById(id);
        if (info != null && info.getStatus().equals("ready")) {
            result.put("status", "success");
            broadcastQuizRepository.deleteById(id);
        } else {
            result.put("status", "message");
        }

        return result;
    }
}

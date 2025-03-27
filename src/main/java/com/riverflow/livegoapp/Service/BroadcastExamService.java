package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.Broadcast;
import com.riverflow.livegoapp.Entity.BroadcastExam;
import com.riverflow.livegoapp.Repository.BroadcastEnvRepository;
import com.riverflow.livegoapp.Repository.BroadcastExamRepository;
import com.riverflow.livegoapp.Repository.BroadcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
public class BroadcastExamService {

    public String filePath = "fileUpload/excelSample/sample.xlsx";

    @Autowired
    BroadcastRepository broadcastRepository;

    @Autowired
    BroadcastExamRepository broadcastExamRepository;

    /*
     *@  시험 생성
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> inseartBroadcastExam(HashMap<String, Object> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String parentIdString = (String) params.get("parentId");
        String subject = (String) params.get("subject");
        String author = (String) params.get("author");
        String problems = (String) params.get("problems");
        int timeLimit = (int) params.get("timelimit");
        int totalJumsu = (int) params.get("totalJumsu");

        if (parentIdString == null || parentIdString.isEmpty()) {
            empty = true;
        }

        if (subject == null || subject.isEmpty()) {
            empty = true;
        }
        if (author == null || author.isEmpty()) {
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
        Long parentId = Long.parseLong(parentIdString);

        BroadcastExam broadcastExam = BroadcastExam.builder()
                .parentId(parentId)
                .subject(subject)
                .author(author)
                .problems(problems)
                .status("ready")
                .timeLimit(timeLimit)
                .totalJumsu(totalJumsu)
                .actType("insert")
                .build();

        BroadcastExam info = broadcastExamRepository.save(broadcastExam);

        result.put("status", "success");
        result.put("info", info);

        return result;
    }

    @Transactional
    public HashMap<String, Object> updateBroadcastExam(HashMap<String, Object> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String problems = (String) params.get("problems");
        if (problems == null || problems.isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        if (params.get("id") != null) {
            String idString = (String) params.get("id");
            Long id = Long.parseLong(idString);
            BroadcastExam isInfo = broadcastExamRepository.getById(id);

            BroadcastExam broadcastExam = BroadcastExam.builder()
                    .parentId(isInfo.getParentId())
                    .subject(isInfo.getSubject())
                    .author(isInfo.getAuthor())
                    .problems(problems)
                    .status("ready")
                    .timeLimit(isInfo.getTimeLimit())
                    .totalJumsu(isInfo.getTotalJumsu())
                    .createAt(isInfo.getCreateAt())
                    .actType("update")
                    .actId(id)
                    .build();

            broadcastExamRepository.save(broadcastExam);
            result.put("status", "success");
        } else {
            result.put("status", "fail");
        }

        return result;
    }

    /*
     *@  시험 정보상태변경
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> updateBroadcastExamByStatus(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        String idString = params.get("id");
        Long id = Long.parseLong(idString);
        String status = params.get("status");
        broadcastExamRepository.updateSQLStatus(id, status);

        result.put("status", "success");

        return result;
    }

    public boolean getBoolExam(Long parentId) {
        List<BroadcastExam> info = broadcastExamRepository.getByParentIdOrderByIdDesc(parentId);
        if (info == null) {
            return false;
        } else {
            return (info.size() > 0) ? true : false;
        }
    }

    /*
     *@  시험 정보
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastExamId(Long id) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        BroadcastExam info = broadcastExamRepository.getById(id);

        result.put("info", info);
        result.put("status", "success");

        return result;
    }

    /*
     *@  시험 목록 (parentId)
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastExamByParentId(Long parentId) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        List<BroadcastExam> list = getExamByParentId(parentId);

        result.put("list", list);
        result.put("status", "success");

        return result;
    }

    public List<BroadcastExam> getExamByParentId(Long parentId) {
        return broadcastExamRepository.getByParentIdOrderByIdDesc(parentId);

    }

    /*
     *@  시험 삭제
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> deleteBroadcastExam(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        String idString = params.get("id");
        Long id = Long.parseLong(idString);
        BroadcastExam info = broadcastExamRepository.getById(id);
        if (info != null && info.getStatus().equals("ready")) {
            result.put("status", "success");
            broadcastExamRepository.deleteById(id);
        } else {
            result.put("status", "message");
        }

        return result;
    }


    /*
     *@ 샘플파일 가져오기
     */
    public void getSample(HttpServletResponse response) throws IOException {
        try {
            Path currentPath = Paths.get("");
            String sitePath = currentPath.toAbsolutePath().toString();
            String absolutePath = new File(sitePath).getAbsolutePath() + "/"; // 파일이 저장될 절대 경로

            File file = new File(absolutePath + filePath);
            int fsize = (int) file.length();

            if (fsize > 0) {
                String fileName = "attachment; filename=sample.xlsx";
                response.setContentType("application/octet-stream; charset=utf-8");
                response.setHeader("Content-Disposition", fileName);
                response.setContentLengthLong(fsize);
                BufferedInputStream in = null;
                BufferedOutputStream out = null;

                in = new BufferedInputStream(new FileInputStream(file));
                out = new BufferedOutputStream(response.getOutputStream());
                try {
                    byte[] buffer = new byte[4096];
                    int bytesRead = 0;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    out.flush();
                } finally {
                    in.close();
                    out.close();
                }
            } else {
                throw new FileNotFoundException("error");
            }
        } catch (IOException e) {
            throw new RuntimeException("File Error");
        }


    }
}

package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.Broadcast;
import com.riverflow.livegoapp.Entity.BroadcastBoard;
import com.riverflow.livegoapp.Entity.BroadcastEnv;
import com.riverflow.livegoapp.Repository.BroadcastBoardRepository;
import com.riverflow.livegoapp.Repository.BroadcastEnvRepository;
import com.riverflow.livegoapp.Repository.BroadcastRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class BroadcastService {

    public String imagePath = "fileUpload/broadcast/image";

    @Autowired
    BroadcastRepository broadcastRepository;

    @Autowired
    BroadcastEnvRepository broadcastEnvRepository;

    @Autowired
    BroadcastBoardRepository broadcastBoardRepository;


    public HashMap<String, Object> getMyBroadcastList(String uid) {

        HashMap<String, Object> result = new HashMap<String, Object>();
        List<Broadcast> broadcastList = broadcastRepository.getByUidOrderByIdDesc(uid);
        result.put("status", "success");
        result.put("broadcastList",broadcastList);

        return result;
    }


    /*
     *@  라이브방송 생성
     * params : uid,upass
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> insertBroadcast(HashMap<String, String> params, String uid) {


        HashMap<String, Object> result = new HashMap<String, Object>();


        boolean empty = false;
        if (params.get("subject") == null || params.get("subject").isEmpty()) {
            empty = true;
        }
        if (params.get("vtype") == null || params.get("vtype").isEmpty()) {
            empty = true;
        }
        if (params.get("role") == null || params.get("role").isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Broadcast broadcast = Broadcast.builder()
                .uid(uid)
                .subject(params.get("subject"))
                .vtype(params.get("vtype"))
                .role(params.get("role"))
                .status("ready")
                .actType("insert")
                .build();

        Broadcast info = broadcastRepository.save(broadcast);

        boolean addInfo = initBroadcastEnv(info.getId());
        boolean boardInfo = initBroadcastBoard(info.getId());
        if(addInfo && boardInfo) {
            result.put("status", "success");
            result.put("info", info);
        } else {
            result.put("status", "error");
        }

        return result;
    }
    /*
     *@  라이브방송 시작시간 저장
     */
    @Transactional
    public HashMap<String, Object> updateLiveStartTime(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        LocalDateTime now = LocalDateTime.now();
        String idString = params.get("id");
        Long id = Long.parseLong(idString);

        Broadcast broadcastInfo = broadcastRepository.getById(id);
        if(broadcastInfo.getStartTime()==null) {
            broadcastRepository.updateSQLStartTime(id,now);
        }
        result.put("status", "success");

        return result;
    }
    /*
     *@  라이브방송 환경설정 초기 저장
     */
    private boolean initBroadcastEnv(Long parentId) {


        BroadcastEnv broadcastEnvInfo = broadcastEnvRepository.getByParentId(parentId);
        if(broadcastEnvInfo!=null) {
            return false;
        }
        String afterOpen = "{\"exam\":\"yes\",\"data\":\"yes\",\"poll\":\"yes\",\"vote\":\"yes\",\"quiz\":\"yes\",\"chat\":\"yes\"}";
        String reqdata = "{\"name\":\"yes\",\"email\":\"yes\",\"job\":\"no\",\"par\":\"no\",\"posi\":\"no\",\"sex\":\"no\"}";
        String safety = "no";
        String acceptType = "yes";
        String joinUser = "all";
        String examImmAnswer = "yes";
        String examDown = "yes";

        BroadcastEnv broadcastEnv = BroadcastEnv.builder()
                .parentId(parentId)
                .safety(safety)
                .acceptType(acceptType)
                .reqdata(reqdata)
                .joinUser(joinUser)
                .afterOpen(afterOpen)
                .examDown(examDown)
                .examImmAnswer(examImmAnswer)
                .actType("insert")
                .build();

        broadcastEnvRepository.save(broadcastEnv);
        return true;
    }

    /*
     *@  라이브방송 게시판 초기 저장
     */
    private boolean initBroadcastBoard(Long parentId) {


        BroadcastBoard broadcastBoardInfo = broadcastBoardRepository.getByParentId(parentId);
        if(broadcastBoardInfo!=null) {
            return false;
        }
        boolean empty = false;
        String boardName  = "기본 게시판";
        String writeAuth  = "host";
        String ufileAuth = "host";
        String repleUse = "yes";
        String downLimit = "ing";

        BroadcastBoard broadcastBoard = BroadcastBoard.builder()
                .parentId(parentId)
                .boardName(boardName)
                .writeAuth(writeAuth)
                .ufileAuth(ufileAuth)
                .repleUse(repleUse)
                .downLimit(downLimit)
                .actType("insert")
                .build();

        broadcastBoardRepository.save(broadcastBoard);
        return true;
    }

    @Transactional
    public void updateBroadcastWithZoom(Long id,HashMap<String, Object> meetingInfo) {

        Long meetingNo = (Long) meetingInfo.get("meetingNo");
        String zak = (String) meetingInfo.get("zak");
        Broadcast broadcastInfo = broadcastRepository.getById(id);
        Broadcast broadcast = Broadcast.builder()
                .uid(broadcastInfo.getUid())
                .subject(broadcastInfo.getSubject())
                .vtype(broadcastInfo.getVtype())
                .role(broadcastInfo.getRole())
                .status("ing")
                .createAt(broadcastInfo.getCreateAt())
                .logo(broadcastInfo.getLogo())
                .logoName(broadcastInfo.getLogoName())
                .meetingNo(meetingNo)
                .zak(zak)
                .actType("update")
                .actId(broadcastInfo.getId())
                .build();

        broadcastRepository.save(broadcast);

    }
    /*
     *@  라이브방송 종료
     */
    @Transactional
    public HashMap<String, Object> endLive(HashMap<String, Object> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();

        LocalDateTime now = LocalDateTime.now();
        String idString = (String) params.get("id");
        Long id = Long.parseLong(idString);
        broadcastRepository.updateSQLEnd(id,now);
        result.put("status", "success");

        return result;
    }
    /*
     *@  라이브방송 수정
     * params : uid,upass
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> updateBroadcast(HashMap<String, Object> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();

        boolean empty = false;

        String no = (String) params.get("no");
        if (no == null) {
            empty = true;
        }
        String subject = (String) params.get("subject");
        if (subject == null || subject.isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }

        Long id = Long.parseLong(no);

        Broadcast broadcastInfo = broadcastRepository.getById(id);
        String logo = (String) params.get("logo");
        String logoName = broadcastInfo.getLogoName();
        if (logo != null && !logo.isEmpty()) {
            logoName = (String) params.get("logoName");
        } else {
            logo = broadcastInfo.getLogo();
        }

        Broadcast broadcast = Broadcast.builder()
                .uid(broadcastInfo.getUid())
                .subject(subject)
                .vtype(broadcastInfo.getVtype())
                .role(broadcastInfo.getRole())
                .status(broadcastInfo.getStatus())
                .createAt(broadcastInfo.getCreateAt())
                .logo(logo)
                .logoName(logoName)
                .meetingNo(broadcastInfo.getMeetingNo())
                .zak(broadcastInfo.getZak())
                .actType("update")
                .actId(broadcastInfo.getId())
                .build();

        broadcastRepository.save(broadcast);
        result.put("status", "success");

        return result;
    }

    /*
     *@  라이브방송 삭제
     */
    public HashMap<String, Object> deleteBroadcast(Long id) {


        HashMap<String, Object> result = new HashMap<String, Object>();

        Broadcast info = broadcastRepository.getById(id);
        if (info != null && info.getId()>0) {
            broadcastRepository.deleteById(id);
            result.put("status", "success");

        } else {
            result.put("status", "message");
        }
        return result;
    }
    /*
     *@  라이브방송 정보 가져오기
     * params :
     * return : {status: -> message,success.fail} access_token
     */

    public HashMap<String, Object> getBroadcast(Long id) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        Broadcast broadcast = broadcastRepository.getById(id);
        if(broadcast!=null) {
            result.put("logoName", broadcast.getLogoName());
            result.put("logo", broadcast.getLogo());
            result.put("subject", broadcast.getSubject());
            result.put("uid", broadcast.getUid());
            result.put("vtype", broadcast.getVtype());
            result.put("role", broadcast.getRole());
            result.put("infoStatus", broadcast.getStatus());
            result.put("meetingNo", broadcast.getMeetingNo());
            result.put("zak", broadcast.getZak());
        }

        result.put("status", "success");

        return result;
    }

    /*
     *@  라이브방송 전체정보 가져오기
     * params :
     * return : {status: -> message,success.fail} access_token
     */
    public Map<String, Object>  getBroadcastAllNotComplete(Long id) {


        Map<String,Object> broadcast = broadcastRepository.getSQLBroadcastInfoWithEnvNotComplete(id);

        return broadcast;
    }
    /*
     *@  로고 저장
     * params :
     * return : {status:(message,success,fail)}
     */
    public HashMap<String, Object> updateLogo(MultipartFile dFile) throws IOException {

        HashMap<String, Object> result = new HashMap<String, Object>();
        Path currentPath = Paths.get("");
        String sitePath = currentPath.toAbsolutePath().toString();

        Date now = new Date();
        Long nowTime = now.getTime();

        String absolutePath = new File(sitePath).getAbsolutePath() + "/"; // 파일이 저장될 절대 경로
        String newFileName = "image_" + nowTime;
        String fileExtension = '.' + dFile.getOriginalFilename().replaceAll("^.*\\.(.*)$", "$1"); // 정규식 이용하여 확장자만 추출

        try {
            if (!dFile.isEmpty()) {
                File file = new File(absolutePath + imagePath);
                if (!file.exists()) {
                    file.mkdirs(); // mkdir()과 다르게 상위 폴더가 없을 때 상위폴더까지 생성
                }
                File saveFile = new File(absolutePath + imagePath + "/" + newFileName + fileExtension);
                dFile.transferTo(saveFile);

                result.put("logoImg", newFileName + fileExtension);
                result.put("logoName", dFile.getOriginalFilename());
                result.put("status", "success");

            } else {
                result.put("status", "fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     *@ 로고 이미지 가져오기
     * params :
     * return :
     */
    public byte[] getLogo(String imgName) throws IOException {

        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Path currentPath = Paths.get("");
        String sitePath = currentPath.toAbsolutePath().toString();
        String absolutePath = new File(sitePath).getAbsolutePath() + "/"; // 파일이 저장될 절대 경로

        try {
            fis = new FileInputStream(absolutePath + imagePath + "/" + imgName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int readCount = 0;
        byte[] buffer = new byte[1024];
        byte[] fileArray = null;


        try {
            while ((readCount = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, readCount);
            }
            fileArray = baos.toByteArray();
            fis.close();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException("File Error");
        }
        return fileArray;
    }


    /*
     *@  라이브방송 환경설정 정보 가져오기
     * params :
     * return : {status: -> message,success.fail} access_token
     */

    public HashMap<String, Object> getBroadcastEnv(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        String no = params.get("parentId");
        if (no == null || no.isEmpty()) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Long id = Long.parseLong(no);
        result.put("info", broadcastEnvRepository.getByParentId(id));
        result.put("status", "success");

        return result;
    }

    /*
     *@  라이브방송 환경설정 수정
     * params : uid,upass
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> updateBroadcastEnv(HashMap<String, Object> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();

        boolean empty = false;

        String noString = (String) params.get("no");
        if (noString == null) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        String cmd = (String) params.get("cmd");
        Long id = Long.parseLong(noString);

        BroadcastEnv broadcastInfo = broadcastEnvRepository.getById(id);

        Long parentId = broadcastInfo.getParentId();
        String safety = broadcastInfo.getSafety();
        String acceptType = broadcastInfo.getAcceptType();
        String safetyEmails = broadcastInfo.getSafetyEmails();
        String observeEmails = broadcastInfo.getObserveEmails();
        String reqdata = broadcastInfo.getReqdata();
        String joinUser = broadcastInfo.getJoinUser();
        String afterOpen = broadcastInfo.getAfterOpen();
        String examImmAnswer = broadcastInfo.getExamImmAnswer();
        String examDown = broadcastInfo.getExamDown();


        if(cmd.equals("examSetting")) {
            String examImmAnswerCheck = (String) params.get("examImmAnswer");
            if (examImmAnswerCheck == null || examImmAnswerCheck.isEmpty()) {
                empty = true;
            }
            String examDownCheck = (String) params.get("examDown");
            if (examDownCheck == null || examDownCheck.isEmpty()) {
                empty = true;
            }
            if (empty) {
                result.put("code", "empty");
                result.put("status", "message");
                return result;
            }
            examImmAnswer = (String) params.get("examImmAnswer");
            examDown = (String) params.get("examDown");

        } else {

            String safetyCheck = (String) params.get("safety");
            if (safetyCheck == null || safetyCheck.isEmpty()) {
                empty = true;
            }
            String acceptTypeCheck = (String) params.get("acceptType");
            if (acceptTypeCheck == null || acceptTypeCheck.isEmpty()) {
                empty = true;
            }
            if (empty) {
                result.put("code", "empty");
                result.put("status", "message");
                return result;
            }
            safety = (String) params.get("safety");
            acceptType = (String) params.get("acceptType");
            safetyEmails = (String) params.get("safetyEmails");
            observeEmails = (String) params.get("observeEmails");
            reqdata = (String) params.get("reqdata");
            joinUser = (String) params.get("joinUser");
            afterOpen = (String) params.get("afterOpen");

        }
        BroadcastEnv broadcastEnv = BroadcastEnv.builder()
                .parentId(parentId)
                .safety(safety)
                .safetyEmails(safetyEmails)
                .observeEmails(observeEmails)
                .acceptType(acceptType)
                .reqdata(reqdata)
                .joinUser(joinUser)
                .afterOpen(afterOpen)
                .examDown(examDown)
                .examImmAnswer(examImmAnswer)
                .actType("update")
                .actId(broadcastInfo.getId())
                .build();

        broadcastEnvRepository.save(broadcastEnv);
        result.put("status", "success");

        return result;
    }
}

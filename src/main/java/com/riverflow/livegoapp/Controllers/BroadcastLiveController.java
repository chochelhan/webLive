package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Entity.*;
import com.riverflow.livegoapp.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/controller/broadcastLive/")
public class BroadcastLiveController {

    @Autowired
    BroadcastService broadcastService;

    @Autowired
    BroadcastExamService broadcastExamService;

    @Autowired
    BroadcastQuizService broadcastQuizService;

    @Autowired
    BroadcastPollService broadcastPollService;

    @Autowired
    BroadcastVoteService broadcastVoteService;

    @Autowired
    BroadcastBoardService broadcastBoardService;

    @Autowired
    BroadcastBoardArticleService broadcastBoardArticleService;

    @Autowired
    BroadcastJoinUserService broadcastJoinUserService;

    @Autowired
    BroadcastFileService broadcastFileService;

    @Autowired
    MemberService memberService;


    @Autowired
    ZoomApiService zoomApiService;


    @Autowired
    BroadcastChatService broadcastChatService;

    @Autowired
    BroadcastUserResponseService broadcastUserResponseService;

    @Autowired
    MailService mailService;

    @Autowired
    SmsService smsService;

    /**
     * @param params
     * @param principal
     * @return
     * @throws Exception
     * @ 방송 만들기
     */
    @PostMapping("insertLive")
    public ResponseEntity<HashMap<String, Object>> insertLive(@RequestBody HashMap<String, String> params, Principal principal) throws Exception {

        HashMap<String, Object> resultMap = new HashMap<>();

        String uid = principal.getName();
        resultMap = broadcastService.insertBroadcast(params, uid);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     * @param params
     * @param principal
     * @return
     * @throws Exception
     * @ 방송 체크 (줌 미팅이 없으면 생성한다)
     */
    @PostMapping("checkLiveWithZoomMeeting")
    public ResponseEntity<HashMap<String, Object>> checkLiveWithZoomMeeting(@RequestBody HashMap<String, String> params, Principal principal) throws Exception {

        HashMap<String, Object> resultMap = new HashMap<>();

        String uid = principal.getName();
        Member member = memberService.getMemberByUid(uid);
        String zid = member.getZid();

        if (zid == null || zid.isEmpty()) {
            resultMap.put("status", "error");
        } else {
            String idString = (String) params.get("id");
            Long id = Long.parseLong(idString);
            HashMap<String, Object> broadcastInfo = broadcastService.getBroadcast(id);
            String status = (String) broadcastInfo.get("infoStatus");

            if (status.equals("end")) {
                resultMap.put("code", "end");
                resultMap.put("status", "message");
            } else {
                String zak = (String) broadcastInfo.get("zak");
                if (broadcastInfo.get("zak") == null || zak.isEmpty()) {
                    String subject = (String) broadcastInfo.get("subject");
                    HashMap<String, Object> meetingInfo = zoomApiService.createMeeting(subject, member);
                    broadcastService.updateBroadcastWithZoom(id, meetingInfo);
                }
                resultMap.put("status", "success");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("getLive")
    public ResponseEntity<HashMap<String, Object>> getLive(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = new HashMap<>();
        String no = (String) params.get("no");
        if (no == null || no.isEmpty()) {
            resultMap.put("status", "error");
        } else {
            Long id = Long.parseLong(no);
            resultMap = broadcastService.getBroadcast(id);

        }

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    @PostMapping("updateLive")
    public ResponseEntity<HashMap<String, Object>> updateLive(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = broadcastService.updateBroadcast(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /*
     *@ 방송 시작시간 저장
     */
    @PostMapping("UpdateLiveHostStartTime")
    public ResponseEntity<HashMap<String, Object>> updateLiveHostStartTime(@RequestBody HashMap<String, String> params) {
        HashMap<String, Object> resultMap = broadcastService.updateLiveStartTime(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }
    /*
     *@ 방송 종료
     */
    @PostMapping("endLive")
    public ResponseEntity<HashMap<String, Object>> endLive(@RequestBody HashMap<String, Object> params) {
        HashMap<String, Object> resultMap = broadcastService.endLive(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }
    /*
     *@ 로고 등록
     */
    @PostMapping("updateLogo")
    public ResponseEntity<HashMap<String, Object>> updateLogo(@RequestParam(name = "image") MultipartFile img, HttpServletRequest request) throws IOException {

        HashMap<String, Object> result = broadcastService.updateLogo(img);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /*
     *@ 환경설정 가져오기
     */
    @PostMapping("getLiveEnv")
    public ResponseEntity<HashMap<String, Object>> getLiveEnv(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastService.getBroadcastEnv(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /*
     *@ 환결설정 수정
     */
    @PostMapping("updateLiveEnv")
    public ResponseEntity<HashMap<String, Object>> updateLiveEnv(@RequestBody HashMap<String, Object> params) {

        HashMap<String, Object> resultMap = broadcastService.updateBroadcastEnv(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /*
     *@ 방송중인 정보 가져오기
     */
    @PostMapping("getLiveHostInfo")
    public ResponseEntity<HashMap<String, Object>> getLiveHostInfo(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();

        String idString = params.get("id");
        Long id = Long.parseLong(idString);
        Map<String, Object> broadcastInfo = broadcastService.getBroadcastAllNotComplete(id);
        if (broadcastInfo != null && broadcastInfo.get("subject") != null) {
            resultMap.put("info", broadcastInfo);

            boolean problemInfo = broadcastExamService.getBoolExam(id);
            resultMap.put("problemInfo", problemInfo);

            boolean pollInfo = broadcastPollService.getBoolPoll(id);
            resultMap.put("pollInfo", pollInfo);

            boolean voteInfo = broadcastVoteService.getBoolVote(id);
            resultMap.put("voteInfo", voteInfo);

            BroadcastBoard boardInfo = broadcastBoardService.getBroadcastBoardInfo(id);
            resultMap.put("boardInfo", boardInfo);

            List<BroadcastFile> fileList = broadcastFileService.getFileList(id);
            resultMap.put("fileList", fileList);

            if (boardInfo != null) {
                boolean articleInfo = broadcastBoardArticleService.getBoolArticle(id);
                resultMap.put("articleInfo", articleInfo);
            } else {
                resultMap.put("articleInfo", false);
            }

            HashMap<String, String> sdkInfo = zoomApiService.getSdkKeyPasswd();
            resultMap.put("sdkKey", sdkInfo.get("sdkKey"));
            resultMap.put("sdkPassword", sdkInfo.get("sdkPassword"));


           // BroadcastChat chatInfo = broadcastChatService.getBroadcastChatByParentId(id);
           // resultMap.put("chatInfo", chatInfo);

            resultMap.put("status", "success");
        } else {
            resultMap.put("status", "fail");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /*
     *@ 시험/투표/설문조사 사용자의 응답정보 필요시에
     */
    @PostMapping("getUserResponse")
    public ResponseEntity<HashMap<String, Object>> getUserResponse(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();

        String idString = params.get("id");
        Long id = Long.parseLong(idString);

        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);

        String gtype = params.get("gtype");

        switch (gtype) {

            case "insertProblem":
                Map<String, Object> examInfo = broadcastExamService.getBroadcastExamId(id);
                BroadcastExam info = (BroadcastExam) examInfo.get("info");

                List<BroadcastUserResponse> userResponses = broadcastUserResponseService.getReplyList(info.getId(),"exam");
                resultMap.put("userResponses", userResponses);
                resultMap.put("info", info);
                break;
            case "insertPoll":
                Map<String, Object> pollInfo = broadcastPollService.getBroadcastPollId(id);
                BroadcastPoll pollInfoData = (BroadcastPoll) pollInfo.get("info");

                List<BroadcastUserResponse> userPollResponses = broadcastUserResponseService.getReplyList(pollInfoData.getId(),"poll");
                resultMap.put("userResponses", userPollResponses);

                resultMap.put("info", pollInfoData);

                break;
            case "insertVote":
                Map<String, Object> voteInfo = broadcastVoteService.getBroadcastVoteId(id);

                BroadcastVote voteInfoData = (BroadcastVote) voteInfo.get("info");

                List<BroadcastUserResponse> userVoteResponses = broadcastUserResponseService.getReplyList(voteInfoData.getId(),"vote");
                resultMap.put("userResponses", userVoteResponses);
                resultMap.put("info",voteInfoData);
                break;
            case "insertQuiz":
                Map<String, Object> quizInfo =  broadcastQuizService.getBroadcastQuizId(id);
                BroadcastQuiz quizInfoData = (BroadcastQuiz) quizInfo.get("info");

                List<BroadcastUserResponse> userQuizResponses = broadcastUserResponseService.getReplyList(quizInfoData.getId(),"quiz");
                resultMap.put("userResponses", userQuizResponses);
                resultMap.put("info", quizInfoData);
                break;
        }
        Map<String, Object> joinUsers = broadcastJoinUserService.getBroadcastJoinUsersByParentId(parentId);
        resultMap.put("joinUserList", joinUsers.get("list"));
        resultMap.put("status", "success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /*
     *@ 환결설정 수정
     */
    @PostMapping("getChatInfo")
    public ResponseEntity<HashMap<String, Object>> getChatInfo(@RequestBody HashMap<String, String> params) {
        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);

        HashMap<String, Object> resultMap = new HashMap<>();
        BroadcastChat info = broadcastChatService.getBroadcastChatByParentId(parentId);

        resultMap.put("messages",info);
        resultMap.put("status", "success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /*
     *@ 방송 참여자 정보
     */
    @PostMapping("getLiveUserInfo")
    public ResponseEntity<HashMap<String, Object>> getLiveUserInfo(@RequestBody HashMap<String, String> params) {


        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        HashMap<String, Object> resultMap = broadcastJoinUserService.getBroadcastJoinUsersByParentId(parentId);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /*
     *@ 방송 삭제
    */
    @PostMapping("deleteLive")
    public ResponseEntity<HashMap<String, Object>> deleteLive(@RequestBody HashMap<String, String> params) {


        String idString = params.get("id");
        Long id = Long.parseLong(idString);
        HashMap<String, Object> resultMap = broadcastService.deleteBroadcast(id);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /*
     *@ 사용자 초대
     */
    @PostMapping("sendInvitedUser")
    public ResponseEntity<HashMap<String, Object>> sendInvitedUser(@RequestBody HashMap<String, Object> params, Principal principal) {
        HashMap<String, Object> resultMap = new HashMap<>();
        String uid = principal.getName();
        Member member = memberService.getMemberByUid(uid);
        String fromEmail = member.getEmail();
        if(fromEmail==null || fromEmail.isEmpty()) {
            fromEmail = "admin@livego.co.kr";
        }
        fromEmail = "admin@livego.co.kr";
        String from = fromEmail;
        String subject = (String) params.get("name")+"님의 웹라이브 초대";
        String name = (String) params.get("name");
        String url = (String) params.get("url");
        String cmd = (String) params.get("cmd");
        String htmlContent = "";
        if(cmd.equals("pcs")) {
            htmlContent = "안녕하세요, "+name+"님의 웹라이브에 초대되었습니다<br>아래 링크를 클릭하여 ";
            htmlContent += name+"님의 웹라이브에 참가하세요. "+url;

        } else {
            htmlContent = "안녕하세요, "+name+"님의 웹라이브에 초대되었습니다 아래 링크를 클릭하여 ";
            htmlContent += name+"님의 웹라이브에 참가하세요. <a href='"+url+"' target='_blank'>";
            htmlContent += url+"</a>";

        }
        String Content = htmlContent;

        List<String> emails = (List<String>) params.get("emails");
        emails.forEach((email) -> {
            if(email!=null) {
                String to = email.toString();
                if(!to.isEmpty()) {
                    if(cmd.equals("pcs")) {
                        smsService.sendSMSAsync(to,Content,true);
                    } else {
                        mailService.mailSend(to,from,subject,Content);
                    }

                }

            }

        });
        resultMap.put("status", "success");

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


}

package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Entity.BroadcastBoardArticle;
import com.riverflow.livegoapp.Entity.Member;
import com.riverflow.livegoapp.Service.BroadcastBoardArticleService;
import com.riverflow.livegoapp.Service.BroadcastBoardService;
import com.riverflow.livegoapp.Service.BroadcastCommentService;
import com.riverflow.livegoapp.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

@RestController
@RequestMapping("/api/controller/broadcastBoardArticle/")
public class BroadcastBoardArticleController {

    @Autowired
    BroadcastBoardService broadcastBoardService;

    @Autowired
    BroadcastBoardArticleService broadcastBoardArticleService;

    @Autowired
    MemberService memberService;

    @Autowired
    BroadcastCommentService broadcastCommentService;

    /**
     * @ 게시글   작성/ 수정시
     * @param params
     * @return
     */
    @PostMapping("getBoardArticle")
    public ResponseEntity<HashMap<String, Object>> getBoardArticle(@RequestBody HashMap<String, String> params) {
        HashMap<String, Object> resultMap = new HashMap<>();

        HashMap<String, Object> boardInfo = broadcastBoardService.getBroadcastBoard(params);
        String boardStatus = (String) boardInfo.get("status");
        if(boardStatus.equals("success")) {
            HashMap<String, Object> boardArticleInfo = broadcastBoardArticleService.getBroadcastBoardArticle(params);
            resultMap.put("boardArticleInfo", boardArticleInfo);
            resultMap.put("boardInfo", boardInfo.get("info"));
            resultMap.put("status", "success");
        } else {
            resultMap.put("status", "error");
        }


        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @ 게시글   상세
     * @param params
     * @return
     */
    @PostMapping("getBoardArticleView")
    public ResponseEntity<HashMap<String, Object>> getBoardArticleView(@RequestBody HashMap<String, String> params) {
        HashMap<String, Object> resultMap = new HashMap<>();

        HashMap<String, Object> boardInfo = broadcastBoardService.getBroadcastBoard(params);
        String boardStatus = (String) boardInfo.get("status");
        if(boardStatus.equals("success")) {
            HashMap<String, Object> boardArticleInfo = broadcastBoardArticleService.getBroadcastBoardArticle(params);
            String articleStatus = (String) boardArticleInfo.get("status");
            if(articleStatus.equals("success")) {
                resultMap.put("boardArticleInfo", boardArticleInfo.get("info"));
                resultMap.put("boardInfo", boardInfo.get("info"));


                String no = params.get("no");
                Long pid = Long.parseLong(params.get("no"));
                HashMap<String, Object> commentMap = broadcastCommentService.getBroadcastCommentList(pid);

                resultMap.put("repleList",commentMap.get("list"));
                resultMap.put("status", "success");
            } else {
                resultMap.put("status", "error");
            }
        } else {
            resultMap.put("status", "error");
        }


        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }
    /**
     * @ 게시글   목록
     * @param params
     * @return
     */
    @PostMapping("getBoardArticleList")
    public ResponseEntity<HashMap<String, Object>> getBoardArticleList(@RequestBody HashMap<String, String> params) {
        HashMap<String, Object> resultMap = broadcastBoardArticleService.getBroadcastBoardArticleList(params);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @ 게시글   등록
     * @param params
     * @return
     */
    @PostMapping("insertBoardArticle")
    public ResponseEntity<HashMap<String, Object>> insertBoardArticle(@RequestBody HashMap<String, String> params, Principal principal) {

        String uid = "";
        String userName = params.get("userName");
        if(principal!=null) {
            uid = principal.getName();
            if (uid != null && !uid.isEmpty()) {
                Member member = memberService.getMemberByUid(uid);
                userName = member.getName();
            }
        } else {
            uid = params.get("uid");
        }

        HashMap<String, Object> resultMap = broadcastBoardArticleService.insertBroadcastBoardArticle(params,uid,userName);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @ 게시글   수정
     * @param params
     * @return
     */
    @PostMapping("updateBoardArticle")
    public ResponseEntity<HashMap<String, Object>> updateBoardArticle(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastBoardArticleService.updateBroadcastBoardArticle(params);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @ 게시글   삭제
     * @param params
     * @return
     */
    @PostMapping("deleteArticle")
    public ResponseEntity<HashMap<String, Object>> deleteArticle(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastBoardArticleService.deleteBroadcastBoardArticle(params);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }
    /**
     * @ 파일저장
     * @return
     */
    @PostMapping("uploadBoardArticleFile")
    public ResponseEntity<HashMap<String, Object>> uploadBoardArticleFile(@RequestParam(name = "file") MultipartFile file)  throws IOException {

        HashMap<String, Object> resultMap = broadcastBoardArticleService.uploadFile(file);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    @GetMapping("downBoardArticleFile")
    public  void  downBoardArticleFile(@RequestParam(name = "fileUrl") String fileUrl,
                           @RequestParam(name = "fileName") String fileName,
                           HttpServletResponse response) throws IOException {

        broadcastBoardArticleService.fileDown(fileUrl,fileName,response);
    }
}

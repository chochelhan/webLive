package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.BroadcastBoardArticle;
import com.riverflow.livegoapp.Repository.BroadcastBoardArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class BroadcastBoardArticleService {


    public String filePath = "fileUpload/broadcast/board";

    @Autowired
    BroadcastBoardArticleRepository broadcastBoardArticleRepository;

    /*
     *@  게시글 정보
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastBoardArticle(HashMap<String, String> params) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String no = params.get("no");

        if (no == null || no.isEmpty()) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Long id = Long.parseLong(no);
        BroadcastBoardArticle articleInfo = broadcastBoardArticleRepository.getById(id);
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("parentId", articleInfo.getParentId());
        info.put("uid", articleInfo.getUid());
        info.put("userName", articleInfo.getUserName());
        info.put("subject", articleInfo.getSubject());
        info.put("content", articleInfo.getContent());
        info.put("fileName", articleInfo.getFileName());
        info.put("fileUrl", articleInfo.getFileUrl());
        info.put("notice", articleInfo.getNotice());
        info.put("hit", articleInfo.getHit());
        info.put("createAt", articleInfo.getCreateAt());


        List<BroadcastBoardArticle> list = broadcastBoardArticleRepository.getArticleListByParentId(articleInfo.getParentId());
        List<Long> map = new ArrayList<>();
        int k = 0;
        int selectedK = 0;
        for(BroadcastBoardArticle article : list) {
            map.add(article.getId());
            if(article.getId()== id) {
                selectedK = k;
            }
            k++;
        }
        if(selectedK>0) {

            info.put("next",map.get(selectedK - 1));
        }
        if((selectedK +1) < map.size()) {
            info.put("pre",map.get(selectedK + 1));
        }


        result.put("info", info);

        result.put("status", "success");


        return result;
    }


    /*
     *@  게시글 목록
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getBroadcastBoardArticleList(HashMap<String, String> params) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String parentIdString = params.get("parentId");

        if (parentIdString == null || parentIdString.isEmpty()) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Long parentId = Long.parseLong(parentIdString);
        List<BroadcastBoardArticle> articleList = broadcastBoardArticleRepository.getArticleListByParentId(parentId);

        result.put("status", "success");
        result.put("list", articleList);

        return result;
    }

    public boolean getBoolArticle(Long parentId) {
        List<BroadcastBoardArticle> info = broadcastBoardArticleRepository.getArticleListByParentId(parentId);
        if(info==null) {
            return false;
        } else {
            return (info.size()>0)?true:false;
        }
    }
    /*
     *@  게시글 생성
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> insertBroadcastBoardArticle(HashMap<String, String> params, String uid, String userName) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String parentIdString = params.get("parentId");
        String subject = params.get("subject");
        String content = params.get("content");
        String notice = params.get("notice");
        String fileName = params.get("fileName");
        String fileUrl = params.get("fileUrl");

        int hit = 0;

        if (parentIdString == null || parentIdString.isEmpty()) {
            empty = true;
        }

        if (subject == null || subject.isEmpty()) {
            empty = true;
        }
        if (content == null || content.isEmpty()) {
            empty = true;
        }
        if (notice == null || notice.isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Long parentId = Long.parseLong(parentIdString);
        BroadcastBoardArticle broadcastBoardArticle = BroadcastBoardArticle.builder()
                .parentId(parentId)
                .uid(uid)
                .userName(userName)
                .subject(subject)
                .content(content)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .notice(notice)
                .hit(hit)
                .actType("insert")
                .build();

        BroadcastBoardArticle info = broadcastBoardArticleRepository.save(broadcastBoardArticle);

        result.put("status", "success");
        result.put("info", info);

        return result;
    }

    /*
     *@  게시글 수정
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> updateBroadcastBoardArticle(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        String no = params.get("no");
        String subject = params.get("subject");
        String content = params.get("content");
        String notice = params.get("notice");

        if (no == null || no.isEmpty()) {
            empty = true;
        }

        if (subject == null || subject.isEmpty()) {
            empty = true;
        }
        if (content == null || content.isEmpty()) {
            empty = true;
        }
        if (notice == null || notice.isEmpty()) {
            empty = true;
        }
        if (empty) {
            result.put("code", "empty");
            result.put("status", "message");
            return result;
        }
        Long id = Long.parseLong(no);
        BroadcastBoardArticle articleInfo = broadcastBoardArticleRepository.getById(id);
        if(articleInfo==null) {
            result.put("status", "error");
            return result;
        }
        String fileName = "";
        String fileUrl = "";

        if(params.get("fileUrl") !=null) {
            fileName = params.get("fileName");
            fileUrl = params.get("fileUrl");
        } else {

            fileName = articleInfo.getFileName();
            fileUrl = articleInfo.getFileUrl();
        }



        BroadcastBoardArticle broadcastBoardArticle = BroadcastBoardArticle.builder()
                .parentId(articleInfo.getParentId())
                .uid(articleInfo.getUid())
                .userName(articleInfo.getUserName())
                .subject(subject)
                .content(content)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .notice(notice)
                .hit(articleInfo.getHit())
                .createAt(articleInfo.getCreateAt())
                .actType("update")
                .actId(articleInfo.getId())
                .build();

        broadcastBoardArticleRepository.save(broadcastBoardArticle);

        result.put("status", "success");
        return result;
    }


    /*@  게시글 삭제
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> deleteBroadcastBoardArticle(HashMap<String, String> params) {

        Long id = Long.parseLong(params.get("id"));
        broadcastBoardArticleRepository.deleteById(id);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("status", "success");
        return result;
    }
    /*
     *@  파일 저장
     * params :
     * return : {status:(message,success,fail)}
     */
    public HashMap<String, Object> uploadFile(MultipartFile dFile) throws IOException {

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
                File file = new File(absolutePath + filePath);
                if (!file.exists()) {
                    file.mkdirs(); // mkdir()과 다르게 상위 폴더가 없을 때 상위폴더까지 생성
                }
                File saveFile = new File(absolutePath + filePath + "/" + newFileName + fileExtension);
                dFile.transferTo(saveFile);

                result.put("file", newFileName + fileExtension);
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
     *@  파일다운로드
     */
    public void fileDown(String fileUrl, String fname, HttpServletResponse response) throws IOException {
        try {
            Path currentPath = Paths.get("");
            String sitePath = currentPath.toAbsolutePath().toString();
            String absolutePath = new File(sitePath).getAbsolutePath() + "/"; // 파일이 저장될 절대 경로

            File file = new File(absolutePath + filePath+ "/" + fileUrl);
            int fsize = (int) file.length();

            if (fsize > 0) {
                String fileName = "attachment; filename="+fname;
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

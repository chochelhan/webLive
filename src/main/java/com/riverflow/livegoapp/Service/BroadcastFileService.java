package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.BroadcastExam;
import com.riverflow.livegoapp.Entity.BroadcastFile;
import com.riverflow.livegoapp.Repository.BroadcastFileRepository;
import com.riverflow.livegoapp.Repository.BroadcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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
public class BroadcastFileService {

    public String filePath = "fileUpload/broadcast/file";

    @Autowired
    BroadcastRepository broadcastRepository;

    @Autowired
    BroadcastFileRepository broadcastFileRepository;

    /*
     *@  파일업로드
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> inseartFile(MultipartFile dataFile,
                                                String ext,
                                                String name,
                                                String parentIdString) throws IOException {


        HashMap<String, Object> result = new HashMap<String, Object>();
        boolean empty = false;
        if (parentIdString == null || parentIdString.isEmpty()) {
            empty = true;
        }
        Long parentId  = Long.parseLong(parentIdString);
        String fileUrl = uploadFile(dataFile);
        BroadcastFile broadcastFile = BroadcastFile.builder()
                .parentId(parentId)
                .fileUrl(fileUrl)
                .name(name)
                .ext(ext)
                .status("normal")
                .actType("insert")
                .build();

        BroadcastFile info = broadcastFileRepository.save(broadcastFile);

        result.put("status", "success");
        result.put("info", info);

        return result;
    }

    private String uploadFile(MultipartFile dFile) throws IOException {

        HashMap<String, Object> result = new HashMap<String, Object>();
        Path currentPath = Paths.get("");
        String sitePath = currentPath.toAbsolutePath().toString();

        Date now = new Date();
        Long nowTime = now.getTime();

        String absolutePath = new File(sitePath).getAbsolutePath() + "/"; // 파일이 저장될 절대 경로
        String newFileName = "file_" + nowTime;
        String fileExtension = '.' + dFile.getOriginalFilename().replaceAll("^.*\\.(.*)$", "$1"); // 정규식 이용하여 확장자만 추출
        String fileUrl = "";
        try {
            if (!dFile.isEmpty()) {
                File file = new File(absolutePath + filePath);
                if (!file.exists()) {
                    file.mkdirs(); // mkdir()과 다르게 상위 폴더가 없을 때 상위폴더까지 생성
                }
                File saveFile = new File(absolutePath + filePath + "/" + newFileName + fileExtension);
                dFile.transferTo(saveFile);
                fileUrl = newFileName + fileExtension;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }


    /*
     *@  파일 목록 (parentId)
     * return : {status: -> message,success.fail} access_token
     */
    public List<BroadcastFile> getFileList(Long parentId) {

        List<BroadcastFile> list = broadcastFileRepository.getByParentIdOrderByIdDesc(parentId);

        return list;
    }

    /*
     *@  공유 목록 (parentId)
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getDataByShare(HashMap<String, String> params) {

        HashMap<String, Object> result = new HashMap<String, Object>();
        String parentIdString   = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);

        List<BroadcastFile> list = broadcastFileRepository.getByParentIdAndStatusOrderByIdDesc(parentId,"share");
        result.put("status", "success");
        result.put("list", list);

        return  result;
    }

    /*
     *@  파일 공유 상태 변경
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> updateDataShare(HashMap<String, String> params) {

        HashMap<String, Object> result = new HashMap<String, Object>();

        String idString = params.get("id");
        Long id = Long.parseLong(idString);

        BroadcastFile info = broadcastFileRepository.getById(id);

        BroadcastFile broadcastFile = BroadcastFile.builder()
                .parentId(info.getParentId())
                .fileUrl(info.getFileUrl())
                .name(info.getName())
                .ext(info.getExt())
                .status("share")
                .createAt(info.getCreateAt())
                .actType("update")
                .actId(id)
                .build();

        broadcastFileRepository.save(broadcastFile);

        result.put("status", "success");
        return result;
    }

    /*
     *@  파일 삭제
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> deleteData(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        String idString   = params.get("id");
        Long id = Long.parseLong(idString);
        BroadcastFile info = broadcastFileRepository.getById(id);
        if(info!=null && info.getStatus().equals("normal")) {
            result.put("status", "success");
            broadcastFileRepository.deleteById(id);
        } else {
            result.put("status", "message");
        }

        return result;
    }

    /*
     *@  파일 정보
     * return : {status: -> message,success.fail} access_token
     */
    public HashMap<String, Object> getData(HashMap<String, String> params) {


        HashMap<String, Object> result = new HashMap<String, Object>();
        String idString   = params.get("id");
        Long id = Long.parseLong(idString);
        BroadcastFile info = broadcastFileRepository.getById(id);
        result.put("status", "success");
        result.put("info", info);

        return result;
    }


    /*
     *@ 파일 가져오기
     * params :
     * return :
     */
    public byte[] getFile(String imgName) throws IOException {

        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Path currentPath = Paths.get("");
        String sitePath = currentPath.toAbsolutePath().toString();
        String absolutePath = new File(sitePath).getAbsolutePath() + "/"; // 파일이 저장될 절대 경로

        try {
            fis = new FileInputStream(absolutePath + filePath + "/" + imgName);
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

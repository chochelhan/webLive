package com.riverflow.livegoapp.Controllers;

import com.riverflow.livegoapp.Entity.BroadcastExam;
import com.riverflow.livegoapp.Entity.BroadcastFile;
import com.riverflow.livegoapp.Service.BroadcastExamService;
import com.riverflow.livegoapp.Service.BroadcastFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/controller/broadcastData/")
public class BroadcastDataController {

    @Autowired
    BroadcastFileService broadcastFileService;


    @Autowired
    BroadcastExamService broadcastExamService;

    /**
     * @ 파일저장
     * @return
     */

    @PostMapping("insertFile")
    public ResponseEntity<HashMap<String, Object>> insertFile(@RequestParam(name = "dataFile") MultipartFile dataFile,
                                                              @RequestParam(name = "ext") String ext,
                                                              @RequestParam(name = "name") String name,
                                                              @RequestParam(name = "parentId") String parentId)  throws IOException {



        HashMap<String, Object> resultMap = broadcastFileService.inseartFile(dataFile,ext,name,parentId);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }
    /**
     * @ 파일목록
     * @return
     */
    @PostMapping("getDataList")
    public ResponseEntity<HashMap<String, Object>> getDataList(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();
        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        List<BroadcastFile> list = broadcastFileService.getFileList(parentId);
        resultMap.put("status","success");
        resultMap.put("fileList",list);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @ 파일공유 상태 변경
     * @return
     */
    @PostMapping("updateDataShare")
    public ResponseEntity<HashMap<String, Object>> updateDataShare(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastFileService.updateDataShare(params);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @ 파일, 시험 문제 목록
     * @return
     */
    @PostMapping("getAllData")
    public ResponseEntity<HashMap<String, Object>> getAllData(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = new HashMap<>();
        String parentIdString = params.get("parentId");
        Long parentId = Long.parseLong(parentIdString);
        List<BroadcastFile> list = broadcastFileService.getFileList(parentId);

        List<BroadcastExam> examList =  broadcastExamService.getExamByParentId(parentId);
        resultMap.put("status","success");
        resultMap.put("fileList",list);
        resultMap.put("examList",examList);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     * @ 파일삭제
     * @return
     */
    @PostMapping("deleteData")
    public ResponseEntity<HashMap<String, Object>> deleteData(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastFileService.deleteData(params);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }


    /**
     * @ 파일정보
     * @return
     */
    @PostMapping("getData")
    public ResponseEntity<HashMap<String, Object>> getData(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastFileService.getData(params);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }



    /**
     * @ 방송 종료후에 공유내역
     * @return
     */
    @PostMapping("getDataShareList")
    public ResponseEntity<HashMap<String, Object>> getDataShareList(@RequestBody HashMap<String, String> params) {

        HashMap<String, Object> resultMap = broadcastFileService.getDataByShare(params);
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * 파일 가져오기
     * @return
     */
    @GetMapping("getFile")
    public  @ResponseBody byte[] getFile(@RequestParam(name = "fileUrl") String fileUrl) throws IOException {

        return broadcastFileService.getFile(fileUrl);
    }



    @GetMapping("fileDown")
    public  void  fileDown(@RequestParam(name = "fileUrl") String fileUrl,
                           @RequestParam(name = "fname") String fname,
                           HttpServletResponse response) throws IOException {

        broadcastFileService.fileDown(fileUrl,fname,response);
    }
}

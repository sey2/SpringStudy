package org.zerock.mreview.mreview.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.mreview.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

//    @Value("$org.zerock.upload.path")   // application.properties의 변수
//    private String uploadPath;

    private String uploadPath ="C:\\upload";


    /**
     *
     * MultiPartFile[] 배열을 이용해 여러개의 파일 정보를 처리 하도록 함
     * 메서드 내부에서는 아직 실제 파일을 업로드하지는 않지만 업로드하는 파일의 이름을 파악할 수 있다.
     * 브라우저에 따라 업로드하는 파일의 이름은 전체 경로일 수도 있고(IE계열), 단순히 파일의 이름만 의미할 수 있다.
     *
     * 파일을 저장하는 단계에서는 다음과 같은 사항을 고려해야만 한다.
     * 1. 업로드된 확장자가 이미지만 가능하도록 검사 (첨부파일을 이용한 원격 셀) (스크립트 파일등을 이용해 공격하는 기법 존재)
     * 2. 동일한 이름의 파일이 업로드 된다면 기존 파일을 덮어쓰는 문제      (시간 값을 추가하거나 UUID 이용)
     * 3. 업로드된 파일을 저장하는 폴더의 용량
     *    -> 동일한 폴더에 사진을 너무 많이 넣게 되면 성능저하가 나고 운영체제에 따라 하나의 폴더에 넣을 수 있는 파일의 수에
     *       대한 제한이 있음
     */

    private String makeFolder(){
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        // make folder -----
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdir();
        }

        return folderPath;
    }

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){
        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for(MultipartFile uploadFile: uploadFiles){
            if(uploadFile.getContentType().startsWith("image") == false){
                log.warn("this file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // 실제 파일 이름 IE나 Edge는 전체 경로가 들어 있으므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("fileName: " + fileName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            // UUID
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일 이름 중간에 _를 이용해서 구분
            String saveName = "C:\\upload" + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);

            try{
                uploadFile.transferTo(savePath);

                // 섬네일 생성
                String thumbnailSaveName = "C:\\upload" + File.separator + folderPath + File.separator
                        + "s_" + uuid + "_" + fileName;

                // 섬네일 파일 이름은 중간에 s_로 시작하도록
                File thumbnailFile = new File(thumbnailSaveName);

                // 섬네일 생성
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
            }catch (IOException e){
                e.printStackTrace();
            } // end for
        }// end for

        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    // URL 인코딩된 파일 이름을 파라미터로 받아서 해당 파일을 byte[]로 만들어서 브라우저로 전송
    // java.nio.file 패키지의 Files.probeContentType()을 이용해서 처리하고
    // 파일의 데이터 처리는 스프링에서 제공하는 org.springframework.util.FileCopyUtils를 이용해서 처리한다.
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size) {

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName =  URLDecoder.decode(fileName,"UTF-8");

            log.info("fileName: " + srcFileName);

            File file = new File(uploadPath +File.separator+ srcFileName);

            if(size != null && size.equals("1")){
                file  = new File(file.getParent(), file.getName().substring(2));
            }

            log.info("file: " + file);

            HttpHeaders header = new HttpHeaders();

            //MIME타입 처리
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            //파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){
        String srcFileName = null;

        try{
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            File thumbnail = new File(file.getParentFile(), "s_" + file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

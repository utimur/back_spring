package com.example.backend.controllers;

import com.example.backend.domain.User;
import com.example.backend.repos.UserRepo;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.utils.IoUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/upload")
public class FileUploadController {

    @Autowired
    UserRepo userRepo;

    String imgDirPath = "C:\\Users\\tim\\Desktop\\23.02.20_react_spring_app\\backend\\src\\main\\resources\\static\\img\\";

    @RequestMapping(method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("user_id") Long user_id) throws IOException {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        User user = userRepo.findById(user_id).get();

        File convertFile = new File(imgDirPath + fileName);
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(file.getBytes());
        fout.close();

        user.setAvatar(fileName);
        userRepo.save(user);
        return new ResponseEntity<>("File is uploaded successfully", HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<Object> getImage() throws IOException {
//                String fileName = imgDirPath + userRepo.findById(Integer.toUnsignedLong(1)).get().getAvatar();
//        InputStream in = getClass()
//                .getResourceAsStream(fileName);
//        byte[] array = IoUtils.toByteArray(in);
//        return new ResponseEntity<>(array, HttpStatus.OK);
//    }

        @GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Object> getFile(@RequestParam("user_id") Long user_id) throws IOException {
        String fileName = imgDirPath + userRepo.findById(user_id).get().getAvatar();
        File file = new File(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();

//        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");


        ResponseEntity<Object>
                responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(
                MediaType.parseMediaType("mediatype/form-data")).body(resource);
        return responseEntity;
    }

}
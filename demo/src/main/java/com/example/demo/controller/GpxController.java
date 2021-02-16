package com.example.demo.controller;

import com.example.demo.dao.GpxRepo;
import com.example.demo.model.UserGpx;
import com.example.demo.service.GpxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
public class GpxController {

    @Autowired
    private GpxService gpxService;

    @Autowired
    private GpxRepo gpxRepo;

    @PostMapping("/gpx/upload")
    public ResponseEntity<?> uploadGpx(@RequestParam("file") MultipartFile file, @RequestParam("username") String username) throws Exception {
        Map<String, Object> response = gpxService.uploadFileByUser(file, username);
        return ResponseEntity.status(HttpStatus.OK)
                .header("x-pagination-total", "0").body(response.get("data"));
    }

    @GetMapping(value = "/gpx/lastesttrack/{username}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getLastestTrackByUsername(@PathVariable(value = "username") String username, @RequestParam Map<String, String> param) throws Exception {
        param.put("username", username);
        Map<String, Object> response = gpxService.getLastestTrackByUserName(param);
        return ResponseEntity.status(HttpStatus.OK)
                .header("x-pagination-total", "0").body(response.get("data"));
    }

    @GetMapping(value = "/gpx/{gpxId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getDetail(@PathVariable(value = "gpxId") Long eid) throws Exception {
        Map<String, Object> response = gpxService.getById(eid);
        return ResponseEntity.status(HttpStatus.OK)
                .header("x-pagination-total", "0").body(response.get("data"));
    }

    @GetMapping("/gpx/download/{gpxId}")
    public ResponseEntity<?> downloadGpx(@PathVariable(value = "gpxId") Long eid) throws Exception {
        Optional<UserGpx> userGpx = gpxRepo.findById(eid);
        Resource resource = null;

        if (!userGpx.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        String fileName = userGpx.get().getFileName();
        if (fileName != null && !fileName.isEmpty()) {
            try {
                resource = gpxService.loadFileAsResource(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String contentType = null;
            try {
                contentType = userGpx.get().getDocumentFormat();
            } catch (Exception ex) {

            }

            if (contentType == null) {
                contentType = "application/gpx+xml";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }
}

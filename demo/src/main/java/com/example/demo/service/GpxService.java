package com.example.demo.service;

import com.example.demo.configuration.FileStorageProperties;
import com.example.demo.dao.GpxRepo;
import com.example.demo.model.latestTrackView;
import com.example.demo.model.responseModel;
import com.example.demo.model.UserGpx;
import com.example.demo.model.gpx;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.*;

@Component
public class GpxService {
    private final Path fileStorageLocation;

    private final String fileSupportExtension;

    @Autowired
    GpxRepo gpxRepo;

    @Autowired
    public GpxService(FileStorageProperties fileStorageProperties) throws Exception {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        this.fileSupportExtension = fileStorageProperties.getSupportExtension();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new Exception("Could not create the directory where the uploaded files will be stored.");
        }
    }

    public Map<String, Object> uploadFileByUser(MultipartFile file, String userName) throws Exception {
        String request_id = UUID.randomUUID().toString();
        responseModel responseModel = new responseModel();
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName;
        try {

            if (originalFileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            String fileExtension;
            try {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            } catch (Exception e) {
                fileExtension = "";
            }

            if (this.fileSupportExtension.indexOf(fileExtension) < 0) {
                throw new Exception("Sorry! File not support");
            }

            fileName = userName + "_" + UUID.randomUUID().toString() + "_" + fileExtension;
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            InputStreamReader isReader = new InputStreamReader(file.getInputStream());
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }

            UserGpx userGpx = new UserGpx();
            userGpx.setUsername(userName);
            userGpx.setGpx(sb.toString());
            userGpx.setUploadDir(targetLocation.toString());
            userGpx.setDocumentFormat(file.getContentType());
            userGpx.setFileName(fileName);
            userGpx.setCreatedDate(new Timestamp(new Date().getTime()));
            userGpx.setCreatedBy(userName);
            gpxRepo.save(userGpx);

            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code(0);
            responseModel.setMessage("OK");
            responseModel.setData(userGpx);

        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setRequest_id(request_id);
            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code(500);
            responseModel.setStatus(500);
            responseModel.setMessage(e.getMessage());
        }

        return Map.of("status", 200, "data", responseModel);
    }

    public Map<String, Object> getLastestTrackByUserName(Map<String, String> param) {
        String request_id = UUID.randomUUID().toString();
        responseModel responseModel = new responseModel();

        //get param
        String username = param.getOrDefault("username", "");
        int page = Integer.parseInt(param.getOrDefault("page", "0"));
        if (page != 0)
            page = page - 1;
        int limit = Integer.parseInt(param.getOrDefault("limit", "10"));
        String[] sort = param.getOrDefault("sort", "created_date,desc").split(",");

        try {
            List<UserGpx> userGpxList = new ArrayList<UserGpx>();
            Pageable paging = PageRequest.of(page, limit, Sort.by(sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sort[0]));

            Page<UserGpx> userGpxPage = gpxRepo.findAllByUsername(username, paging);

            if (userGpxPage != null) {
                userGpxList = userGpxPage.getContent();
            }


            //
            JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper(false);
            XmlMapper xmlMapper = new XmlMapper(module);
            List<latestTrackView> lastestTrackViewList = new ArrayList<latestTrackView>();

            latestTrackView lastestTrackView = new latestTrackView();
            for (UserGpx userGpx : userGpxList) {
                gpx gpx = xmlMapper.readValue(userGpx.getGpx(), gpx.class);
                lastestTrackView = new latestTrackView(userGpx.getGpx_id(), gpx.getMetadata().getName(), gpx.getMetadata().getDesc());
                lastestTrackViewList.add(lastestTrackView);
            }

            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code(0);
            responseModel.setMessage("OK");
            responseModel.setData(Map.of("result", lastestTrackViewList, "total",
                    userGpxPage.getTotalElements(), "totalpage", userGpxPage.getTotalPages()));

        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setRequest_id(request_id);
            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code(500);
            responseModel.setStatus(500);
            responseModel.setMessage("Others error");
        }
        return Map.of("status", 200, "data", responseModel);
    }

    public Map<String, Object> getById(Long Id) throws Exception {
        String request_id = UUID.randomUUID().toString();
        responseModel responseModel = new responseModel();

        try {
            Optional<UserGpx> userGpx = gpxRepo.findById(Id);

            if (!userGpx.isPresent()) {
                responseModel.setRequest_id(request_id);
                responseModel.setReference_id(UUID.randomUUID().toString());
                responseModel.setDate_time(new Timestamp(new Date().getTime()));
                responseModel.setResult_code(500);
                responseModel.setMessage("No record");

                return Map.of("status", 404, "data", responseModel);
            }

            gpx gpx = new gpx();
            JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper(false);
            XmlMapper xmlMapper = new XmlMapper(module);
            gpx = xmlMapper.readValue(userGpx.get().getGpx(), gpx.class);

            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code(0);
            responseModel.setMessage("OK");
            responseModel.setData(Map.of("createddate",userGpx.get().getCreatedDate(),"gpx", gpx));

        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setRequest_id(request_id);
            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code(500);
            responseModel.setMessage("Others error");
        }
        return Map.of("status", 200, "data", responseModel);
    }

    public Map<String, Object> downloadFile(Long Id) {
        String request_id = UUID.randomUUID().toString();
        responseModel responseModel = new responseModel();

        try {

            Optional<UserGpx> userGpx = gpxRepo.findById(Id);
            Resource resource = null;

            if (!userGpx.isPresent()) {
                responseModel.setRequest_id(request_id);
                responseModel.setReference_id(UUID.randomUUID().toString());
                responseModel.setDate_time(new Timestamp(new Date().getTime()));
                responseModel.setResult_code(500);
                responseModel.setMessage("No record");

                return Map.of("status", 500, "data", responseModel);
            }

            String fileName=userGpx.get().getFileName();
            if (fileName != null && !fileName.isEmpty()) {
                try {

                    resource = this.loadFileAsResource(fileName);

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

                responseModel.setReference_id(UUID.randomUUID().toString());
                responseModel.setDate_time(new Timestamp(new Date().getTime()));
                responseModel.setResult_code(0);
                responseModel.setMessage("OK");
                responseModel.setData(Map.of("resource",resource,"contentType",contentType,"fileName",fileName));
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setRequest_id(request_id);
            responseModel.setReference_id(UUID.randomUUID().toString());
            responseModel.setDate_time(new Timestamp(new Date().getTime()));
            responseModel.setResult_code(500);
            responseModel.setStatus(500);
            responseModel.setMessage("Others error");
        }

        return Map.of("status", 200, "data", responseModel);
    }

      private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new Exception("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new Exception("File not found " + fileName);
        }
    }

}

package com.pdfsolutions.controller;

import com.pdfsolutions.common.APIResponse;
import com.pdfsolutions.common.CommonUtils;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@RestController
@Log
public class ConverterController {

    public static final String CONVERT = "/api/upload";
    private static final String BASE_URL = "https://api-tasker.onlineconvertfree.com";


    @PostMapping(value = "/api/covert/doxtoxml")
    public ResponseEntity<?> convertDocToXml(@RequestPart(name = "docfile", required = true) MultipartFile docfile,
                                             @RequestPart(name = "token", required = true) String token) {
        APIResponse apiResponse = new APIResponse();
        try {
            // Used to call third party API's
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
            ByteArrayResource contentsAsResource = null;
            contentsAsResource = new ByteArrayResource(docfile.getBytes()) {
                @Override
                public String getFilename() {
                    return docfile.getOriginalFilename();
                }
            };

            multipartBodyBuilder.part("file", contentsAsResource);
            multipartBodyBuilder.part("to", "xml");
            multipartBodyBuilder.part("token", token);
            log.info("TOKEN:  {}" + token);
            MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();
            HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);
            ResponseEntity<?> responseEntity = restTemplate.postForEntity(BASE_URL + CONVERT, httpEntity, String.class);

            String url = CommonUtils.convertStringToJsonObj(responseEntity.getBody().toString()).get("CONVERTED_FILE").getAsString();
            log.info("Response ULRL: {}" + url);

            // apiResponse.setResults(readURLToString(url));
            apiResponse.setResults(url);
            apiResponse.setResponseCode("200");
            apiResponse.setResponseMessage("File Converted successfully");
            ResponseEntity.status(Integer.valueOf(apiResponse.getResponseCode())).body(apiResponse);

        } catch (HttpStatusCodeException ex) {
            log.info(ex.getMessage());
            switch (ex.getRawStatusCode()) {
                case 403:
                    apiResponse.setResponseCode("403");
                    apiResponse.setResponseMessage("Please check the token or time stamp");
                    return ResponseEntity.status(Integer.valueOf(apiResponse.getResponseCode())).body(apiResponse);
                case 500:
                    apiResponse.setResponseCode("500");
                    apiResponse.setResponseMessage("OCFService Are Down");
                    return ResponseEntity.status(Integer.valueOf(apiResponse.getResponseCode())).body(apiResponse);
                default:
                    apiResponse.setResponseCode("500");
                    apiResponse.setResponseMessage("An error occurred, please contact your system administrator.");
                    return ResponseEntity.status(Integer.valueOf(apiResponse.getResponseCode())).body(apiResponse);
            }
        } catch (Exception e) {
            apiResponse.setResponseCode("500");
            apiResponse.setResponseMessage("An error occurred, please contact your system administrator.");
            apiResponse.setErrors(Arrays.asList(e.getMessage()));
            e.printStackTrace();
        }
        return ResponseEntity.status(Integer.valueOf(apiResponse.getResponseCode())).body(apiResponse);
    }

    public String readURLToStringBase64(String url) throws IOException {
        try (InputStream inputStream = new URL(url).openStream()) {
            return Base64.getEncoder().encodeToString(IOUtils.toString(inputStream, StandardCharsets.US_ASCII).getBytes());
        }
    }


    @PostMapping(value = "/api/covert/xmltopdf")
    public ResponseEntity<APIResponse> convertXmlToPdf(@RequestPart(name = "xmlFile", required = true) MultipartFile xmlFile,
                                                       @RequestPart(name = "token", required = true) String token) {
        APIResponse apiResponse = new APIResponse();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
            ByteArrayResource contentsAsResource = null;
            contentsAsResource = new ByteArrayResource(xmlFile.getBytes()) {
                @Override
                public String getFilename() {
                    return xmlFile.getOriginalFilename();
                }
            };

            multipartBodyBuilder.part("file", contentsAsResource);
            multipartBodyBuilder.part("to", "pdf");
            multipartBodyBuilder.part("token", token);
            log.info("TOKEN:  {}" + token);
            MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();
            HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);
            ResponseEntity<?> responseEntity = restTemplate.postForEntity(BASE_URL + CONVERT, httpEntity, String.class);

            String url = CommonUtils.convertStringToJsonObj(responseEntity.getBody().toString()).get("CONVERTED_FILE").getAsString();
            log.info("Response URL: {}" + url);
            apiResponse.setResults(url);
            apiResponse.setResponseCode("200");
            apiResponse.setResponseMessage("File Converted successfully");
            ResponseEntity.status(Integer.valueOf(apiResponse.getResponseCode())).body(apiResponse);

        } catch (HttpStatusCodeException ex) {
            log.info(ex.getMessage());
            switch (ex.getRawStatusCode()) {
                case 403:
                    apiResponse.setResponseCode("403");
                    apiResponse.setResponseMessage("Please check the token or the time stamp");
                    return ResponseEntity.status(Integer.valueOf(apiResponse.getResponseCode())).body(apiResponse);
                case 500:
                    apiResponse.setResponseCode("500");
                    apiResponse.setResponseMessage("OCF Service Are Down");
                    return ResponseEntity.status(Integer.valueOf(apiResponse.getResponseCode())).body(apiResponse);
                default:
                    apiResponse.setResponseCode("500");
                    apiResponse.setResponseMessage("An error occurred, please contact your system administrator.");
                    return ResponseEntity.status(Integer.valueOf(apiResponse.getResponseCode())).body(apiResponse);
            }
        } catch (Exception e) {
            apiResponse.setResponseCode("500");
            apiResponse.setResponseMessage("An error occurred, please contact your system administrator.");
            apiResponse.setErrors(Arrays.asList(e.getMessage()));
            e.printStackTrace();
        }
        return ResponseEntity.status(Integer.valueOf(apiResponse.getResponseCode())).body(apiResponse);
    }

    @GetMapping(value = "/api/gettext")
    public String readURLToString(@RequestParam(value = "url",required = true) String url) throws IOException {
        try (InputStream inputStream = new URL(url).openStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }

}

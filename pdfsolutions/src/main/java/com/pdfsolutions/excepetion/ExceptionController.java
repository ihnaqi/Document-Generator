package com.pdfsolutions.excepetion;

import com.pdfsolutions.common.APIResponse;
import com.pdfsolutions.common.ErrorDTO;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<APIResponse> handleFileException(HttpServletRequest request, Throwable ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(new APIResponse().builder().responseCode("500").responseMessage("File Not Found").build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<APIResponse> handleMissingServletRequestPartException(HttpServletRequest request, Throwable ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(new APIResponse().builder().responseCode("500").responseMessage(ex.getMessage()).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<APIResponse> handleMethodArgumentException(MethodArgumentNotValidException exception) {
        APIResponse<?> serviceResponse = new APIResponse<>();
        List<ErrorDTO> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    ErrorDTO errorDTO = new ErrorDTO(error.getField(), error.getDefaultMessage());
                    errors.add(errorDTO);
                });
        serviceResponse.setResponseCode("400");
        serviceResponse.setResponseMessage("BAD_REQUEST");
        serviceResponse.setErrors(errors);
        return ResponseEntity.status(Integer.valueOf(serviceResponse.getResponseCode())).body(serviceResponse);

    }
}
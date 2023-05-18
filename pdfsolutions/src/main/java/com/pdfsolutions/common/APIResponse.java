package com.pdfsolutions.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
public class APIResponse<T> {

    private String responseCode;
    private String responseMessage;
    private List<ErrorDTO> errors;
    private Object results;

}
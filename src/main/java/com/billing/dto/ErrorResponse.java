package com.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ErrorResponse {
    private List<Error> errors;
    private LocalDateTime timestamp;
    private String errorCode;

    public boolean hasErrors() {
        log.debug("Inside hasErrors method {}.", this.errors);
        return this.errors.size() > 0;
    }

}

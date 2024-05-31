package com.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    private String type;
    private ErrorResponse errorResponse;
    private String description;
    private LocalDateTime timestamp;
}

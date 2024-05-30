//package com.billing.controller.rest;
//
//import com.billing.constant.Constants;
//import com.billing.dto.ErrorResponse;
//import com.billing.dto.Response;
//import com.billing.entity.Estimation;
//import com.billing.service.EstimationService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//
//@Slf4j
//@RestController
//public class BillingController {
//
//    @Autowired
//    private EstimationService estimationService;
//
//    @PostMapping("/estimation/save")
//    public Response saveEstimation(@RequestBody Estimation estimation) {
//        log.debug("Inside saveEstimation {}", estimation);
//        Response response = Response.builder()
//                .type(Constants.SUCCESS)
//                .description("Save is successful.")
//                .timestamp(LocalDateTime.now())
//                .build();
//        ErrorResponse errorResponse = estimationService.validateEstimation(estimation);
//        if (errorResponse.hasErrors()) {
//            response.setErrorResponse(errorResponse);
//            response.setType(Constants.ERROR);
//            response.setDescription("Data validation error");
//            return response;
//        }
//        estimationService.save(estimation);
//        return response;
//    }
//
//    @PostMapping("/estimation/print")
//    public Response printEstimation(@RequestBody Estimation estimation) {
//        log.debug("Inside saveEstimation {}", estimation);
//        Response response = Response.builder()
//                .type(Constants.SUCCESS)
//                .description("Print is successful.")
//                .timestamp(LocalDateTime.now())
//                .build();
//        ErrorResponse errorResponse = estimationService.validateEstimation(estimation);
//        if (errorResponse.hasErrors()) {
//            response.setErrorResponse(errorResponse);
//            response.setType(Constants.ERROR);
//            response.setDescription("Data validation error");
//            return response;
//        }
//        estimationService.print(estimation);
//        return response;
//    }
//}

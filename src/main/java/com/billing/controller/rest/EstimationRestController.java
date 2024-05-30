package com.billing.controller.rest;

import com.billing.dto.EstimationDTO;
import com.billing.service.EstimationService;
import com.billing.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/estimation")
public class EstimationRestController {

    private final EstimationService estimationService;
    private final PaymentService paymentService;

    public EstimationRestController(EstimationService estimationService, PaymentService paymentService) {
        this.estimationService = estimationService;
        this.paymentService = paymentService;
    }

    // Get all estimations
    @GetMapping
    public List<EstimationDTO> getAllEstimations() {
        return estimationService.getAllEstimations();
    }

//    @GetMapping("/customer/{id}/estimations")
//    public List<EstimationDTO> customerEstimations(@PathVariable Long id) {
//        return estimationService.getAllEstimationsByCustomerId(id);
//    }

    // Get estimation by ID
    @GetMapping("/{id}")
    public ResponseEntity<EstimationDTO> getEstimationById(@PathVariable Long id) {
        EstimationDTO estimation = estimationService.getEstimationById(id);
        return new ResponseEntity<>(estimation, HttpStatus.OK);
    }

    // Create a new estimation
    @PostMapping
    public ResponseEntity<EstimationDTO> createEstimation(@RequestBody EstimationDTO estimation) {
        EstimationDTO savedEstimation = estimationService.createEstimation(estimation);
        return new ResponseEntity<>(savedEstimation, HttpStatus.CREATED);
    }

    // Update an existing estimation
    @PutMapping("/{id}")
    public ResponseEntity<EstimationDTO> updateEstimation(@PathVariable Long id, @RequestBody EstimationDTO estimation) {
        EstimationDTO updatedEstimation = estimationService.updateEstimation(id, estimation);
        if (updatedEstimation != null) {
            return ResponseEntity.ok(updatedEstimation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a estimation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstimation(@PathVariable Long id) {
        boolean deleted = estimationService.deleteEstimation(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}

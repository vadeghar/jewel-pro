package com.billing.service;

import com.billing.dto.ErrorResponse;
import com.billing.dto.Error;
import com.billing.entity.Worker;
import com.billing.repository.WorkerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.billing.constant.ErrorCode.DATA_ERROR_WORKER;
@Slf4j
@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Optional<Worker> getWorkerById(Long id) {
        return workerRepository.findById(id);
    }

    public Worker createWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    public Worker updateWorker(Long id, Worker worker) {
        if (workerRepository.existsById(id)) {
            worker.setId(id);
            return workerRepository.save(worker);
        }
        return null; // or throw an exception indicating worker not found
    }

    public void deleteWorker(Long id) {
        workerRepository.deleteById(id);
    }

    public ErrorResponse validateWorker(Worker worker) {
        log.debug("Validating worker {}", worker);
        List<Error> errors = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse(errors, LocalDateTime.now(), DATA_ERROR_WORKER.getCode());

        // Check if worker has a name
        if (worker.getName() == null || worker.getName().isEmpty()) {
            errorResponse.getErrors().add(new Error("Name is required.", "Error"));
        }

        // Check if worker has a phone number
        if (worker.getPhone() == null || worker.getPhone().isEmpty()) {
            errorResponse.getErrors().add(new Error("Phone number is required.", "Error"));
        }

        // Check if worker has an area
        if (worker.getArea() == null || worker.getArea().isEmpty()) {
            errorResponse.getErrors().add(new Error("Area is required.", "Error"));
        }

        log.debug("Exiting validate worker, Found errors: {}", errorResponse.getErrors().size());
        return errorResponse;
    }

}


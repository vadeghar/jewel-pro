package com.billing.service;

import com.billing.entity.WorkOrder;
import com.billing.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    public List<WorkOrder> getAllWorkOrders() {
        return workOrderRepository.findAll();
    }

    public Optional<WorkOrder> getWorkOrderById(Long id) {
        return workOrderRepository.findById(id);
    }

    public WorkOrder createWorkOrder(WorkOrder workOrder) {
        return workOrderRepository.save(workOrder);
    }

    public WorkOrder updateWorkOrder(Long id, WorkOrder workOrder) {
        if (workOrderRepository.existsById(id)) {
            workOrder.setId(id);
            return workOrderRepository.save(workOrder);
        }
        return null; // or throw an exception indicating work order not found
    }

    public void deleteWorkOrder(Long id) {
        workOrderRepository.deleteById(id);
    }
}


package com.billing.controller;

import com.billing.dto.ErrorResponse;
import com.billing.entity.Worker;
import com.billing.entity.Worker;
import com.billing.service.WorkerService;
import com.billing.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/worker")
public class WorkerController {
        @Autowired
        private WorkerService workerService;

        @GetMapping
        public String showWorkerList(Model model) {
            List<Worker> workerlist = workerService.getAllWorkers();
            model.addAttribute("workerList", workerlist);
            model.addAttribute("worker", new Worker());
            return "worker";
        }

//    @GetMapping("/add")
//    public String showAddWorkerForm(Model model) {
//        model.addAttribute("workerList", workerService.getAllWorkers());
//        model.addAttribute("worker", new Worker());
//        return "worker";
//    }

        @PostMapping("/save")
        public String addWorker(@ModelAttribute Worker worker, Model model) {
            ErrorResponse errorResponse = workerService.validateWorker(worker);
            model.addAttribute("workerList", workerService.getAllWorkers());
            if(errorResponse.hasErrors()) {
                model.addAttribute("worker", worker);
                model.addAttribute("errorResponse", errorResponse);
                return "worker";
            }

            workerService.createWorker(worker);
            return "redirect:/worker";
        }

//    @GetMapping("/edit/{id}")
//    public String showEditWorkerForm(@PathVariable Long id, Model model) {
//        Optional<Worker> worker = workerService.getWorkerById(id);
//        worker.ifPresent(value -> model.addAttribute("worker", value));
//        model.addAttribute("workerList", workerService.getAllWorkers());
//        return "worker";
//    }

        @PostMapping("/edit/{id}")
        public String editWorker(@PathVariable Long id, @ModelAttribute Worker worker) {
            workerService.updateWorker(id, worker);
            return "redirect:/worker";
        }

        @GetMapping("/delete/{id}")
        public String deleteWorker(@PathVariable Long id) {
            workerService.deleteWorker(id);
            return "redirect:/worker";
        }
}

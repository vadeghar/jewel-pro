package com.billing.controller;

import com.billing.dto.ErrorResponse;
import com.billing.dto.PurchaseDTO;
import com.billing.entity.Purchase;
import com.billing.service.PurchaseItemService;
import com.billing.service.PurchaseService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PurchaseItemService purchaseItemService;
    public PurchaseController(PurchaseService purchaseService, PurchaseItemService purchaseItemService) {
        this.purchaseService = purchaseService;
        this.purchaseItemService = purchaseItemService;
    }

    @GetMapping
    public String get(Model model, @RequestParam(required = false) Long id) {
        model.addAttribute("purchaseId", id);
        return "views/purchase/purchase";
    }

    @GetMapping("purchase-list")
    public String list() {
        return "views/purchase/purchase-list";
    }

    @GetMapping("/view")
    public String view(@RequestParam Long id, Model model) {
        model.addAttribute("purchaseId", id);
        return "views/purchase/purchase-view";
    }

    @GetMapping("add-purchase")
    public String addPurchase(Model model) {
        return "views/purchase/purchase";
    }

    @PostMapping("edit-purchase")
    public String editPurchase(@ModelAttribute PurchaseDTO purchaseDTO, Model model) {
        model.addAttribute("purchaseId", purchaseDTO.getId());
        return "views/purchase/purchase";
    }

    @GetMapping("/add-payment")
    public String payment(Model model) {
        return "views/payment/add-payment-modal";
    }

    @GetMapping("/payment-modal")
    public String paymentModal() {
        return "views/payment/list-payment-modal";
    }
    @PostMapping(value = "/purchase-items")
    public String purchaseItems(@ModelAttribute PurchaseDTO purchaseDTO, Model model) {
        model.addAttribute("purchaseId", purchaseDTO.getId());
        return "views/purchase/purchase-items";
    }

//    @PostMapping
//    public String edit(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("purchase") Purchase purchase,
//                      BindingResult result,
//                      Model model) {
//        if (httpHeaders.containsKey("X-Application-Name")) {
//            System.out.println("Found X-Application-Name in header");
//        }
//        if (purchase.getId() == null) {
//            throw new RuntimeException("purchase id missing.");
//        }
//        List<Purchase> purchaseList = purchaseService.getAll();
//        model.addAttribute("purchaseList", purchaseList);
//        model.addAttribute("purchase", purchaseService.getById(purchase.getId()));
//        return "purchase";
//    }
//
//    @PostMapping("/save")
//    public String registration(@RequestHeader HttpHeaders httpHeaders, @Valid @ModelAttribute("purchase") Purchase purchase,
//                               BindingResult result,
//                               Model model) {
//        if (httpHeaders.containsKey("X-Application-Name")) {
//            System.out.println("Found X-Application-Name in header");
//        }
//        ErrorResponse errorResponse = purchaseService.validatePurchase(purchase);
//        model.addAttribute("purchaseList", purchaseService.getAll());
//        if(errorResponse.hasErrors()) {
//            model.addAttribute("purchase", purchase);
//            model.addAttribute("errorResponse", errorResponse);
//            return "item";
//        }
//        model.addAttribute("purchase", purchaseService.save(purchase));
//        return "redirect:/purchase?success";
//    }
//
//
//    @PostMapping("/items")
//    public String items(@ModelAttribute("purchase") Purchase purchase,
//                                                  BindingResult result,
//                                                  Model model) {
//        Purchase dbPurchase = purchaseService.getById(purchase.getId());
//        System.out.println("DB Purchase: "+dbPurchase);
//        model.addAttribute("purchase", dbPurchase);
//        model.addAttribute("purchaseItems", purchase.getPurchaseItems());
//        return "purchaseItems";
//    }
//
//    @PostMapping("/items/saveItems")
//    public String saveItems(@ModelAttribute("purchase") Purchase purchase,
//                                      BindingResult result,
//                                      Model model) {
//        System.out.println("Save items: "+purchase.getPurchaseItems());
//        Purchase dbPurchase = purchaseService.getById(purchase.getId());
//        purchase.getPurchaseItems()
//                .stream()
//                        .forEach(pi -> pi.setPurchase(dbPurchase));
//        dbPurchase.setPurchaseItems(purchase.getPurchaseItems());
//        purchaseService.save(dbPurchase);
//        return items(purchase, result, model);
//    }

}

//package com.billing.repository;
//
//import com.billing.entity.Purchase;
//import com.billing.model.ReportFilters;
//import org.springframework.data.jpa.domain.Specification;
//
//import javax.persistence.criteria.*;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.temporal.TemporalAdjusters;
//
//public class PurchaseSpecification {
//
//    public static Specification<Purchase> byFilters(ReportFilters filters) {
//
//        return (Root<Purchase> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
//            Predicate p = cb.conjunction();
//
//            final LocalDate startDate1 = (filters.getStartDate() == null)
//                    ? LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
//                    : filters.getStartDate();
//
//            final LocalDate endDate1 = (filters.getEndDate() == null)
//                    ? LocalDate.now()
//                    : filters.getEndDate();
//
//            if (filters.getPaymentMode() != null) {
//                p = cb.and(p, cb.equal(root.get("paymentMode"), filters.getPaymentMode()));
//            }
//
//            if (filters.getSupplierId() != null) {
//                p = cb.and(p, cb.equal(root.get("supplier").get("id"), filters.getSupplierId()));
//            }
//
//            if (filters.getMetalType() != null) {
//                p = cb.and(p, cb.equal(root.get("metalType"), filters.getMetalType()));
//            }
//
//            if (filters.getPurchaseType() != null) {
//                p = cb.and(p, cb.equal(root.get("purchaseType"), filters.getPurchaseType()));
//            }
//
//            p = cb.and(p, cb.between(root.get("purchaseDate"), startDate1, endDate1));
//
//            if (filters.getMinPurchaseAmount() != null && filters.getMaxPurchaseAmount() != null) {
//                p = cb.and(p, cb.between(root.get("purchaseAmount"), filters.getMinPurchaseAmount(), filters.getMaxPurchaseAmount()));
//            }
//
//            if (filters.getMinBalAmount() != null && filters.getMaxBalAmount() != null) {
//                p = cb.and(p, cb.between(root.get("balAmount"), filters.getMinBalAmount(), filters.getMaxBalAmount()));
//            }
//
//            return p;
//        };
//    }
//}
//

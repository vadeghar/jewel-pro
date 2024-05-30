package com.billing.repository;

import com.billing.entity.Purchase;
import com.billing.model.ChartData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findAllByActivePurchase(String activePurchase);

    List<Purchase> findBySupplierId(Long id);

    @Query("SELECT new com.billing.model.ChartData(CONCAT(YEAR(p.purchaseDate), ''), SUM(p.totalPurchaseAmount)) " +
            "FROM Purchase p " +
            "WHERE p.purchaseDate BETWEEN :startDate AND :endDate " +
            "GROUP BY CONCAT(YEAR(p.purchaseDate), '') " +
            "ORDER BY CONCAT(YEAR(p.purchaseDate), '')")
    List<ChartData> findPurchasesByYear(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);



    @Query("SELECT new com.billing.model.ChartData(CONCAT(YEAR(p.purchaseDate), '-', MONTH(p.purchaseDate)), SUM(p.totalPurchaseAmount)) " +
            "FROM Purchase p " +
            "WHERE p.purchaseDate BETWEEN :startDate AND :endDate " +
            "GROUP BY CONCAT(YEAR(p.purchaseDate), '-', MONTH(p.purchaseDate)) " +
            "ORDER BY CONCAT(YEAR(p.purchaseDate), '-', MONTH(p.purchaseDate)) ")
    List<ChartData> findPurchasesByMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.billing.model.ChartData(CONCAT(YEAR(p.purchaseDate), '-', FUNCTION('WEEK', p.purchaseDate)), SUM(p.totalPurchaseAmount)) " +
            "FROM Purchase p " +
            "WHERE p.purchaseDate BETWEEN :startDate AND :endDate " +
            "GROUP BY CONCAT(YEAR(p.purchaseDate), '-', FUNCTION('WEEK', p.purchaseDate)) " +
            "ORDER BY CONCAT(YEAR(p.purchaseDate), '-', FUNCTION('WEEK', p.purchaseDate)) ")
    List<ChartData> findPurchasesByWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(p.totalPurchaseAmount) FROM Purchase p " +
            "WHERE YEAR(p.purchaseDate) = YEAR(CURRENT_DATE()) " +
            "AND MONTH(p.purchaseDate) = MONTH(CURRENT_DATE())")
    BigDecimal getCurrentMonthTotalPurchaseAmount();

    @Query("SELECT new com.billing.model.ChartData(p.supplier.name, SUM(p.totalPurchaseAmount)) " +
            "FROM Purchase p " +
            "WHERE p.purchaseDate >= CURRENT_DATE - 30 " +
            "GROUP BY p.supplier.name " +
            "ORDER BY SUM(p.totalPurchaseAmount) DESC")
    List<ChartData> findTopSuppliersByTotalAmountLast5Days(PageRequest pageable);
}

package com.billing.repository;

import com.billing.entity.Sale;
import com.billing.model.ChartData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT MAX(s.id) FROM Sale s")
    Long findMaxId();

    List<Sale> findByCustomerId(Long customerId);

    @Query("SELECT new com.billing.model.ChartData(CONCAT(YEAR(s.saleDate), '-', MONTH(s.saleDate)), SUM(s.totalSaleAmount)) " +
            "FROM Sale s " +
            "WHERE s.saleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY CONCAT(YEAR(s.saleDate), '-', MONTH(s.saleDate)) " +
            "ORDER BY CONCAT(YEAR(s.saleDate), '-', MONTH(s.saleDate)) ")
    List<ChartData> findSalesByMonthly(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.billing.model.ChartData(CONCAT(YEAR(s.saleDate), '-', FUNCTION('WEEK', s.saleDate)), SUM(s.totalSaleAmount)) " +
            "FROM Sale s " +
            "WHERE s.saleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY CONCAT(YEAR(s.saleDate), '-', FUNCTION('WEEK', s.saleDate)) " +
            "ORDER BY CONCAT(YEAR(s.saleDate), '-', FUNCTION('WEEK', s.saleDate)) ")
    List<ChartData> findSalesByWeekly(LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(s.totalSaleAmount) FROM Sale s " +
            "WHERE YEAR(s.saleDate) = YEAR(CURRENT_DATE()) " +
            "AND MONTH(s.saleDate) = MONTH(CURRENT_DATE())")
    BigDecimal getCurrentMonthTotalSaleAmount();
    @Query("SELECT new com.billing.model.ChartData(CONCAT(YEAR(s.saleDate), ''), SUM(s.totalSaleAmount)) " +
            "FROM Sale s " +
            "WHERE s.saleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY CONCAT(YEAR(s.saleDate), '') " +
            "ORDER BY CONCAT(YEAR(s.saleDate), '')")
    List<ChartData> findSalesByYearly(LocalDate startDate, LocalDate endDate);

//    @Query(value = "SELECT customer_id, (SUM(total_sale_amount) + SUM(total_exchange_amount)) AS total_amount " +
//            "FROM sale " +
//            "WHERE sale_date >= CURDATE() - INTERVAL 5 DAY " +
//            "GROUP BY customer_id " +
//            "ORDER BY total_amount DESC " +
//            "LIMIT 3", nativeQuery = true)
//    List<CustomerTotalAmount> findTop3CustomersByTotalAmountLast5Days();

    @Query("SELECT new com.billing.model.ChartData(s.customer.name, SUM(s.totalSaleAmount + s.totalExchangeAmount)) " +
            "FROM Sale s " +
            "WHERE s.saleDate >= CURRENT_DATE - 5 " +
            "GROUP BY s.customer.name " +
            "ORDER BY SUM(s.totalSaleAmount + s.totalExchangeAmount) DESC")
    List<ChartData> findTopCustomersByTotalAmountLast5Days(Pageable pageable);
}

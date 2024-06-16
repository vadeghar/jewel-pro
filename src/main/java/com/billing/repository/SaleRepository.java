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

    @Query("SELECT new com.billing.model.ChartData(s.customer.name, SUM(s.totalSaleAmount + s.totalExchangeAmount)) " +
            "FROM Sale s " +
            "WHERE s.saleDate >= CURRENT_DATE - 5 " +
            "GROUP BY s.customer.name " +
            "ORDER BY SUM(s.totalSaleAmount + s.totalExchangeAmount) DESC")
    List<ChartData> findTopCustomersByTotalAmountLast5Days(Pageable pageable);

    @Query(value = "SELECT " +
            "s.sale_date AS startDate, " +
            "s.sale_date AS endDate, " +
            "s.total_net_weight AS totalNetWeight, " +
            "(s.c_gst_amount + s.s_gst_amount) AS totalGst, " +
            "s.total_sale_amount AS totalPurchaseAmount, " +
            "s.paid_amount AS totalPaidAmount, " +
            "s.bal_amount AS totalBalAmount " +
            "FROM sale s " +
            "WHERE (s.sale_date BETWEEN :startDate AND :endDate) " +
//            "  AND ((:metalType IS NULL AND s.metal_type IN ('GOLD', 'SILVER')) OR s.metal_type = :metalType) " +
            "  AND ((:paymentMode IS NULL AND LOWER(s.payment_mode) IN ('cash', 'online')) OR LOWER(s.payment_mode) = LOWER(:paymentMode)) " +
            "  AND (:customerId IS NULL OR s.customer_id = :customerId) " +
            "  AND ((:saleType IS NULL AND LOWER(s.sale_type) IN ('sale', 'return')) OR LOWER(s.sale_type) = LOWER(:saleType)) " +
            "  AND ((:isGstPurchase IS NULL AND s.is_gst_sale IS NOT NULL) OR s.is_gst_sale = :isGstPurchase) " +
            "ORDER BY s.sale_date",
            nativeQuery = true)
    List<Object[]> getAllTransactionsReport(LocalDate startDate, LocalDate endDate,
                                            String paymentMode,
                                            Long customerId, String saleType, String isGstPurchase);

    @Query(value = "SELECT year_data.start_date AS startDate, " +
            "year_data.end_date AS endDate, " +
            "SUM(s.total_net_weight) AS totalNetWeight, " +
            "SUM(s.c_gst_amount + s.s_gst_amount) AS totalGst, " +
            "SUM(s.total_sale_amount) AS totalPurchaseAmount, " +
            "SUM(s.paid_amount) AS totalPaidAmount, " +
            "SUM(s.bal_amount) AS totalBalAmount " +
            "FROM sale s " +
            "JOIN ( " +
            "   SELECT " +
            "       YEAR(sale_date) AS _year, " +
            "       MIN(DATE_SUB(sale_date, INTERVAL (DAY(sale_date) - 1) DAY)) AS start_date, " +
            "       MAX(LAST_DAY(sale_date)) AS end_date " +
            "   FROM sale " +
            "   WHERE sale_date BETWEEN :startDate AND :endDate " +
            "   GROUP BY YEAR(sale_date) " +
            ") AS year_data " +
            "ON YEAR(s.sale_date) = year_data._year " +
            "WHERE s.sale_date BETWEEN :startDate AND :endDate " +
//            "   AND (:metalType IS NULL OR s.metal_type IN ('GOLD', 'SILVER') OR s.metal_type = :metalType) " +
            "   AND (:paymentMode IS NULL OR LOWER(s.payment_mode) IN ('cash', 'online') OR LOWER(s.payment_mode) = LOWER(:paymentMode)) " +
            "   AND (:customerId IS NULL OR s.customer_id = :customerId) " +
            "   AND (:saleType IS NULL OR LOWER(s.sale_type) IN ('sale', 'return') OR LOWER(s.sale_type) = LOWER(:saleType)) " +
            "  AND ((:isGstPurchase IS NULL AND s.is_gst_sale IS NOT NULL) OR s.is_gst_sale = :isGstPurchase) " +
            "GROUP BY year_data.start_date, year_data.end_date " +
            "ORDER BY year_data.start_date", nativeQuery = true)
    List<Object[]> getYearlyReport(LocalDate startDate, LocalDate endDate,
                                   String paymentMode,
                                   Long customerId, String saleType, String isGstPurchase);

    @Query(value = "SELECT month_data.start_date AS startDate, " +
            "month_data.end_date AS endDate, " +
            "SUM(s.total_net_weight) AS totalNetWeight, " +
            "SUM(s.c_gst_amount + s.s_gst_amount) AS totalGst, " +
            "SUM(s.total_sale_amount) AS totalPurchaseAmount, " +
            "SUM(s.paid_amount) AS totalPaidAmount, " +
            "SUM(s.bal_amount) AS totalBalAmount " +
            "FROM sale s " +
            "JOIN ( " +
            "   SELECT " +
            "       YEAR(sale_date) AS _year, " +
            "       MONTH(sale_date) AS _month, " +
            "       MIN(start_date) AS start_date, " +
            "       MAX(end_date) AS end_date " +
            "   FROM ( " +
            "       SELECT " +
            "           sale_date, " +
            "           DATE_SUB(sale_date, INTERVAL (DAY(sale_date) - 1) DAY) AS start_date, " +
            "           LAST_DAY(sale_date) AS end_date " +
            "       FROM sale " +
            "       WHERE sale_date BETWEEN :startDate AND :endDate " +
            "   ) AS subquery " +
            "   GROUP BY YEAR(sale_date), MONTH(sale_date) " +
            ") AS month_data " +
            "ON YEAR(s.sale_date) = month_data._year AND MONTH(s.sale_date) = month_data._month " +
            "WHERE s.sale_date BETWEEN :startDate AND :endDate " +
//            "   AND (:metalType IS NULL OR s.metal_type IN ('GOLD', 'SILVER') OR s.metal_type = :metalType) " +
            "   AND (:paymentMode IS NULL OR LOWER(s.payment_mode) IN ('cash', 'online') OR LOWER(s.payment_mode) = LOWER(:paymentMode)) " +
            "   AND (:customerId IS NULL OR s.customer_id = :customerId) " +
            "   AND (:saleType IS NULL OR LOWER(s.sale_type) IN ('sale', 'return') OR LOWER(s.sale_type) = LOWER(:saleType)) " +
            "  AND ((:isGstPurchase IS NULL AND s.is_gst_sale IS NOT NULL) OR s.is_gst_sale = :isGstPurchase) " +
            "GROUP BY month_data.start_date, month_data.end_date " +
            "ORDER BY month_data.start_date", nativeQuery = true)
    List<Object[]> getMonthlyReport(LocalDate startDate, LocalDate endDate,
                                    String paymentMode,
                                    Long customerId, String saleType, String isGstPurchase);

    @Query(value = "SELECT " +
            "s.sale_date AS saleDate, " +
            "SUM(s.total_net_weight) AS totalNetWeight, " +
            "SUM(s.c_gst_amount + s.s_gst_amount) AS totalGst, " +
            "SUM(s.total_sale_amount) AS totalPurchaseAmount, " +
            "SUM(s.paid_amount) AS totalPaidAmount, " +
            "SUM(s.bal_amount) AS totalBalAmount " +
            "FROM sale s " +
            "WHERE s.sale_date BETWEEN :startDate AND :endDate " +
//            "  AND ((:metalType IS NULL AND s.metal_type IN ('GOLD', 'SILVER')) OR s.metal_type = :metalType) " +
            "  AND ((:paymentMode IS NULL AND LOWER(s.payment_mode) IN ('cash', 'online')) OR LOWER(s.payment_mode) = LOWER(:paymentMode)) " +
            "  AND (:customerId IS NULL OR s.customer_id = :customerId) " +
            "  AND ((:saleType IS NULL AND LOWER(s.sale_type) IN ('sale', 'return')) OR LOWER(s.sale_type) = LOWER(:saleType)) " +
            "  AND ((:isGstPurchase IS NULL AND s.is_gst_sale IS NOT NULL) OR s.is_gst_sale = :isGstPurchase) " +
            "GROUP BY s.sale_date " +
            "ORDER BY s.sale_date",
            nativeQuery = true)
    List<Object[]> getDailyReport(LocalDate startDate, LocalDate endDate,
                                  String paymentMode,
                                  Long customerId, String saleType, String isGstPurchase);

    @Query(value = "SELECT " +
            "week_data.start_date AS startDate, " +
            "week_data.end_date AS endDate, " +
            "SUM(s.total_net_weight) AS totalNetWeight, " +
            "SUM(s.c_gst_amount + s.s_gst_amount) AS totalGst, " +
            "SUM(s.total_sale_amount) AS totalPurchaseAmount, " +
            "SUM(s.paid_amount) AS totalPaidAmount, " +
            "SUM(s.bal_amount) AS totalBalAmount " +
            "FROM sale s " +
            "JOIN ( " +
            "  SELECT " +
            "    YEAR(sale_date) AS year, " +
            "    WEEK(sale_date) AS week, " +
            "    MIN(DATE_SUB(sale_date, INTERVAL (DAYOFWEEK(sale_date) - 1) DAY)) AS start_date, " +
            "    MAX(DATE_ADD(sale_date, INTERVAL (7 - DAYOFWEEK(sale_date)) DAY)) AS end_date " +
            "  FROM sale " +
            "  WHERE sale_date BETWEEN :startDate AND :endDate " +
            "  GROUP BY YEAR(sale_date), WEEK(sale_date) " +
            ") week_data " +
            "ON YEAR(s.sale_date) = week_data.year AND WEEK(s.sale_date) = week_data.week " +
            "WHERE s.sale_date BETWEEN :startDate AND :endDate " +
//            "  AND ((:metalType IS NULL AND s.metal_type IN ('GOLD', 'SILVER')) OR s.metal_type = :metalType) " +
            "  AND ((:paymentMode IS NULL AND LOWER(s.payment_mode) IN ('cash', 'online')) OR LOWER(s.payment_mode) = LOWER(:paymentMode)) " +
            "  AND (:customerId IS NULL OR s.customer_id = :customerId) " +
            "  AND ((:saleType IS NULL AND LOWER(s.sale_type) IN ('sale', 'return')) OR LOWER(s.sale_type) = LOWER(:saleType)) " +
            "  AND ((:isGstPurchase IS NULL AND s.is_gst_sale IS NOT NULL) OR s.is_gst_sale = :isGstPurchase) " +
            "GROUP BY week_data.start_date, week_data.end_date " +
            "ORDER BY week_data.start_date",
            nativeQuery = true)
    List<Object[]> getWeeklyReport(LocalDate startDate, LocalDate endDate,
                                   String paymentMode,
                                   Long customerId, String saleType, String isGstPurchase);
}

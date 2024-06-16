package com.billing.repository;

import com.billing.entity.Purchase;
import com.billing.model.ChartData;
import com.billing.model.WeeklyReport;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>/*, JpaSpecificationExecutor<Purchase>*/ {

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

//    @Query("SELECT new com.billing.model.WeeklyReport(" +
//            "DATE_ADD(:startDate, INTERVAL (WEEK(p.purchaseDate) - 1) WEEK), " + // Start date of the week
//            "DATE_ADD(:startDate, INTERVAL WEEK(p.purchaseDate) WEEK), " + // End date of the week
//            "SUM(p.totalNetWeight), " +
//            "SUM(p.totalCgstAmount + p.totalSgstAmount), " +
//            "SUM(p.totalPurchaseAmount), " +
//            "SUM(p.balAmount), " +
//            "SUM(p.paidAmount) )" +
//            "FROM Purchase p " +
//            "WHERE p.purchaseDate BETWEEN :startDate AND :endDate " +
//            "GROUP BY WEEK(p.purchaseDate) " +
//            "ORDER BY WEEK(p.purchaseDate)")
//    List<WeeklyReport> getWeeklyReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT " +
            "week_data.start_date AS startDate, " +
            "week_data.end_date AS endDate, " +
            "SUM(p.total_net_weight) AS totalNetWeight, " +
            "SUM(p.total_cgst_amount + p.totalsgst_amount) AS totalGst, " +
            "SUM(p.total_purchase_amount) AS totalPurchaseAmount, " +
            "SUM(p.paid_amount) AS totalPaidAmount, " +
            "SUM(p.bal_amount) AS totalBalAmount " +
            "FROM purchase p " +
            "JOIN ( " +
            "  SELECT " +
            "    YEAR(purchase_date) AS year, " +
            "    WEEK(purchase_date) AS week, " +
            "    MIN(DATE_SUB(purchase_date, INTERVAL (DAYOFWEEK(purchase_date) - 1) DAY)) AS start_date, " +
            "    MAX(DATE_ADD(purchase_date, INTERVAL (7 - DAYOFWEEK(purchase_date)) DAY)) AS end_date " +
            "  FROM purchase " +
            "  WHERE purchase_date BETWEEN :startDate AND :endDate " +
            "  GROUP BY YEAR(purchase_date), WEEK(purchase_date) " +
            ") week_data " +
            "ON YEAR(p.purchase_date) = week_data.year AND WEEK(p.purchase_date) = week_data.week " +
            "WHERE p.purchase_date BETWEEN :startDate AND :endDate " +
            "  AND ((:metalType IS NULL AND p.metal_type IN ('GOLD', 'SILVER')) OR p.metal_type = :metalType) " +
            "  AND ((:paymentMode IS NULL AND LOWER(p.payment_mode) IN ('cash', 'online')) OR LOWER(p.payment_mode) = LOWER(:paymentMode)) " +
            "  AND (:supplierId IS NULL OR p.supplier_id = :supplierId) " +
            "  AND ((:purchaseType IS NULL AND LOWER(p.purchase_type) IN ('purchase', 'return')) OR LOWER(p.purchase_type) = LOWER(:purchaseType)) " +
            "  AND ((:isGstPurchase IS NULL AND p.is_gst_purchase IS NOT NULL) OR p.is_gst_purchase = :isGstPurchase) " +
            "GROUP BY week_data.start_date, week_data.end_date " +
            "ORDER BY week_data.start_date",
            nativeQuery = true)
    List<Object[]> getWeeklyReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                   @Param("metalType") String metalType, @Param("paymentMode") String paymentMode,
                                   @Param("supplierId") Long supplierId, @Param("purchaseType") String purchaseType,
                                   @Param("isGstPurchase") String isGstPurchase);

    @Query(value = "SELECT " +
            "p.purchase_date AS startDate, " +
            "p.purchase_date AS endDate, " +
            "p.total_net_weight AS totalNetWeight, " +
            "(p.total_cgst_amount + p.totalsgst_amount) AS totalGst, " +
            "p.total_purchase_amount AS totalPurchaseAmount, " +
            "p.paid_amount AS totalPaidAmount, " +
            "p.bal_amount AS totalBalAmount " +
            "FROM purchase p " +
            "WHERE (p.purchase_date BETWEEN :startDate AND :endDate) " +
            "  AND ((:metalType IS NULL AND p.metal_type IN ('GOLD', 'SILVER')) OR p.metal_type = :metalType) " +
            "  AND ((:paymentMode IS NULL AND LOWER(p.payment_mode) IN ('cash', 'online')) OR LOWER(p.payment_mode) = LOWER(:paymentMode)) " +
            "  AND (:supplierId IS NULL OR p.supplier_id = :supplierId) " +
            "  AND ((:purchaseType IS NULL AND LOWER(p.purchase_type) IN ('purchase', 'return')) OR LOWER(p.purchase_type) = LOWER(:purchaseType)) " +
            "  AND ((:isGstPurchase IS NULL AND p.is_gst_purchase IS NOT NULL) OR p.is_gst_purchase = :isGstPurchase) " +
            "ORDER BY p.purchase_date",
            nativeQuery = true)
    List<Object[]> getAllTransactionsReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                  @Param("metalType") String metalType, @Param("paymentMode") String paymentMode,
                                  @Param("supplierId") Long supplierId, @Param("purchaseType") String purchaseType,
                                            @Param("isGstPurchase") String isGstPurchase);

    @Query(value = "SELECT " +
            "p.purchase_date AS purchaseDate, " +
            "SUM(p.total_net_weight) AS totalNetWeight, " +
            "SUM(p.total_cgst_amount + p.totalsgst_amount) AS totalGst, " +
            "SUM(p.total_purchase_amount) AS totalPurchaseAmount, " +
            "SUM(p.paid_amount) AS totalPaidAmount, " +
            "SUM(p.bal_amount) AS totalBalAmount " +
            "FROM purchase p " +
            "WHERE p.purchase_date BETWEEN :startDate AND :endDate " +
            "  AND ((:metalType IS NULL AND p.metal_type IN ('GOLD', 'SILVER')) OR p.metal_type = :metalType) " +
            "  AND ((:paymentMode IS NULL AND LOWER(p.payment_mode) IN ('cash', 'online')) OR LOWER(p.payment_mode) = LOWER(:paymentMode)) " +
            "  AND (:supplierId IS NULL OR p.supplier_id = :supplierId) " +
            "  AND ((:purchaseType IS NULL AND LOWER(p.purchase_type) IN ('purchase', 'return')) OR LOWER(p.purchase_type) = LOWER(:purchaseType)) " +
            "  AND ((:isGstPurchase IS NULL AND p.is_gst_purchase IS NOT NULL) OR p.is_gst_purchase = :isGstPurchase) " +
            "GROUP BY p.purchase_date " +
            "ORDER BY p.purchase_date",
            nativeQuery = true)
    List<Object[]> getDailyReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                  @Param("metalType") String metalType, @Param("paymentMode") String paymentMode,
                                  @Param("supplierId") Long supplierId, @Param("purchaseType") String purchaseType,
                                  @Param("isGstPurchase") String isGstPurchase);

    @Query(value = "SELECT month_data.start_date AS startDate, " +
            "month_data.end_date AS endDate, " +
            "SUM(p.total_net_weight) AS totalNetWeight, " +
            "SUM(p.total_cgst_amount + p.totalsgst_amount) AS totalGst, " +
            "SUM(p.total_purchase_amount) AS totalPurchaseAmount, " +
            "SUM(p.paid_amount) AS totalPaidAmount, " +
            "SUM(p.bal_amount) AS totalBalAmount " +
            "FROM purchase p " +
            "JOIN ( " +
            "   SELECT " +
            "       YEAR(purchase_date) AS _year, " +
            "       MONTH(purchase_date) AS _month, " +
            "       MIN(start_date) AS start_date, " +
            "       MAX(end_date) AS end_date " +
            "   FROM ( " +
            "       SELECT " +
            "           purchase_date, " +
            "           DATE_SUB(purchase_date, INTERVAL (DAY(purchase_date) - 1) DAY) AS start_date, " +
            "           LAST_DAY(purchase_date) AS end_date " +
            "       FROM purchase " +
            "       WHERE purchase_date BETWEEN :startDate AND :endDate " +
            "   ) AS subquery " +
            "   GROUP BY YEAR(purchase_date), MONTH(purchase_date) " +
            ") AS month_data " +
            "ON YEAR(p.purchase_date) = month_data._year AND MONTH(p.purchase_date) = month_data._month " +
            "WHERE p.purchase_date BETWEEN :startDate AND :endDate " +
            "   AND (:metalType IS NULL OR p.metal_type IN ('GOLD', 'SILVER') OR p.metal_type = :metalType) " +
            "   AND (:paymentMode IS NULL OR LOWER(p.payment_mode) IN ('cash', 'online') OR LOWER(p.payment_mode) = LOWER(:paymentMode)) " +
            "   AND (:supplierId IS NULL OR p.supplier_id = :supplierId) " +
            "   AND (:purchaseType IS NULL OR LOWER(p.purchase_type) IN ('purchase', 'return') OR LOWER(p.purchase_type) = LOWER(:purchaseType)) " +
            "  AND ((:isGstPurchase IS NULL AND p.is_gst_purchase IS NOT NULL) OR p.is_gst_purchase = :isGstPurchase) " +
            "GROUP BY month_data.start_date, month_data.end_date " +
            "ORDER BY month_data.start_date", nativeQuery = true)
    List<Object[]> getMonthlyReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                    @Param("metalType") String metalType, @Param("paymentMode") String paymentMode,
                                    @Param("supplierId") Long supplierId, @Param("purchaseType") String purchaseType,
                                    @Param("isGstPurchase") String isGstPurchase);

    @Query(value = "SELECT year_data.start_date AS startDate, " +
            "year_data.end_date AS endDate, " +
            "SUM(p.total_net_weight) AS totalNetWeight, " +
            "SUM(p.total_cgst_amount + p.totalsgst_amount) AS totalGst, " +
            "SUM(p.total_purchase_amount) AS totalPurchaseAmount, " +
            "SUM(p.paid_amount) AS totalPaidAmount, " +
            "SUM(p.bal_amount) AS totalBalAmount " +
            "FROM purchase p " +
            "JOIN ( " +
            "   SELECT " +
            "       YEAR(purchase_date) AS _year, " +
            "       MIN(DATE_SUB(purchase_date, INTERVAL (DAY(purchase_date) - 1) DAY)) AS start_date, " +
            "       MAX(LAST_DAY(purchase_date)) AS end_date " +
            "   FROM purchase " +
            "   WHERE purchase_date BETWEEN :startDate AND :endDate " +
            "   GROUP BY YEAR(purchase_date) " +
            ") AS year_data " +
            "ON YEAR(p.purchase_date) = year_data._year " +
            "WHERE p.purchase_date BETWEEN :startDate AND :endDate " +
            "   AND (:metalType IS NULL OR p.metal_type IN ('GOLD', 'SILVER') OR p.metal_type = :metalType) " +
            "   AND (:paymentMode IS NULL OR LOWER(p.payment_mode) IN ('cash', 'online') OR LOWER(p.payment_mode) = LOWER(:paymentMode)) " +
            "   AND (:supplierId IS NULL OR p.supplier_id = :supplierId) " +
            "   AND (:purchaseType IS NULL OR LOWER(p.purchase_type) IN ('purchase', 'return') OR LOWER(p.purchase_type) = LOWER(:purchaseType)) " +
            "  AND ((:isGstPurchase IS NULL AND p.is_gst_purchase IS NOT NULL) OR p.is_gst_purchase = :isGstPurchase) " +
            "GROUP BY year_data.start_date, year_data.end_date " +
            "ORDER BY year_data.start_date", nativeQuery = true)
    List<Object[]> getYearlyReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                   @Param("metalType") String metalType, @Param("paymentMode") String paymentMode,
                                   @Param("supplierId") Long supplierId, @Param("purchaseType") String purchaseType,
                                   @Param("isGstPurchase") String isGstPurchase);


}

-- MySQL dump 10.13  Distrib 8.0.36, for macos14 (x86_64)
--
-- Host: mysql-2339a621-lakshman-6868.a.aivencloud.com    Database: gld
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'a4dfa5a1-bfad-11ee-8c66-c2d33be6b5a8:1-534';

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKglkhkmh2vyn790ijs6hiqqpi` (`address_id`),
  CONSTRAINT `FKglkhkmh2vyn790ijs6hiqqpi` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estimation`
--

DROP TABLE IF EXISTS `estimation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estimation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `c_gst_percentage` double DEFAULT NULL,
  `c_gst_price` decimal(19,2) DEFAULT NULL,
  `default_mc_enabled` varchar(255) DEFAULT NULL,
  `discount` decimal(19,2) DEFAULT NULL,
  `estimation_date_ts` datetime(6) DEFAULT NULL,
  `estimation_no` varchar(255) DEFAULT NULL,
  `existing_item` varchar(255) DEFAULT NULL,
  `grand_total_after_discount` decimal(19,2) DEFAULT NULL,
  `is_gst_estimation` varchar(255) DEFAULT NULL,
  `item_code` varchar(255) DEFAULT NULL,
  `item_master_id` bigint DEFAULT NULL,
  `item_metal` varchar(255) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `item_weight` decimal(19,2) DEFAULT NULL,
  `mc` decimal(19,2) DEFAULT NULL,
  `net_weight` decimal(19,2) DEFAULT NULL,
  `net_weight_price` decimal(19,2) DEFAULT NULL,
  `pcs` int NOT NULL,
  `rate` decimal(19,2) DEFAULT NULL,
  `s_gst_percentage` double DEFAULT NULL,
  `s_gst_price` decimal(19,2) DEFAULT NULL,
  `stone_name` varchar(255) DEFAULT NULL,
  `stone_price` decimal(19,2) DEFAULT NULL,
  `stone_price_per_ct` decimal(19,2) DEFAULT NULL,
  `stone_weight` decimal(19,2) DEFAULT NULL,
  `stone_wt_in_cts` decimal(19,2) DEFAULT NULL,
  `tag_no` varchar(255) DEFAULT NULL,
  `total_price` decimal(19,2) DEFAULT NULL,
  `total_price_incl_gst` decimal(19,2) DEFAULT NULL,
  `total_price_incl_mc` decimal(19,2) DEFAULT NULL,
  `total_price_incl_stn` decimal(19,2) DEFAULT NULL,
  `total_price_incl_va` decimal(19,2) DEFAULT NULL,
  `va_percentage` decimal(19,2) DEFAULT NULL,
  `va_price` decimal(19,2) DEFAULT NULL,
  `va_weight` decimal(19,2) DEFAULT NULL,
  `weight_incl_va_wt` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exchange_item`
--

DROP TABLE IF EXISTS `exchange_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exchange_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `exchange_value` decimal(19,2) DEFAULT NULL,
  `item_desc` varchar(255) DEFAULT NULL,
  `melt_percentage` decimal(19,2) DEFAULT NULL,
  `net_weight` decimal(19,2) DEFAULT NULL,
  `rate` decimal(19,2) DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `source_id` bigint DEFAULT NULL,
  `wastage_in_gms` decimal(19,2) DEFAULT NULL,
  `weight` decimal(19,2) DEFAULT NULL,
  `sale_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7p8uuw8stlx3ohla03y5ggc37` (`sale_id`),
  CONSTRAINT `FK7p8uuw8stlx3ohla03y5ggc37` FOREIGN KEY (`sale_id`) REFERENCES `sale` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_category`
--

DROP TABLE IF EXISTS `item_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_master`
--

DROP TABLE IF EXISTS `item_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_master` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item_code` varchar(255) DEFAULT NULL,
  `item_huid` varchar(255) DEFAULT NULL,
  `item_metal` varchar(255) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `item_quality` varchar(255) DEFAULT NULL,
  `mc` decimal(19,2) DEFAULT NULL,
  `pcs` int NOT NULL,
  `stone_name` varchar(255) DEFAULT NULL,
  `stone_pcs` int NOT NULL,
  `stone_weight` decimal(19,2) DEFAULT NULL,
  `stone_wt_in_cts` decimal(19,2) DEFAULT NULL,
  `tag_no` varchar(255) DEFAULT NULL,
  `va_percentage` decimal(19,2) DEFAULT NULL,
  `wastage_in_gms` decimal(19,2) DEFAULT NULL,
  `weight` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_type`
--

DROP TABLE IF EXISTS `item_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `metal_rate`
--

DROP TABLE IF EXISTS `metal_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `metal_rate` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item_metal` varchar(255) DEFAULT NULL,
  `last_update_at` datetime(6) DEFAULT NULL,
  `last_updated_by` varchar(255) DEFAULT NULL,
  `rate` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `actual_purity` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `is_gst_purchase` varchar(10) DEFAULT NULL,
  `metal_type` varchar(30) DEFAULT NULL,
  `payment_mode` varchar(10) DEFAULT NULL,
  `purchase_bill_no` varchar(30) DEFAULT NULL,
  `purchase_date` date DEFAULT NULL,
  `purchase_purity` varchar(255) DEFAULT NULL,
  `purchase_rate` decimal(20,2) DEFAULT NULL,
  `purchase_type` varchar(30) DEFAULT NULL,
  `total_cgst_amount` decimal(20,2) DEFAULT NULL,
  `total_gross_weight` decimal(20,3) DEFAULT NULL,
  `total_mc_amount` decimal(20,2) DEFAULT NULL,
  `total_net_weight` decimal(20,3) DEFAULT NULL,
  `total_pcs` int DEFAULT NULL,
  `total_purchase_amount` decimal(20,2) DEFAULT NULL,
  `totalsgst_amount` decimal(20,2) DEFAULT NULL,
  `total_stn_weight` decimal(20,3) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8omm6fki86s9oqk0o9s6w43h5` (`supplier_id`),
  CONSTRAINT `FK8omm6fki86s9oqk0o9s6w43h5` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase_item`
--

DROP TABLE IF EXISTS `purchase_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `actual_purity` decimal(19,2) DEFAULT NULL,
  `c_gst_amount` decimal(19,2) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `item_amount` decimal(20,2) DEFAULT NULL,
  `pcs` int DEFAULT NULL,
  `purchasemc` decimal(20,2) DEFAULT NULL,
  `purchase_purity` decimal(19,2) DEFAULT NULL,
  `purchase_rate` decimal(20,2) DEFAULT NULL,
  `purchase_stn_cost_per_ct` decimal(20,2) DEFAULT NULL,
  `s_gst_amount` decimal(20,2) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `item_type_id` bigint DEFAULT NULL,
  `purchase_id` bigint DEFAULT NULL,
  `stock_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK87ys9ahpf8s05pwphia5teqdq` (`item_type_id`),
  KEY `FK1mncc5yaore1sibgpj3jc4a7u` (`purchase_id`),
  KEY `FKf3gqd14uimf6xspbauyxj4cdr` (`stock_id`),
  CONSTRAINT `FK1mncc5yaore1sibgpj3jc4a7u` FOREIGN KEY (`purchase_id`) REFERENCES `purchase` (`id`),
  CONSTRAINT `FK87ys9ahpf8s05pwphia5teqdq` FOREIGN KEY (`item_type_id`) REFERENCES `item_type` (`id`),
  CONSTRAINT `FKf3gqd14uimf6xspbauyxj4cdr` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sale`
--

DROP TABLE IF EXISTS `sale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sale` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bal_amount` decimal(19,2) DEFAULT NULL,
  `c_gst_amount` decimal(19,2) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `discount` decimal(19,2) DEFAULT NULL,
  `grand_total_sale_amount` decimal(19,2) DEFAULT NULL,
  `invoice_no` varchar(255) DEFAULT NULL,
  `is_gst_sale` varchar(255) DEFAULT NULL,
  `last_updated_ts` datetime(6) DEFAULT NULL,
  `paid_amount` decimal(19,2) DEFAULT NULL,
  `payment_mode` varchar(255) DEFAULT NULL,
  `s_gst_amount` decimal(19,2) DEFAULT NULL,
  `sale_date` date DEFAULT NULL,
  `sale_type` varchar(255) DEFAULT NULL,
  `total_exchange_amount` decimal(19,2) DEFAULT NULL,
  `total_sale_amount` decimal(19,2) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjw88ojfoqquyd9f1obip1ar0g` (`customer_id`),
  CONSTRAINT `FKjw88ojfoqquyd9f1obip1ar0g` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sale_item`
--

DROP TABLE IF EXISTS `sale_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sale_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `huid` varchar(255) DEFAULT NULL,
  `item_total` decimal(19,2) DEFAULT NULL,
  `making_charge` decimal(19,2) DEFAULT NULL,
  `metal_type` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `net_weight` decimal(19,2) DEFAULT NULL,
  `pcs` int NOT NULL,
  `purity` varchar(255) DEFAULT NULL,
  `rate` decimal(19,2) DEFAULT NULL,
  `stn_cost_per_ct` decimal(19,2) DEFAULT NULL,
  `stn_type` varchar(255) DEFAULT NULL,
  `stn_weight` decimal(19,2) DEFAULT NULL,
  `va_weight` decimal(19,2) DEFAULT NULL,
  `weight` decimal(19,2) DEFAULT NULL,
  `purchase_item_id` bigint DEFAULT NULL,
  `sale_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgvmqre6a04p0h0xyhw6mvgfas` (`purchase_item_id`),
  KEY `FKar9qqr4n69xw1shum20oflleo` (`sale_id`),
  CONSTRAINT `FKar9qqr4n69xw1shum20oflleo` FOREIGN KEY (`sale_id`) REFERENCES `sale` (`id`),
  CONSTRAINT `FKgvmqre6a04p0h0xyhw6mvgfas` FOREIGN KEY (`purchase_item_id`) REFERENCES `purchase_item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `code` varchar(30) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `huid` varchar(20) DEFAULT NULL,
  `item_total_weight` decimal(20,3) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `purity` varchar(20) DEFAULT NULL,
  `salemc` decimal(20,2) DEFAULT NULL,
  `stn_cost_per_ct` decimal(20,3) DEFAULT NULL,
  `stn_type` varchar(20) DEFAULT NULL,
  `stn_weight` decimal(20,3) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `va_weight` decimal(20,3) DEFAULT NULL,
  `weight` decimal(20,3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stone_master`
--

DROP TABLE IF EXISTS `stone_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stone_master` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price_per_ct` double DEFAULT NULL,
  `stone_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_order`
--

DROP TABLE IF EXISTS `work_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `advance_paid` decimal(19,2) DEFAULT NULL,
  `balance` decimal(19,2) DEFAULT NULL,
  `delivery_date` datetime(6) DEFAULT NULL,
  `gross_weight` decimal(19,2) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `mc` decimal(19,2) DEFAULT NULL,
  `net_weight` decimal(19,2) DEFAULT NULL,
  `order_date` datetime(6) DEFAULT NULL,
  `order_total` decimal(19,2) DEFAULT NULL,
  `rate` decimal(19,2) DEFAULT NULL,
  `va_or_wastage` decimal(19,2) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `worker_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt0bw3op1441q7wxibmxsk5ekv` (`customer_id`),
  KEY `FKi36l9ysh77e5lxpeqntkymygo` (`worker_id`),
  CONSTRAINT `FKi36l9ysh77e5lxpeqntkymygo` FOREIGN KEY (`worker_id`) REFERENCES `worker` (`id`),
  CONSTRAINT `FKt0bw3op1441q7wxibmxsk5ekv` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `worker`
--

DROP TABLE IF EXISTS `worker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `worker` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `area` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-13 20:55:54

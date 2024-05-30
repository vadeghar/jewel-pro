INSERT INTO `roles` VALUES (1,'ROLE_ADMIN');
INSERT INTO `users` VALUES (1,'vadeghar@gmail.com','Lakshman Vadeghar','$2a$10$/x85wHhmsYjBdGneLXJWguEdGLG2jPcgQ5ryKpOianvUE8zLkBHgm','2222'),(7,'vadeghar@gmail1.com','Lakshman1 Vadeghar','$2a$10$XE75.4GhrWOoHV3YDfDJUuFT.kLws.GfcOVoplksZcsAJLrMt1jhu','1111'),(16,'test@test.com','Lakshman1 Vadeghar2','$2a$10$O1DV4pftx8s6Z9xSMuZIS.icea9yjMHSZ0nK9X7DsbQTsokpA8szy','3333'),(17,NULL,'Lakshman Vadeghar','$2a$10$.wcbr9HnW3UvLdDihGGUr.zBgvNt.Tsg.WewCNyaj3bMXn2O2zhW.','3214');
INSERT INTO `users_roles` VALUES (16,1),(17,1);

ALTER TABLE `gld-retail`.`address`
CHANGE COLUMN `city` `city` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `state` `state` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `street` `street` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `zip_code` `zip_code` VARCHAR(10) NULL DEFAULT NULL ;

ALTER TABLE `gld-retail`.`customer`
CHANGE COLUMN `address` `address` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `email` `email` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `first_name` `first_name` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `last_name` `last_name` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `name` `name` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `phone` `phone` VARCHAR(12) NULL DEFAULT NULL ;

ALTER TABLE `gld-retail`.`estimation`
CHANGE COLUMN `created_by` `created_by` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `estimation_no` `estimation_no` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `is_gst_estimation` `is_gst_estimation` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `status` `status` VARCHAR(50) NULL DEFAULT NULL ;

ALTER TABLE `gld-retail`.`estimation_exchange_item`
CHANGE COLUMN `item_desc` `item_desc` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `source` `source` VARCHAR(50) NULL DEFAULT NULL ;


ALTER TABLE `gld-retail`.`estimation_item`
CHANGE COLUMN `code` `code` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `huid` `huid` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `metal_type` `metal_type` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `name` `name` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `purity` `purity` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `stn_type` `stn_type` VARCHAR(50) NULL DEFAULT NULL ;

ALTER TABLE `gld-retail`.`exchange_item`
CHANGE COLUMN `item_desc` `item_desc` VARCHAR(50) NULL DEFAULT NULL ,
CHANGE COLUMN `source` `source` VARCHAR(50) NULL DEFAULT NULL ;


INSERT INTO `gld-retail`.supplier (`name`, location, phone, `active`, business_Name, business_Gst_No, updated_Date, created_Date, created_By)
VALUES
('Alice Smith', 'Hyderabad', '9887654321', 1, 'TechCorp', '22AAAAA0000A1Z5', CURRENT_DATE, CURRENT_DATE, 1111),
('Bob Johnson', 'Kolkata', '9876543210', 1, 'BizSolutions', '22BBBBB1111B2Z6', CURRENT_DATE, CURRENT_DATE, 1111),
('Charlie Brown', 'Bombai', '9865432109', 1, 'InnoVentures', '22CCCCC2222C3Z7', CURRENT_DATE, CURRENT_DATE, 1111),
('David Williams', 'Machilipatnam', '9854321098', 1, 'WebDynamics', '22DDDDD3333D4Z8', CURRENT_DATE, CURRENT_DATE, 1111),
('Ella Davis', 'Vijyawada', '9843210987', 1, 'MarketMasters', '22EEEEE4444E5Z9', CURRENT_DATE, CURRENT_DATE, 1111),
('Frank Miller', 'Hyderabad', '9832109876', 1, 'CloudSolutions', '22FFFFF5555F6ZA', CURRENT_DATE, CURRENT_DATE, 1111),
('Grace Lee', 'Kolkata', '9821098765', 1, 'DataStreams', '22GGGGG6666G7ZB', CURRENT_DATE, CURRENT_DATE, 1111),
('Henry Wilson', 'Bombai', '9810987654', 1, 'NetSavvy', '22HHHHH7777H8ZC', CURRENT_DATE, CURRENT_DATE, 1111),
('Ivy Moore', 'Machilipatnam', '9809876543', 1, 'CyberTech', '22IIIII8888I9ZD', CURRENT_DATE, CURRENT_DATE, 1111),
('Jack Taylor', 'Vijyawada', '9798765432', 1, 'Techwave', '22JJJJJ9999J0ZE', CURRENT_DATE, CURRENT_DATE, 1111),
('Kate Anderson', 'Hyderabad', '9787654321', 1, 'InfoSys', '22KKKKK0000K1ZF', CURRENT_DATE, CURRENT_DATE, 1111),
('Leo Thomas', 'Kolkata', '9776543210', 1, 'NetEase', '22LLLLL1111L2ZG', CURRENT_DATE, CURRENT_DATE, 1111),
('Mia Harris', 'Bombai', '9765432109', 1, 'FinTech', '22MMMMM2222M3ZH', CURRENT_DATE, CURRENT_DATE, 1111),
('Noah Clark', 'Machilipatnam', '9754321098', 1, 'HealthCorp', '22NNNNN3333N4ZI', CURRENT_DATE, CURRENT_DATE, 1111),
('Olivia Rodriguez', 'Vijyawada', '9743210987', 1, 'EcoGoods', '22OOOOO4444O5ZJ', CURRENT_DATE, CURRENT_DATE, 1111),
('Paul Lewis', 'Hyderabad', '9732109876', 1, 'CleanEnergy', '22PPPPP5555P6ZK', CURRENT_DATE, CURRENT_DATE, 1111),
('Quinn Walker', 'Kolkata', '9721098765', 1, 'SmartHomes', '22QQQQQ6666Q7ZL', CURRENT_DATE, CURRENT_DATE, 1111),
('Riley Hall', 'Bombai', '9710987654', 1, 'GreenLife', '22RRRRR7777R8ZM', CURRENT_DATE, CURRENT_DATE, 1111),
('Sophie Allen', 'Machilipatnam', '9709876543', 1, 'AgriTech', '22SSSSS8888S9ZN', CURRENT_DATE, CURRENT_DATE, 1111),
('Thomas Young', 'Vijyawada', '9698765432', 1, 'EduWorld', '22TTTTT9999T0ZO', CURRENT_DATE, CURRENT_DATE, 1111),
('Uma King', 'Hyderabad', '9687654321', 1, 'TravelPro', '22UUUUU0000U1ZP', CURRENT_DATE, CURRENT_DATE, 1111),
('Vince Scott', 'Kolkata', '9676543210', 1, 'EventMagic', '22VVVVV1111V2ZQ', CURRENT_DATE, CURRENT_DATE, 1111),
('Wendy Green', 'Bombai', '9665432109', 1, 'MediCare', '22WWWWW2222W3ZR', CURRENT_DATE, CURRENT_DATE, 1111),
('Xander Baker', 'Machilipatnam', '9654321098', 1, 'FoodieHeaven', '22XXXXX3333X4ZS', CURRENT_DATE, CURRENT_DATE, 1111),
('Yara Nelson', 'Vijyawada', '9643210987', 1, 'AutoHub', '22YYYYY4444Y5ZT', CURRENT_DATE, CURRENT_DATE, 1111);



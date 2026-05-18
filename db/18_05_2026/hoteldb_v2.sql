-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hoteldb_v2
-- ------------------------------------------------------
-- Server version	9.3.0

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

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `expected_check_in` date NOT NULL,
  `expected_check_out` date NOT NULL,
  `actual_check_in` datetime DEFAULT NULL,
  `actual_check_out` datetime DEFAULT NULL,
  `total_amount` decimal(15,2) DEFAULT '0.00',
  `special_request` text,
  `status` enum('PENDING','CONFIRMED','CHECKED_IN','CHECKED_OUT','CANCELLED') DEFAULT 'PENDING',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `chk_dates` CHECK ((`expected_check_out` > `expected_check_in`))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (1,1,'2026-05-15','2026-05-19','2026-05-15 14:00:00',NULL,4000000.00,'Phòng tầng cao, yên tĩnh','CHECKED_IN','2026-05-18 02:25:10','2026-05-18 02:25:10'),(2,2,'2026-05-20','2026-05-22',NULL,NULL,2000000.00,'Cần thêm nệm phụ','CONFIRMED','2026-05-18 02:25:10','2026-05-18 02:25:10'),(3,1,'2026-05-21','2026-05-24',NULL,NULL,3000000.00,NULL,'CANCELLED','2026-05-18 02:25:10','2026-05-18 02:25:10');
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_room`
--

DROP TABLE IF EXISTS `booking_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int NOT NULL,
  `room_id` int NOT NULL,
  `price_at_booking` decimal(15,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `booking_id` (`booking_id`),
  KEY `room_id` (`room_id`),
  CONSTRAINT `booking_room_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `booking_room_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_room`
--

LOCK TABLES `booking_room` WRITE;
/*!40000 ALTER TABLE `booking_room` DISABLE KEYS */;
INSERT INTO `booking_room` VALUES (1,1,2,1000000.00),(2,1,4,1000000.00),(3,2,1,1000000.00),(4,3,3,1000000.00);
/*!40000 ALTER TABLE `booking_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_service`
--

DROP TABLE IF EXISTS `booking_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_service` (
  `id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int NOT NULL,
  `service_id` int NOT NULL,
  `quantity` int DEFAULT '1',
  `price_at_usage` decimal(15,2) NOT NULL,
  `note` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `booking_id` (`booking_id`),
  KEY `service_id` (`service_id`),
  CONSTRAINT `booking_service_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `booking_service_ibfk_2` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_service`
--

LOCK TABLES `booking_service` WRITE;
/*!40000 ALTER TABLE `booking_service` DISABLE KEYS */;
INSERT INTO `booking_service` VALUES (1,1,1,3,150000.00,'Ăn sáng 3 người ngày 16, 17, 18','2026-05-18 02:25:10'),(2,1,3,1,300000.00,'Đón từ sân bay Tân Sơn Nhất lúc 12h trưa','2026-05-18 02:25:10'),(3,2,6,1,150000.00,'Thuê xe máy tay ga','2026-05-18 02:25:10');
/*!40000 ALTER TABLE `booking_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(254) DEFAULT NULL,
  `phone` varchar(15) NOT NULL,
  `address` text,
  `user_id` int DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'Nguyễn Đinh Nhật Trường','tn696199@gmail.com','0967294349','Hóc Môn, TP. Hồ Chí Minh',NULL,1),(2,'Nguyễn Văn Tuấn','tuan557552@gmail.com','0334903055','Quận 2, TP. Hồ Chí Minh',1,1),(3,'Phạm Hoàng Long','longpham99@gmail.com','0987654321','Hải Châu, Đà Nẵng',4,1),(4,'Lê Minh Thành','thanh.le@yahoo.com','0934567890','Hoàn Kiếm, Hà Nội',5,1),(5,'Vũ Thị Hồng Nhung','nhungvu.pink@gmail.com','0945678901','Ninh Kiều, Cần Thơ',2,1),(6,'Đặng Hoàng Nam','namdang.dh@gmail.com','0961234567','Thành phố Quảng Ngãi',NULL,1),(7,'Bùi Minh Tuấn','tuanbui.dev@gmail.com','0972345678','Quận 7, TP. Hồ Chí Minh',NULL,1),(8,'Đỗ Thùy Dương','duongdo.95@gmail.com','0953456789','Vũng Tàu, Bà Rịa - Vũng Tàu',NULL,1),(9,'Ngô Gia Bảo','baongo.gia@gmail.com','0924567890','Biên Hòa, Đồng Nai',NULL,1),(10,'Hoàng Ngọc Mai','maihoang.ngoc@gmail.com','0909876543','Quận Tân Bình, TP. Hồ Chí Minh',NULL,1),(11,'Lý Văn Thắng','thangly.92@outlook.com','0919765432','Nha Trang, Khánh Hòa',NULL,1),(12,'Đinh Thị Thu Thảo','thaodinh.thuthu@gmail.com','0989123456','Đà Lạt, Lâm Đồng',NULL,1),(13,'Tạ Minh Triết','trietta.minh@gmail.com','0939234567','Thủ Dầu Một, Bình Dương',NULL,1),(14,'Phan Thanh Bình','binhphan.thanh@gmail.com','0949345678','Vinh, Nghệ An',NULL,1),(15,'Trịnh Công Sơn','sontrinh.music@gmail.com','0969456789','Huế, Thừa Thiên Huế',NULL,1),(16,'Vương Gia Hân','hanvuong.gia@gmail.com','0979567890','Quận 1, TP. Hồ Chí Minh',NULL,1),(17,'Mai Đức Chung','chungmai.duc@gmail.com','0988678901','Hạ Long, Quảng Ninh',NULL,1),(18,'Cao Minh Khang','khangcao.minh@gmail.com','0911234567','Buôn Ma Thuột, Đắk Lắk',NULL,1),(19,'Đoàn Văn Hậu','haudoan.van@gmail.com','0933456789','Thái Bình',NULL,1),(20,'Nguyễn Thị Kim Oanh','oanhnguyen.kim@gmail.com','0944567890','Phan Thiết, Bình Thuận',NULL,1);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int NOT NULL,
  `staff_id` int DEFAULT NULL,
  `amount` decimal(15,2) NOT NULL,
  `payment_method` enum('MOMO','VNPAY','CASH','BANK_TRANSFER') NOT NULL,
  `payment_context` enum('PAYMENT','REFUND') NOT NULL,
  `status` enum('PENDING','COMPLETED','FAILED') DEFAULT 'PENDING',
  `transaction_code` varchar(255) DEFAULT NULL,
  `note` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `transaction_code` (`transaction_code`),
  KEY `booking_id` (`booking_id`),
  KEY `staff_id` (`staff_id`),
  CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `payment_ibfk_2` FOREIGN KEY (`staff_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,1,2,2000000.00,'VNPAY','PAYMENT','COMPLETED','TXN20260515001','Khách đã cọc trước 50% qua VNPay','2026-05-18 02:25:10'),(2,2,2,2000000.00,'MOMO','PAYMENT','COMPLETED','MOMO2026051799X','Thanh toán hoàn tất qua Ví MoMo','2026-05-18 02:25:10');
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `room_number` varchar(10) NOT NULL,
  `floor` int DEFAULT NULL,
  `type_id` int NOT NULL,
  `status` enum('VACANT_CLEAN','VACANT_DIRTY','OCCUPIED_CLEAN','OCCUPIED_DIRTY') DEFAULT 'VACANT_CLEAN',
  `availability_status` enum('READY','MAINTENANCE') DEFAULT 'READY',
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `room_number` (`room_number`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `room_type` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'101',1,1,'VACANT_CLEAN','READY',1),(2,'102',1,1,'OCCUPIED_CLEAN','READY',1),(3,'201',2,1,'VACANT_DIRTY','READY',1),(4,'202',2,1,'OCCUPIED_DIRTY','READY',1),(5,'301',3,1,'VACANT_CLEAN','MAINTENANCE',1);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_inventory`
--

DROP TABLE IF EXISTS `room_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_inventory` (
  `id` int NOT NULL AUTO_INCREMENT,
  `room_type_id` int NOT NULL,
  `inventory_date` date NOT NULL,
  `total_rooms` int NOT NULL,
  `available_rooms` int NOT NULL,
  `reserved_rooms` int DEFAULT '0',
  `maintenance_rooms` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `room_type_id` (`room_type_id`,`inventory_date`),
  CONSTRAINT `room_inventory_ibfk_1` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_inventory`
--

LOCK TABLES `room_inventory` WRITE;
/*!40000 ALTER TABLE `room_inventory` DISABLE KEYS */;
INSERT INTO `room_inventory` VALUES (1,1,'2026-05-17',10,2,8,0);
/*!40000 ALTER TABLE `room_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_type`
--

DROP TABLE IF EXISTS `room_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `base_price` decimal(15,2) NOT NULL,
  `capacity` int DEFAULT '2',
  `description` text,
  `image` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  CONSTRAINT `room_type_chk_1` CHECK ((`base_price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_type`
--

LOCK TABLES `room_type` WRITE;
/*!40000 ALTER TABLE `room_type` DISABLE KEYS */;
INSERT INTO `room_type` VALUES (1,'VIP',1000000.00,3,'HIện đại tiệng nghi',NULL,1);
/*!40000 ALTER TABLE `room_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES (1,'Ăn sáng buffet tại nhà hàng',150000.00,1),(2,'Dịch vụ Spa & Massage toàn thân (60 phút)',450000.00,1),(3,'Đưa/Đón sân bay (Xe 4 chỗ)',300000.00,1),(4,'Đưa/Đón sân bay (Xe 7 chỗ VIP)',500000.00,1),(5,'Giặt ủi cấp tốc (Tính theo kg)',500000.00,1),(6,'Thuê xe máy tự lái (24 giờ)',150000.00,1),(7,'Set menu ăn tối lãng mạn tại hồ bơi',800000.00,1),(8,'Trang trí phòng tân hôn / kỷ niệm',350000.00,1);
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `role` enum('ROLE_ADMIN','ROLE_STAFF','ROLE_HOUSEKEEPING','ROLE_CUSTOMER') DEFAULT 'ROLE_CUSTOMER',
  `avatar` varchar(255) DEFAULT 'https://res.cloudinary.com/dt1pa28g2/image/upload/v1766659167/dhtavt_r2rxdm.jpg',
  `active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','$2a$12$wV/sS6FXhLkHizb9eE53XOd78yFywL8hwO.LhuMPylOFn69RZ/NdK','tuan557552@gmail.com','0334903055','ROLE_ADMIN','https://res.cloudinary.com/dt1pa28g2/image/upload/v1766659167/dhtavt_r2rxdm.jpg',1,'2026-05-16 23:13:48'),(2,'staff_le','$2a$12$UBLh2VKTDE412Hv6GMu3cesB2Q7y6/WlnfZc0D70MipiIx/vJASJ.','staff.le@gmail.com','0911111111','ROLE_STAFF','https://res.cloudinary.com/dt1pa28g2/image/upload/v1766659167/dhtavt_r2rxdm.jpg',1,'2026-05-18 07:06:25'),(3,'maid_hoa','$2a$12$UBLh2VKTDE412Hv6GMu3cesB2Q7y6/WlnfZc0D70MipiIx/vJASJ.','maid.hoa@gmail.com','0922222222','ROLE_HOUSEKEEPING','https://res.cloudinary.com/dt1pa28g2/image/upload/v1766659167/dhtavt_r2rxdm.jpg',1,'2026-05-18 07:06:25'),(4,'cus_long','$2a$12$UBLh2VKTDE412Hv6GMu3cesB2Q7y6/WlnfZc0D70MipiIx/vJASJ.','longpham99@gmail.com','0987654321','ROLE_CUSTOMER','https://res.cloudinary.com/dt1pa28g2/image/upload/v1766659167/dhtavt_r2rxdm.jpg',1,'2026-05-18 07:06:25'),(5,'cus_thanh','$2a$12$UBLh2VKTDE412Hv6GMu3cesB2Q7y6/WlnfZc0D70MipiIx/vJASJ.','thanh.le@yahoo.com','0934567890','ROLE_CUSTOMER','https://res.cloudinary.com/dt1pa28g2/image/upload/v1766659167/dhtavt_r2rxdm.jpg',1,'2026-05-18 07:06:25');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-18 18:37:42

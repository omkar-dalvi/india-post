-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: indiapost
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Table structure for table `articles`
--

DROP TABLE IF EXISTS `articles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articles` (
  `bagNumber` varchar(50) NOT NULL,
  `articleNumber` varchar(50) NOT NULL,
  `reason` varchar(50) DEFAULT NULL,
  `weight` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`bagNumber`,`articleNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articles`
--

LOCK TABLES `articles` WRITE;
/*!40000 ALTER TABLE `articles` DISABLE KEYS */;
INSERT INTO `articles` VALUES ('12345741','784564ASDFA','01    Reason','78'),('4159753','789456','25    Reason four','1.2kg'),('741258','789654321','01    Reason','4564'),('741258a','789654321','01    Reason','4564'),('78945','189700','01    Reason','2.8KG'),('7894561','74128963','01    - Reason','7.8kg'),('7894561','77894351','01    - Reason','7.8kg'),('78945IN','785610BN','01    Reason','0.8'),('adfasdf','sadfsfsd','01    Reason','asdfsadf'),('adfsdaf','sdfsdfas','01    Reason','asdfdf'),('adsf','asdfsadf','01    Reason','asdfasdf'),('adsfasdf','sadfsdfsaf','01    Reason','asdfsadfd'),('afasdf','asdfsadf','01    Reason','asfasdf'),('ASDF','ADSF','01    Reason','ADSF'),('asdf','adsfasdf','01    - Reason','saf'),('ASDF','ASDF','01    Reason','ADSF'),('asdf','asdfasd','01    - Reason','adsf'),('asdf','asdfasdasdf','01    - Reason','adsf'),('asdf','asdfasdf','01    Reason','asdf'),('asdf','asdfasf','01    - Reason','asdf'),('asdf','asdfq','01    Reason','adsf'),('asdf','sadf','01    Reason','asdf'),('asdfa','sfsafsadfa','01    Reason','asdfsaf'),('ASDFASDF','ASDF','01    Reason','ASDF'),('ASDFASDF','ASDFSAFAS','01    Reason','ASDFSAF'),('asdfasf','asdfasfsf','01    Reason','asdfasfasf'),('asdfasfasdfsadf','asdfasfsfasdfsaf','01    Reason','asdfasfasf'),('asdfdsf','adsfasdf','12    - Reason five','saf'),('asdfsad','fasdfsadfsdf','01    Reason','asdfsadfsadf'),('asdfsadf','asdfasf','01    Reason','asdfsadf'),('asdfsadf','asdfsdaf','01    Reason','adfsadf'),('asdfsadf','sadfsadfsad','01    Reason','asdfasdf'),('asdfsadf','sadfsafsfsaf','01    Reason','asfsafsdfsaf'),('asdfsadf','sfsafasfs','01    Reason','asdfasf'),('asdfsaf','sadfsaf','01    Reason','asdfasdf'),('asdfsaf','sadfsafsaf','01    Reason','asdfsadf'),('asdfsdaf','sadfasdf','01    Reason','asdfadsf'),('asfasdf','sdfsadfas','01    - Reason','asdfasdf'),('asfsaf','sdfasdfsfas','01    Reason','asdfasdf'),('qwergga','qwerasdfasf','01    Reason','arwerwer'),('sadfasdf','adsfsafasd','01    Reason','asdfasdf'),('sadfasdf','asdfasfasf','01    Reason','asfsadfsa'),('sadfsadf','sdfsfasfas','01    Reason','asdfasdfasf'),('sdfsdf','asdfsdf','01    Reason','asdfsad');
/*!40000 ALTER TABLE `articles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-24 20:56:04

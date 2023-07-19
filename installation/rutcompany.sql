-- MySQL dump 10.13  Distrib 5.5.34, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: corporation_develop
-- ------------------------------------------------------
-- Server version	5.5.34-0ubuntu0.13.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Account`
--

DROP TABLE IF EXISTS `Account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `accountClass` int(11) DEFAULT NULL,
  `accountCode` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `readOnly` bit(1) NOT NULL,
  `book_id` bigint(20) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_c7icliqkhcdwkh2op185erhgg` (`book_id`),
  KEY `FK_1e1n9rkotpjcw1xoeamklak6k` (`parent_id`),
  CONSTRAINT `FK_1e1n9rkotpjcw1xoeamklak6k` FOREIGN KEY (`parent_id`) REFERENCES `Account` (`id`),
  CONSTRAINT `FK_c7icliqkhcdwkh2op185erhgg` FOREIGN KEY (`book_id`) REFERENCES `Book` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=648 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Book`
--

DROP TABLE IF EXISTS `Book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `currency` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=233 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BookDataFile`
--

DROP TABLE IF EXISTS `BookDataFile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BookDataFile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `encoding` varchar(255) DEFAULT NULL,
  `fileData` longblob,
  `script` longtext,
  `uploaded` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Book_BookDataFile`
--

DROP TABLE IF EXISTS `Book_BookDataFile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Book_BookDataFile` (
  `Book_id` bigint(20) NOT NULL,
  `bookDataFiles_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_q7jby8wibyrffrohj4hask8ao` (`bookDataFiles_id`),
  KEY `FK_j2yk78hpidpuu9vsn5eq5r4nc` (`Book_id`),
  CONSTRAINT `FK_j2yk78hpidpuu9vsn5eq5r4nc` FOREIGN KEY (`Book_id`) REFERENCES `Book` (`id`),
  CONSTRAINT `FK_q7jby8wibyrffrohj4hask8ao` FOREIGN KEY (`bookDataFiles_id`) REFERENCES `BookDataFile` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Book_ImportTransactionHandler`
--

DROP TABLE IF EXISTS `Book_ImportTransactionHandler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Book_ImportTransactionHandler` (
  `Book_id` bigint(20) NOT NULL,
  `importTransactionHandlers_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_27ab5p536kqroi4xin28k0q18` (`importTransactionHandlers_id`),
  KEY `FK_g5n01oxmd0apvq8677nb143gt` (`Book_id`),
  CONSTRAINT `FK_g5n01oxmd0apvq8677nb143gt` FOREIGN KEY (`Book_id`) REFERENCES `Book` (`id`),
  CONSTRAINT `FK_27ab5p536kqroi4xin28k0q18` FOREIGN KEY (`importTransactionHandlers_id`) REFERENCES `ImportTransactionHandler` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Book_Transaction`
--

DROP TABLE IF EXISTS `Book_Transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Book_Transaction` (
  `Book_id` bigint(20) NOT NULL,
  `transactions_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_4krh0q8q331as1fyim9y48wck` (`transactions_id`),
  KEY `FK_7uc4deoe943y3pxbwuusb5fsn` (`Book_id`),
  CONSTRAINT `FK_7uc4deoe943y3pxbwuusb5fsn` FOREIGN KEY (`Book_id`) REFERENCES `Book` (`id`),
  CONSTRAINT `FK_4krh0q8q331as1fyim9y48wck` FOREIGN KEY (`transactions_id`) REFERENCES `Transaction` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Company`
--

DROP TABLE IF EXISTS `Company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `companyType` int(11) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `stateIdNumber` varchar(255) DEFAULT NULL,
  `book_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_bmguu25tmneqnlu331rce58ww` (`book_id`),
  CONSTRAINT `FK_bmguu25tmneqnlu331rce58ww` FOREIGN KEY (`book_id`) REFERENCES `Book` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=233 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ImportTransactionHandler`
--

DROP TABLE IF EXISTS `ImportTransactionHandler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ImportTransactionHandler` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `script` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Part`
--

DROP TABLE IF EXISTS `Part`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `transaction_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_moree8dy32j4h15x2y8op4o2e` (`account_id`),
  KEY `FK_fux5bw7bg9n1o11tgwtc6apg9` (`transaction_id`),
  CONSTRAINT `FK_fux5bw7bg9n1o11tgwtc6apg9` FOREIGN KEY (`transaction_id`) REFERENCES `Transaction` (`id`),
  CONSTRAINT `FK_moree8dy32j4h15x2y8op4o2e` FOREIGN KEY (`account_id`) REFERENCES `Account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=797 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Transaction`
--

DROP TABLE IF EXISTS `Transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `baseDocument` varchar(255) DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `dateNoticed` datetime DEFAULT NULL,
  `dateOccurred` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `number` bigint(20) DEFAULT NULL,
  `otherParty` varchar(255) DEFAULT NULL,
  `correctedTransaction_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_tp3br251ne0c2axqd9x3cybpr` (`correctedTransaction_id`),
  CONSTRAINT `FK_tp3br251ne0c2axqd9x3cybpr` FOREIGN KEY (`correctedTransaction_id`) REFERENCES `Transaction` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=399 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-05-11  0:26:08

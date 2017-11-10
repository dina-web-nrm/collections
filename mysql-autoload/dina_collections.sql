-- MySQL dump 10.13  Distrib 5.6.19, for osx10.7 (i386)
--
-- Host: localhost    Database: dina_collections
-- ------------------------------------------------------
-- Server version	5.7.18

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
-- Table structure for table `cataloged_unit`
--

DROP TABLE IF EXISTS `cataloged_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cataloged_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Auto-incrementing surrogate primary key.',
  `version` int(11) NOT NULL DEFAULT '1',
  `catalog_number` varchar(50) NOT NULL COMMENT 'A unique identifier for a unit within a catalog',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cataloged_unit`
--

LOCK TABLES `cataloged_unit` WRITE;
/*!40000 ALTER TABLE `cataloged_unit` DISABLE KEYS */;
/*!40000 ALTER TABLE `cataloged_unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feature_observation`
--

DROP TABLE IF EXISTS `feature_observation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feature_observation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Auto-incrementing surrogate primary key.',
  `version` int(11) NOT NULL DEFAULT '1',
  `applies_to_individual_group_id` bigint(20) DEFAULT NULL COMMENT 'Key to the IndividualGroup to which this FeatureObservation applies',
  `is_of_feature_observation_type_id` bigint(20) NOT NULL COMMENT 'Key to the FeatureObservationType of the FeatureObservation.',
  `feature_observation_text` text COMMENT 'A text describing the observation, in any chosen format.',
  PRIMARY KEY (`id`),
  KEY `feature_observation_individual_group_id_idx` (`applies_to_individual_group_id`),
  KEY `feature_observation_feature_observation_type_id_idx` (`is_of_feature_observation_type_id`),
  CONSTRAINT `feature_observation_feature_observation_type_id` FOREIGN KEY (`is_of_feature_observation_type_id`) REFERENCES `feature_observation_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `feature_observation_individual_group_id` FOREIGN KEY (`applies_to_individual_group_id`) REFERENCES `individual_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feature_observation`
--

LOCK TABLES `feature_observation` WRITE;
/*!40000 ALTER TABLE `feature_observation` DISABLE KEYS */;
/*!40000 ALTER TABLE `feature_observation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feature_observation_type`
--

DROP TABLE IF EXISTS `feature_observation_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feature_observation_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Auto-incrementing surrogate primary key.',
  `version` int(11) NOT NULL DEFAULT '1',
  `feature_observation_type_name` varchar(255) NOT NULL COMMENT 'The name of a defined FeatureObservationType.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feature_observation_type`
--

LOCK TABLES `feature_observation_type` WRITE;
/*!40000 ALTER TABLE `feature_observation_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `feature_observation_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `identification`
--

DROP TABLE IF EXISTS `identification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `identification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Auto-incrementing surrogate primary key.',
  `version` int(11) NOT NULL DEFAULT '1',
  `applies_to_individual_group_id` bigint(20) NOT NULL COMMENT 'Key to the individualGroup to which this observation apply.',
  `identification_text` text COMMENT 'A text describing a single Identification (including e.g. taxon name, identified by, and identification date), in any chosen format.',
  PRIMARY KEY (`id`),
  KEY `identification_individual_group_id_idx` (`applies_to_individual_group_id`),
  CONSTRAINT `identification_individual_group_id` FOREIGN KEY (`applies_to_individual_group_id`) REFERENCES `individual_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `identification`
--

LOCK TABLES `identification` WRITE;
/*!40000 ALTER TABLE `identification` DISABLE KEYS */;
/*!40000 ALTER TABLE `identification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `individual_group`
--

DROP TABLE IF EXISTS `individual_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `individual_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Auto-incrementing surrogate primary key.',
  `version` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `individual_group`
--

LOCK TABLES `individual_group` WRITE;
/*!40000 ALTER TABLE `individual_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `individual_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `occurrence`
--

DROP TABLE IF EXISTS `occurrence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `occurrence` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Auto-incrementing surrogate primary key.',
  `version` int(11) NOT NULL DEFAULT '1',
  `involves_individual_group_id` bigint(20) NOT NULL COMMENT 'Key to IndividualGroup involved in the occurrence.',
  `collectors_text` text COMMENT 'A text describing the collector(s) in any chosen format.',
  `locality_text` text COMMENT 'A text describing where the Occurrence took place, in any chosen format.',
  `occurrence_date_text` text COMMENT 'A text describing the date of the Occurrence, in any chosen format.',
  PRIMARY KEY (`id`),
  KEY `occurrence_individual_group_id_idx` (`involves_individual_group_id`),
  CONSTRAINT `occurrence_individual_group_id` FOREIGN KEY (`involves_individual_group_id`) REFERENCES `individual_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `occurrence`
--

LOCK TABLES `occurrence` WRITE;
/*!40000 ALTER TABLE `occurrence` DISABLE KEYS */;
/*!40000 ALTER TABLE `occurrence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `physical_unit`
--

DROP TABLE IF EXISTS `physical_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `physical_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Auto-incrementing surrogate primary key.',
  `version` int(11) NOT NULL DEFAULT '1',
  `belongs_to_cataloged_unit_id` bigint(20) DEFAULT NULL COMMENT 'Key to the CatalogedUnit to which this physical unit belongs.',
  `is_collected_at_occurrence_id` bigint(20) DEFAULT NULL COMMENT 'Key to the Occurrence at which the material was collected.',
  `represents_individual_group_id` bigint(20) DEFAULT NULL COMMENT 'Key to the IndividualGroup this physical unit represent.',
  `physical_unit_text` text COMMENT 'A text describing the PhysicalUnit, in any chosen format.',
  `normal_storage_location` text COMMENT 'A text describing the normal physical storage location, in any chosen format.',
  PRIMARY KEY (`id`),
  KEY `physical_unit_cataloged_unit_id_idx` (`belongs_to_cataloged_unit_id`),
  KEY `physical_unit_occurrence_id_idx` (`is_collected_at_occurrence_id`),
  KEY `physical_unit_individual_group_id_idx` (`represents_individual_group_id`),
  CONSTRAINT `physical_unit_cataloged_unit_id` FOREIGN KEY (`belongs_to_cataloged_unit_id`) REFERENCES `cataloged_unit` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `physical_unit_individual_group_id` FOREIGN KEY (`represents_individual_group_id`) REFERENCES `individual_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `physical_unit_occurrence_id` FOREIGN KEY (`is_collected_at_occurrence_id`) REFERENCES `occurrence` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `physical_unit`
--

LOCK TABLES `physical_unit` WRITE;
/*!40000 ALTER TABLE `physical_unit` DISABLE KEYS */;
/*!40000 ALTER TABLE `physical_unit` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-06 11:50:10

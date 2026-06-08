-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versione server:              10.9.8-MariaDB - mariadb.org binary distribution
-- S.O. server:                  Win64
-- HeidiSQL Versione:            12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dump della struttura del database vaadinflottatest
DROP DATABASE IF EXISTS `vaadinflottatest`;
CREATE DATABASE IF NOT EXISTS `vaadinflottatest` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `vaadinflottatest`;

-- Dump della struttura di tabella vaadinflottatest.flotta
DROP TABLE IF EXISTS `flotta`;
CREATE TABLE IF NOT EXISTS `flotta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_marca` int(11) NOT NULL,
  `id_modello` int(11) NOT NULL,
  `targa` varchar(10) NOT NULL,
  `data_ultima_prenotazione` date NOT NULL,
  `giorni_di_utlima_prenotazione` int(11) NOT NULL,
  `data_prima_disponibilita` date NOT NULL,
  `data_scadenza_assicurazione` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `marca` (`id_marca`),
  KEY `modello` (`id_modello`),
  CONSTRAINT `marca` FOREIGN KEY (`id_marca`) REFERENCES `marche` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `modello` FOREIGN KEY (`id_modello`) REFERENCES `modelli` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- L’esportazione dei dati non era selezionata.

-- Dump della struttura di tabella vaadinflottatest.marche
DROP TABLE IF EXISTS `marche`;
CREATE TABLE IF NOT EXISTS `marche` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome_marca` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- L’esportazione dei dati non era selezionata.

-- Dump della struttura di tabella vaadinflottatest.modelli
DROP TABLE IF EXISTS `modelli`;
CREATE TABLE IF NOT EXISTS `modelli` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_marca` int(11) NOT NULL DEFAULT 0,
  `nome` varchar(100) NOT NULL DEFAULT '0',
  `id_tipologia_veicolo` int(11) NOT NULL DEFAULT 0,
  `numero_cilindri` int(11) NOT NULL DEFAULT 0,
  `cilindrata` int(11) NOT NULL DEFAULT 0,
  `alimentazione` varchar(50) NOT NULL DEFAULT '0',
  `numero_posti_passeggero` int(11) NOT NULL DEFAULT 0,
  `costo_noleggio_giornaliero_in_euro` float(6,2) NOT NULL DEFAULT 0.00,
  `quantita` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `id_marca` (`id_marca`),
  KEY `id_tipologia` (`id_tipologia_veicolo`),
  CONSTRAINT `id_marca` FOREIGN KEY (`id_marca`) REFERENCES `marche` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_tipologia` FOREIGN KEY (`id_tipologia_veicolo`) REFERENCES `tipologieveicoli` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- L’esportazione dei dati non era selezionata.

-- Dump della struttura di tabella vaadinflottatest.tipologieveicoli
DROP TABLE IF EXISTS `tipologieveicoli`;
CREATE TABLE IF NOT EXISTS `tipologieveicoli` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- L’esportazione dei dati non era selezionata.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

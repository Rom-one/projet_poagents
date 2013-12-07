# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Hôte: 127.0.0.1 (MySQL 5.5.25)
# Base de données: db_poagent
# Temps de génération: 2013-12-07 15:15:00 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Affichage de la table categorie
# ------------------------------------------------------------

DROP TABLE IF EXISTS `categorie`;

CREATE TABLE `categorie` (
  `idCategorie` int(4) NOT NULL AUTO_INCREMENT,
  `typeCategorie` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`idCategorie`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



# Affichage de la table objet
# ------------------------------------------------------------

DROP TABLE IF EXISTS `objet`;

CREATE TABLE `objet` (
  `refObjet` int(4) NOT NULL AUTO_INCREMENT,
  `nomObjet` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `motCle` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `prixVente` int(8) NOT NULL,
  `idCategorie` int(4) DEFAULT NULL,
  PRIMARY KEY (`refObjet`),
  KEY `FOREIGN KEY CATEGORIE` (`idCategorie`) COMMENT 'id de la categorie',
  CONSTRAINT `fk_idCategorie` FOREIGN KEY (`idCategorie`) REFERENCES `categorie` (`idCategorie`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Affichage de la table stock
# ------------------------------------------------------------

DROP TABLE IF EXISTS `stock`;

CREATE TABLE `stock` (
  `idStock` int(4) NOT NULL AUTO_INCREMENT,
  `refObjet` int(4) NOT NULL,
  `dateStockage` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `datePaiement` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `nbVendu` int(4) NOT NULL DEFAULT '0',
  `quantite` int(4) NOT NULL,
  `prixAchat` decimal(6,0) NOT NULL,
  PRIMARY KEY (`idStock`),
  KEY `FOREIGN KEY OBJET` (`refObjet`) COMMENT 'Reference d''un objet',
  CONSTRAINT `fk_refObjet` FOREIGN KEY (`refObjet`) REFERENCES `objet` (`refObjet`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



# Affichage de la table vendeur
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vendeur`;

CREATE TABLE `vendeur` (
  `idVendeur` int(4) NOT NULL AUTO_INCREMENT,
  `tresorerie` decimal(6,0) NOT NULL DEFAULT '0',
  `stockTotal` int(5) NOT NULL,
  PRIMARY KEY (`idVendeur`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Affichage de la table vente
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vente`;

CREATE TABLE `vente` (
  `idVente` int(4) NOT NULL AUTO_INCREMENT,
  `refObjet` int(4) NOT NULL,
  `idStock` int(4) NOT NULL,
  `dateVente` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `prixVente` decimal(6,0) DEFAULT '0',
  `acheteur` varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'anonymous',
  PRIMARY KEY (`idVente`),
  UNIQUE KEY `FOREIGN KEY STOCK` (`idStock`) COMMENT 'Reference dans les stock',
  KEY `FOREIGN KEY OBJET` (`refObjet`) COMMENT 'Reference d''un objet',
  CONSTRAINT `fk_vente_idStock` FOREIGN KEY (`idStock`) REFERENCES `stock` (`idStock`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_vente_refObjet` FOREIGN KEY (`refObjet`) REFERENCES `objet` (`refObjet`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

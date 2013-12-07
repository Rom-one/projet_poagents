-- phpMyAdmin SQL Dump
-- version 4.0.3
-- http://www.phpmyadmin.net
--
-- Client: 127.0.0.1
-- Généré le: Sam 07 Décembre 2013 à 17:46
-- Version du serveur: 5.6.11-log
-- Version de PHP: 5.5.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `db_poagent`
--
CREATE DATABASE IF NOT EXISTS `db_poagent` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `db_poagent`;

-- --------------------------------------------------------

--
-- Structure de la table `categorie`
--

CREATE TABLE IF NOT EXISTS `categorie` (
  `idCategorie` int(4) NOT NULL AUTO_INCREMENT,
  `typeCategorie` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`idCategorie`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Contenu de la table `categorie`
--

INSERT INTO `categorie` (`idCategorie`, `typeCategorie`) VALUES
(1, 'dvd'),
(2, 'jeux'),
(3, 'camescope'),
(4, 'tv'),
(5, 'cd');

--
-- Structure de la table `objet`
--

CREATE TABLE IF NOT EXISTS `objet` (
  `refObjet` int(4) NOT NULL AUTO_INCREMENT,
  `nomObjet` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `motCle` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `prixVente` int(8) NOT NULL,
  `idCategorie` int(4) DEFAULT NULL,
  PRIMARY KEY (`refObjet`),
  KEY `FOREIGN KEY CATEGORIE` (`idCategorie`) COMMENT 'id de la categorie'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Contenu de la table `objet`
--

INSERT INTO `objet` (`refObjet`, `nomObjet`, `motCle`, `prixVente`, `idCategorie`) VALUES
(1, 'Django Unchained', 'j. foxx,l. di caprio,sl. jackson,western spaghetti', '15', 1),
(2, 'Gravity ', '3D,thriller,drame,science-fiction,g. clooney,s. bullock', '15', 1),
(3, 'Iron Man 3', 'marvel,action,super-héros,r. downey jr,g. paltrow,d. cheadle', '15', 1),
(4, 'Moi, Moche et Méchant', 'animation,famille', '15', 1),
(5, 'Insaisissable', 'thriller policier,e. ricourt,l. letterier', '15', 1),
(6, 'World War Z', 'horreur,post-apocalyptique,science-fiction,b. pitt,m. enos', '15', 1),
(7, 'Man of Steel', 'action,super-héros,h. cavill,a. adams,k. costner', '15', 1),
(8, 'Very Bad Trip 3', 'comedie,b. cooper,e. helms,z. galifianakis', '15', 1),
(9, 'Les Croods', 'animation,famille,n. cage,r. reynolds', '15', 1),
(10, 'Jappeloup', 'drame,g. canet,m. hands,d. auteuil', '15', 1),
(12, 'Grand Theft Auto V', 'gta-like,action,-18ans,rockstar game', '15', 2),
(13, 'Watch Dogs', 'aventure,action,infiltration,-18ans,ubisoft', '15', 2),
(14, 'Crysis 3', 'fps,electronic arts', '15', 2),
(15, 'The Last Of Us', 'action,survival-horror,sony,naughty dog', '15', 2),
(16, 'Metro : Last Night ', 'fps,4A games,koch media,-18ans', '15', 2),
(17, 'Dead Space 3', 'survival horror,electronic arts,-18ans\r\n', '15', 2),
(18, 'Diablo III : Reaper of Souls', 'jeux de rôle,action,blizzard,activision', '15', 2),
(19, 'BattleField 4', 'fps,electronic arts,dice,-18ans', '15', 2),
(20, 'FIFA 14', 'sport,football,ea sports,electronic arts', '15', 2),
(21, 'Dark Soul II', 'jeu de rôle,action,-16ans,namco bandai,from software', '15', 2),
(22, 'TOSHIBA Camileo X150', 'black,flash,toshiba\r\n', '15', 3),
(23, 'JVC GZ-E10', 'argent,flash,mémoire+,jvc,photo', '15', 3),
(24, 'SAMSUNG F900', 'argent,8go,samsung', '15', 3),
(25, 'TOSHIBA Z100', '3d,hd,blanc,toshiba,photo', '15', 3),
(26, 'PANASONIC HC-V110', 'noir,hd,mémoire+,panasonic,photo', '15', 3),
(27, 'SONY PJ220', 'video-projecteur,8go,hd,sony', '15', 3),
(28, 'SONY FDR-AX1', '4k,noir,sony,photo', '15', 3),
(29, 'SAMSUNG QF30', 'stabilisateur,blanc,hd,samsung,photo', '15', 3),
(30, 'PANASONIC HC-V210', 'grand angle,noir,panasonic', '15', 3),
(31, 'CANON HFR36', 'wifi,canon,grand angle', '15', 3),
(32, 'TOSHIBA 65L9363DF', '4k,toshiba,smart,800hz,led,169cm,noir', '15', 4),
(33, 'SAMSUNG UE46F5000', '100hz,cmr,116cm,led', '15', 4),
(34, 'PHILLIPS 46PFL4208H', 'smart,200hz,pmr,117cm,edge led,wifi,hdtv', '15', 4),
(35, 'LG 47LA641', '3d,smart,200hz,mci,119cm,wifi,led', '15', 4),
(36, 'LISTO 275DLEDUSB-595', '70cm,direct led,noir', '15', 4),
(37, 'TOSHIBA 24W1334G', 'blanc,61cm,edgs led', '15', 4),
(38, 'PANASONIC TX-L32E6E', 'argent,smart,100hz,bls,hdtv,1080p,wifi', '15', 4),
(39, 'MUSE M-115', 'portable,18cm,lcd', '15', 4),
(40, 'SAMSUNG PS43F4500', 'plasma,109cm,noir', '15', 4),
(41, 'LG 55EA980V', 'oled,smart,139cm,3d,incurvé', '15', 4),
(42, 'Songz for the Philipines', 'various artiste,pop', '15', 5),
(43, 'Long Courrier', 'bb brunes,rock', '15', 5),
(44, 'True', 'avicii,house,dance', '15', 5),
(45, 'Midnight Memories', 'one direction,pop', '15', 5),
(46, 'NRJ Music Awards 15th', 'pop,various artists', '15', 5),
(47, 'Prism', 'katty perry,pop', '15', 5),
(48, 'Sarbacane', 'francis cabrel,variété française', '15', 5),
(49, 'Racine Carré', 'stromae,variété française', '15', 5),
(50, 'The Marshall Mathers LP2', 'eminem,hip-hop', '15', 5),
(51, 'If You Wait', 'alternative,london grammar', '15', 5);

-- --------------------------------------------------------

--
-- Structure de la table `stock`
--

CREATE TABLE IF NOT EXISTS `stock` (
  `idStock` int(4) NOT NULL AUTO_INCREMENT,
  `refObjet` int(4) NOT NULL,
  `dateStockage` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `datePaiement` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `quantite` int(4) NOT NULL,
  `prixAchat` decimal(6,0) NOT NULL,
  PRIMARY KEY (`idStock`),
  KEY `FOREIGN KEY OBJET` (`refObjet`) COMMENT 'Reference d''un objet'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `vendeur`
--

CREATE TABLE IF NOT EXISTS `vendeur` (
  `idVendeur` int(4) NOT NULL AUTO_INCREMENT,
  `tresorerie` decimal(6,0) NOT NULL DEFAULT '0',
  `stockTotal` int(5) NOT NULL,
  PRIMARY KEY (`idVendeur`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `vente`
--

CREATE TABLE IF NOT EXISTS `vente` (
  `idVente` int(4) NOT NULL AUTO_INCREMENT,
  `refObjet` int(4) NOT NULL,
  `dateVente` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `prixVente` decimal(6,0) DEFAULT '0',
  `acheteur` varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'anonymous',
  PRIMARY KEY (`idVente`),
  KEY `FOREIGN KEY OBJET` (`refObjet`) COMMENT 'Reference d''un objet'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `objet`
--
ALTER TABLE `objet`
  ADD CONSTRAINT `fk_idCategorie` FOREIGN KEY (`idCategorie`) REFERENCES `categorie` (`idCategorie`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `stock`
--
ALTER TABLE `stock`
  ADD CONSTRAINT `fk_refObjet` FOREIGN KEY (`refObjet`) REFERENCES `objet` (`refObjet`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `vente`
--
ALTER TABLE `vente`
  ADD CONSTRAINT `fk_vente_refObjet` FOREIGN KEY (`refObjet`) REFERENCES `objet` (`refObjet`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

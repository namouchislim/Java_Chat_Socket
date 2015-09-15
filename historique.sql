-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Serveur: localhost
-- Généré le : Sam 01 Février 2014 à 18:01
-- Version du serveur: 5.5.8
-- Version de PHP: 5.3.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `chat`
--

-- --------------------------------------------------------

--
-- Structure de la table `historique`
--

CREATE TABLE IF NOT EXISTS `historique` (
  `datemsg` varchar(30) NOT NULL,
  `msg` varchar(512) NOT NULL,
  `port` int(8) NOT NULL,
  `heuremsg` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `historique`
--

INSERT INTO `historique` (`datemsg`, `msg`, `port`, `heuremsg`) VALUES
('2014/02/01', '  Serveur :   sdsdsd', 20000, '18:54:49'),
('2014/02/01', '  Client   :   dffdfd', 51226, '18:54:53'),
('2014/02/01', '  Serveur :   dffdf', 20000, '18:55:1'),
('2014/02/01', '  Serveur :   xdffdfd', 20000, '18:55:13'),
('2014/02/01', '  Client   :   slt', 51236, '18:55:38'),
('2014/02/01', '  Client   :   je suis namouchi slim', 51236, '18:55:46'),
('2014/02/01', '  Serveur :   slt Slim :)', 20000, '18:55:56');

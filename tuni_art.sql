-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : lun. 12 fév. 2024 à 20:14
-- Version du serveur : 10.4.28-MariaDB
-- Version de PHP : 8.1.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `tuni'art`
--

-- --------------------------------------------------------

--
-- Structure de la table `art`
--

CREATE TABLE `art` (
  `art_ref` int(11) NOT NULL,
  `art_title` varchar(512) NOT NULL,
  `art_price` float NOT NULL,
  `type` varchar(512) NOT NULL,
  `creation` date NOT NULL,
  `description` varchar(512) NOT NULL,
  `style` varchar(512) NOT NULL,
  `artist_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `artist`
--

CREATE TABLE `artist` (
  `aid` int(11) NOT NULL,
  `fname` varchar(128) NOT NULL,
  `lname` varchar(128) NOT NULL,
  `email` varchar(512) NOT NULL,
  `gender` tinyint(1) NOT NULL,
  `phone_nb` int(11) NOT NULL,
  `profile_pic` varchar(512) DEFAULT NULL,
  `birth_date` date NOT NULL,
  `password` varchar(512) NOT NULL,
  `biography` varchar(512) NOT NULL,
  `portfolio` varchar(512) NOT NULL,
  `verification_code` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `cart`
--

CREATE TABLE `cart` (
  `cart_ref` int(11) NOT NULL,
  `totalPrice` float NOT NULL,
  `uid` int(11) NOT NULL,
  `state` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `cart_art`
--

CREATE TABLE `cart_art` (
  `cart_ref` int(11) NOT NULL,
  `art_ref` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `delivery`
--

CREATE TABLE `delivery` (
  `delivery_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `estimated_date` date NOT NULL,
  `delivery_fees` float NOT NULL,
  `destination` varchar(512) NOT NULL,
  `state` tinyint(1) NOT NULL,
  `agency_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `delivery_agency`
--

CREATE TABLE `delivery_agency` (
  `agency_id` int(11) NOT NULL,
  `agency_name` varchar(512) NOT NULL,
  `agency_address` varchar(512) NOT NULL,
  `nb_deliveries` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `order`
--

CREATE TABLE `order` (
  `order_id` int(11) NOT NULL,
  `order_date` date NOT NULL,
  `paymentMethod` varchar(512) NOT NULL,
  `receiptionMethod` varchar(512) NOT NULL,
  `cart_ref` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `review`
--

CREATE TABLE `review` (
  `review_id` int(11) NOT NULL,
  `art_ref` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `date_published` date NOT NULL,
  `rating` int(11) NOT NULL,
  `comment` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `uid` int(11) NOT NULL,
  `fname` varchar(128) NOT NULL,
  `lname` varchar(128) NOT NULL,
  `email` varchar(512) NOT NULL,
  `gender` tinyint(1) NOT NULL,
  `phone_nb` int(11) NOT NULL,
  `profile_pic` varchar(512) DEFAULT NULL,
  `birth_date` date NOT NULL,
  `password` varchar(512) NOT NULL,
  `verification_code` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`uid`, `fname`, `lname`, `email`, `gender`, `phone_nb`, `profile_pic`, `birth_date`, `password`, `verification_code`) VALUES
(2, 'Arij', 'Mahouechi', 'arij.mahouechi@esprit.tn', 1, 22334455, NULL, '2001-10-12', '12131415', NULL);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `art`
--
ALTER TABLE `art`
  ADD PRIMARY KEY (`art_ref`),
  ADD KEY `fk_artist_art` (`artist_id`);

--
-- Index pour la table `artist`
--
ALTER TABLE `artist`
  ADD PRIMARY KEY (`aid`);

--
-- Index pour la table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`cart_ref`),
  ADD KEY `fk_user_cart` (`uid`);

--
-- Index pour la table `cart_art`
--
ALTER TABLE `cart_art`
  ADD PRIMARY KEY (`cart_ref`,`art_ref`),
  ADD KEY `fk_art_cartart` (`art_ref`);

--
-- Index pour la table `delivery`
--
ALTER TABLE `delivery`
  ADD PRIMARY KEY (`delivery_id`),
  ADD KEY `fk_order_delivery` (`order_id`),
  ADD KEY `fk_agency_delivery` (`agency_id`);

--
-- Index pour la table `delivery_agency`
--
ALTER TABLE `delivery_agency`
  ADD PRIMARY KEY (`agency_id`);

--
-- Index pour la table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `fk_cart_order` (`cart_ref`);

--
-- Index pour la table `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`review_id`),
  ADD KEY `fk_art_review` (`art_ref`),
  ADD KEY `fk_user_review` (`uid`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`uid`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `art`
--
ALTER TABLE `art`
  MODIFY `art_ref` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `artist`
--
ALTER TABLE `artist`
  MODIFY `aid` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `cart`
--
ALTER TABLE `cart`
  MODIFY `cart_ref` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `delivery`
--
ALTER TABLE `delivery`
  MODIFY `delivery_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `delivery_agency`
--
ALTER TABLE `delivery_agency`
  MODIFY `agency_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `order`
--
ALTER TABLE `order`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `review`
--
ALTER TABLE `review`
  MODIFY `review_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `uid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `art`
--
ALTER TABLE `art`
  ADD CONSTRAINT `fk_artist_art` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`aid`);

--
-- Contraintes pour la table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `fk_user_cart` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);

--
-- Contraintes pour la table `cart_art`
--
ALTER TABLE `cart_art`
  ADD CONSTRAINT `fk_art_cartart` FOREIGN KEY (`art_ref`) REFERENCES `art` (`art_ref`),
  ADD CONSTRAINT `fk_cart_cartart` FOREIGN KEY (`cart_ref`) REFERENCES `cart` (`cart_ref`);

--
-- Contraintes pour la table `delivery`
--
ALTER TABLE `delivery`
  ADD CONSTRAINT `fk_agency_delivery` FOREIGN KEY (`agency_id`) REFERENCES `delivery_agency` (`agency_id`),
  ADD CONSTRAINT `fk_order_delivery` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`);

--
-- Contraintes pour la table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `fk_cart_order` FOREIGN KEY (`cart_ref`) REFERENCES `cart` (`cart_ref`);

--
-- Contraintes pour la table `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `fk_art_review` FOREIGN KEY (`art_ref`) REFERENCES `art` (`art_ref`),
  ADD CONSTRAINT `fk_user_review` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

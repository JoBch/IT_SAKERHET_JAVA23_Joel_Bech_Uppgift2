-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3307
-- Generation Time: Oct 08, 2024 at 02:11 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `itsakerhet`
--

-- --------------------------------------------------------

--
-- Table structure for table `timecapsule`
--

CREATE TABLE `timecapsule` (
  `message_id` int(11) NOT NULL,
  `message` text DEFAULT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `timecapsule`
--

INSERT INTO `timecapsule` (`message_id`, `message`, `user_id`) VALUES
(1, 'mEDCsd+2vew3AF2rbO84+Q==', 2),
(2, 'Ji5hjERx2CQEkO2eWQrCJw==', 2),
(3, 'mEDCsd+2vew3AF2rbO84+Q==', 3),
(4, 'XeN82j755t1K5/ZbnmfxFw==', 2),
(5, 'bwQ7Ej8fEi9hDAkZxgNsXw==', 4),
(6, 'gHtSlhoqq2fWYdx1kFoQWA==', 2),
(7, 'x0aSWrGoSkLaJPC0g+dvxYoLKnWu+xdS0/6wuuwmR4Y=', 2);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `email`) VALUES
(2, 'hasse', '$2a$10$K1CqDmag1LMXTsGi8CaOFe6Ax9y6f8UYizR.VdnSktsU8M.0x5nNS', 'hasse'),
(3, 'joel', '$2a$10$8sIxZm4NZDFuc4RLPm5ILe8bSRjSd4BlFOKi485./1zpXb3FJer6m', 'joel@grit.se'),
(4, 'kalle', '$2a$10$cMqyDK68XWR75mwaPyZQ7.NY7F6pbXtSKg9iVt1DW433pacZS3arC', 'kalle');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `timecapsule`
--
ALTER TABLE `timecapsule`
  ADD PRIMARY KEY (`message_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `timecapsule`
--
ALTER TABLE `timecapsule`
  MODIFY `message_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `timecapsule`
--
ALTER TABLE `timecapsule`
  ADD CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

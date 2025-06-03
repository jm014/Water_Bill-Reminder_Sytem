-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 03, 2025 at 03:23 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `water_billing`
--

-- --------------------------------------------------------

--
-- Table structure for table `bill`
--

CREATE TABLE `bill` (
  `id` int(11) NOT NULL,
  `meter_no` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `month` varchar(20) NOT NULL,
  `cubic_meters` int(11) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `status` varchar(20) DEFAULT 'Unpaid'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `name` varchar(30) NOT NULL,
  `meter_no` varchar(20) NOT NULL,
  `address` varchar(50) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `province` varchar(30) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`name`, `meter_no`, `address`, `city`, `province`, `email`, `phone`) VALUES
('aldred', '089873', '5', 'baler', 'aurora', 'aldred@gmail.com', '123456'),
('willian', '443883', 'bazal', 'maria aurora', 'aurora', 'willian@gmail.com', '13456789'),
('marco', '499967', 'bazal', 'maria', 'aurora', 'marco@gmail.com', '123456789'),
('Jose Rizal', '858977', 'Brgy. 5, Poblacion', 'Calamba', 'Laguna', 'jose@gmail.com', '09612248581');

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `meter_no` varchar(20) NOT NULL,
  `username` varchar(30) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `user` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`meter_no`, `username`, `name`, `password`, `user`) VALUES
('', 'admin', 'admin', '123456', 'Admin'),
('089873', 'aldred', 'aldred', '123456', 'Customer'),
('443883', 'willian', 'willian', '123456', 'Customer'),
('499967', 'marco', 'marco', '123456', 'Customer'),
('587719', 'rave@gmail.com', 'Rave Aquino', '', 'customer'),
('858977', 'Josepogi', 'Jose Rizal', '1234567', 'Customer');

-- --------------------------------------------------------

--
-- Table structure for table `meter_info`
--

CREATE TABLE `meter_info` (
  `meter_no` varchar(20) NOT NULL,
  `meter_location` varchar(20) DEFAULT NULL,
  `meter_type` varchar(20) DEFAULT NULL,
  `phase_code` varchar(20) DEFAULT NULL,
  `bill_type` varchar(20) DEFAULT NULL,
  `days` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `meter_info`
--

INSERT INTO `meter_info` (`meter_no`, `meter_location`, `meter_type`, `phase_code`, `bill_type`, `days`) VALUES
('089873', 'Outside', 'Standard Water Meter', '011', 'Normal', '30'),
('443883', 'Outside', 'Standard Water Meter', '011', 'Normal', '30'),
('499967', 'Outside', 'Standard Water Meter', '011', 'Normal', '30'),
('587719', 'Outside', 'Standard Water Meter', '011', 'Normal', '30'),
('858977', 'Outside', 'Smart Water Meter', '011', 'Normal', '30');

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL,
  `meter_no` varchar(255) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`id`, `meter_no`, `message`, `date`) VALUES
(4, '499967', 'dasdasdas', '2025-06-02 18:58:31'),
(5, '089873', 'asdasd', '2025-06-02 19:01:43'),
(6, '499967', 'asdsad', '2025-06-02 19:01:49'),
(7, '858977', 'Your bill needs to be paid.', '2025-06-02 20:34:31'),
(8, '587719', 'Your bill needs to be paid.', '2025-06-02 20:52:16'),
(9, '443883', 'mag bayad ka na sa akin', '2025-06-03 09:19:15');

-- --------------------------------------------------------

--
-- Table structure for table `tax`
--

CREATE TABLE `tax` (
  `cost_per_unit` varchar(20) DEFAULT NULL,
  `meter_rent` varchar(20) DEFAULT NULL,
  `service_charge` varchar(20) DEFAULT NULL,
  `service_tax` varchar(20) DEFAULT NULL,
  `environment_fee` varchar(20) DEFAULT NULL,
  `fixed_tax` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tax`
--

INSERT INTO `tax` (`cost_per_unit`, `meter_rent`, `service_charge`, `service_tax`, `environment_fee`, `fixed_tax`) VALUES
('10', '10', '10', '10', '5', '10');

-- --------------------------------------------------------

--
-- Table structure for table `waterbill`
--

CREATE TABLE `waterbill` (
  `id` int(11) NOT NULL,
  `meter_no` varchar(50) DEFAULT NULL,
  `customer_name` varchar(100) DEFAULT NULL,
  `previous_reading` double DEFAULT NULL,
  `current_reading` double DEFAULT NULL,
  `amount_due` double DEFAULT NULL,
  `bill_month` varchar(20) DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `waterbill`
--

INSERT INTO `waterbill` (`id`, `meter_no`, `customer_name`, `previous_reading`, `current_reading`, `amount_due`, `bill_month`, `due_date`, `status`) VALUES
(37, '499967', 'marco', 100, 300, 2236.5, 'January', '2025-06-12', 'Paid'),
(38, '089873', 'aldred', 100, 200, 1136.5, 'January', '2025-06-04', 'Paid'),
(39, '858977', 'Jose Rizal', 100, 150, 586.5, 'June', '2025-06-30', 'Paid'),
(41, '443883', 'willian', 100, 200, 1136.5, 'June', '2025-06-30', 'Paid'),
(42, '443883', 'willian', 100, 200, 1136.5, 'January', '2025-06-30', 'Paid');

-- --------------------------------------------------------

--
-- Table structure for table `water_tariff`
--

CREATE TABLE `water_tariff` (
  `id` int(11) NOT NULL,
  `rate_per_cubic_meter` int(11) DEFAULT NULL,
  `service_charge` int(11) DEFAULT NULL,
  `environment_fee` int(11) DEFAULT NULL,
  `maintenance_fee` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `water_tariff`
--

INSERT INTO `water_tariff` (`id`, `rate_per_cubic_meter`, `service_charge`, `environment_fee`, `maintenance_fee`) VALUES
(1, 20, 50, 30, 40);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bill`
--
ALTER TABLE `bill`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`meter_no`);

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`meter_no`);

--
-- Indexes for table `meter_info`
--
ALTER TABLE `meter_info`
  ADD PRIMARY KEY (`meter_no`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `waterbill`
--
ALTER TABLE `waterbill`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `water_tariff`
--
ALTER TABLE `water_tariff`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bill`
--
ALTER TABLE `bill`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `waterbill`
--
ALTER TABLE `waterbill`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `water_tariff`
--
ALTER TABLE `water_tariff`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

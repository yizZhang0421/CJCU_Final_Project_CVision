-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- 主機: 127.0.0.1:3306
-- 產生時間： 2019 年 04 月 06 日 08:39
-- 伺服器版本: 5.7.24
-- PHP 版本： 7.2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `cvision_new`
--

-- --------------------------------------------------------

--
-- 資料表結構 `label`
--

DROP TABLE IF EXISTS `label`;
CREATE TABLE IF NOT EXISTS `label` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `model_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`,`model_id`),
  KEY `label_ibfk_1` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `label`
--

INSERT INTO `label` (`id`, `name`, `model_id`) VALUES
('1554535793433-577033C3-7A9E-4A41-95BD-79A775A3B3D1', 'labelA', '1554454200330-F180135E-BE2C-4BB7-8BB7-A53D20BD3135'),
('1554458308901-CC996BFD-0CED-4CA2-A521-0F54F4CD6DAF', 'labelA', '1554457143155-F56629CC-9093-4F7D-A563-5DAEEF4C6F32'),
('1554535860063-52C44F77-5E8B-4884-A60D-653BCF529530', 'labelB', '1554454200330-F180135E-BE2C-4BB7-8BB7-A53D20BD3135'),
('1554475673081-853ED747-4381-4E87-9D04-AED6A558B775', 'labelB', '1554457143155-F56629CC-9093-4F7D-A563-5DAEEF4C6F32');

-- --------------------------------------------------------

--
-- 資料表結構 `model`
--

DROP TABLE IF EXISTS `model`;
CREATE TABLE IF NOT EXISTS `model` (
  `id` varchar(50) NOT NULL,
  `key` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `size` float DEFAULT NULL,
  `acc` float DEFAULT NULL,
  `loss` float DEFAULT NULL,
  `train_status` tinyint(4) DEFAULT '-1',
  `class_label` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `model`
--

INSERT INTO `model` (`id`, `key`, `name`, `size`, `acc`, `loss`, `train_status`, `class_label`) VALUES
('1554454200330-F180135E-BE2C-4BB7-8BB7-A53D20BD3135', '1554445357166-CA9B0944-CBCF-407E-B2C3-D15BB16F169F', 'ModelB', NULL, NULL, NULL, -1, NULL),
('1554457143155-F56629CC-9093-4F7D-A563-5DAEEF4C6F32', '1554445357166-CA9B0944-CBCF-407E-B2C3-D15BB16F169F', 'ModelA', 247.256, 0.6, 0.733624, 0, '{\"0\": \"labelA\", \"1\": \"labelB\"}'),
('1554514535890-FFED4974-FED8-4888-9476-139C6140338C', '1554445357166-CA9B0944-CBCF-407E-B2C3-D15BB16F169F', 'TrainingA', NULL, NULL, NULL, 1, NULL),
('1554514541000-A33B9C50-A5C7-495C-99BE-657156DF64C2', '1554445357166-CA9B0944-CBCF-407E-B2C3-D15BB16F169F', 'TrainingB', NULL, NULL, NULL, 2, NULL),
('1554514779499-10D8BA87-AFE5-467D-B3D8-7B91677A136A', '1554445357166-CA9B0944-CBCF-407E-B2C3-D15BB16F169F', 'PredictableA', NULL, NULL, NULL, 0, NULL);

-- --------------------------------------------------------

--
-- 資料表結構 `model_users`
--

DROP TABLE IF EXISTS `model_users`;
CREATE TABLE IF NOT EXISTS `model_users` (
  `model_id` varchar(50) NOT NULL,
  `key` varchar(50) NOT NULL,
  `permission` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`model_id`,`key`),
  KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `key` varchar(50) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `user`
--

INSERT INTO `user` (`key`, `email`, `name`) VALUES
('1554445357166-CA9B0944-CBCF-407E-B2C3-D15BB16F169F', 'z58774556@gmail.com', '張逸宗'),
('1554457252259-12B226FA-28AA-4CA1-851D-F5F345CA6DAA', 'h24563026@mailst.cjcu.edu.tw', 'yiz Zhang');

--
-- 已匯出資料表的限制(Constraint)
--

--
-- 資料表的 Constraints `label`
--
ALTER TABLE `label`
  ADD CONSTRAINT `label_ibfk_1` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的 Constraints `model`
--
ALTER TABLE `model`
  ADD CONSTRAINT `model_ibfk_1` FOREIGN KEY (`key`) REFERENCES `user` (`key`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的 Constraints `model_users`
--
ALTER TABLE `model_users`
  ADD CONSTRAINT `model_users_ibfk_1` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `model_users_ibfk_2` FOREIGN KEY (`key`) REFERENCES `user` (`key`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

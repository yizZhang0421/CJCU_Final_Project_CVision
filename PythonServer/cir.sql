-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- 主機: 127.0.0.1:3306
-- 產生時間： 2019-01-27 13:11:38
-- 伺服器版本: 5.7.23
-- PHP 版本： 7.2.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `cir`
--

-- --------------------------------------------------------

--
-- 資料表結構 `broadcast`
--

DROP TABLE IF EXISTS `broadcast`;
CREATE TABLE IF NOT EXISTS `broadcast` (
  `content` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `label`
--

DROP TABLE IF EXISTS `label`;
CREATE TABLE IF NOT EXISTS `label` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `model_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_label_name` (`name`,`model_id`),
  KEY `model_id` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `label`
--

INSERT INTO `label` (`id`, `name`, `model_id`) VALUES
('1547914901677-ac75acd9-f3e0-45ea-a738-40cd34bb9514', 'Coaster', '1547914894173-334481f0-5657-400d-ac2f-334a52138306'),
('1547914910933-578c74b4-3e4e-4e7b-8a67-e28d9da2ba6e', 'Septim coin', '1547914894173-334481f0-5657-400d-ac2f-334a52138306'),
('1547987286716-d9baefa5-6750-4c6a-9613-1ba73039fc19', 'test', '1547987162253-f860e8f0-c0d9-4c37-b6eb-b18770efe252');

-- --------------------------------------------------------

--
-- 資料表結構 `label_class`
--

DROP TABLE IF EXISTS `label_class`;
CREATE TABLE IF NOT EXISTS `label_class` (
  `model_id` varchar(50) NOT NULL,
  `dict_string` text NOT NULL,
  `acc_list` text,
  PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `label_class`
--

INSERT INTO `label_class` (`model_id`, `dict_string`, `acc_list`) VALUES
('1547914894173-334481f0-5657-400d-ac2f-334a52138306', '{\"0\": \"Coaster\", \"1\": \"Septim coin\"}', '[\"0.5284090909090909\", \"0.5681818181818182\", \"0.8863636363636364\", \"0.7670454545454546\", \"0.9204545454545454\", \"0.9602272727272727\", \"0.9886363636363636\", \"0.9829545454545454\"]');

-- --------------------------------------------------------

--
-- 資料表結構 `label_group`
--

DROP TABLE IF EXISTS `label_group`;
CREATE TABLE IF NOT EXISTS `label_group` (
  `label_id` varchar(50) NOT NULL,
  `add_label` varchar(50) NOT NULL,
  PRIMARY KEY (`label_id`,`add_label`),
  KEY `add_label` (`add_label`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `label_group`
--

INSERT INTO `label_group` (`label_id`, `add_label`) VALUES
('1547914901677-ac75acd9-f3e0-45ea-a738-40cd34bb9514', '1547914901677-ac75acd9-f3e0-45ea-a738-40cd34bb9514'),
('1547914910933-578c74b4-3e4e-4e7b-8a67-e28d9da2ba6e', '1547914910933-578c74b4-3e4e-4e7b-8a67-e28d9da2ba6e'),
('1547987286716-d9baefa5-6750-4c6a-9613-1ba73039fc19', '1547987286716-d9baefa5-6750-4c6a-9613-1ba73039fc19');

-- --------------------------------------------------------

--
-- 資料表結構 `model`
--

DROP TABLE IF EXISTS `model`;
CREATE TABLE IF NOT EXISTS `model` (
  `id` varchar(50) NOT NULL,
  `owner_email` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `trainStatu` tinyint(1) NOT NULL DEFAULT '0',
  `size` int(11) DEFAULT NULL,
  `acc` float DEFAULT NULL,
  `loss` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_model_of_user` (`owner_email`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `model`
--

INSERT INTO `model` (`id`, `owner_email`, `name`, `trainStatu`, `size`, `acc`, `loss`) VALUES
('1547914894173-334481f0-5657-400d-ac2f-334a52138306', 'z58774556@gmail.com', 'Septim Coin and Coaster', 1, 197, 0.982955, 0.0628503),
('1547987162253-f860e8f0-c0d9-4c37-b6eb-b18770efe252', 'z58774556@gmail.com', 'test', 0, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- 資料表結構 `model_users`
--

DROP TABLE IF EXISTS `model_users`;
CREATE TABLE IF NOT EXISTS `model_users` (
  `model_id` varchar(50) NOT NULL,
  `user` varchar(100) NOT NULL,
  PRIMARY KEY (`model_id`,`user`),
  KEY `user` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `model_users`
--

INSERT INTO `model_users` (`model_id`, `user`) VALUES
('1547914894173-334481f0-5657-400d-ac2f-334a52138306', 'z58774556@gmail.com'),
('1547987162253-f860e8f0-c0d9-4c37-b6eb-b18770efe252', 'z58774556@gmail.com');

-- --------------------------------------------------------

--
-- 資料表結構 `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `email` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `user`
--

INSERT INTO `user` (`email`, `name`) VALUES
('h24563026@mailst.cjcu.edu.tw', 'yiz Zhang'),
('z58774556@gmail.com', '張逸宗');

--
-- 已匯出資料表的限制(Constraint)
--

--
-- 資料表的 Constraints `label`
--
ALTER TABLE `label`
  ADD CONSTRAINT `label_ibfk_1` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的 Constraints `label_class`
--
ALTER TABLE `label_class`
  ADD CONSTRAINT `label_class_ibfk_1` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的 Constraints `label_group`
--
ALTER TABLE `label_group`
  ADD CONSTRAINT `label_group_ibfk_1` FOREIGN KEY (`add_label`) REFERENCES `label` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `label_group_ibfk_2` FOREIGN KEY (`label_id`) REFERENCES `label` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的 Constraints `model`
--
ALTER TABLE `model`
  ADD CONSTRAINT `model_ibfk_1` FOREIGN KEY (`owner_email`) REFERENCES `user` (`email`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的 Constraints `model_users`
--
ALTER TABLE `model_users`
  ADD CONSTRAINT `model_users_ibfk_1` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `model_users_ibfk_2` FOREIGN KEY (`user`) REFERENCES `user` (`email`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

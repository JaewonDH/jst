--INSERT INTO member (id,user_name,phone,create_date,update_date) SELECT 'jwlee@test.com','jwlee','010-0000-0000',now(),now()
--FROM DUAL where NOT EXISTS(SELECT * FROM member WHERE id = '4f6026d4-c771-4b5c-a8d6-3095d1abd309');

INSERT INTO post_category (id,category_name,create_date,update_date) SELECT 1,'문의',now(),now()
FROM DUAL where NOT EXISTS(SELECT * FROM post_category WHERE id = 1);

INSERT INTO post_category (id,category_name,create_date,update_date) SELECT 2,'요청',now(),now()
FROM DUAL where NOT EXISTS(SELECT * FROM post_category WHERE id = 2);

INSERT INTO post_category (id,category_name,create_date,update_date) SELECT 3,'자유',now(),now()
FROM DUAL where NOT EXISTS(SELECT * FROM post_category WHERE id = 3);

--CREATE TABLE IF NOT EXISTS `post_category` (
--  `create_date` datetime(6) DEFAULT NULL,
--  `update_date` datetime(6) DEFAULT NULL,
--  `category_name` varchar(255) DEFAULT NULL,
--  `id` varchar(255) NOT NULL,
--  PRIMARY KEY (`id`)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

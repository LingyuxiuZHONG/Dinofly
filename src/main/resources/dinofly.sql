CREATE TABLE `review`(
                         `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         `reservation_id` INT NOT NULL,
                         `rating` INT NOT NULL,
                         `comment` VARCHAR(255) NULL
);
CREATE TABLE `ad`(
                     `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                     `home_type` ENUM(
                         'Apartment',
                         'Townhouse',
                         'Single-Family'
                         ) NOT NULL,
                     `total_occupancy` INT NOT NULL,
                     `total_bedrooms` INT NOT NULL,
                     `total_bathrooms` INT NOT NULL,
                     `description` VARCHAR(255) NULL,
                     `address` VARCHAR(255) NOT NULL,
                     `has_tv` TINYINT(1) NOT NULL,
                     `has_kitchen` TINYINT(1) NOT NULL,
                     `has_air_con` TINYINT(1) NOT NULL,
                     `has_heating` TINYINT(1) NOT NULL,
                     `has_internet` TINYINT(1) NOT NULL,
                     `price` INT NOT NULL,
                     `published_at` TIMESTAMP NULL,
                     `owner_id` INT NOT NULL,
                     `created_at` TIMESTAMP NULL,
                     `updated_at` TIMESTAMP NULL,
                     `latitude` DOUBLE(8, 2) NULL,
                     `longitude` DOUBLE(8, 2) NULL
);
CREATE TABLE `media`(
                        `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `model_id` INT NOT NULL,
                        `model_type` ENUM('ad', 'review') NOT NULL,
                        `file_name` VARCHAR(255) NOT NULL
);
CREATE TABLE `reservation`(
                              `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                              `user_id` INT NOT NULL,
                              `ad_id` INT NOT NULL,
                              `start_date` DATE NOT NULL,
                              `end_date` DATE NOT NULL,
                              `price` INT NOT NULL,
                              `total` INT NOT NULL,
                              `created_at` DATETIME NOT NULL,
                              `updated_at` DATETIME NOT NULL
);
CREATE TABLE `user`(
                       `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       `username` VARCHAR(255) NOT NULL,
                       `email` VARCHAR(255) NULL,
                       `email_verified_at` TIMESTAMP NULL,
                       `password` VARCHAR(255) NOT NULL,
                       `phone_number` VARCHAR(255) NULL,
                       `description` VARCHAR(255) NULL,
                       `profile_image` VARCHAR(255) NULL,
                       `role` ENUM('Admin', 'Landlord', 'Tenant') NOT NULL
);
ALTER TABLE
    `user` ADD UNIQUE `user_username_unique`(`username`);
ALTER TABLE
    `user` ADD UNIQUE `user_email_unique`(`email`);
ALTER TABLE
    `user` ADD UNIQUE `user_phone_number_unique`(`phone_number`);
ALTER TABLE
    `ad` ADD CONSTRAINT `ad_owner_id_foreign` FOREIGN KEY(`owner_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `review` ADD CONSTRAINT `review_reservation_id_foreign` FOREIGN KEY(`reservation_id`) REFERENCES `reservation`(`id`);
ALTER TABLE
    `reservation` ADD CONSTRAINT `reservation_user_id_foreign` FOREIGN KEY(`user_id`) REFERENCES `user`(`id`);
ALTER TABLE
    `reservation` ADD CONSTRAINT `reservation_ad_id_foreign` FOREIGN KEY(`ad_id`) REFERENCES `ad`(`id`);
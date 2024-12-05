insert into account(id,email,first_name,last_name)values
(1,'admin@mail.com','admin','admin'),
(2,'user_manager@mail.com','user_manager','admin'),
(3,'role_manager@mail.com','role_manager','admin'),
(4,'test@mail.com','test','test');
insert into role(id,name) values
(1,'admin'),
(2,'user_manager'),
(3,'role_manager');
insert into permission(id,resource_name,resource_code,scope) values
(1,'all','ADMIN','ALL'),
(2,'user','USER','CREATE'),
(3,'user','USER','READ'),
(4,'user','USER','UPDATE'),
(5,'user','USER','DELETE'),
(6,'user','USER','ALL'),
(7,'role','ROLE','CREATE'),
(8,'role','ROLE','READ'),
(9,'role','ROLE','UPDATE'),
(10,'role','ROLE','DELETE'),
(11,'role','ROLE','ALL');

insert into role_permission(id,resource_code,scope,role_id,permission_id) values
(1,'ADMIN','ALL',1,1),
(2,'USER','CREATE',2,2),
(3,'USER','READ',2,3),
(4,'USER','UPDATE',2,4),
(5,'USER','DELETE',2,5),
(6,'USER','ALL',2,6),
(7,'ROLE','CREATE',3,7),
(8,'ROLE','READ',3,8),
(9,'ROLE','UPDATE',3,9),
(10,'ROLE','DELETE',3,10),
(11,'ROLE','ALL',3,11);
insert into account_role(id,account_id,role_id) values
(1,1,1),
(2,2,2),
(3,3,3);
INSERT INTO account (id, address, deleted, email, first_name, last_name, locked, password, phone, picture, verified, date_of_birth)
VALUES
    (21, '123 Đường Trần Phú, Hà Nội', false, 'example21@mail.com', 'Nguyễn Văn', 'An', false, 'password123', '0123456789', 'https://example.com/picture21.jpg', true, '1985-06-15'),
    (22, '456 Đường Lê Lợi, Đà Nẵng', true, 'example22@mail.com', 'Trần Thị', 'Bích', true, 'password456', '0987654321', 'https://example.com/picture22.jpg', false, '1978-12-03'),
    (23, '789 Đường Hùng Vương, TP.HCM', false, 'example23@mail.com', 'Phạm Đức', 'Cường', false, 'password789', '1234567890', 'https://example.com/picture23.jpg', true, '1965-04-20'),
    (24, '321 Đường Nguyễn Huệ, Hải Phòng', false, 'example24@mail.com', 'Lê Thanh', 'Dung', false, 'password321', '0981234567', 'https://example.com/picture24.jpg', true, '1945-03-11'),
    (25, '654 Đường Phan Đình Phùng, Hà Nội', true, 'example25@mail.com', 'Đặng Phương', 'Hải', true, 'password654', '0123987654', 'https://example.com/picture25.jpg', false, '1991-10-25'),
    (26, '987 Đường Điện Biên Phủ, Cần Thơ', false, 'example26@mail.com', 'Hoàng Đức', 'Khải', false, 'password987', '0321987654', 'https://example.com/picture26.jpg', true, '1983-09-15'),
    (27, '456 Đường Võ Văn Kiệt, Đà Nẵng', false, 'example27@mail.com', 'Võ Nhật', 'Linh', true, 'password1234', '0934567890', 'https://example.com/picture27.jpg', false, '1975-05-05'),
    (28, '789 Đường Trường Chinh, TP.HCM', true, 'example28@mail.com', 'Ngô Huy', 'Minh', false, 'password5678', '0987654321', 'https://example.com/picture28.jpg', true, '1960-11-30'),
    (29, '123 Đường Láng, Hà Nội', false, 'example29@mail.com', 'Đinh Bảo', 'Ngọc', false, 'password91011', '0123456789', 'https://example.com/picture29.jpg', true, '1950-02-14'),
    (30, '654 Đường Hoàng Văn Thụ, Nha Trang', true, 'example30@mail.com', 'Nguyễn Quốc', 'Phong', true, 'password1213', '0987651234', 'https://example.com/picture30.jpg', false, '1940-07-07'),
    (31, '123 Đường Tô Hiến Thành, Hà Nội', false, 'example31@mail.com', 'Nguyễn Văn', 'Hoàng', false, 'password1231', '0123456789', 'https://example.com/picture31.jpg', true, '2003-06-12'),
    (32, '456 Đường Lê Quý Đôn, Đà Nẵng', true, 'example32@mail.com', 'Trần Thị', 'Lan', true, 'password4562', '0987654321', 'https://example.com/picture32.jpg', false, '2015-09-08'),
    (33, '789 Đường Bạch Đằng, TP.HCM', false, 'example33@mail.com', 'Phạm Đức', 'Anh', false, 'password7893', '1234567890', 'https://example.com/picture33.jpg', true, '2010-12-25'),
    (34, '321 Đường Phạm Ngũ Lão, Hải Phòng', false, 'example34@mail.com', 'Lê Thanh', 'Bảo', false, 'password3214', '0981234567', 'https://example.com/picture34.jpg', true, '2022-01-15'),
    (35, '654 Đường Hai Bà Trưng, Hà Nội', true, 'example35@mail.com', 'Đặng Phương', 'Khánh', true, 'password6545', '0123987654', 'https://example.com/picture35.jpg', false, '2005-03-21'),
    (36, '987 Đường Nguyễn Đình Chiểu, Cần Thơ', false, 'example36@mail.com', 'Hoàng Đức', 'Quân', false, 'password9876', '0321987654', 'https://example.com/picture36.jpg', true, '2001-11-13'),
    (37, '456 Đường Trường Sơn, Đà Nẵng', false, 'example37@mail.com', 'Võ Nhật', 'Hùng', true, 'password12347', '0934567890', 'https://example.com/picture37.jpg', false, '2019-08-30'),
    (38, '789 Đường Lê Thánh Tôn, TP.HCM', true, 'example38@mail.com', 'Ngô Huy', 'Tú', false, 'password56788', '0987654321', 'https://example.com/picture38.jpg', true, '2012-04-04'),
    (39, '123 Đường Nguyễn Tri Phương, Hà Nội', false, 'example39@mail.com', 'Đinh Bảo', 'Nam', false, 'password910119', '0123456789', 'https://example.com/picture39.jpg', true, '2000-02-28'),
    (40, '654 Đường Võ Thị Sáu, Nha Trang', true, 'example40@mail.com', 'Nguyễn Quốc', 'Vinh', true, 'password121340', '0987651234', 'https://example.com/picture40.jpg', false, '2020-07-10');


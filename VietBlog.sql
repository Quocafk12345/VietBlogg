CREATE DATABASE VietBlog
GO

USE VietBlog
GO

-- Tài khoản người dùng
CREATE TABLE Users
(
    Gioi_Tinh BIT,
    Email NVARCHAR(255) NOT NULL,
    Dien_Thoai VARCHAR(15),
    Mat_Khau NVARCHAR(MAX) NOT NULL,
    Ten_Dang_Nhap NVARCHAR(255) NOT NULL,
    Ten_Nguoi_Dung NVARCHAR(255) NOT NULL,
    User_Id INT IDENTITY(1, 1) NOT NULL,
    Hinh_Dai_Dien NVARCHAR(MAX),
    Ngay_Tao DATE NOT NULL,
    Vai_Tro NVARCHAR(255) NOT NULL, -- phân quyền Admin với User thường
    Ngay_Sinh DATE, -- Thuộc tính Ngày Sinh được thêm vào
    PRIMARY KEY (User_Id)
);
GO


-- Bài viết của người dùng
CREATE TABLE Bai_Viet
(
    Tieu_De NVARCHAR(MAX),
    Noi_Dung NVARCHAR(MAX),
    Id_Bai_Viet INT IDENTITY(1, 1) NOT NULL,
    Ngay_Tao DATETIME NOT NULL,
    Id_Nhom INT,
    Id_Bai_Viet_Chia_Se INT, -- Id bài viết được chia sẻ
    Trang_Thai NVARCHAR(255) NOT NULL, -- "Bài Nháp" để lưu lại bản nháp, "Đã đăng" để hiện lên trang chủ
    User_Id INT NOT NULL,
    PRIMARY KEY (Id_Bai_Viet),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Id_Nhom) REFERENCES Nhom(Id_Nhom),
    FOREIGN KEY (Id_Bai_Viet_Chia_Se) REFERENCES Bai_Viet(Id_Bai_Viet),
);
GO

CREATE TABLE Da_Phuong_Tien
̣(
    Id_Phuong_Tien INT IDENTITY(1, 1) NOT NULL,
    Id_BaiViet INT NOT NULL,
    Duong_Dan NVARCHAR(MAX) NOT NULL,
    PRIMARY KEY (Id_Phuong_Tien),
    FOREIGN KEY (Id_Bai_Viet) REFERENCES Bai_Viet(Id_Bai_Viet)
);
GO

-- Thông báo của người dùng
CREATE TABLE Thong_Bao
(
    Noi_Dung NVARCHAR(MAX) NOT NULL,
    Duong_Dan NVARCHAR(MAX),
    Da_Doc BIT NOT NULL,
    Id_Thong_Bao INT IDENTITY(1, 1) NOT NULL,
    Ngay_Tao DATETIME NOT NULL,
    User_Id INT NOT NULL,
    PRIMARY KEY (Id_Thong_Bao),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id)
);
GO

-- Bình luận và phản hồi bình luận
CREATE TABLE Binh_Luan
(
    Id_Binh_Luan INT IDENTITY(1,1) NOT NULL,
    Level_Binh_Luan INT NOT NULL,
    Noi_Dung NVARCHAR(MAX) NOT NULL,
    Id_BL_Cha INT,
    Ngay_Tao DATETIME NOT NULL,
    Id_Bai_Viet INT NOT NULL,
    User_Id INT NOT NULL,
    PRIMARY KEY (Id_Binh_Luan),
    FOREIGN KEY (Id_Bai_Viet) REFERENCES Bai_Viet(Id_Bai_Viet),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Id_BL_Cha) REFERENCES Binh_Luan(Id_Binh_Luan)
);
GO

CREATE TABLE Nhom
(
    Id_Nhom INT IDENTITY(1, 1) NOT NULL,
    Ten NVARCHAR(255) NOT NULL,
    Gioi_Thieu NVARCHAR(MAX) NOT NULL,
    Hinh_Dai_Dien NVARCHAR(MAX),
    Ngay_Tao DATETIME NOT NULL,
    PRIMARY KEY (Id_Nhom)
);
GO

CREATE TABLE DS_Luat_Nhom
(
    Id_Luat IDENTITY(1, 1) NOT NULL,
    Noi_Dung NVARCHAR(MAX) NOT NULL,
    Ten NVARCHAR(MAX) NOT NULL,
    Id_Nhom INT NOT NULL,
    PRIMARY KEY (Id_Luat),
    FOREIGN KEY (Id_Nhom) REFERENCES Nhom(Id_Nhom)
);

CREATE TABLE Thanh_Vien
(
    Vai_Tro NVARCHAR(255) NOT NULL,
    Id_Nhom INT NOT NULL,
    User_Id INT NOT NULL,
    PRIMARY KEY (Id_Nhom, User_Id),
    FOREIGN KEY (Id_Nhom) REFERENCES Nhom(Id_Nhom),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id)
);
GO

CREATE TABLE Luu_Bai_Viet
(
    User_Id INT NOT NULL,
    Id_Bai_Viet INT NOT NULL,
    PRIMARY KEY (User_Id, Id_Bai_Viet),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Id_Bai_Viet) REFERENCES Bai_Viet(Id_Bai_Viet)
);
GO

CREATE TABLE Luot_Follow
(
    User_Id INT NOT NULL,
    User_Follow_Id INT NOT NULL,
    PRIMARY KEY (User_Id, User_Follow_Id),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (User_Follow_Id) REFERENCES Users(User_Id)
);
GO

CREATE TABLE Luot_Like_Bai_Viet
(
    User_Id INT NOT NULL,
    Id_Bai_Viet INT NOT NULL,
    PRIMARY KEY (User_Id, Id_Bai_Viet),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Id_Bai_Viet) REFERENCES Bai_Viet(Id_Bai_Viet)
);
GO

CREATE TABLE Luot_Like_Binh_Luan
(
    User_Id INT NOT NULL,
    Id_Binh_Luan INT NOT NULL,
    PRIMARY KEY (User_Id, Id_Binh_Luan),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Id_Binh_Luan) REFERENCES Binh_Luan(Id_Binh_Luan)
);
GO


ALTER TABLE Users
ADD CONSTRAINT UQ_Ten_Dang_Nhap UNIQUE (Ten_Dang_Nhap);


-- Thêm dữ liệu mẫu cho bảng Users
INSERT INTO Users (Gioi_Tinh, Email, Dien_Thoai, Mat_Khau, Ten_Dang_Nhap, Ten_Nguoi_Dung, Ngay_Tao, Vai_Tro) VALUES
(1, 'lehoangbao232@gmail.com', '0354356463', 'password123', 'lehoangbao232', N'Lê Hoàng Bảo', '2022-01-01', 'USER'),
(1, 'huuthothai467@gmail.com', '0843675245', 'password123', 'huuthothai467', N'Thái Hữu Thọ', '2022-01-01', 'USER'),
(1, 'letronghung557@gmail.com', '0934532467', 'password123', 'letronghung557', N'Lê Trọng Hưng', '2022-01-01', 'ADMIN'),
(1, 'hoangvunguyen816@gmail.com', '0954459379', 'password123', 'hoangvunguyen816', N'Nguyễn Hoàng Vũ', '2022-01-01', 'USER'),
(1, 'hoangtruongminh271@gmail.com', '0365472478', 'password123', 'hoangtruongminh271', N'Admin User', '2022-01-01', 'USER'),
(0, 'linhnguyenthuy504@gmail.com', '0812354789', 'password123', 'linhnguyenthuy504', N'Admin User', '2022-01-01', 'USER'),
(0, 'chautranthi344@gmail.com', '0934663426', 'password123', 'chautranthi344', N'Admin User', '2022-01-01', 'USER'),
(0, 'phuongnguyenchau@gmail.com', '0946725468', 'password123', 'phuongnguyenchau', N'Admin User', '2022-01-01', 'USER'),
(0, 'trinhhoangmyle@gmail.com', '0745568864', 'password123', 'trinhhoangmyle', N'Admin User', '2022-01-01', 'USER'),
(0, 'minhnguyenthi@gmail.com', '0724534574', 'password123', 'minhnguyenthi', N'Admin User', '2022-01-01', 'USER'),
(0, 'nhuquynhngocle@gmail.com', '0378974563', 'password123', 'nhuquynhngocle', N'Admin User', '2022-01-01', 'USER'),
(1, 'baothaihoang@gmail.com', '0954857968', 'password123', 'baothaihoang', N'Admin User', '2022-01-01', 'USER'),
(1, 'cuongngocle@gmail.com', '0967535567', 'password123', 'cuongngocle', N'Admin User', '2022-01-01', 'USER'),
(1, 'phucminhhoang@gmail.com', '0957765350', 'password123', 'phucminhhoang', N'Admin User', '2022-01-01', 'USER'),
(0, 'anhnhatlethi@gmail.com', '0356379707', 'password123', 'anhnhatlethi', N'Admin User', '2022-01-01', 'ADMIN'),
(1, 'nguyenanhquan@gmail.com', '0367567870', 'password123', 'nguyenanhquan', N'Admin User', '2022-01-01', 'ADMIN'),
(1, 'nguyenthaiquoc@gmail.com', '0946456697', 'password123', 'nguyenthaiquoc', N'User One', '2022-02-01', 'ADMIN'),
(1, 'nguyenanhqui@gmail.com', '0354675454', 'password123', 'nguyenanhqui', N'User Two', '2022-03-01', 'ADMIN');

-- Thêm dữ liệu mẫu cho bảng Bai_Viet
INSERT INTO Bai_Viet (Id_Nhom ,Tieu_De, Noi_Dung, Ngay_Tao, Id_Nhom, Trang_Thai, User_Id, Id_Bai_Viet_Chia_Se) VALUES
(NULL,'Is it just me or...', 'This scene right here is so fucking cold bro is laughing on an island full of enemy and enemy captains are standing right there.....if this mf came to hachinosu in his prime he would have saved coby plus captured/killed black beard pirate''s captains....this mf was laughing his whole fight, scared the captains, injured everyone, destroyed town and said "not enough" and he is fking 70+ of age ....bro is the definition of "HIM"Is it just me or anyone else also want luffy to get angry when he hear about garp captured by BB and in war with BB saves garp', '2024-10-18 10:00:00', 1, N'ĐĂNG', 1, NULL),
(NULL,'Telegram: Mối đe dọa đến quyền riêng tư', 'Ê biết app nào nhắn tin bảo mật , an toàn không mậy? Xài telegram đi, tin tao, đảm bảo không bốc phét. Thiệt không vậy cha? TLDR (Dài quá không đọc): Điều khiến Telegram trở nên khác biệt (và là lý do chính khiến nó trở nên phổ biến) là các Chanel và những kênh này (và các cuộc trò chuyện nhóm) không an toàn để sử .Nếu không có mã hóa đầu cuối, Telegram (hoặc bất kỳ ai có thể gây sức ép với Telegram hoặc truy cập vào hệ thống của Telegram) đều có thể toàn bộ nội dung được đăng trong các cuộc trò chuyện như vậy. Và vì tất cả người dùng Telegram đều được xác định bằng số điện thoại thực của họ (có thể ẩn khỏi những người dùng khác trên các kênh nhưng lại hiển thị với Telegram), nên họ có thể dễ dàng bị xác định danh tính. Tham khảo**:** https://protonvpn.com/blog/is-telegram-safe', '2023-02-01 10:00:00', 2, 'NHÁP', 2, NULL);
(NULL, N'Muốn có tình yêu... đẹp giống như hồi đi học', N'Mình đi làm khá lâu rồi mặc dù mới ra trường 1 năm, nhưng dạo này lại hay buồn tủi nên cũng muốn có bồ quá ạ. Nhiều lúc thấy vã lắm rồi nhưng không chấp nhận việc yêu vội hay yêu bừa được. Mình nhận không ít thiệp cưới của mấy cặp yêu đương từ thời áo trắng quần tây đến khi lên lễ đường. Có case bên trai dev lương nghìn đô còn bên gái bán bánh mỳ, vẫn ok tốt đẹp. Cũng có case bên gái làm CEO bên trai chạy Grab mà case nào cũng thấy họ hạnh phúc ghê. Mình cũng thèm được như vậy quá ạ. Riêng mình thì hồi xưa hèn quá với cắm đầu vô công việc nhiều từ thời mới là sinh viên nên bỏ lỡ khá nhiều cơ hội tốt. Thỉnh thoảng vô stalk người mình thích hồi 18 mà cứ ôm cái điện thoại buồn buồn mếu máo. Ở cty cũ mình làm TL nên cũng ít thời gian rảnh, thỉnh thoảng có mấy em tester xinh tươi qua chửi nên hồi đó k nghĩ nhiều về yêu đương. Bây giờ mình chuyển qua cty khác, làm mảng khác nhưng làm culi thì thời gian rảnh nhiều nên cũng dễ cảm thấy cô đơn hơn. Không có stress về công việc nhưng nhiều khi buồn vì cô đơn quá cũng phát ốm. Trong năm nay mình cũng dạn lên đi date tầm 4 em quanh Hà Nội, quen trên app dating hoặc mấy group linh tinh. Nhưng lúc gặp thì lại tụt mood xong chả nói được câu nào nên tạch luôn. Mà kiểu cũng không thấy có cảm xúc gì. Nhớ hồi nhỏ thấy mấy tụi có bồ chỉ cần có điểm chung gì với nửa kia rồi quan tâm nhau chút là auto đổ. Nhưng mình già rồi nên trước khi date cũng toàn bị đối phương soi lý lịch như tôn giáo, mindset, nghề nghiệp, tài chính... đủ thứ trên đời coi mình có đủ tốt với họ không :( . Mình pass được mớ tiêu chí đó thì đến lúc gặp người ta mình cứ auto tụt mood nên sau đó cũng không còn có sau đó. Bằng tuổi mình có người vẫn chung thủy với tình đầu lâu năm của họ, có người lăng nhăng thay bồ như thay áo. Chỉ còn mỗi mình cứ cô đơn lăn lộn 1 mình giữa lòng thủ đô chỉ để tìm hình bóng của 1 người mà mình có thể yêu thương vô điều kiện :( :(', '2024-10-18 10:00:00', 1, N'ĐĂNG', 1, NULL),
(),
(),
();
-- Thêm dữ liệu mẫu cho bảng Da_Phuong_Tien
INSERT INTO Da_Phuong_Tien (Id_BaiViet, Duong_Dan) VALUES
(1, '/path/to/image1.png'),
(1, '/path/to/image2.png');

-- Thêm dữ liệu mẫu cho bảng Thong_Bao
INSERT INTO Thong_Bao (Noi_Dung, Duong_Dan, Da_Doc, Ngay_Tao, User_Id) VALUES
('Thong bao so 1', '/path/to/notification1', 0, '2023-01-01 10:00:00', 1),
('Thong bao so 2', '/path/to/notification2', 1, '2023-02-01 10:00:00', 2);

-- Thêm dữ liệu mẫu cho bảng Binh_Luan
INSERT INTO Binh_Luan (Level_Binh_Luan, Noi_Dung, Id_BL_Cha, Ngay_Tao, Id_Bai_Viet, User_Id) VALUES
(1, 'Binh luan so 1', NULL, '2023-01-01 10:00:00', 1, 1),
(2, 'Phan hoi binh luan so 1', 1, '2023-01-01 11:00:00', 1, 2);

-- Thêm dữ liệu mẫu cho bảng Nhom
INSERT INTO Nhom (Ten, Gioi_Thieu, Hinh_Dai_Dien) VALUES
('Nhom 1', 'Gioi thieu nhom 1', '/path/to/image_ng1.png'),
('Nhom 2', 'Gioi thieu nhom 2', '/path/to/image_ng2.png');

-- Thêm dữ liệu mẫu cho bảng DS_Luat_Nhom
INSERT INTO DS_Luat_Nhom (Noi_Dung, Ten, Id_Nhom) VALUES
('Luat nhom 1', 'Luat 1', 1),
('Luat nhom 2', 'Luat 2', 2);

-- Thêm dữ liệu mẫu cho bảng Thanh_Vien
INSERT INTO Thanh_Vien (Vai_Tro, Id_Nhom, User_Id) VALUES
('Admin', 1, 1),
('Thanh Vien', 1, 2),
('Thanh Vien', 2, 3);

-- Thêm dữ liệu mẫu cho bảng Luu_Bai_Viet
INSERT INTO Luu_Bai_Viet (User_Id, Id_Bai_Viet) VALUES
(1, 1),
(2, 2);

-- Thêm dữ liệu mẫu cho bảng Luot_Follow
INSERT INTO Luot_Follow (User_Id, User_Follow_Id) VALUES
(1, 2),
(2, 3);

-- Thêm dữ liệu mẫu cho bảng Luot_Like_Bai_Viet
INSERT INTO Luot_Like_Bai_Viet (User_Id, Id_Bai_Viet) VALUES
(1, 1),
(2, 2);

-- Thêm dữ liệu mẫu cho bảng Luot_Like_Binh_Luan
INSERT INTO Luot_Like_Binh_Luan (User_Id, Id_Binh_Luan) VALUES
(1, 1),
(2, 2);

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
    Tieu_De NVARCHAR(MAX) NOT NULL,
    Thumbnail NVARCHAR(MAX),
    Noi_Dung NVARCHAR(MAX) NOT NULL,
    Id_Bai_Viet INT IDENTITY(1, 1) NOT NULL,
    Ngay_Tao DATETIME NOT NULL,
    Id_Nhom INT,
    Trang_Thai NVARCHAR(255) NOT NULL, -- "Bài Nháp" để lưu lại bản nháp, "Đã đăng" để hiện lên trang chủ
    User_Id INT NOT NULL,
    PRIMARY KEY (Id_Bai_Viet),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Id_Nhom) REFERENCES Nhom(Id_Nhom)
);
GO


-- Thông báo của người dùng
CREATE TABLE Thong_Bao
(
    Noi_Dung NVARCHAR(MAX) NOT NULL,
    Duong_Dan NVARCHAR(MAX),
    Id_Thong_Bao INT IDENTITY(1, 1) NOT NULL,
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
    PRIMARY KEY (Id_Nhom)
);
GO

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
    Follower_Id INT NOT NULL,
    PRIMARY KEY (User_Id, Follower_Id),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Follower_Id) REFERENCES Users(User_Id)
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

-- Thêm dữ liệu mẫu cho bảng Users
INSERT INTO Users (Gioi_Tinh, Email, Dien_Thoai, Mat_Khau, Ten_Dang_Nhap, Ten_Nguoi_Dung, Ngay_Tao, Vai_Tro) VALUES
(1, 'admin@vietblog.com', '0123456789', 'admin123', 'admin', 'Admin VietBlog', '2024-10-03', 'Admin'),
(0, 'user1@example.com', '0987654321', 'user123', 'user1', N'Nguyễn Văn A', '2024-10-03', 'User'),
(1, 'user2@example.com', '0976543210', 'user456', 'user2', N'Trần Thị B', '2024-10-02', 'User'),
(0, 'user3@example.com', '0965432109', 'user789', 'user3', N'Phạm Văn C', '2024-10-01', 'User');
GO

-- Thêm dữ liệu mẫu cho bảng Bai_Viet
INSERT INTO Bai_Viet (Tieu_De, Thumbnail, Noi_Dung, Ngay_Tao, Trang_Thai, User_Id) VALUES
(N'Bài viết về công nghệ', 'thumbnail1.jpg', N'Nội dung bài viết về công nghệ...', '2024-10-03 10:00:00', N'Đã đăng', 1),
(N'Bài viết về du lịch', 'thumbnail2.jpg', N'Nội dung bài viết về du lịch...', '2024-10-02 14:30:00', N'Đã đăng', 2),
(N'Bài viết về ẩm thực', 'thumbnail3.jpg', N'Nội dung bài viết về ẩm thực...', '2024-10-01 09:15:00', N'Bài Nháp', 3);
GO

-- Thêm dữ liệu mẫu cho bảng Thong_Bao
INSERT INTO Thong_Bao (Noi_Dung, Duong_Dan, User_Id) VALUES
(N'Chào mừng bạn đến với VietBlog!', '/trang-chu', 2),
(N'Bạn có một thông báo mới!', '/thong-bao', 3);
GO

-- Thêm dữ liệu mẫu cho bảng Binh_Luan
INSERT INTO Binh_Luan (Noi_Dung, Id_BL_Cha, Ngay_Tao, Id_Bai_Viet, User_Id, Level_Binh_Luan) VALUES
(N'Bình luận 1', NULL, '2024-10-03 11:00:00', 1, 2, 1), -- Chèn bình luận gốc trước
(N'Bình luận 2', NULL, '2024-10-02 15:00:00', 2, 1, 1); -- Chèn bình luận gốc trước
GO

-- Sau đó mới chèn phản hồi
INSERT INTO Binh_Luan (Noi_Dung, Id_BL_Cha, Ngay_Tao, Id_Bai_Viet, User_Id, Level_Binh_Luan) VALUES
(N'Phản hồi cho bình luận 1', 1, '2024-10-03 11:30:00', 1, 3, 2);
GO

-- Thêm dữ liệu mẫu cho bảng Nhom
INSERT INTO Nhom (Ten, Gioi_Thieu) VALUES
(N'Nhóm Công nghệ', N'Nhóm thảo luận về công nghệ'),
(N'Nhóm Du lịch', N'Nhóm chia sẻ kinh nghiệm du lịch');
GO

-- Thêm dữ liệu mẫu cho bảng Thanh_Vien
INSERT INTO Thanh_Vien (Vai_Tro, Id_Nhom, User_Id) VALUES
('Quản trị viên', 1, 1),
('Thành viên', 1, 2),
('Thành viên', 2, 3);
GO

-- Thêm dữ liệu mẫu cho bảng Luu_Bai_Viet
INSERT INTO Luu_Bai_Viet (User_Id, Id_Bai_Viet) VALUES
(2, 1),
(3, 2);
GO

-- Thêm dữ liệu mẫu cho bảng Luot_Follow
INSERT INTO Luot_Follow (User_Id, Follower_Id) VALUES
(1, 2),
(2, 3);
GO

-- Thêm dữ liệu mẫu cho bảng Luot_Like_Bai_Viet
INSERT INTO Luot_Like_Bai_Viet (User_Id, Id_Bai_Viet) VALUES
(2, 1),
(3, 1);
GO

-- Thêm dữ liệu mẫu cho bảng Luot_Like_Binh_Luan
INSERT INTO Luot_Like_Binh_Luan (User_Id, Id_Binh_Luan) VALUES
(1, 1),
(2, 2);
GO
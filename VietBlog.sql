-- Tạo cơ sở dữ liệu
CREATE DATABASE vietblog;
\c VietBlog

-- Tài khoản người dùng
CREATE TABLE Users
(
    User_Id BIGSERIAL PRIMARY KEY,
    Gioi_Tinh BOOLEAN,
    Email VARCHAR(255) NOT NULL UNIQUE,
    Dien_Thoai VARCHAR(15),
    Mat_Khau TEXT NOT NULL,
    Ten_Dang_Nhap VARCHAR(255) NOT NULL UNIQUE,
    Ten_Nguoi_Dung VARCHAR(255) NOT NULL,
    Hinh_Dai_Dien TEXT,
    Ngay_Tao DATE NOT NULL,
    Vai_Tro VARCHAR(255) NOT NULL,
    Ngay_Sinh DATE
);

-- Tạo bảng Nhom
CREATE TABLE Nhom
(
    Id_Nhom BIGSERIAL PRIMARY KEY,
    Ten VARCHAR(255) NOT NULL,
    Gioi_Thieu TEXT NOT NULL,
    Hinh_Dai_Dien TEXT,
    Ngay_Tao TIMESTAMP NOT NULL
);

-- Luật nhóm
CREATE TABLE DS_Luat_Nhom
(
    Id_Luat BIGSERIAL PRIMARY KEY,
    Noi_Dung TEXT NOT NULL,
    Ten VARCHAR(255) NOT NULL,
    Id_Nhom BIGINT NOT NULL REFERENCES Nhom(Id_Nhom)
);

-- Bài viết
CREATE TABLE Bai_Viet
(
    Id_Bai_Viet BIGSERIAL PRIMARY KEY,
    Tieu_De TEXT,
    Noi_Dung TEXT,
    Ngay_Tao TIMESTAMP NOT NULL,
    Id_Nhom BIGINT REFERENCES Nhom(Id_Nhom),
    Id_Bai_Viet_Chia_Se BIGINT REFERENCES Bai_Viet(Id_Bai_Viet),
    Trang_Thai VARCHAR(255) NOT NULL,
    User_Id BIGINT NOT NULL REFERENCES Users(User_Id)
);

-- Đa phương tiện
CREATE TABLE Da_Phuong_Tien
(
    Id_Phuong_Tien BIGSERIAL PRIMARY KEY,
    Id_Bai_Viet BIGINT NOT NULL REFERENCES Bai_Viet(Id_Bai_Viet),
    Duong_Dan TEXT NOT NULL
);

-- Thông báo
CREATE TABLE Thong_Bao
(
    Id_Thong_Bao BIGSERIAL PRIMARY KEY,
    Noi_Dung TEXT NOT NULL,
    Duong_Dan TEXT,
    Da_Doc BOOLEAN NOT NULL,
    Ngay_Tao TIMESTAMP NOT NULL,
    User_Id BIGINT NOT NULL REFERENCES Users(User_Id)
);

-- Bình luận
CREATE TABLE Binh_Luan
(
    Id_Binh_Luan BIGSERIAL PRIMARY KEY,
    Level_Binh_Luan INT NOT NULL,
    Noi_Dung TEXT NOT NULL,
    Id_BL_Cha BIGINT REFERENCES Binh_Luan(Id_Binh_Luan),
    Ngay_Tao TIMESTAMP NOT NULL,
    Id_Bai_Viet BIGINT NOT NULL REFERENCES Bai_Viet(Id_Bai_Viet),
    User_Id BIGINT NOT NULL REFERENCES Users(User_Id)
);

-- Thành viên nhóm
CREATE TABLE Thanh_Vien
(
    Id_Nhom BIGINT NOT NULL REFERENCES Nhom(Id_Nhom),
    User_Id BIGINT NOT NULL REFERENCES Users(User_Id),
    Vai_Tro VARCHAR(255) NOT NULL,
    Ngay_Tham_Gia DATE,
    PRIMARY KEY (Id_Nhom, User_Id)
);

-- Lưu bài viết
CREATE TABLE Luu_Bai_Viet
(
    User_Id BIGINT NOT NULL REFERENCES Users(User_Id),
    Id_Bai_Viet BIGINT NOT NULL REFERENCES Bai_Viet(Id_Bai_Viet),
    PRIMARY KEY (User_Id, Id_Bai_Viet)
);

-- Follow
CREATE TABLE Luot_Follow
(
    User_Id BIGINT NOT NULL REFERENCES Users(User_Id),
    User_Follow_Id BIGINT NOT NULL REFERENCES Users(User_Id),
    PRIMARY KEY (User_Id, User_Follow_Id)
);

-- Like bài viết
CREATE TABLE Luot_Like_Bai_Viet
(
    User_Id BIGINT NOT NULL REFERENCES Users(User_Id),
    Id_Bai_Viet BIGINT NOT NULL REFERENCES Bai_Viet(Id_Bai_Viet),
    PRIMARY KEY (User_Id, Id_Bai_Viet)
);

-- Like bình luận
CREATE TABLE Luot_Like_Binh_Luan
(
    User_Id BIGINT NOT NULL REFERENCES Users(User_Id),
    Id_Binh_Luan BIGINT NOT NULL REFERENCES Binh_Luan(Id_Binh_Luan),
    PRIMARY KEY (User_Id, Id_Binh_Luan)
);

-- Cập nhật bảng Users
ALTER TABLE Users ADD COLUMN Theme VARCHAR(255);
ALTER TABLE Users ADD COLUMN Font VARCHAR(255);

-- Giới hạn chia sẻ bài viết thành 1 cấp
CREATE FUNCTION check_share_limit() RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT Id_Bai_Viet_Chia_Se FROM Bai_Viet WHERE Id_Bai_Viet = NEW.Id_Bai_Viet_Chia_Se) IS NOT NULL THEN
        RAISE EXCEPTION 'Không thể chia sẻ bài viết đã được chia sẻ.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER TR_BaiViet_ChiaSe
    BEFORE INSERT OR UPDATE ON Bai_Viet
    FOR EACH ROW EXECUTE FUNCTION check_share_limit();
-- Thêm dữ liệu mẫu cho bảng Users
INSERT INTO Users (Gioi_Tinh, Email, Dien_Thoai, Mat_Khau, Ten_Dang_Nhap, Ten_Nguoi_Dung, Ngay_Tao, Vai_Tro) VALUES
                                                                                                                 (TRUE, 'lehoangbao232@gmail.com', '0354356463', 'password123', 'lehoangbao232', 'Lê Hoàng Bảo', '2022-01-01', 'USER'),
                                                                                                                 (TRUE, 'huuthothai467@gmail.com', '0843675245', 'password123', 'huuthothai467', 'Thái Hữu Thọ', '2022-01-01', 'USER'),
                                                                                                                 (TRUE, 'letronghung557@gmail.com', '0934532467', 'password123', 'letronghung557', 'Lê Trọng Hưng', '2022-01-01', 'ADMIN'),
                                                                                                                 (TRUE, 'hoangvunguyen816@gmail.com', '0954459379', 'password123', 'hoangvunguyen816', 'Nguyễn Hoàng Vũ', '2022-01-01', 'USER'),
                                                                                                                 (FALSE, 'linhnguyenthuy504@gmail.com', '0812354789', 'password123', 'linhnguyenthuy504', 'Nguyễn Thùy Linh', '2022-01-01', 'USER');

-- Thêm dữ liệu mẫu cho bảng Nhom
INSERT INTO Nhom (Ten, Gioi_Thieu, Hinh_Dai_Dien, Ngay_Tao) VALUES
                                                                ('Nhóm Công nghệ', 'Nhóm thảo luận về công nghệ', 'hinhmau_nhomcongnghe.jpg', '2023-02-01 10:00:00'),
                                                                ('Nhóm Du lịch', 'Nhóm chia sẻ kinh nghiệm du lịch', 'hinhmau_nhomdulich.jpg', '2023-02-01 10:00:00');

-- Thêm dữ liệu mẫu cho bảng Bai_Viet
INSERT INTO Bai_Viet (Id_Nhom, Tieu_De, Noi_Dung, Ngay_Tao, Trang_Thai, User_Id, Id_Bai_Viet_Chia_Se) VALUES
                                                                                                          (NULL, 'Is it just me or...', 'This scene is so intense...', '2024-10-18 10:00:00', 'ĐÃ ĐĂNG', 1, NULL),
                                                                                                          (NULL, 'Telegram: Mối đe dọa đến quyền riêng tư', 'TLDR: Không an toàn!', '2023-02-01 10:00:00', 'NHÁP', 2, NULL);

-- Thêm dữ liệu mẫu cho bảng Da_Phuong_Tien
INSERT INTO Da_Phuong_Tien (Id_Bai_Viet, Duong_Dan) VALUES
                                                        (5, '/path/to/image1.png'),
                                                        (5, '/path/to/image2.png');

-- Thêm dữ liệu mẫu cho bảng Thong_Bao
INSERT INTO Thong_Bao (Noi_Dung, Duong_Dan, Da_Doc, Ngay_Tao, User_Id) VALUES
                                                                           ('Thông báo số 1', '/path/to/notification1', FALSE, '2023-01-01 10:00:00', 1),
                                                                           ('Thông báo số 2', '/path/to/notification2', TRUE, '2023-02-01 10:00:00', 2);

-- Thêm dữ liệu mẫu cho bảng Binh_Luan
INSERT INTO Binh_Luan (Noi_Dung, Id_BL_Cha, Ngay_Tao, Id_Bai_Viet, User_Id, Level_Binh_Luan) VALUES
                                                                                                 ('Bình luận 1', NULL, '2024-10-03 11:00:00', 1, 2, 1),
                                                                                                 ('Bình luận 2', NULL, '2024-10-02 15:00:00', 2, 1, 1);

-- Chèn phản hồi cho bình luận
INSERT INTO Binh_Luan (Noi_Dung, Id_BL_Cha, Ngay_Tao, Id_Bai_Viet, User_Id, Level_Binh_Luan) VALUES
    ('Phản hồi cho bình luận 1', 1, '2024-10-03 11:30:00', 1, 3, 2);

-- Thêm dữ liệu mẫu cho bảng DS_Luat_Nhom
INSERT INTO DS_Luat_Nhom (Noi_Dung, Ten, Id_Nhom) VALUES
                                                      ('Tất cả mọi người đều muốn một cuộc tranh luận văn minh, vì thế hãy kiềm chế ngôn từ', 'Hãy tranh luận văn minh!', 1),
                                                      ('Hãy đưa ra dẫn chứng cho quan điểm của mình trong khi đối thoại và tranh luận, đừng nói suông, nói phét', 'Nói có sách, mách có chứng', 2);

-- Thêm dữ liệu mẫu cho bảng Thanh_Vien
INSERT INTO Thanh_Vien (Vai_Tro, Id_Nhom, User_Id) VALUES
                                                       ('Quản trị viên', 1, 1),
                                                       ('Thành viên', 1, 2),
                                                       ('Thành viên', 2, 3);

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
    (1, 1);

-- Cập nhật ngày sinh cho Users
UPDATE Users
SET Ngay_Sinh = '2002-10-10';

-- Cập nhật trạng thái bài viết
UPDATE Bai_Viet
SET Trang_Thai = 'ĐÃ ĐĂNG'
WHERE Trang_Thai = 'ĐĂNG';

-- Thêm nhóm mới
INSERT INTO Nhom (Ten, Gioi_Thieu, Hinh_Dai_Dien, Ngay_Tao) VALUES
                                                                ('Nhóm Âm nhạc', 'Nhóm dành cho những người yêu âm nhạc', 'hinhmau_nhomnhac.jpg', NOW()),
                                                                ('Nhóm Thể thao', 'Nhóm thảo luận về các môn thể thao', 'hinhmau_nhomthethao.jpg', NOW()),
                                                                ('Nhóm Đọc sách', 'Nhóm chia sẻ về sách và đam mê đọc sách', 'hinhmau_nhomsach.jpg', NOW()),
                                                                ('Nhóm Phim ảnh', 'Nhóm dành cho những người yêu thích phim ảnh', 'hinhmau_nhomphim.jpg', NOW());

-- Thêm User_Id = 1 vào các nhóm với vai trò "Quản trị viên"
INSERT INTO Thanh_Vien (Vai_Tro, Id_Nhom, User_Id, Ngay_Tham_Gia) VALUES
                                                                      ('Quản trị viên', 3, 1, NOW()),
                                                                      ('Quản trị viên', 4, 1, NOW()),
                                                                      ('Quản trị viên', 5, 1, NOW()),
                                                                      ('Quản trị viên', 6, 1, NOW());

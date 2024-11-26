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
    User_Id BIGINT IDENTITY(1, 1) NOT NULL,
    Hinh_Dai_Dien NVARCHAR(MAX),
    Ngay_Tao DATE NOT NULL,
    Vai_Tro NVARCHAR(255) NOT NULL, -- phân quyền Admin với User thường
    Ngay_Sinh DATE, -- Thuộc tính Ngày Sinh được thêm vào
    PRIMARY KEY (User_Id)
);
GO

-- Tạo bảng Nhom trước Bai_Viet vì Bai_Viet có khóa ngoại đến Nhom
CREATE TABLE Nhom
(
    Id_Nhom BIGINT IDENTITY(1, 1) NOT NULL,
    Ten NVARCHAR(255) NOT NULL,
    Gioi_Thieu NVARCHAR(MAX) NOT NULL,
    Hinh_Dai_Dien NVARCHAR(MAX),
    Ngay_Tao DATETIME NOT NULL,
    PRIMARY KEY (Id_Nhom)
);
GO

-- Luật nhóm
CREATE TABLE DS_Luat_Nhom
(
    Id_Luat BIGINT IDENTITY(1, 1) NOT NULL,
    Noi_Dung NVARCHAR(MAX) NOT NULL,
    Ten NVARCHAR(MAX) NOT NULL,
    Id_Nhom BIGINT NOT NULL,
    PRIMARY KEY (Id_Luat),
    FOREIGN KEY (Id_Nhom) REFERENCES Nhom(Id_Nhom)
);

-- Bài viết của người dùng
CREATE TABLE Bai_Viet
(
    Tieu_De NVARCHAR(MAX),
    Noi_Dung NVARCHAR(MAX),
    Id_Bai_Viet BIGINT IDENTITY(1, 1) NOT NULL,
    Ngay_Tao DATETIME NOT NULL,
    Id_Nhom BIGINT,
    Id_Bai_Viet_Chia_Se BIGINT, -- Id bài viết được chia sẻ
    Trang_Thai NVARCHAR(255) NOT NULL, -- "Bài Nháp" để lưu lại bản nháp, "Đã ĐÃ ĐĂNG" để hiện lên trang chủ
    User_Id BIGINT NOT NULL,
    PRIMARY KEY (Id_Bai_Viet),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Id_Nhom) REFERENCES Nhom(Id_Nhom),
    FOREIGN KEY (Id_Bai_Viet_Chia_Se) REFERENCES Bai_Viet(Id_Bai_Viet),
);
GO

CREATE TABLE Da_Phuong_Tien
(
    Id_Phuong_Tien BIGINT IDENTITY(1, 1) NOT NULL,
    Id_Bai_Viet BIGINT NOT NULL,
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
    Id_Thong_Bao BIGINT IDENTITY(1, 1) NOT NULL,
    Ngay_Tao DATETIME NOT NULL,
    User_Id BIGINT NOT NULL,
    PRIMARY KEY (Id_Thong_Bao),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id)
);
GO

-- Tạo bảng Binh_Luan sau Bai_Viet vì Binh_Luan có khóa ngoại đến Bai_Viet
-- Bình luận và phản hồi bình luận
CREATE TABLE Binh_Luan
(
    Id_Binh_Luan BIGINT IDENTITY(1,1) NOT NULL,
    Level_Binh_Luan INT NOT NULL,
    Noi_Dung NVARCHAR(MAX) NOT NULL,
    Id_BL_Cha BIGINT,
    Ngay_Tao DATETIME NOT NULL,
    Id_Bai_Viet BIGINT NOT NULL,
    User_Id BIGINT NOT NULL,
    PRIMARY KEY (Id_Binh_Luan),
    FOREIGN KEY (Id_Bai_Viet) REFERENCES Bai_Viet(Id_Bai_Viet),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Id_BL_Cha) REFERENCES Binh_Luan(Id_Binh_Luan)
);
GO


CREATE TABLE Thanh_Vien
(
    Vai_Tro NVARCHAR(255) NOT NULL,
    Id_Nhom BIGINT NOT NULL,
    User_Id BIGINT NOT NULL,
    PRIMARY KEY (Id_Nhom, User_Id),
    FOREIGN KEY (Id_Nhom) REFERENCES Nhom(Id_Nhom),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id)
);
GO

CREATE TABLE Luu_Bai_Viet
(
    User_Id BIGINT NOT NULL,
    Id_Bai_Viet BIGINT NOT NULL,
    PRIMARY KEY (User_Id, Id_Bai_Viet),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Id_Bai_Viet) REFERENCES Bai_Viet(Id_Bai_Viet)
);
GO

CREATE TABLE Luot_Follow
(
    User_Id BIGINT NOT NULL,
    User_Follow_Id BIGINT NOT NULL,
    PRIMARY KEY (User_Id, User_Follow_Id),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (User_Follow_Id) REFERENCES Users(User_Id)
);
GO

CREATE TABLE Luot_Like_Bai_Viet
(
    User_Id BIGINT NOT NULL,
    Id_Bai_Viet BIGINT NOT NULL,
    PRIMARY KEY (User_Id, Id_Bai_Viet),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Id_Bai_Viet) REFERENCES Bai_Viet(Id_Bai_Viet)
);
GO

CREATE TABLE Block_User
(
    User_Id BIGINT NOT NULL,
    Block_User_Id BIGINT NOT NULL,
    PRIMARY KEY (User_Id, Block_User_Id),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Block_User_Id) REFERENCES Users(User_Id)
);


GO
CREATE TABLE Luot_Like_Binh_Luan
(
    User_Id BIGINT NOT NULL,
    Id_Binh_Luan BIGINT NOT NULL,
    PRIMARY KEY (User_Id, Id_Binh_Luan),
    FOREIGN KEY (User_Id) REFERENCES Users(User_Id),
    FOREIGN KEY (Id_Binh_Luan) REFERENCES Binh_Luan(Id_Binh_Luan)
);
GO

-- Đặt Unique chống trùng lặp cho tên ĐÃ ĐĂNG nhập của User
ALTER TABLE Users
    ADD CONSTRAINT UQ_Ten_Dang_Nhap UNIQUE (Ten_Dang_Nhap);
GO

-- Đặt Unique chống trùng lặp cho Email của User
ALTER TABLE Users
    ADD CONSTRAINT UQ_Email UNIQUE (Email);
GO

-- Cập nhật các thuộc tính Mau_Nen, Font_Chu, Co_Chu cho bảng Users
ALTER TABLE Users ADD Mau_Nen NVARCHAR(255);
ALTER TABLE Users ADD Font_Chu NVARCHAR(255);
ALTER TABLE Users ADD Co_Chu FLOAT;
GO

UPDATE Users
Set Ngay_Sinh = '2002-10-10'
GO

UPDATE Users
Set Co_Chu = 16
GO

UPDATE Users
Set Font_Chu = 'Helvetica Neue'
GO

UPDATE Users
Set Mau_Nen = N'TRẮNG'
GO

-- Xóa cột cũ
ALTER TABLE Users DROP COLUMN Mau_Nen, Font_Chu, Co_Chu;
GO

-- Cập nhật các thuộc tính Mau_Nen, Font_Chu, Co_Chu cho bảng Users
ALTER TABLE Users ADD Theme NVARCHAR(255);
ALTER TABLE Users ADD Font NVARCHAR(255);
GO

UPDATE Users
Set Ngay_Sinh = '2002-10-10'
GO

UPDATE Users
Set Font = 'Helvetica Neue'
GO

UPDATE Users
Set Theme = N'SANG'
GO

ALTER TABLE Thanh_Vien ADD Ngay_Tham_Gia DATE;
GO

-- Giới hạn chia sẻ bài viết thành 1 cấp (không thể chia sẻ bài chia sẻ của người khác)
CREATE TRIGGER TR_BaiViet_ChiaSe
ON Bai_Viet
FOR INSERT, UPDATE
AS
BEGIN
    IF EXISTS (
        SELECT 1
        FROM inserted i
        JOIN Bai_Viet bv ON i.Id_Bai_Viet_Chia_Se = bv.Id_Bai_Viet
        WHERE bv.Id_Bai_Viet_Chia_Se IS NOT NULL
    )
    BEGIN
        RAISERROR('Bài viết đã được chia sẻ, không thể chia sẻ chuyển tiếp.', 16, 1)
        ROLLBACK TRANSACTION
    END
END
GO

-- Thêm dữ liệu mẫu cho bảng Users
INSERT INTO Users (Gioi_Tinh, Email, Dien_Thoai, Mat_Khau, Ten_Dang_Nhap, Ten_Nguoi_Dung, Ngay_Tao, Vai_Tro) VALUES
                                                                                                                 (1, 'lehoangbao232@gmail.com', '0354356463', 'password123', 'lehoangbao232', N'Lê Hoàng Bảo', '2022-01-01', 'USER'),
                                                                                                                 (1, 'huuthothai467@gmail.com', '0843675245', 'password123', 'huuthothai467', N'Thái Hữu Thọ', '2022-01-01', 'USER'),
                                                                                                                 (1, 'letronghung557@gmail.com', '0934532467', 'password123', 'letronghung557', N'Lê Trọng Hưng', '2022-01-01', 'ADMIN'),
                                                                                                                 (1, 'hoangvunguyen816@gmail.com', '0954459379', 'password123', 'hoangvunguyen816', N'Nguyễn Hoàng Vũ', '2022-01-01', 'USER'),
                                                                                                                 (1, 'hoangtruongminh271@gmail.com', '0365472478', 'password123', 'hoangtruongminh271', N'Trương Minh Hoàng', '2022-01-01', 'USER'),
                                                                                                                 (0, 'linhnguyenthuy504@gmail.com', '0812354789', 'password123', 'linhnguyenthuy504', N'Nguyễn Thùy Linh', '2022-01-01', 'USER'),
                                                                                                                 (0, 'chautranthi344@gmail.com', '0934663426', 'password123', 'chautranthi344', N'Trần Thị Châu', '2022-01-01', 'USER'),
                                                                                                                 (0, 'phuongnguyenchau@gmail.com', '0946725468', 'password123', 'phuongnguyenchau', N'Nguyễn Châu Phương', '2022-01-01', 'USER'),
                                                                                                                 (0, 'trinhkieumyle@gmail.com', '0745568864', 'password123', 'trinhhoangmyle', N'Lê Mỹ Kiều Trinh', '2022-01-01', 'USER'),
                                                                                                                 (0, 'minhnguyenthi@gmail.com', '0724534574', 'password123', 'minhnguyenthi', N'Nguyễn Thị Minh', '2022-01-01', 'USER'),
                                                                                                                 (0, 'nhuquynhngocle@gmail.com', '0378974563', 'password123', 'nhuquynhngocle', N'Lê Ngọc Quỳnh Như', '2022-01-01', 'USER'),
                                                                                                                 (1, 'baothaihoang@gmail.com', '0954857968', 'password123', 'baothaihoang', N'Hoàng Thái Bảo', '2022-01-01', 'USER'),
                                                                                                                 (1, 'cuongngocle@gmail.com', '0967535567', 'password123', 'cuongngocle', N'Lê Ngọc Cường', '2022-01-01', 'USER'),
                                                                                                                 (1, 'phucminhhoang@gmail.com', '0957765350', 'password123', 'phucminhhoang', N'Hoàng Minh Phúc', '2022-01-01', 'USER'),
                                                                                                                 (0, 'anhnhatlethi@gmail.com', '0356379707', 'password123', 'anhnhatlethi', N'Lê Thị Nhật Ánh', '2022-01-01', 'ADMIN'),
                                                                                                                 (1, 'nguyenanhquan@gmail.com', '0367567870', 'password123', 'nguyenanhquan', N'Nguyễn Anh Quân', '2022-01-01', 'ADMIN'),
                                                                                                                 (1, 'nguyenthaiquoc@gmail.com', '0946456697', 'password123', 'nguyenthaiquoc', N'Nguyễn Thái Quốc', '2022-02-01', 'ADMIN'),
                                                                                                                 (1, 'nguyenanhqui@gmail.com', '0354675454', 'password123', 'nguyenanhqui', N'Nguyễn Anh Qui', '2022-03-01', 'ADMIN');

-- Thêm dữ liệu mẫu cho bảng Nhom
INSERT INTO Nhom (Ten, Gioi_Thieu, Hinh_Dai_Dien, Ngay_Tao) VALUES
                                                                (N'Nhóm Công nghệ', N'Nhóm thảo luận về công nghệ', 'hinhmau_nhomcongnghe.jpg', '2023-02-01 10:00:00'),
                                                                (N'Nhóm Du lịch', N'Nhóm chia sẻ kinh nghiệm du lịch', 'hinhmau_nhomdulich.jpg', '2023-02-01 10:00:00');
GO

-- Thêm dữ liệu mẫu cho bảng Bai_Viet
INSERT INTO Bai_Viet (Id_Nhom ,Tieu_De, Noi_Dung, Ngay_Tao, Trang_Thai, User_Id, Id_Bai_Viet_Chia_Se) VALUES
(NULL,'Is it just me or...', 'This scene right here is so fucking cold bro is laughing on an island full of enemy and enemy captains
are standing right there.....if this mf came to hachinosu in his prime he would have saved coby plus captured/killed black beard pirate
''s captains....this mf was laughing his whole fight, scared the captains, injured everyone, destroyed town and said "not enough" and he
is fking 70+ of age ....bro is the definition of "HIM"Is it just me or anyone else also want luffy to get angry when he hear about garp
captured by BB and in war with BB saves garp', '2024-10-18 10:00:00', N'ĐÃ ĐĂNG', 1, NULL),
(NULL, N'Telegram: Mối đe dọa đến quyền riêng tư', N'Ê biết app nào nhắn tin bảo mật, an toàn không mậy? Xài telegram đi, tin tao, đảm bảo không bốc phét.
Thiệt không vậy cha? TLDR (Dài quá không đọc): Điều khiến Telegram trở nên khác biệt (và là lý do chính khiến nó trở nên phổ biến)
là các Channel (kênh) và những kênh này (và các cuộc trò chuyện nhóm) không an toàn để sử dụng. Nếu không có mã hóa đầu cuối, Telegram
(hoặc bất kỳ ai có thể gây sức ép với Telegram hoặc truy cập vào hệ thống của Telegram) đều có thể toàn bộ nội dung được ĐÃ ĐĂNG trong các
cuộc trò chuyện như vậy. Và vì tất cả người dùng Telegram đều được xác định bằng số điện thoại thực của họ (có thể ẩn khỏi những người
dùng khác trên các kênh nhưng lại hiển thị với Telegram), nên họ có thể dễ dàng bị xác định danh tính.
Tham khảo**:** https://protonvpn.com/blog/is-telegram-safe', '2023-02-01 10:00:00', N'NHÁP', 2, NULL),
(NULL, N'Muốn có tình yêu... đẹp giống như hồi đi học', N'Mình đi làm khá lâu rồi mặc dù mới ra trường 1 năm,
nhưng dạo này lại hay buồn tủi nên cũng muốn có bồ quá ạ. Nhiều lúc thấy vã lắm rồi nhưng không chấp nhận việc yêu vội hay yêu bừa được.
Mình nhận không ít thiệp cưới của mấy cặp yêu đương từ thời áo trắng quần tây đến khi lên lễ đường. Có case bên trai dev lương nghìn đô
còn bên gái bán bánh mỳ, vẫn ok tốt đẹp. Cũng có case bên gái làm CEO bên trai chạy Grab mà case nào cũng thấy họ hạnh phúc ghê.
Mình cũng thèm được như vậy quá ạ. Riêng mình thì hồi xưa hèn quá với cắm đầu vô công việc nhiều từ thời mới là sinh viên nên bỏ
lỡ khá nhiều cơ hội tốt. Thỉnh thoảng vô stalk người mình thích hồi 18 mà cứ ôm cái điện thoại buồn buồn mếu máo. Ở cty cũ mình làm TL
nên cũng ít thời gian rảnh, thỉnh thoảng có mấy em tester xinh tươi qua chửi nên hồi đó k nghĩ nhiều về yêu đương. Bây giờ mình chuyển
qua cty khác, làm mảng khác nhưng làm culi thì thời gian rảnh nhiều nên cũng dễ cảm thấy cô đơn hơn. Không có stress về công việc nhưng
nhiều khi buồn vì cô đơn quá cũng phát ốm. Trong năm nay mình cũng dạn lên đi date tầm 4 em quanh Hà Nội, quen trên app dating hoặc mấy
group linh tinh. Nhưng lúc gặp thì lại tụt mood xong chả nói được câu nào nên tạch luôn. Mà kiểu cũng không thấy có cảm xúc gì. Nhớ hồi
nhỏ thấy mấy tụi có bồ chỉ cần có điểm chung gì với nửa kia rồi quan tâm nhau chút là auto đổ. Nhưng mình già rồi nên trước khi date cũng
toàn bị đối phương soi lý lịch như tôn giáo, mindset, nghề nghiệp, tài chính... đủ thứ trên đời coi mình có đủ tốt với họ không :( .
Mình pass được mớ tiêu chí đó thì đến lúc gặp người ta mình cứ auto tụt mood nên sau đó cũng không còn có sau đó. Bằng tuổi mình có người
vẫn chung thủy với tình đầu lâu năm của họ, có người lăng nhăng thay bồ như thay áo. Chỉ còn mỗi mình cứ cô đơn lăn lộn 1 mình giữa lòng
thủ đô chỉ để tìm hình bóng của 1 người mà mình có thể yêu thương vô điều kiện :( :(', '2024-10-18 10:00:00', N'ĐÃ ĐĂNG', 1, NULL);
GO

-- Thêm dữ liệu mẫu cho bảng Da_Phuong_Tien
INSERT INTO Da_Phuong_Tien (Id_Bai_Viet, Duong_Dan) VALUES
(1, '/path/to/image1.png'),
(1, '/path/to/image2.png');

-- Thêm dữ liệu mẫu cho bảng Thong_Bao
INSERT INTO Thong_Bao (Noi_Dung, Duong_Dan, Da_Doc, Ngay_Tao, User_Id) VALUES
                                                                           ('Thong bao so 1', '/path/to/notification1', 0, '2023-01-01 10:00:00', 1),
                                                                           ('Thong bao so 2', '/path/to/notification2', 1, '2023-02-01 10:00:00', 2);

-- Thêm dữ liệu mẫu cho bảng Binh_Luan
INSERT INTO Binh_Luan (Noi_Dung, Id_BL_Cha, Ngay_Tao, Id_Bai_Viet, User_Id, Level_Binh_Luan) VALUES
                                                                                                 (N'Bình luận 1', NULL, '2024-10-03 11:00:00', 1, 2, 1), -- Chèn bình luận gốc trước
                                                                                                 (N'Bình luận 2', NULL, '2024-10-02 15:00:00', 2, 1, 1); -- Chèn bình luận gốc trước
GO

-- Sau đó mới chèn phản hồi
INSERT INTO Binh_Luan (Noi_Dung, Id_BL_Cha, Ngay_Tao, Id_Bai_Viet, User_Id, Level_Binh_Luan) VALUES
(N'Phản hồi cho bình luận 1', 1, '2024-10-03 11:30:00', 1, 3, 2);
GO

-- Thêm dữ liệu mẫu cho bảng DS_Luat_Nhom
INSERT INTO DS_Luat_Nhom (Noi_Dung, Ten, Id_Nhom) VALUES
(N'Tất cả mọi người đều muốn một cuộc tranh luận văn minh, vì thế hãy kiềm chế ngôn từ', N'Hãy tranh luận văn minh!', 1),
(N'Hãy đưa ra dẫn chứng cho quan điểm của mình trong khi đối thoại và tranh luận, đừng nói suông, nói phét', N'Nói có sách, mách có chứng', 2);

-- Thêm dữ liệu mẫu cho bảng Thanh_Vien
INSERT INTO Thanh_Vien (Vai_Tro, Id_Nhom, User_Id) VALUES
                                                       (N'Quản trị viên', 1, 1),
                                                       (N'Thành viên', 1, 2),
                                                       (N'Thành viên', 2, 3);
GO

-- Thêm dữ liệu mẫu cho bảng Luu_Bai_Viet
INSERT INTO Luu_Bai_Viet (User_Id, Id_Bai_Viet) VALUES
(1, 1),
(2, 2);

-- Thêm dữ liệu mẫu cho bảng Luot_Follow
INSERT INTO Luot_Follow (User_Id, User_Follow_Id) VALUES
                                                      (1, 2),
                                                      (2, 3);
GO

-- Thêm dữ liệu mẫu cho bảng Luot_Like_Bai_Viet
INSERT INTO Luot_Like_Bai_Viet (User_Id, Id_Bai_Viet) VALUES
(1, 1),
(2, 2);

-- Thêm dữ liệu mẫu cho bảng Luot_Like_Binh_Luan
INSERT INTO Luot_Like_Binh_Luan (User_Id, Id_Binh_Luan) VALUES
                                                            (1, 1),
                                                            (2, 2);
GO

-- DƯỚI ĐÂY LÀ CÁC CÂU LỆNH CẬP NHẬT DATABASE CẦN BẮT BUỘC PHẢI CHẠY CHO DỰ ÁN VIETBLOG
USE VietBlog
GO
UPDATE Users
Set Ngay_Sinh = '2002-10-10'
GO

UPDATE Bai_Viet
SET Trang_Thai = N'ĐÃ ĐĂNG'
WHERE Trang_Thai = N'ĐĂNG'
GO

-- Thêm mới để sẵn các nhóm
-- Thêm nhóm Âm nhạc
INSERT INTO Nhom (Ten, Gioi_Thieu, Hinh_Dai_Dien, Ngay_Tao) VALUES
(N'Nhóm Âm nhạc', N'Nhóm dành cho những người yêu âm nhạc', 'hinhmau_nhomnhac.jpg', GETDATE()),
(N'Nhóm Thể thao', N'Nhóm thảo luận về các môn thể thao', 'hinhmau_nhomthethao.jpg', GETDATE()),
(N'Nhóm Đọc sách', N'Nhóm chia sẻ về sách và đam mê đọc sách', 'hinhmau_nhomsach.jpg', GETDATE()),
(N'Nhóm Phim ảnh', N'Nhóm dành cho những người yêu thích phim ảnh', 'hinhmau_nhomphim.jpg', GETDATE()),
(N'Nhóm Game', N'Nhóm thảo luận về các trò chơi', 'hinhmau_nhomgame.jpg', GETDATE()),
(N'Nhóm Học tập', N'Nhóm hỗ trợ học tập và chia sẻ kiến thức', 'hinhmau_nhomhoctap.jpg', GETDATE());
GO

-- Khai báo tất cả các biến ở đây
DECLARE @NhomAmNhacId BIGINT;
DECLARE @NhomTheThaoId BIGINT;
DECLARE @NhomDocSachId BIGINT;
DECLARE @NhomPhimAnhId BIGINT;
DECLARE @NhomGameId BIGINT;
DECLARE @NhomHocTapId BIGINT;

-- Lấy danh sách Id_Nhom của các nhóm vừa tạo
SET @NhomAmNhacId = (SELECT Id_Nhom FROM Nhom WHERE Ten = N'Nhóm Âm nhạc');
SET @NhomTheThaoId = (SELECT Id_Nhom FROM Nhom WHERE Ten = N'Nhóm Thể thao');
SET @NhomDocSachId = (SELECT Id_Nhom FROM Nhom WHERE Ten = N'Nhóm Đọc sách');
SET @NhomPhimAnhId = (SELECT Id_Nhom FROM Nhom WHERE Ten = N'Nhóm Phim ảnh');
SET @NhomGameId = (SELECT Id_Nhom FROM Nhom WHERE Ten = N'Nhóm Game');
SET @NhomHocTapId = (SELECT Id_Nhom FROM Nhom WHERE Ten = N'Nhóm Học tập');

-- Thêm User_Id = 1 vào các nhóm với vai trò "Quản trị viên"
INSERT INTO Thanh_Vien (Vai_Tro, Id_Nhom, User_Id, Ngay_Tham_Gia) VALUES
(N'Quản trị viên', 3, 1, GETDATE()),
(N'Quản trị viên', 4, 1, GETDATE()),
(N'Quản trị viên', 5, 1, GETDATE()),
(N'Quản trị viên', 6, 1, GETDATE()),
(N'Quản trị viên', 7, 1, GETDATE()),
(N'Quản trị viên', 8, 1, GETDATE());
GO

--Dùng để xóa nhóm
-- Thêm ON DELETE CASCADE vào khóa ngoại trong bảng DS_Luat_Nhom
ALTER TABLE DS_Luat_Nhom
DROP CONSTRAINT FK__DS_Luat_N__Id_Nh__3B75D760;
GO

ALTER TABLE DS_Luat_Nhom
ADD CONSTRAINT FK_DS_Luat_Nhom_Nhom 
FOREIGN KEY (Id_Nhom) REFERENCES Nhom(Id_Nhom) ON DELETE CASCADE; 


-- Thêm ON DELETE CASCADE vào khóa ngoại trong bảng Bai_Viet
ALTER TABLE Bai_Viet
DROP CONSTRAINT FK__Bai_Viet__Id_Nho__3F466844;

ALTER TABLE Bai_Viet
ADD CONSTRAINT FK_Bai_Viet_Nhom
FOREIGN KEY (Id_Nhom) REFERENCES Nhom(Id_Nhom) ON DELETE CASCADE;


-- Thêm ON DELETE CASCADE vào khóa ngoại trong bảng Thanh_Vien
ALTER TABLE Thanh_Vien
DROP CONSTRAINT FK__Thanh_Vie__Id_Nh__4D94879B;

ALTER TABLE Thanh_Vien
ADD CONSTRAINT FK_Thanh_Vien_Nhom 
FOREIGN KEY (Id_Nhom) REFERENCES Nhom(Id_Nhom) ON DELETE CASCADE;



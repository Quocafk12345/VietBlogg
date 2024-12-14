let host_BaiViet = "http://localhost:8080/api/bai-viet";

mainApp.controller("BaiVietController", function ($scope, $http, $q, timeService, BaiVietService, $sce, $timeout) {  // Inject $q

    $scope.bangTin = [];
    $scope.dangTheoDoi = [];
    $scope.chiTietBaiViet = {};
    $scope.danhSachLuu = [];
    $scope.baiVietNhap = [];

    $scope.baiViet = {};
    $scope.DSdaPhuongTien = [];

    $scope.loaiBaiDang = "caNhan";
    $scope.chinhSuaBaiViet = false;

    $scope.mucDuocChon = {ten: 'Chọn nhóm'};
    $scope.thongTinUser = [{ten: currentUser.tenNguoiDung, hinhDaiDien: currentUser.hinhDaiDien}];

    $scope.tinhThoiGianDang = timeService.tinhThoiGianDang; // Gán hàm từ service
    $scope.baiVietNguoiDung = []; // Dữ liệu bài viết của người dùng


    // lấy bài viết của người dùng, load vào trang cá nhân
    $scope.layBaiVietCuaUser = function (userId) {
        var url = `${host_BaiViet}/user/${userId}`;
        $http.get(url)
            .then(resp => {
                $scope.xuLyThongTinBaiViet(resp.data)
                    .then(baiViet => {
                        $scope.baiVietCaNhan = baiViet;
                    });
            })
            .catch((error) => {
                console.error("Error", error);
            });
    };

    // tải tất cả bài viết đã đăng
    $scope.taiBaiViet = function () {
        var url = `${host_BaiViet}`;
        $http.get(url)
            .then((resp) => {
                // Lọc ra các bài viết có trạng thái là "DA_DANG"
                var baiVietDaDang = resp.data.filter(baiViet => baiViet.trangThai === "DA_DANG");
                $scope.xuLyThongTinBaiViet(baiVietDaDang)
                    .then(baiViet => {
                        $scope.bangTin = baiViet;
                        console.log($scope.bangTin);
                    });
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };

    // code xử lý bài viết
    $scope.xuLyThongTinBaiViet = function (baiViet) {
        var promises = [];
        angular.forEach(baiViet, function (baiVietObject) {
            promises.push($scope.demLuotLike(baiVietObject.id));
            promises.push($scope.demLuotBinhLuan(baiVietObject.id));
            promises.push($scope.kiemTraLike(baiVietObject)
                .then(function (daLike) {
                    baiVietObject.daLike = daLike; // Sửa baiViet thành baiVietObject
                }));
            promises.push($scope.kiemTraLuuBaiViet(baiVietObject)
                .then(function (daLuu) {
                    baiVietObject.daLuu = daLuu; // Sửa baiViet thành baiVietObject
                }));
        });
        return $q.all(promises).then(function (results) {
            for (var i = 0; i < baiViet.length; i++) {
                baiViet[i].luotLike = results[i * 4];
                baiViet[i].luotBinhLuan = results[i * 4 + 1];
                baiViet[i].thoiGianDang = $scope.tinhThoiGianDang(baiViet[i].ngayTao);
                baiViet[i].noiDung = $sce.trustAsHtml(baiViet[i].noiDung);
            }
            return baiViet; // Trả về mảng bài viết đã xử lý
        });
    };


    // kiểm tra bài có được like
    $scope.kiemTraLike = function (baiViet) {
        if (!baiViet || !baiViet.id) {
            console.error("Bài viết không hợp lệ:", baiViet);
            return;
        }

        var url = `${host_BaiViet}/thich/kiem-tra?idBaiViet=${baiViet.id}&userId=${currentUser.id}`; // Tạo URL với query string

        return $http.get(url) // Gọi API với URL đã tạo
            .then(function (response) {
                return response.data; // Trả về giá trị boolean trực tiếp
            });
    }

    // chuyển đổi like và hủy like
    $scope.toggleLike = function (baiViet) {
        if (!baiViet || !baiViet.id) {
            console.error("Bài viết không hợp lệ:", baiViet);
            return;
        }
        var url = `${host_BaiViet}/thich`;

        var duLieu_luotLike = new FormData();
        duLieu_luotLike.append('idBaiViet', baiViet.id);
        duLieu_luotLike.append('userId', currentUser.id);

        $http.post(url, duLieu_luotLike, {
            transformRequest: angular.identity, // Không serialize dữ liệu
            headers: {'Content-Type': undefined} // Để browser tự set Content-Type
        })
            .then(function (response) {
                var daLike = response.data; // Lấy giá trị thật
                baiViet.daLike = daLike;
                baiViet.luotLike += daLike ? 1 : -1;
                if (baiViet.daLike) {
                    console.log("Like thành công");
                } else {
                    console.log("Hủy like thành công");
                }
            })
            .catch((error) => {
                console.error("Lỗi khi toggle like:", error);
            });
    };

    // đếm số lượt like
    $scope.demLuotLike = function (idBaiViet) {
        var url = `${host_BaiViet}/${idBaiViet}/luot-like`;
        return $http
            .get(url)
            .then((resp) => {
                return resp.data;
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };

    $scope.demLuotBinhLuan = function (idBaiViet) {
        var url = `${host_BaiViet}/${idBaiViet}/luot-binh-luan`;
        return $http
            .get(url)
            .then((resp) => {
                return resp.data;
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };


    // kiểm tra người đăng nhập có phải chủ bài viết không
    $scope.kiemTraChuBaiViet = function (baiViet) {
        return baiViet.user.id === currentUser.id;
    };

    $scope.xoaBaiViet = function (baiVietId) {
        $http
            .delete(`${host_BaiViet}/${baiVietId}`)
            .then((resp) => {
                $scope.bangTin = $scope.bangTin.filter(baiViet => baiViet.id !== baiVietId);
                alert("Xóa bài viết thành công!");
                console.log("Xóa thành công", resp.data);
            })
            .catch((error) => {
                alert("Xóa bài viết thất bại!");
                console.log("Xóa thất bại, có lỗi xảy ra", error);
            });
    };


    $scope.toggleLuuBaiViet = function (baiViet) { // Nhận baiVietId làm tham số
        var url = `${host_BaiViet}/luu-bai`; // Sử dụng endpoint /luu-bai

        var duLieu_LuuBai = new FormData();
        duLieu_LuuBai.append('idBaiViet', baiViet.id);
        duLieu_LuuBai.append('userId', currentUser.id);

        $http.post(url, duLieu_LuuBai, {
            transformRequest: angular.identity, // Không serialize dữ liệu
            headers: {'Content-Type': undefined} // Để browser tự set Content-Type
        })
            .then((resp) => {
                var daLuu = resp.data; // Lấy giá trị thật
                $scope.taiDSLuu();
                console.log("Lưu thành công", resp.data);
                baiViet.daLuu = daLuu;
                if (baiViet.daLuu) {
                    console.log("Lưu thành công");
                } else {
                    console.log("Hủy lưu thành công");
                }
            })
            .catch((error) => {
                alert("Lưu bài viết thất bại!");
                console.log("Lưu thất bại, có lỗi xảy ra", error);
            });
    };

    $scope.taiDSLuu = function () {
        $http
            .get(`${host_BaiViet}/luu/${currentUser.id}`)
            .then((resp) => {
                $scope.danhSachLuu = resp.data;
            })
            .catch((error) => {
                console.log("Có lỗi xảy ra", error);
            });
    };

    $scope.kiemTraLuuBaiViet = function (baiViet) { // Nhận baiVietId làm tham số
        if (!baiViet || !baiViet.id) {
            console.error("Bài viết không hợp lệ:", baiViet);
            return;
        }

        var url = `${host_BaiViet}/luu-bai/kiem-tra?idBaiViet=${baiViet.id}&userId=${currentUser.id}`; // Tạo URL với query string

        return $http.get(url) // Gọi API với URL đã tạo
            .then(function (response) {
                return response.data; // Trả về giá trị boolean trực tiếp
            });
    };




    // chuyển sang trang Chi tiết bài viết
    $scope.chuyenTrang = function ($event, baiVietId, duongDan) {
        console.log(baiVietId);
        $event.preventDefault();
        $event.target.href = duongDan + baiVietId;
        window.location.href = $event.target.href;
    };

    // tải bài viết trong Chi tiết bài viết
    $scope.taiBaiVietDuocChon = function (baiVietId) {
        var url = `${host_BaiViet}/${baiVietId}`;
        $http
            .get(url)
            .then((resp) => {
                $scope.chiTietBaiViet = resp.data;

                // Lấy lượt like và lượt bình luận cho bài viết đơn lẻ
                $scope.demLuotLike($scope.chiTietBaiViet.id)
                    .then(function (luotLike) {
                        $scope.chiTietBaiViet.luotLike = luotLike;
                    });

                $scope.demLuotBinhLuan($scope.chiTietBaiViet.id)
                    .then(function (luotBinhLuan) {
                        $scope.chiTietBaiViet.luotBinhLuan = luotBinhLuan;
                    });

                $scope.kiemTraLike($scope.chiTietBaiViet)
                    .then(function (ketQua) {
                        $scope.chiTietBaiViet.daLike = ketQua;
                    });

                $scope.kiemTraLuuBaiViet($scope.chiTietBaiViet)
                    .then(function (ketQua) {
                        $scope.chiTietBaiViet.daLuu = ketQua;
                    });

                $scope.chiTietBaiViet.thoiGianDang = $scope.tinhThoiGianDang($scope.chiTietBaiViet.ngayTao);
                $scope.chiTietBaiViet.noiDung = $sce.trustAsHtml(DOMPurify.sanitize($scope.chiTietBaiViet.noiDung, {
                    ALLOWED_TAGS: ['b', 'i', 'u', 'p', 'br', 'span', 'div', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'ol', 'ul', 'li', 'a'],
                    ALLOWED_ATTR: ['href', 'style']
                }));
                console.log($scope.chiTietBaiViet);
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };


    // đăng bài kèm hình ảnh
    $scope.xuLyDangBai = function (trangThai) {
        var url = `${host_BaiViet}/dang-bai`;
        // Tạo object bài viết mới

        var baiViet = {
            tieuDe: document.getElementById('tieuDe').value, // Lấy tiêu đề từ input
            noiDung: CKEDITOR.instances.editor1.getData(), // Lấy nội dung từ textarea
            user: {id: currentUser.id}, // Lấy thông tin user từ biến currentUser
            nhom: null,
            trangThai: trangThai
        };

        // Nếu có nhóm được chọn, thêm thông tin nhóm vào bài viết
        if ($scope.loaiBaiDang === "nhom") {
            baiViet.nhom = {id: $scope.mucDuocChon.id}; // Chỉ gửi ID của nhóm
        } else baiViet.nhom = null;

        // Gọi API để thêm bài viết mới
        $http.post(url, baiViet)
            .then(function (response) {
                var idBaiVietMoi = response.data.id; // Lấy ID của bài viết vừa tạo

                // Xử lý upload hình ảnh/video sau khi đăng bài thành công
                var uploadPromises = [];
                $scope.DSdaPhuongTien.forEach(function (media) {
                    var formData = new FormData();
                    formData.append('DSfile', media.file);
                    formData.append('DSMoTa', media.moTa);

                    // Gọi API upload đa phương tiện
                    uploadPromises.push($http.post(`${host_BaiViet}/da-phuong-tien/dang-tai/${idBaiVietMoi}`, formData, {
                        transformRequest: angular.identity,
                        headers: {'Content-Type': undefined}
                    }));
                });

                // Chờ tất cả các upload hoàn thành
                $q.all(uploadPromises)
                    .then(function () {
                        console.log('Upload tất cả đa phương tiện thành công');

                        // Xử lý khi thêm bài viết thành công
                        console.log('Thêm bài viết thành công:', response.data);

                        // Cập nhật danh sách bài viết
                        $scope.bangTin.unshift(response.data);

                        // Reset form
                        document.getElementById('tieuDe').value = '';
                        CKEDITOR.instances.editor1.setData('');
                        document.getElementById('imageUpload').value = '';
                        $scope.nhomDuocChon = {ten: 'Chọn nhóm'};

                        // Giải phóng objectURL sau khi upload
                        $scope.DSdaPhuongTien.forEach(function (media) {
                            URL.revokeObjectURL(media.preview);
                        });
                        $scope.DSdaPhuongTien = []; // Xóa mảng sau khi upload

                    })
                    .catch(function (error) {
                        console.error('Lỗi khi upload đa phương tiện:', error);

                        // Xử lý lỗi upload, ví dụ: hiển thị thông báo lỗi, xóa bài viết vừa tạo
                    });
            })
            .catch(function (error) {
                // Xử lý khi thêm bài viết thất bại
                console.error('Lỗi khi thêm bài viết:', error);
            });
    };

    $scope.chonNoiDangBai = function (mucDuocChon) {
        $scope.mucDuocChon = mucDuocChon;
        if (mucDuocChon.ten === currentUser.tenNguoiDung) { // Kiểm tra nếu chọn "Trang cá nhân"
            $scope.loaiBaiDang = "caNhan";
        } else {
            $scope.loaiBaiDang = "nhom";
        }
        console.log($scope.mucDuocChon);
    };

    $scope.dangBai = function () {
        $scope.xuLyDangBai("DA_DANG"); // Gọi hàm chung với trangThai = "DA_DANG"
        alert("Đăng bài thành công");
    }

    // tải tất cả bài viết nháp
    $scope.taiBaiVietNhap = function () {
        var url = `${host_BaiViet}/${currentUser.id}/nhap`;
        $http.get(url)
            .then((resp) => {
                // Lọc ra các bài viết có trạng thái là "NHAP"
                var baiVietNhap = resp.data.filter(baiViet => baiViet.trangThai === "NHAP");
                $scope.xuLyThongTinBaiViet(baiVietNhap)
                    .then(baiViet => {
                        $scope.baiVietNhap = baiViet;
                    });
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };

    // đăng bài kèm hình ảnh
    $scope.luuNhap = function () {
        $scope.xuLyDangBai("NHAP"); // Gọi hàm chung với trangThai = "DA_DANG"
        alert("Lưu nháp thành công");
    };

    // Hàm upload đa phương tiện
    $scope.uploadDaPhuongTien = function (baiVietId, file, moTa) {
        var url = `${host_BaiViet}/da-phuong-tien/dang-tai/${baiVietId}`; // API trong DaPhuongTienController

        var formData = new FormData();
        formData.append('file', file);
        formData.append('moTa', moTa); // Thêm moTa vào FormData

        return $http.post(url, formData, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        });
    }

    $scope.xemTruocHinhAnh = function (input) {
        if (input.files && input.files.length > 0) {
            // Giới hạn số lượng file tối đa là 100
            const fileHienCo = $scope.DSdaPhuongTien.length; // Số lượng file hiện có
            const fileMoi = input.files.length; // Số lượng file mới
            const gioiHanFile = 100 - fileHienCo; // Số lượng file tối đa được phép thêm
            const fileDangXuLy = Array.from(input.files).slice(0, gioiHanFile); // Lấy số lượng file cho phép

            // Sử dụng $q.all để xử lý bất đồng bộ (có thể bỏ nếu không cần thiết)
            fileDangXuLy.forEach(function (file) {
                // Tạo objectURL và thêm vào DSdaPhuongTien
                var objectUrl = URL.createObjectURL(file);
                // Xác định loại file
                var loaiFile = file.type.startsWith('image/') ? 'HINH_ANH' :
                    file.type.startsWith('video/') ? 'VIDEO' :
                        'KHAC';
                $scope.DSdaPhuongTien.push({
                    file: file,
                    moTa: null,
                    preview: objectUrl,
                    loai: loaiFile // Thêm thuộc tính loai
                });
                $timeout(function () {
                }); // Thêm dòng này để buộc cập nhật giao diện
            });
        }
        console.log($scope.DSdaPhuongTien);
    };




    //Bài Viết nhóm trong CHITIETNHOm
    $scope.loadBaiVietNhom = function () {
        // Lấy danh sách bài viết của nhóm từ API
        var url = `${host_Nhom}/${idNhom}/bai-viet`;
        $http.get(url)
            .then((resp) => {
                // Lọc ra các bài viết có trạng thái là "DA_DANG"
                var baiVietDaDang = resp.data.filter(baiViet => baiViet.trangThai === "DA_DANG");
                $scope.xuLyThongTinBaiViet(baiVietDaDang)
                    .then(baiViet => {
                        $scope.baiVietNhom = baiViet;
                    });
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };

    $scope.$on('loadBaiVietNhom', function () {
        $scope.loadBaiVietNhom();
    });

    $scope.taiBaiViet();
    $scope.taiDSLuu();
    $scope.taiBaiVietNhap();

    // Kiểm tra xem có baiVietId được truyền từ Thymeleaf không
    if (typeof baiVietId_chiTietBaiViet !== 'undefined') {
        $scope.taiBaiVietDuocChon(baiVietId_chiTietBaiViet);
    }

    if (typeof baiVietId_chinhSua !== 'undefined' && baiVietId_chinhSua !== null) {
        $scope.chinhSuaBaiViet = true; // Đang chỉnh sửa
    } else {
        $scope.chinhSuaBaiViet = false; // Đang tạo mới
    }

    // phần xử lý thông tin đẩy lên form
    if ($scope.chinhSuaBaiViet) { // Nếu đang chỉnh sửa
        $http.get(`${host_BaiViet}/${baiVietId_chinhSua}`)
            .then(function (response) {
                $scope.baiViet = response.data;
                console.log($scope.baiViet);

                if (response.data.nhom === null) {
                    $scope.chonNoiDangBai($scope.thongTinUser[0]);
                } else $scope.chonNoiDangBai($scope.baiViet.nhom);

                // Hiển thị tiêu đề và nội dung
                $timeout(function () {
                    document.getElementById('tieuDe').value = $scope.baiViet.tieuDe;
                    CKEDITOR.instances.editor1.setData($scope.baiViet.noiDung);
                });

                // ... (Hiển thị các thông tin khác của bài viết và đa phương tiện)
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy bài viết:", error);
            });

        // Lấy danh sách đa phương tiện
        $http.get(`${host_BaiViet}/da-phuong-tien/${baiVietId_chinhSua}/chinh-sua`) // Gọi API lấy danh sách đa phương tiện
            .then(function (response) {
                $scope.DSdaPhuongTien = response.data;
                console.log($scope.DSdaPhuongTien);

                // Tạo objectURL cho mỗi đa phương tiện
                $scope.DSdaPhuongTien.forEach(function (media) {
                    media.preview = URL.createObjectURL(media.file); // Giả sử media.file là File object

                });

                // ... (Hiển thị các thông tin khác của bài viết)
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy danh sách đa phương tiện:", error);
            });
    }

});


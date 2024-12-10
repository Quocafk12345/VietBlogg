let host_BaiViet = "http://localhost:8080/api/bai-viet";

mainApp.controller("BaiVietController", function ($scope, $http, $q, timeService, $sce, $timeout) {  // Inject $q
    $scope.bangTin = [];
    $scope.dangTheoDoi = [];
    $scope.chiTietBaiViet = {};

    $scope.DSdaPhuongTien = []; // Khai báo danh sách DSdaPhuongTien
    $scope.loaiBaiDang = "caNhan";

    $scope.mucDuocChon = {ten: 'Chọn nhóm'};
    $scope.thongTinUser = [
        {ten: currentUser.tenNguoiDung, hinhDaiDien: 'https://via.placeholder.com/20x20'}
    ];

    $scope.chonNoiDangBai = function (mucDuocChon) {
        $scope.mucDuocChon = mucDuocChon;
        if (mucDuocChon.ten === currentUser.tenNguoiDung) { // Kiểm tra nếu chọn "Trang cá nhân"
            $scope.loaiBaiDang = "caNhan";
        } else {
            $scope.loaiBaiDang = "nhom";
        }
    };

    $scope.tinhThoiGianDang = timeService.tinhThoiGianDang; // Gán hàm từ service
    $scope.baiVietNguoiDung = []; // Dữ liệu bài viết của người dùng

    $scope.layBaiVietCuaUser = function (userId) {
        var url = `${host_BaiViet}/user/${userId}`;
        $http.get(url)
            .then(resp => {
                $scope.baiVietCaNhan = resp.data;
                var promises = [];
                angular.forEach($scope.baiVietCaNhan, function (baiViet) {
                    // Sử dụng baiViet.id thay cho baiVietId
                    promises.push($scope.demLuotLike(baiViet.id));
                    promises.push($scope.demLuotBinhLuan(baiViet.id));
                });
                $q.all(promises).then(function (result) {
                    for (var i = 0; i < $scope.baiVietCaNhan.length; i++) {
                        $scope.baiVietCaNhan[i].luotLike = result[i * 2];
                        $scope.baiVietCaNhan[i].luotBinhLuan = result[i * 2 + 1];
                        $scope.baiVietCaNhan[i].thoiGianDang = $scope.tinhThoiGianDang($scope.baiVietCaNhan[i].ngayTao);
                        $scope.baiVietCaNhan[i].daLike = $scope.kiemTraLike($scope.baiVietCaNhan[i]);
                    }
                });
            })
            .catch((error) => {
                console.error("Error", error);
            });
    };

    $scope.taiBaiViet = function () {
        var url = `${host_BaiViet}`;
        $http
            .get(url)
            .then((resp) => {
                $scope.bangTin = resp.data;
                var promises = [];
                angular.forEach($scope.bangTin, function (baiViet) {
                    promises.push($scope.demLuotLike(baiViet.id));
                    promises.push($scope.demLuotBinhLuan(baiViet.id)); // Thêm promise cho lượt bình luận
                    promises.push($scope.kiemTraLike(baiViet)
                        .then(function (daLike) {
                            baiViet.daLike = daLike;
                        }));
                });
                $q.all(promises).then(function (results) {
                    for (var i = 0; i < $scope.bangTin.length; i++) {
                        $scope.bangTin[i].luotLike = results[i * 3]; // Kết quả lượt like ở vị trí i * 2
                        $scope.bangTin[i].luotBinhLuan = results[i * 3 + 1]; // Kết quả lượt bình luận ở vị trí i * 2 + 1
                        $scope.bangTin[i].thoiGianDang = $scope.tinhThoiGianDang($scope.bangTin[i].ngayTao);
                    }
                });
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };

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
                    console.log(baiViet);
                } else {
                    console.log("Hủy like thành công");
                }
            })
            .catch((error) => {
                console.error("Lỗi khi toggle like:", error);
            });
    };

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

    $scope.chuyenTrang = function ($event, baiVietId) {
        console.log(baiVietId);
        $event.preventDefault();
        $event.target.href = '/bai-viet/' + baiVietId;
        window.location.href = $event.target.href;
    };

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

    $scope.dangBai = function () {
        var url = `${host_BaiViet}/dang-bai`;
        // Tạo object bài viết mới

        var baiViet = {
            tieuDe: document.getElementById('tieuDe').value, // Lấy tiêu đề từ input
            noiDung: CKEDITOR.instances.editor1.getData(), // Lấy nội dung từ textarea
            user: {id: currentUser.id}, // Lấy thông tin user từ biến currentUser
            nhom: null
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

                    console.log(formData);

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
                        alert('Thêm bài viết thành công');

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

    //Bài Viết nhóm trong CHITIETNHOm
    $scope.loadBaiVietNhom = function () {
        // Lấy danh sách bài viết của nhóm từ API
        $http.get(`${host_Nhom}/${idNhom}/bai-viet`)
            .then(resp => {
                $scope.baiVietNhom = resp.data;

                // Xử lý lượt like, lượt bình luận, thời gian đăng cho mỗi bài viết
                var promises = [];
                angular.forEach($scope.baiVietNhom, function (baiViet) {
                    promises.push($scope.demLuotLike(baiViet.id));
                    promises.push($scope.demLuotBinhLuan(baiViet.id));
                });
                $q.all(promises).then(function (results) {
                    for (var i = 0; i < $scope.baiVietNhom.length; i++) {
                        $scope.baiVietNhom[i].luotLike = results[i * 2];
                        $scope.baiVietNhom[i].luotBinhLuan = results[i * 2 + 1];
                        $scope.baiVietNhom[i].thoiGianDang = $scope.tinhThoiGianDang($scope.baiVietNhom[i].ngayTao);
                        $scope.baiVietNhom[i].daLike = $scope.kiemTraLike($scope.baiVietNhom[i]);

                    }
                });
            })
            .catch(error => {
                console.error("Lỗi khi lấy bài viết của nhóm:", error);
            });
    };

    $scope.previewFiles = function (input) {
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
                $scope.DSdaPhuongTien.push({
                    file: file,
                    moTa: '',
                    preview: objectUrl
                });
                $timeout(function () {
                }); // Thêm dòng này để buộc cập nhật giao diện
            });
        }
        console.log($scope.DSdaPhuongTien);
    };

    $scope.taiBaiViet();

    $scope.$on('loadBaiVietNhom', function () {
        $scope.loadBaiVietNhom();
    });

    // Kiểm tra xem có baiVietId được truyền từ Thymeleaf không
    if (typeof baiVietId !== 'undefined') {
        $scope.taiBaiVietDuocChon(baiVietId);
    }

});


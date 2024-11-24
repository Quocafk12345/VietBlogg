let host_BaiViet = "http://localhost:8080/api/bai-viet";
mainApp.controller("BaiVietController", function ($scope, $http, $q, timeService, $sce) {  // Inject $q
    $scope.bangTin = [];
    $scope.dangTheoDoi = [];
    $scope.chiTietBaiViet = {};

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

    // $scope.daLike = false;
    // $scope.userId = currentUserId;
    $scope.tinhThoiGianDang = timeService.tinhThoiGianDang; // Gán hàm từ service
    $scope.baiVietNguoiDung = []; // Dữ liệu bài viết của người dùng

    $scope.getBaiVietByUserId = function (userId) {
        var url = `${host_BaiViet}/user/${userId}`;
        $http.get(url)
            .then(resp => {
                $scope.baiVietNguoiDung = resp.data;
                var promises = [];
                angular.forEach($scope.baiVietNguoiDung, function (baiViet) {
                    // Sử dụng baiViet.id thay cho baiVietId
                    promises.push($scope.demLuotLike(baiViet.id));
                    promises.push($scope.demLuotBinhLuan(baiViet.id));
                });
                $q.all(promises).then(function (result) {
                    for (var i = 0; i < $scope.baiVietNguoiDung.length; i++) {
                        $scope.baiVietNguoiDung[i].luotLike = result[i * 2];
                        $scope.baiVietNguoiDung[i].luotBinhLuan = result[i * 2 + 1];
                        $scope.baiVietNguoiDung[i].thoiGianDang = $scope.tinhThoiGianDang($scope.baiVietNguoiDung[i].ngayTao);
                    }
                });
            })
            .catch((error) => {
                console.error("Error", error);
            });
    };


    $scope.load_bai_viet = function () {
        var url = `${host_BaiViet}`;
        $http
            .get(url)
            .then((resp) => {
                $scope.bangTin = resp.data;
                var promises = [];
                angular.forEach($scope.bangTin, function (baiViet) {
                    promises.push($scope.demLuotLike(baiViet.id));
                    promises.push($scope.demLuotBinhLuan(baiViet.id)); // Thêm promise cho lượt bình luận
                });
                $q.all(promises).then(function (results) {
                    for (var i = 0; i < $scope.bangTin.length; i++) {
                        $scope.bangTin[i].luotLike = results[i * 2]; // Kết quả lượt like ở vị trí i * 2
                        $scope.bangTin[i].luotBinhLuan = results[i * 2 + 1]; // Kết quả lượt bình luận ở vị trí i * 2 + 1
                        $scope.bangTin[i].thoiGianDang = $scope.tinhThoiGianDang($scope.bangTin[i].ngayTao);
                        // $scope.bangTin[i].daLike = results[i*3+1];
                    }
                });
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };


    $scope.toggleLike = function (baiViet) {
        if (!baiViet || !baiViet.id) {
            console.error("Bài viết không hợp lệ:", baiViet);
            return;
        }
        $scope.toggleSuccess = function () {
            $scope.daLike = !$scope.daLike;
        };
        var url = `${host_BaiViet}/${baiViet.id}/like?userId=${currentUserId}`;
        $http.post(url)
            .then((resp) => {
                console.log("Like thành công:", resp.data);
                baiViet.daLike = !baiViet.daLike; // Đổi trạng thái like
                baiViet.luotLike += baiViet.daLike ? -1 : 1; // Cập nhật lượt like
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

    $scope.chuyenTrang = function($event, baiVietId) {
        console.log(baiVietId);
        $event.preventDefault();
        $event.target.href = '/bai-viet/' + baiVietId;
        window.location.href = $event.target.href;
    };

    $scope.loadBaiVietDuocChon = function (baiVietId) {
        var url = `${host_BaiViet}/${baiVietId}`;
        $http
            .get(url)
            .then((resp) => {
                $scope.chiTietBaiViet = resp.data;

                // Lấy lượt like và lượt bình luận cho bài viết đơn lẻ
                $scope.demLuotLike($scope.chiTietBaiViet.id)
                    .then(function(luotLike) {
                        $scope.chiTietBaiViet.luotLike = luotLike;
                    });

                $scope.demLuotBinhLuan($scope.chiTietBaiViet.id)
                    .then(function(luotBinhLuan) {
                        $scope.chiTietBaiViet.luotBinhLuan = luotBinhLuan;
                    });
                $scope.chiTietBaiViet.thoiGianDang = $scope.tinhThoiGianDang($scope.chiTietBaiViet.ngayTao);
                $scope.chiTietBaiViet.noiDung = $sce.trustAsHtml(DOMPurify.sanitize($scope.chiTietBaiViet.noiDung, {
                    ALLOWED_TAGS: ['b', 'i', 'u', 'p', 'br', 'span', 'div', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'ol', 'ul', 'li', 'a'],
                    ALLOWED_ATTR: ['href', 'style']
                }));
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };
    //
    // $scope.sanitizeNoiDungBaiViet = function (baiVietId) {
    //
    // }

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
            baiViet.nhom = $scope.mucDuocChon;
        } else baiViet.nhom = null;

        // Gọi API để thêm bài viết mới
        $http.post(url, baiViet)
            .then(function (response) {
                // Xử lý khi thêm bài viết thành công
                console.log('Thêm bài viết thành công:', response.data);

                // Cập nhật danh sách bài viết
                $scope.bangTin.unshift(response.data);

                // Reset form
                document.getElementById('tieuDe').value = '';
                CKEDITOR.instances.editor1 = '';
                $scope.nhomDuocChon = {ten: 'Chọn nhóm'};

            })
            .catch(function (error) {
                // Xử lý khi thêm bài viết thất bại
                console.error('Lỗi khi thêm bài viết:', error);
            });
    };

    $scope.getBaiVietByUserId($scope.userId);
    $scope.load_bai_viet();

    // Kiểm tra xem có baiVietId được truyền từ Thymeleaf không
    if (typeof baiVietId !== 'undefined') {
        $scope.loadBaiVietDuocChon(baiVietId);
    }

    //Bài Vit nhóm trong CHITIETNHOm
    $scope.loadBaiVietNhom = function() {
        // Lấy danh sách bài viết của nhóm từ API
        $http.get(`${host_Nhom}/${$rootScope.idNhom}/baiviet`)
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
                    }
                });
            })
            .catch(error => {
                console.error("Lỗi khi lấy bài viết của nhóm:", error);
            });
    };

    $scope.$on('loadBaiVietNhom', function() {
        $scope.loadBaiVietNhom();
    });

});
let host_BaiViet = "http://localhost:8080/api/bai-viet";
mainApp.controller("BaiVietController", function ($scope, $http, $q, timeService) {  // Inject $q
    $scope.bangTin = [];
    $scope.dangTheoDoi = [];
    $scope.chiTietBaiViet = {};

    $scope.tinhThoiGianDang = timeService.tinhThoiGianDang; // Gán hàm từ service

    $scope.load_bai_viet = function () {
        var url = `${host_BaiViet}`;
        $http
            .get(url)
            .then((resp) => {
                $scope.bangTin = resp.data;
                var promises = [];
                angular.forEach($scope.bangTin, function (baiViet) {
                    promises.push($scope.getLuotLike(baiViet.id));
                    promises.push($scope.getLuotBinhLuan(baiViet.id)); // Thêm promise cho lượt bình luận
                });
                $q.all(promises).then(function (results) {
                    for (var i = 0; i < $scope.bangTin.length; i++) {
                        $scope.bangTin[i].luotLike = results[i * 2]; // Kết quả lượt like ở vị trí i * 2
                        $scope.bangTin[i].luotBinhLuan = results[i * 2 + 1]; // Kết quả lượt bình luận ở vị trí i * 2 + 1
                        $scope.bangTin[i].thoiGianDang = $scope.tinhThoiGianDang($scope.bangTin[i].ngayTao);
                    }
                });
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };

    $scope.getLuotLike = function (idBaiViet) {
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

    $scope.getLuotBinhLuan = function (idBaiViet) {
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
        $event.target.href = '/chi-tiet-bai-viet/' + baiVietId;
        window.location.href = $event.target.href;
    };

    $scope.loadBaiVietDuocChon = function (baiVietId) {
        var url = `${host_BaiViet}/${baiVietId}`;
        $http
            .get(url)
            .then((resp) => {
                $scope.chiTietBaiViet = resp.data;

                // Lấy lượt like và lượt bình luận cho bài viết đơn lẻ
                $scope.getLuotLike($scope.chiTietBaiViet.id)
                    .then(function(luotLike) {
                        $scope.chiTietBaiViet.luotLike = luotLike;
                    });

                $scope.getLuotBinhLuan($scope.chiTietBaiViet.id)
                    .then(function(luotBinhLuan) {
                        $scope.chiTietBaiViet.luotBinhLuan = luotBinhLuan;
                    });
                $scope.chiTietBaiViet.thoiGianDang = $scope.tinhThoiGianDang($scope.chiTietBaiViet.ngayTao);
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };

    $scope.load_bai_viet();

    // Kiểm tra xem có baiVietId được truyền từ Thymeleaf không
    if (typeof baiVietId !== 'undefined') {
        $scope.loadBaiVietDuocChon(baiVietId);
    }
});
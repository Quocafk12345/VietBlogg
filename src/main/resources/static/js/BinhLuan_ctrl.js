let host_BinhLuan = "http://localhost:8080/api/binh-luan";

mainApp.controller('BinhLuanController', function($scope, $http, timeService) {
    $scope.danhSachBinhLuan = [];

    $scope.tinhThoiGianDang = timeService.tinhThoiGianDang; // Gán hàm từ service

    $scope.layBinhLuan = function(baiVietId) {
        $http.get(`${host_BinhLuan}/${baiVietId}/binh-luan-goc`)
            .then(resp => {
                $scope.danhSachBinhLuan = resp.data;
                console.log(resp.data);
                angular.forEach($scope.danhSachBinhLuan, function(binhLuan) {
                    binhLuan.thoiGianDang = $scope.tinhThoiGianDang(binhLuan.ngayTao);
                });
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };



    $scope.layBinhLuan(baiVietId);
});

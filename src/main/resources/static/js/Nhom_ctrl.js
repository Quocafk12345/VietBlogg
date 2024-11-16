let host_Nhom = "http://localhost:8080/api/nhom";

mainApp.controller("nhomController", function ($scope, $http) {
    $scope.DSnhom = [];

    $scope.mucDuocChon = {ten: 'Chọn nhóm'};

    $scope.chonNoiDangBai = function (item) {
        $scope.mucDuocChon = item;
        console.log($scope.mucDuocChon);
    };

    $scope.loadNhom = function () {

        $http.get(`${host_Nhom}/user/${currentUser.id}`)
            .then(resp => {
                $scope.DSnhom = resp.data;
            })
            .catch(error => {
                console.log("Error", error);
            });
    }

    $scope.layThongTinNhom = function (idNhom) {
        $http.get('/api/nhom/' + idNhom)
            .then(function (response) {
                $scope.nhomDuocChon = response.data;
            });
    };

    $scope.layThongTinNhom_SoThanhVien = function (idNhom) {
        $http.get('/api/nhom/' + idNhom + '/thanh-vien/so-luong')
            .then(function (response) {
                $scope.nhomDuocChon.soLuongThanhVien = response.data;
            });
    };

    $scope.loadNhom();
});
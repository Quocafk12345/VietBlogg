let host_Nhom = "http://localhost:8080/api/nhom";

mainApp.controller("nhomController", function ($scope, $http) {
    $scope.DSnhom = [];

    $scope.nhomDuocChon = {};

    $scope.loadNhom = function () {

        $http.get(`${host_Nhom}/user/${currentUserId}`)
            .then(resp => {
                $scope.DSnhom = resp.data;
            })
            .catch(error => {
                console.log("Error", error);
            });
    }

    $scope.hienThiNhomDuocChon = function (idNhom) {
        $http.get('/api/nhom/' + idNhom)
            .then(function(response) {
                $scope.nhomDuocChon = response.data;
            });

        $http.get('/api/nhom/' + idNhom + '/thanh-vien/so-luong')
            .then(function(response) {
                $scope.nhomDuocChon.soLuongThanhVien = response.data;
            });
    }

    $scope.loadNhom();
});
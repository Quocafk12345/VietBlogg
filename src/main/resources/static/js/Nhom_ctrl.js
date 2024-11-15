let host_Nhom = "http://localhost:8080/api/nhom";

mainApp.controller("nhomController", function ($scope, $http, $window) {
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

    // $scope.hienThiNhomDuocChon = function (idNhom) {
    //     $http.get('/api/nhom/' + idNhom)
    //         .then(function(response) {
    //             $scope.nhomDuocChon = response.data;
    //         });
    //
    //     $http.get('/api/nhom/' + idNhom + '/thanh-vien/so-luong')
    //         .then(function(response) {
    //             $scope.nhomDuocChon.soLuongThanhVien = response.data;
    //         });
    // }

    $scope.loadNhom();

    $scope.chuyenTrang = function($event, idNhom) {
        console.log(idNhom);
        $event.preventDefault();
        $event.target.href = '/nhom/chi-tiet/' + idNhom;
        window.location.href = $event.target.href;
    };

    $scope.hienThiNhomDuocChon = function (idNhom) {
        $http.get(`${host_Nhom}/${idNhom}`)
            .then(function(response) {
                $scope.nhomDuocChon = response.data;
                // Chuyển hướng đến trang chi tiết nhóm
            })
            .catch(error => {
                console.log("Error", error);
            });
    }

    if (typeof idNhom !== 'undefined') {
        $scope.hienThiNhomDuocChon(idNhom);
    }

    // Hàm tạo nhóm
    $scope.taoNhom = function() {
        // Tạo object nhom
        var nhom = {
            ten: document.getElementById("tenNhom").value, // Lấy giá trị từ input
            gioiThieu: document.getElementById("moTa").value  // Lấy giá trị từ textarea
        };
        // Sử dụng currentUser từ Thymeleaf
        if (currentUser) {
            nhom.nguoiTao = { id: currentUser.id }; // Thêm userId vào object nguoiTao
        } else {
            alert("Vui lòng đăng nhập để tạo nhóm.");
            return;
        }

        // Tạo FormData và thêm object nhom vào
        var formData = new FormData();
        formData.append('nhom', JSON.stringify(nhom)); // Chuyển object nhom thành JSON string
        // Thêm userId vào formData
        formData.append('userId', currentUser.id);
        // Kiểm tra file ảnh
        var hinhAnhInput = document.getElementById('hinhAnh');
        if (hinhAnhInput.files && hinhAnhInput.files[0]) {
            formData.append('hinhAnh', hinhAnhInput.files[0]);
        } else {
            alert("Vui lòng chọn ảnh đại diện cho nhóm.");
            return;
        }

        $http.post(`${host_Nhom}/tao-nhom`, formData, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function(response) {
            if (response.status === 201) {
                alert('Tạo nhóm thành công!');
                window.location.href = '/nhom/chi-tiet/' + response.data.id;
            } else {
                alert('Lỗi tạo nhóm.');
            }
        }).catch(function(error) {
            console.error('Lỗi:', error);
            alert('Đã có lỗi xảy ra.');
        });
    };

});


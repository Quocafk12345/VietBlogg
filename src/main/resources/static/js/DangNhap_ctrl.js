let host_DangNhap = "http://localhost:8080";
mainApp.controller('loginController', function ($scope, $http, $window) {

    $scope.login = function() {

        // Tạo FormData để gửi dữ liệu dạng form-urlencoded
        var duLieu_dangNhap = new FormData();
        duLieu_dangNhap.append('identifiers', $scope.identifiers);
        duLieu_dangNhap.append('password', $scope.password);

        $http.post(`${host_DangNhap}/api/user/dang-nhap`, duLieu_dangNhap, {
            transformRequest: angular.identity, // Không serialize dữ liệu
            headers: {'Content-Type': undefined} // Để browser tự set Content-Type
        }).then(function successCallback(response) {
            var user = response.data; // Lấy User từ response

            $http.post(`${host_DangNhap}/dang-nhap-thanh-cong`, user)
                .then(function () {
                    $window.location.href = `${host_DangNhap}/index`; // Chuyển hướng đến /index
            });
        }).catch((error) => {
            console.log("Error", error);
        });
    };

    $scope.logout = function () {
        $http({
            method: 'POST',
            url: `${host_DangNhap}/api/user/dang-xuat`,
            responseType: 'text' // Chỉ định responseType là 'text' nếu máy chủ trả về chuỗi
        }).then(function(response) {
            $window.location.href = `${host_DangNhap}/dang-nhap`; // Redirect đến /logout
            console.log(response.data); // xử lý phản hồi ở đây
            // Thêm xử lý điều hướng nếu cần
        }, function(error) {
            console.error("Lỗi khi đăng xuất:", error);
        });
    };

    $scope.showPassword = false;

    $scope.togglePassword = function() {
        $scope.showPassword = !$scope.showPassword;
        if ($scope.showPassword) {
            document.getElementById("password").setAttribute("type", "text");
        } else {
            document.getElementById("password").setAttribute("type", "password");
        }
    };
});
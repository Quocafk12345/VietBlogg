let host_DangNhap = "http://localhost:8080";
var appDangNhap = angular.module('loginApp', []);
appDangNhap.controller('loginController', function ($scope, $http, $window) {

    $scope.login = function() {

        // Tạo FormData để gửi dữ liệu dạng form-urlencoded
        var formData = new FormData();
        formData.append('identifiers', $scope.identifiers);
        formData.append('password', $scope.password);

        $http.post(`${host_DangNhap}/api/user/dang-nhap`, formData, {
            transformRequest: angular.identity, // Không serialize dữ liệu
            headers: {'Content-Type': undefined} // Để browser tự set Content-Type
        }).then(function successCallback(response) {
            var user = response.data; // Lấy User từ response

            $http.post(`${host_DangNhap}/login-success`, user).then(function () {
                $window.location.href = `${host_DangNhap}/index`; // Chuyển hướng đến /index
            });
        }).catch((error) => {
            console.log("Error", error);
        });
    };

    $scope.logout = function () {
        $http.post(`${host_DangNhap}/api/user/dang-xuat`)
            .then(function successCallback(response) {
                console.log(response); // In ra thông báo đăng xuất thành công
                $window.location.href = `${host_DangNhap}/logout`; // Redirect đến /logout
            }).catch((error) => {
                console.log("Error", error); // Xử lý lỗi nếu có
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
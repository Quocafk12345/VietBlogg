let host = "http://localhost:8080";
var app = angular.module('loginApp', []);
app.controller('loginController', function($scope, $http, $window) {
    $scope.login = function() {

        // Tạo FormData để gửi dữ liệu dạng form-urlencoded
        var formData = new FormData();
        formData.append('identifiers', $scope.identifiers);
        formData.append('password', $scope.password);

        $http.post('http://localhost:8080/api/user/dang-nhap', formData, {
            transformRequest: angular.identity, // Không serialize dữ liệu
            headers: {'Content-Type': undefined} // Để browser tự set Content-Type
        }).then(function successCallback(response) {
            var user = response.data; // Lấy User từ response

            $http.post('http://localhost:8080/login-success', user).then(function() {
                $window.location.href = 'http://localhost:8080/index'; // Chuyển hướng đến /index
            });
        }).catch((error) => {
            console.log("Error", error);
        });
    };
});
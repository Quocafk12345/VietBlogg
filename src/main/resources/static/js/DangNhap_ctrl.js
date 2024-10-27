let host =
angular.module('loginApp', [])
    .controller('LoginController', function($scope, $http) {
        $scope.login = function() {
            $scope.errorMessage = null; // Xóa thông báo lỗi cũ

            $http.post('/api/user/login', {
                identifier: $scope.identifier,
                password: $scope.password
            })
                .then(function(response) {
                    // Xử lý đăng nhập thành công (ví dụ: chuyển hướng)
                    window.location.href = '/profilepage?userId=' + response.data.id;
                })
                .catch(function(error) {
                    // Hiển thị thông báo lỗi
                    $scope.errorMessage = error.data;
                });
        };
    });
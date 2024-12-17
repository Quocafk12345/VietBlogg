// angular.module('adminController', [])
//
//     .controller('AdminController', ['$scope', function($scope) {
//         // Xử lý logic cho trang Admin (nếu cần)
//     }])
//
//     .controller('QuanLyNguoiDungController', ['$scope', '$http', function($scope, $http) {
//         $http.get("http://localhost:8080/api/user/get-all")
//             .then(function(response) {
//                 $scope.users = response.data;
//             });
//     }])
//
//     .controller('QuanLyBaiVietController', ['$scope', '$http', function($scope, $http) {
//         $http.get("http://localhost:8080/api/bai-viet/get-all")
//             .then(function(response) {
//                 $scope.baiViets = response.data;
//             });
//     }]);
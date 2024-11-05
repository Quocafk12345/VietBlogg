let host = "http://localhost:8080/api";
const app = angular.module("app", []);

app.controller("nhomController", function ($scope, $http) {
    $scope.nhoms = [];

    $scope.loadNhom = function () {
        // Giả sử bạn có thể lấy userId từ session hoặc từ model Thymeleaf
        let userId = /* Lấy userId từ session hoặc model */

        $http.get(`${host}/nhom/user/${userId}`)
            .then(resp => {
                $scope.nhoms = resp.data;
            })
            .catch(error => {
                console.log("Error", error);
            });
    }
});
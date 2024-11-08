let host_Nhom = "http://localhost:8080/api/nhom";

mainApp.controller("nhomController", function ($scope, $http) {
    $scope.DSnhom = [];

    $scope.loadNhom = function () {

        $http.get(`${host_Nhom}/user/${currentUserId}`)
            .then(resp => {
                $scope.DSnhom = resp.data;
            })
            .catch(error => {
                console.log("Error", error);
            });
    }

    $scope.loadNhom();
});
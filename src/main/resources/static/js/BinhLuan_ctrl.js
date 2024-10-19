let host = "http://localhost:8080/api/bai-viet";
const app = angular.module("app", []);

$scope.get_luot_binh_luan = function (idBaiViet) {
    var url = `${host}/${idBaiViet}/binh-luan/luot-binh-luan`;
    return $http
        .get(url)
        .then((resp) => {
            return resp.data;
        })
        .catch((error) => {
            console.log("Error", error);
        });
};
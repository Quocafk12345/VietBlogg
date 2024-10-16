let host = "http://localhost:8080/api/bai-viet";
const app = angular.module("app", []);
app.controller("ctrl", function ($scope, $http, $q) {  // Inject $q
    $scope.baiChinh = [];
    $scope.baiPhu = [];
    $scope.baiSoiNoi = [];

    $scope.load_bai_viet = function () {
        var url = `${host}`;
        $http
            .get(url)
            .then((resp) => {
                $scope.baiChinh = resp.data;
                var promises = [];
                angular.forEach($scope.baiChinh, function (baiViet) {
                    promises.push($scope.get_luot_like(baiViet.id));
                    promises.push($scope.get_luot_binh_luan(baiViet.id)); // Thêm promise cho lượt bình luận
                });
                $q.all(promises).then(function (results) {
                    for (var i = 0; i < $scope.baiChinh.length; i++) {
                        $scope.baiChinh[i].luotLike = results[i * 2]; // Kết quả lượt like ở vị trí i * 2
                        $scope.baiChinh[i].luotBinhLuan = results[i * 2 + 1]; // Kết quả lượt bình luận ở vị trí i * 2 + 1
                    }
                });
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };

    $scope.get_luot_like = function (idBaiViet) {
        var url = `${host}/${idBaiViet}/luot-like`;
        return $http
            .get(url)
            .then((resp) => {
                return resp.data;
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };

    $scope.get_luot_binh_luan = function (idBaiViet) {
        var url = `${host}/${idBaiViet}/luot-binh-luan`;
        return $http
            .get(url)
            .then((resp) => {
                return resp.data;
            })
            .catch((error) => {
                console.log("Error", error);
            });
    };

    $scope.load_bai_viet();
});
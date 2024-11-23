let host_Follow = "https://localhost:8080/api/user";
mainApp.controller("UserController", function ($scope, $http, $q, timeService, $window) {
    $scope.isFollowing = false;
    $scope.userProfile={};
    $scope.currentUserId = currentUserId;

    // Cập nhật lại logic toggleFollow
    $scope.toggleFollow = function (userIdToFollow){
        if(!userIdToFollow){
            console.error("Không hợp lệ:", userIdToFollow);
            return;
        }

        // Lấy userId của người đang đăng nhập từ session và userId của người được theo dõi
        var userFollowerId = $scope.currentUserId; // Người dùng đang đăng nhập
        var url = `${host_Follow}/${userIdToFollow}/follow?currentUserId=${userFollowerId}`;

        $http.post(url)
            .then((resp) => {
                console.log("Follow successfully", resp.data);
                $scope.dangTheoDoi[userIdToFollow] = !$scope.dangTheoDoi[userIdToFollow]; // Cập nhật trạng thái follow
            })
            .catch((error) => {
                console.error("Error:", error);
            });
    };

    // Kiểm tra trạng thái theo dõi
    $scope.kiemTraDangTheoDoi = function (userIdToCheck) {
        var url = `${host_Follow}/${userIdToCheck}/follow?currentUserId=${$scope.currentUserId}`;
        $http.get(url)
            .then((resp) => {
                $scope.dangTheoDoi[userIdToCheck] = resp.data;
            })
            .catch((error) => {
                console.error("Error:", error);
            });
    };

    $scope.loadTrangCaNhan = function (userId) {
        $scope.getBaiVietByUserId(userId);
        $scope.kiemTraDangTheoDoi(userId);
    }
})
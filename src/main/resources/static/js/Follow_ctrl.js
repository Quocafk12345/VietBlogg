let host_Follow = "http://localhost:8080/api/user";
mainApp.controller("UserController", function ($scope, $http) {
    $scope.isFollowing = false;
    $scope.isBlock = false;
    $scope.userProfile={};
    const url = window.location.href;
    const userId = url.split("/").pop(); // Lấy phần cuối URL , người dùng cần follow
    const userFollowId = currentUser;// Ngươi dùng đăng nhập
    let socket = new WebSocket("ws://localhost:8080/ws/follow-status");
    const blockUserId = currentUser;// Ngươi dùng đăng nhập

    socket.onmessage = function (event) {
        const data = JSON.parse(event.data);
        if (data.userId === parseInt(userId)) {
            $scope.isFollowing = data.isFollowing;
            $scope.$apply();
        }
    }
    // Kiểm tra nếu là trang cá nhân
    $scope.isOwnProfile = currentUser === userFollowId;


    // Lấy trạng thái follow ban đầu
    const checkFollowStatus = function() {
        if (!userId) {
            console.error("Không hợp lệ:", userId);
            return;
        }

        var checkUrl = `${host_Follow}/${userId}/checkFollowStatus?userFollowId=${userFollowId}`;

        $http.get(checkUrl)
            .then((resp) => {
                $scope.isFollowing = resp.data.isFollowing;  // Lưu trạng thái follow
                console.log("Follow status:", $scope.isFollowing);
            })
            .catch((error) => {
                console.error("Error checking follow status:", error);
            });
    };

    const checkBlockStatus = function () {
        if (!userId) {
            console.log("Không hợp lệ", userId);
            return;
        }
        var checkUrl = `${host_Follow}/${userId}/checkBlockStatus?blockUserId=${blockUserId}`;
        $http.get(checkUrl)
        .then((resp) => {
            $scope.isBlocking = resp.data.isBlocking;
            console.log("Block status:", $scope.isBlocking);
        })
        .catch((error) => {
            console.error("Error checking block status:", error);
        });
    };
    // Gọi hàm kiểm tra trạng thái follow khi trang được tải
    checkFollowStatus();
    checkBlockStatus();
    // Logic toggle follow
    $scope.toggleFollow = function() {
        if (!userId) {
            console.error("Không hợp lệ:", userId);
            return;
        }

        var url = `${host_Follow}/${userId}/toggleFollow?userFollowId=${userFollowId}`;

        $http.post(url)
            .then((resp) => {
                // Ngay sau khi thay đổi trạng thái, cập nhật isFollowing
                $scope.isFollowing = !$scope.isFollowing;  // Đảo ngược trạng thái follow
                $scope.totalFollows += $scope.isFollowing?-1:1;
                console.log("Follow status toggled:", $scope.isFollowing);
            })
            .catch((error) => {
                console.error("Error:", error);
            });
    };

    $scope.toggleBlock = function() {
        if (!userId) {
            console.error("Không hợp lệ:", userId);
            return;
        }
        var url = `${host_Follow}/${userId}/toggleBlock?blockUserId=${blockUserId}`;
        $http.post(url)
        .then((resp) => {
            $scope.isBlock = !$scope.isBlock;
            $scope.totalFollows += $scope.isFollowing?-1:1;
            console.log("Block status:", $scope.isBlock);
        })
        .catch((error) => {
            console.error("Error:", error);
        });
    };

    $scope.handleAction = function (action) {
        if(action === "unfollow") {
            $scope.toggleFollow();
        }else if (action === "toggleBlock") {
             $scope.toggleBlock();
        }
    }

})
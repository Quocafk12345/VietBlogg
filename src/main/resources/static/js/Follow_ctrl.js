let host_Follow = "http://localhost:8080/api/user";

mainApp.controller("UserController", function ($scope, $http) {
    $scope.isFollowing = false;
    $scope.isBlock = false;

    const url = window.location.href;
    const userId = url.split("/").pop(); // Lấy phần cuối URL , người dùng cần follow
    const userFollowId = currentUser; // Người dùng đăng nhập
    let socket = new WebSocket("ws://localhost:8080/ws/follow-status");
    $scope.baiVietNguoiDung = {};
    socket.onmessage = function (event) {
        const data = JSON.parse(event.data);
        if (data.userId === parseInt(userId)) {
            $scope.isFollowing = data.isFollowing;
            $scope.$apply();
        }
    }

    // Kiểm tra nếu là trang cá nhân
    $scope.isOwnProfile = currentUser === userFollowId;

    // lấy bài viết của người dùng, load vào trang cá nhân
    $scope.layBaiVietTheoUserId = function (userId) {
        const urll = window.location.href;
        userId = urll.split("/").pop();
        if (!userId) {
            console.error("UserId không hợp lệ:", userId);
            return;
        }

        var url = `${host_BaiViet}/user/${userId}`;
        $http.get(url)
            .then((resp) => {
                $scope.baiVietNguoiDung = resp.data; // Gán danh sách bài viết vào scope
                console.log("Danh sách bài viết:", $scope.baiVietNguoiDung);
            })
            .catch((error) => {
                console.error("Lỗi khi lấy danh sách bài viết:", error);
            });
    };
    $scope.layBaiVietTheoUserId(userId);

    // chuyển sang trang Chi tiết bài viết
    $scope.chuyenTrang = function ($event, baiVietId) {
        console.log(baiVietId);
        $event.preventDefault();
        $event.target.href = '/bai-viet/' + baiVietId;
        window.location.href = $event.target.href;
    };


    const checkBlockStatus = function () {
        if (!userId) {
            console.log("Không hợp lệ", userId);
            return;
        }
        var checkUrl = `${host_Follow}/${userId}/checkBlockStatus?blockUserId=${currentUser.id}`;
        $http.get(checkUrl)
            .then((resp) => {
                $scope.isBlock = resp.data.isBlock;
                console.log("Block status:", $scope.isBlock);
            })
            .catch((error) => {
                console.error("Error checking block status:", error);
            });
    };
    const checkFollowStatus = function () {
        if (!userId) {
            console.log("Không hợp lệ", userId);
            return;
        }
        var checkUrl = `${host_Follow}/${userId}/isFollowing?userFollowId=${currentUser.id}`;
        $http.get(checkUrl)
            .then((resp) => {
                console.log("API Response:", resp); // Kiểm tra toàn bộ dữ liệu trả về
                $scope.isFollowing = resp.data; // Gán giá trị trả về
                console.log("Follow status:", $scope.isFollowing);            })
            .catch((error) => {
                console.error("Error checking follow status:", error);
            });
    };
    $scope.checkFollowStatus = function() {
        var checkUrl = `${host_Follow}/${userId}/isFollowing?userFollowId=${currentUser.id}`;
        $http.get(checkUrl)
            .then(function(response) {
                // Kiểm tra dữ liệu trả về
                if (response.data && typeof response.data.isFollowing !== "undefined") {
                    $scope.isFollowing = response.data.isFollowing; // Cập nhật đúng trạng thái follow
                    console.log("Trạng thái follow:", $scope.isFollowing);
                } else {
                    console.error("Dữ liệu không hợp lệ hoặc không có trạng thái follow");
                }
            })
            .catch(function(error) {
                console.error("Lỗi khi kiểm tra trạng thái follow:", error);
            });
    };


    // Gọi hàm kiểm tra trạng thái follow và block khi trang được tải
    checkBlockStatus();

    $scope.selectedAction = "";
    // Xử lý Follow / Unfollow
    // $scope.toggleFollow = function () {
    //     if (!userId) {
    //         console.error("Không hợp lệ:", userId);
    //         return;
    //     }
    //     const url = `${host_Follow}/${userId}/toggleFollow?userFollowId=${currentUser.id}`;
    //     $http.post(url, {isFollowing: $scope.isFollowing})
    //         .then(function (resp)  {
    //             console.log("API Response:", resp); // Kiểm tra toàn bộ dữ liệu trả về
    //             console.log("Follow status toggled:", $scope.isBlock);
    //         })
    //         .catch((error) => {
    //             console.error("Lỗi khi toggle follow:", error);
    //         });
    // };
    $scope.toggleFollow = function() {
        // Đảo trạng thái follow/unfollow
        $scope.isFollowing = !$scope.isFollowing;

        // Gửi yêu cầu API để cập nhật trạng thái follow
        const url = `${host_Follow}/${userId}/toggleFollow?userFollowId=${currentUser.id}`;
        $http.post(url, { isFollowing: $scope.isFollowing })
            .then(function(response) {
                console.log("Trạng thái follow đã thay đổi:", response.data);
            })
            .catch(function(error) {
                console.error("Lỗi khi thay đổi trạng thái follow:", error);
            });
    };

    $scope.toggleBlock = function () {
        if (!userId) {
            console.error("Không hợp lệ:", userId);
            return;
        }

        var url = `${host_Follow}/${userId}/toggleBlock?blockUserId=${currentUser.id}`;

        $http.post(url)
            .then((resp) => {
                $scope.isBlock = !$scope.isBlock; // Đảo ngược trạng thái block
                alert("Chặn thành công")
                console.log("API Response:", resp); // Kiểm tra toàn bộ dữ liệu trả về
                console.log("Block status toggled:", $scope.isBlock);
            })
            .catch((error) => {
                console.error("Error:", error);
            });
    };

    // Xử lý hành động từ combobox
    $scope.handleAction = function (action) {
        if (action === "unfollow") {
            $scope.toggleFollow(); // Gọi hàm hủy follow
        } else if (action === "report") {
            $scope.reportAccount(); // Gọi hàm báo cáo tài khoản
        }
    };

    // Báo cáo tài khoản
    $scope.reportAccount = function () {
        const url = `${host_Follow}/${userId}/reportAccount`;
        $http.post(url)
            .then((resp) => {
                alert("Báo cáo tài khoản thành công!");
            })
            .catch((error) => {
                console.error("Lỗi khi báo cáo tài khoản:", error);
            });
    };

    // Khởi tạo
    checkFollowStatus();


    $scope.handleAction = function (action) {
        if (action === "unfollow") {
            $scope.toggleFollow();
        } else if (action === "toggleBlock") {
            $scope.toggleBlock();
        }
    };

    // Lấy danh sách người theo dõi
    $scope.layDanhSachNguoiTheoDoi = function () {
        const url = `${host_Follow}/${currentUser.id}/followers`;

        $http.get(url)
            .then((resp) => {
                $scope.followers = resp.data;
                console.log("Followers:", $scope.followers);
            })
            .catch((error) => {
                console.log("Error:", error);
            });
    };

    // Lấy danh sách người đang theo dõi
    $scope.layDanhSachNguoiDangTheoDoi = function () {
        const url = `${host_Follow}/${currentUser.id}/following`;

        $http.get(url)
            .then((resp) => {
                $scope.following = resp.data;
                console.log("Following:", $scope.following);
            })
            .catch((error) => {
                console.log("Error:", error);
            });
    };

    // Gọi hàm lấy danh sách người theo dõi và người đang theo dõi khi trang được tải
    $scope.layDanhSachNguoiTheoDoi();
    $scope.layDanhSachNguoiDangTheoDoi();
});

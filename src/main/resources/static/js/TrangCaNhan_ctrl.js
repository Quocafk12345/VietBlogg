let host_Follow = "http://localhost:8080/api/user";

mainApp.controller("UserController", function ($scope, $http) {
    $scope.isFollowing = false;
    $scope.isBlock = false;
    $scope.followers = []; // Mảng lưu trữ danh sách người theo dõi
    $scope.followingList = []; // Mảng lưu trữ danh sách người đang theo dõi

    $scope.selectedTab = 'followers';
    const url = window.location.href;
    const userId = url.split("/").pop(); // Lấy phần cuối URL , người dùng cần follow
    const userFollowId = currentUser; // Người dùng đăng nhập

    let socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    socket.onmessage = function (event) {
        let data = JSON.parse(event.data);
        if (data.userFollowId === currentUser.id) {
            // Người được follow
            $scope.$apply(() => {
                $scope.luotFollower += data.isFollowing ? 1 : -1;
            });
        }
    };
    // Kiểm tra xem người dùng có follow chưa
    $scope.kiemTraFollow = function (userFollowId, userId) {
        var url = `/api/follow/kiem-tra?userFollowId=${userFollowId}&userId=${userId}`;
        return $http.get(url)
            .then(function (response) {
                return response.data; // Trả về giá trị boolean
            })
            .catch(function (error) {
                console.error("Lỗi khi kiểm tra follow:", error);
            });
    };

    // Follow user
    $scope.followUser = function (userId, userFollowId) {
        var url = "/api/follow/follow";
        return $http.post(url, null, { params: { userId: userId, userFollowId: userFollowId } })
            .then(function (response) {
                var isFollowing = response.data;
                if (isFollowing) {
                    console.log("Follow thành công");
                    // Cập nhật lại UI sau khi follow
                    $scope.updateFollowStatus(userFollowId, true);
                } else {
                    console.log("Người dùng đã follow rồi");
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi follow:", error);
            });
    };

    // Unfollow user
    $scope.unfollowUser = function (userId, userFollowId) {
        var url = "/api/follow/unfollow";
        return $http.post(url, null, { params: { userId: userId, userFollowId: userFollowId } })
            .then(function (response) {
                var isFollowing = response.data;
                if (!isFollowing) {
                    console.log("Unfollow thành công");
                    // Cập nhật lại UI sau khi unfollow
                    $scope.updateFollowStatus(userFollowId, false);
                } else {
                    console.log("Người dùng không follow.");
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi unfollow:", error);
            });
    };

    // Cập nhật trạng thái follow/unfollow trên UI
    $scope.updateFollowStatus = function (userFollowId, isFollowing) {
        var user = $scope.dangTheoDoi.find(u => u.id === userFollowId);
        if (user) {
            user.isFollowing = isFollowing;
        }
    };

    // Chọn tab
    $scope.selectTab = function(tab) {
        $scope.selectedTab = tab;
        if (tab === 'followers') {
            $scope.layDanhSachNguoiTheoDoi();
        } else if (tab === 'following') {
            $scope.layDanhSachNguoiDangTheoDoi();
        }
    };

    // Toggle follow/unfollow
    $scope.toggleFollow = function (userId, userFollowId) {
        $scope.kiemTraFollow(userFollowId, userId).then(function (isFollowing) {
            if (isFollowing) {
                // Nếu đã follow, hủy follow
                $scope.unfollowUser(userId, userFollowId);
            } else {
                // Nếu chưa follow, tiến hành follow
                $scope.followUser(userId, userFollowId);
            }
        });
    }

    stompClient.connect({}, function (frame){
        console.log('Connected: ' + frame);

        //Subcribe de nhan trang thai follow tu server
        stompClient.subscribe('/topic/follow-status', function (message){
            const data = JSON.parse(message.body);
            if(data.userId === parseInt(userId)){
                $scope.isFollowing = data.isFollowing;
                $scope.$apply(); //Update View
            }
        });
    });
    $scope.baiVietNguoiDung = {};

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

    $scope.chuyenTrang = function ($event, userId) {
        $event.preventDefault();
        $event.target.href = '/trang-ca-nhan/' + userId;
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
        const payload = {
            followId: currentUser.id,
            followedId: userId
        };
        $http.post(url, payload)
            .then((response) =>{
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
    $scope.initFollowState = function () {
        angular.forEach($scope.dangTheoDoi, function (userFollow) {
            $scope.kiemTraFollow(userFollow);
        });
    };
    $scope.initFollowState();

    // Gọi hàm lấy danh sách người theo dõi và người đang theo dõi khi trang được tải
    $scope.layDanhSachNguoiTheoDoi();
    $scope.layDanhSachNguoiDangTheoDoi();
});

let host_BinhLuan = "http://localhost:8080/api/binh-luan";

mainApp.controller('BinhLuanController', function($scope, $http, timeService) {
    $scope.danhSachBinhLuan = [];

    $scope.tinhThoiGianDang = timeService.tinhThoiGianDang; // Gán hàm từ service

    $scope.chuyenTrang = function($event, userId) {
        console.log(userId);
        $event.preventDefault();
        $event.target.href = '/trang-ca-nhan/' + userId;
        window.location.href = $event.target.href;
    };
    $scope.layBinhLuan = function(baiVietId) {
        $http.get(`${host_BinhLuan}/${baiVietId}/binh-luan-goc`)
            .then(resp => {
                $scope.danhSachBinhLuan = resp.data;
                angular.forEach($scope.danhSachBinhLuan, function(binhLuan) {
                    binhLuan.thoiGianDang = $scope.tinhThoiGianDang(binhLuan.ngayTao);
                    binhLuan.showReplies = false; // Ẩn bình luận con ban đầu
                    binhLuan.replies = []; // Khởi tạo mảng replies
                });
            })
            .catch((error) => {
                console.error("Error", error);
            });
    };

    $scope.hienThiBinhLuanCon = function(binhLuan) {
        if (binhLuan.replies.length === 0) {
            $http.get(`${host_BinhLuan}/${binhLuan.id}/binh-luan-con`)
                .then(resp => {
                    binhLuan.replies = resp.data;
                    angular.forEach(binhLuan.replies, function(reply) {
                        reply.thoiGianDang = $scope.tinhThoiGianDang(reply.ngayTao);
                        reply.showReplies = false; // Hiển thị tất cả bình luận con cấp 2
                        reply.replies = []; // Khởi tạo mảng replies cho bình luận cấp 2
                    });
                })
                .catch(error => {
                    console.error("Lỗi khi lấy bình luận con:", error);
                });
        }
        binhLuan.showReplies = !binhLuan.showReplies; // Đảo trạng thái hiển thị cho bình luận cấp 1
    };

    $scope.layBinhLuanConCap2 = function(binhLuan) {
        $http.get(`${host_BinhLuan}/${binhLuan.id}/binh-luan-con`)
            .then(resp => {
                binhLuan.replies = resp.data;
                angular.forEach(binhLuan.replies, function(reply) {
                    reply.thoiGianDang = $scope.tinhThoiGianDang(reply.ngayTao);
                    if (reply.binhLuanCha)
                    $scope.layBinhLuanConCap2(reply.replies);
                });
            })
            .catch(error => {
                console.error("Lỗi khi lấy bình luận con cấp 2:", error);
            });
    };

    $scope.themBinhLuan = function(binhLuan) {
        var binhLuanMoi = {
            noiDung: null,
            baiViet: { id: baiVietId }, // Gửi kèm ID bài viết
            user: { id: currentUser.id }, // Thay 1 bằng ID người dùng hiện tại
            binhLuanCha: null
        };

        if (binhLuan !== undefined) {
            // Lấy nội dung từ input Phản hồi
            var replyInputId = 'replyInput-' + binhLuan.id;
            binhLuanMoi.noiDung = document.getElementById(replyInputId).value;
            if (binhLuan.level === 'CAP_2') {
                binhLuanMoi.binhLuanCha = { id: binhLuan.binhLuanCha.id }; // Gán ID bình luận cha
            } else {
                binhLuanMoi.binhLuanCha = { id: binhLuan.id }; // Gán ID bình luận cha
            }
        } else {
            // Lấy nội dung từ input bình luận gốc
            binhLuanMoi.noiDung = document.getElementById('binhLuanInput').value;
        }

        if (binhLuanMoi.noiDung.trim() === "") {
            console.log("Vui lòng nhập nội dung bình luận.");
        }

        $http.post(`${host_BinhLuan}/${baiVietId}`, binhLuanMoi)
            .then(resp => {
                console.log("Bình luận đã được thêm:", resp.data);
                $scope.layBinhLuanGoc(baiVietId);
                document.getElementById('binhLuanInput').value = "";
            })
            .catch(error => {
                console.error("Lỗi khi thêm bình luận:", error);
                alert("Có lỗi xảy ra khi thêm bình luận.");
            });
    };

    $scope.hienThiInputTraLoi = function(binhLuan) {
        binhLuan.showReplyForm = !binhLuan.showReplyForm; // Đảo trạng thái hiển thị form
    };

    $scope.layBinhLuan(baiVietId);
});

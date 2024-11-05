let host_BinhLuan = "http://localhost:8080/api/binh-luan";

mainApp.controller('BinhLuanController', function($scope, $http, timeService) {
    $scope.danhSachBinhLuan = [];

    $scope.tinhThoiGianDang = timeService.tinhThoiGianDang; // Gán hàm từ service

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
        if (binhLuan.replies.length === 0) { // Chỉ lấy bình luận con nếu chưa lấy
            $http.get(`${host_BinhLuan}/${binhLuan.id}/binh-luan-con`)
                .then(resp => {
                    binhLuan.replies = resp.data;
                    angular.forEach(binhLuan.replies, function(reply) {
                        reply.thoiGianDang = $scope.tinhThoiGianDang(reply.ngayTao);
                    });
                })
                .catch(error => {
                    console.error("Lỗi khi lấy bình luận con:", error);
                });
        }
        binhLuan.showReplies = !binhLuan.showReplies; // Đảo trạng thái hiển thị
    };

    $scope.themBinhLuan = function(binhLuanCha) {
        var noiDung;
        if (binhLuanCha) {
            // Lấy nội dung từ input Phản hồi
            var replyInputId = 'replyInput-' + binhLuanCha.id;
            noiDung = document.getElementById(replyInputId).value;
        } else {
            // Lấy nội dung từ input bình luận gốc
            noiDung = document.getElementById('binhLuanInput').value;
        }

        if (noiDung.trim() === "") {
            console.log("Vui lòng nhập nội dung bình luận.");
        }

        var binhLuanMoi = {
            noiDung: noiDung,
            baiViet: { id: baiVietId }, // Gửi kèm ID bài viết
            user: { id: currentUserId } // Thay 1 bằng ID người dùng hiện tại
        };

        if (binhLuanCha) {
            binhLuanMoi.binhLuanCha = { id: binhLuanCha.id }; // Gán ID bình luận cha
        }

        $http.post(`${host_BinhLuan}/${baiVietId}`, binhLuanMoi)
            .then(resp => {
                console.log("Bình luận đã được thêm:", resp.data);
                $scope.layBinhLuan(baiVietId);
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

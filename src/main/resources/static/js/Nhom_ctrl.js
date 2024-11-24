let host_Nhom = "http://localhost:8080/api/nhom";

mainApp.controller("nhomController", function ($scope, $http, $window) {
    $scope.DSnhom = [];
    $scope.danhSachNhom = [];
    $scope.nhomDuocChon = {};
    $scope.nguoiNhanId = null; // Khai báo biến nguoiNhanId cho hàm nhượng quyền

    $scope.loadNhom = function () {
        //DSNhom trong sidebar
        $http.get(`${host_Nhom}/user/${currentUser.id}`)
            .then(resp => {
                $scope.DSnhom = resp.data;
            })
            .catch(error => {
                console.log("Error", error);
            });

        //DSNhom trong CongDong
        $http.get(`${host_Nhom}/danh-sach`)
            .then(resp => {
                $scope.danhSachNhom = resp.data;

                // Kiểm tra trạng thái tham gia của từng nhóm
                $scope.danhSachNhom.forEach(nhom => {
                    $http.get(`${host_Nhom}/${nhom.id}`)
                        .then(response => {
                            nhom.daThamGia = response.data.daThamGia;
                        });
                });
            })
            .catch(error => {
                console.log("Error", error);
            });
    }

    $scope.layThongTinNhom = function (idNhom) {
        $http.get('/api/nhom/' + idNhom)
            .then(function (response) {
                $scope.thongTinNhom = response.data;
            });
    };

    $scope.chuyenTrang = function($event, idNhom) {
        console.log(idNhom);
        $event.preventDefault();
        $event.target.href = '/nhom/chi-tiet/' + idNhom;
        window.location.href = $event.target.href;
    };

    $scope.hienThiNhomDuocChon = function (idNhom) {
        $http.get(`${host_Nhom}/${idNhom}`)
            .then(function(response) {
                $scope.thongTinNhom = response.data.nhom;
                $scope.vaiTroTrongNhom = response.data.vaiTro;
                $scope.layThongTinNhom_SoThanhVien(idNhom)
                    .then(function(soThanhVien) {
                        $scope.thongTinNhom.soLuongThanhVien = soThanhVien;
                    });

                // Lấy danh sách thành viên
                $scope.layDanhSachThanhVien(idNhom)
                    .then(function(danhSachThanhVien) {
                        $scope.danhSachThanhVien_TabThanhVien = danhSachThanhVien; // Lưu vào biến riêng cho tab "Thành viên"
                    });
                // Gửi event 'loadBaiVietNhom' đến BaiVietController
                $scope.$broadcast('loadBaiVietNhom');
            })
            .catch(error => {
                console.log("Error", error);
            });

        // Lấy danh sách bài viết của nhóm
        $http.get(`${host_Nhom}/${idNhom}/bai-viet`)
            .then(function(response) {
                $scope.danhSachBaiViet_TabBaiVietNhom = response.data;
            })
            .catch(error => {
                console.log("Error", error);
            });

        // Lấy danh sách bài viết cá nhân của người dùng trong nhóm
        $http.get(`${host_BaiViet}/nhom/${idNhom}/user/${currentUser.id}`)
            .then(function(response) {
                $scope.danhSachBaiViet_TabBaiVietCaNhan = response.data;
            })
            .catch(error => {
                console.log("Error", error);
            });

    }

    $scope.layThongTinNhom_SoThanhVien = function (idNhom) { return $http.get('/api/nhom/' + idNhom + '/thanh-vien/so-luong')
        .then((resp) => {
            return resp.data;
        }).catch((error) => {
            console.log("Error", error);
        });
    };

    if (typeof idNhom !== 'undefined') {
        $scope.hienThiNhomDuocChon(idNhom);
    }

    // Hàm tạo nhóm
    $scope.taoNhom = function() {
        // Tạo object nhom
        var nhom = {
            ten: document.getElementById("tenNhom").value, // Lấy giá trị từ input
            gioiThieu: document.getElementById("moTa").value  // Lấy giá trị từ textarea
        };
        // Sử dụng currentUser từ Thymeleaf
        if (currentUserId) {
            nhom.nguoiTao = { id: currentUser.id }; // Thêm userId vào object nguoiTao
        } else {
            alert("Vui lòng đăng nhập để tạo nhóm.");
            return;
        }


        // Tạo FormData và thêm object nhom vào
        var formData = new FormData();
        formData.append('nhom', JSON.stringify(nhom)); // Chuyển object nhom thành JSON string
        // Thêm userId vào formData
        formData.append('userId', currentUser.id);
        // Kiểm tra file ảnh
        var hinhAnhInput = document.getElementById('hinhAnh');
        if (hinhAnhInput.files && hinhAnhInput.files[0]) {
            formData.append('hinhAnh', hinhAnhInput.files[0]);
        } else {
            alert("Vui lòng chọn ảnh đại diện cho nhóm.");
            return;
        }

        $http.post(`${host_Nhom}/tao-nhom`, formData, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function(response) {
            if (response.status === 201) {
                alert('Tạo nhóm thành công!');
                window.location.href = '/nhom/chi-tiet/' + response.data.id;
            } else {
                alert('Lỗi tạo nhóm.');
            }
        }).catch(function(error) {
            console.error('Lỗi:', error);
            alert('Đã có lỗi xảy ra.');
        });
    };

    //Hàm lấy danh sách thành viên trong ChiTietNhom
    $scope.layDanhSachThanhVien = function (idNhom) {
        return $http.get(`${host_Nhom}/${idNhom}/thanh-vien`) // Giả sử bạn có API này
            .then(function(response) {
                return response.data;
            })
            .catch(function(error) {
                console.error("Lỗi khi lấy danh sách thành viên:", error);
            });
    };

    //Hàm giải tán nhóm trong ChiTietNhom
    $scope.giaiTanNhom = function(nhomId) {
        if (confirm('Bạn có chắc chắn muốn giải tán nhóm này?')) {
            // Gọi API để giải tán nhóm
            $http.delete(`${host_Nhom}/${nhomId}`)
                .then(function(response) {
                    // Xử lý kết quả
                    alert('Giải tán nhóm thành công!');
                    // Chuyển hướng về trang chủ hoặc trang danh sách nhóm
                    $window.location.href = '/cong-dong';
                })
                .catch(function(error) {
                    console.error('Lỗi:', error);
                    alert('Đã có lỗi xảy ra.');
                });
        }
    };

    //Hàm rời khỏi nhóm trong ChiTietNhom
    $scope.roiNhom = function(nhomId) {
        if (confirm('Bạn có chắc chắn muốn rời khỏi nhóm này?')) {
            $http.post(`${host_Nhom}/${nhomId}/roi-nhom/${currentUser.id}`)
                .then(function(response) {
                    // Xử lý kết quả
                    alert('Rời nhóm thành công!');
                    // Chuyển hướng về trang chủ hoặc trang danh sách nhóm
                    $window.location.href = '/cong-dong';
                })
                .catch(function(error) {
                    console.error('Lỗi:', error);
                    alert('Đã có lỗi xảy ra.');
                });
        }
    };

    //Hàm rời khỏi nhóm và nhượng quyền cho quản trị viên trong ChiTietNhom
    // $scope.roiNhomVaNhuongQuyen = function(nhomId, nguoiNhanId) {
    //     if (confirm('Bạn có chắc chắn muốn rời khỏi nhóm này và nhượng quyền cho người đã chọn?')) {
    //         $http.post(`${host_Nhom}/${nhomId}/roi-nhom/${currentUser.id}/nhuong-quyen/${nguoiNhanId}`)
    //             .then(function(response) {
    //                 // Xử lý kết quả
    //                 alert('Rời nhóm thành công!');
    //                 // Chuyển hướng về trang chủ hoặc trang danh sách nhóm
    //                 $window.location.href = '/api/nhom/CongDong';
    //             })
    //             .catch(function(error) {
    //                 console.error('Lỗi:', error);
    //                 alert('Đã có lỗi xảy ra.');
    //             });
    //     }
    // };

    $scope.roiNhomVaNhuongQuyen = function(nhomId, nguoiNhanId) {
        if (confirm('Bạn có chắc chắn muốn rời khỏi nhóm này và nhượng quyền cho người đã chọn?')) {
            console.log("người nhận id: ",nguoiNhanId);
            setTimeout(function() {
                $http.post(`${host_Nhom}/${nhomId}/roi-nhom/${currentUser.id}/nhuong-quyen/${nguoiNhanId}`)
                    .then(function(response) {
                        // Xử lý kết quả
                        alert('Rời nhóm thành công!');

                        // Cập nhật vai trò trong thông tin nhóm
                        $scope.capNhatVaiTroTrongNhom(nhomId, nguoiNhanId);

                        // Chuyển hướng về trang chủ hoặc trang danh sách nhómw  
                        $window.location.href = '/cong-dong';
                    })
                    .catch(function(error) {
                        console.error('Lỗi:', error);
                        alert('Đã có lỗi xảy ra.');
                    });
            }, 100); // Delay 100 milliseconds
        }
    };

// Hàm cập nhật vai trò trong thông tin nhóm của ChiTietNhom.html
    $scope.capNhatVaiTroTrongNhom = function(nhomId, nguoiNhanId) {
        // Gọi API để lấy thông tin nhóm mới nhất
        $http.get(`${host_Nhom}/${nhomId}`)
            .then(function(response) {
                $scope.thongTinNhom = response.data.nhom;
                $scope.vaiTroTrongNhom = response.data.vaiTro;
            })
            .catch(function(error) {
                console.error('Lỗi khi cập nhật vai trò:', error);
            });
    };

    // Lấy danh sách tất cả nhóm hiển thij trong trang Cộng Đồng
    $http.get(`${host_Nhom}/danh-sach`)
        .then(resp => {
            $scope.danhSachNhom = resp.data;

            $scope.danhSachNhom.forEach(nhom => {
                $http.get(`${host_Nhom}/${nhom.id}`)
                    .then(response => {
                        nhom.daThamGia = response.data.daThamGia;
                        nhom.vaiTro = response.data.vaiTro; // Lấy vai trò từ response
                    });
            });
        })
        .catch(error => {
            console.log("Error", error);
        });

    //Hàm tham gia nhóm trong CongDong
    $scope.thamGiaNhom = function(nhomId) {
        $http.post(`${host_Nhom}/${nhomId}/tham-gia/${currentUser.id}`)
            .then(function(response) {
                if (response.status === 200) {
                    alert('Tham gia nhóm thành công!');
                    // Tìm nhóm trong danh sách và cập nhật trạng thái
                    var nhom = $scope.danhSachNhom.find(n => n.id === nhomId);
                    if (nhom) {
                        nhom.daThamGia = true;
                    }
                    $window.location.href = '/nhom/chi-tiet/' + nhomId;
                } else {
                    alert('Lỗi tham gia nhóm.');
                }
            })
            .catch(function(error) {
                console.error('Lỗi:', error);
                alert('Đã có lỗi xảy ra.');
            });
    };

});


var mainApp = angular.module('mainApp', ['ngRoute', 'ngSanitize']);
mainApp.controller('quanLyController', function($scope, $http, $window, $sce) {
    $scope.currentUser = JSON.parse(localStorage.getItem('currentUser'));

    // Lấy danh sách người dùng
    $http.get(`${host_DangNhap}/api/user/user`)
        .then(function (response) {
            $scope.users = response.data;
        });

    // ... (Các hàm khác)

    // Lấy danh sách bài viết và vẽ biểu đồ cột
    $http.get(`${host_DangNhap}/api/bai-viet`)
        .then(function(response) {
            $scope.baiViets = response.data;

            // Khởi tạo biến để lưu số lượng bài viết theo trạng thái
            var trangThaiCounts = {
                "NHAP": 0,
                "DA_DANG": 0,
                "CHO_DUYET": 0
            };

            // Duyệt qua danh sách bài viết
            $scope.baiViets.forEach(function(baiViet) {
                trangThaiCounts[baiViet.trangThai]++;
                $sce.trustAsHtml(baiViet.noiDung);
            });

            // Cập nhật tổng số bài viết đã đăng và chưa duyệt
            document.getElementById("tongDaDang").innerText = trangThaiCounts['DA_DANG'];
            document.getElementById("tongChuaDuyet").innerText = trangThaiCounts['CHO_DUYET'];
            document.getElementById("tongNhap").innerText = trangThaiCounts['NHAP'];

            // Vẽ biểu đồ cột cho trạng thái bài viết
            var ctx = document.getElementById('baiVietChart').getContext('2d');
            var myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: ['NHÁP', 'ĐÃ ĐĂNG', 'CHỜ DUYỆT'],
                    datasets: [{
                        label: 'Số lượng Bài Viết trạng thái',
                        data: [trangThaiCounts['NHAP'], trangThaiCounts['DA_DANG'], trangThaiCounts['CHO_DUYET']],
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)'
                        ],
                        borderWidth: 1,
                        barThickness: 20
                    }]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                stepSize: 1
                            }
                        }]
                    },
                    datasets: {
                        bar: {
                            barThickness: 20
                        }
                    },
                    responsive: true,
                    maintainAspectRatio: false
                }
            });
        });

// Lấy danh sách Admin và vẽ biểu đồ
    $http.get(`${host_DangNhap}/api/user/admin`)
        .then(function (response) {
            $scope.admins = response.data;

            Promise.all([
                $http.get(`${host_DangNhap}/api/user/user`),
                $http.get(`${host_DangNhap}/api/user/admin`),
                $http.get(`${host_DangNhap}/api/bai-viet`) // Lấy thêm dữ liệu bài viết
            ])
                .then(function (results) {
                    $scope.users = results[0].data;
                    $scope.admins = results[1].data;
                    $scope.baiViets = results[2].data; // Lưu dữ liệu bài viết

                    // ... (Code vẽ biểu đồ User và Admin)

                    // Tạo object để lưu trữ số lượng bài viết của mỗi user
                    var baiVietUserCounts = {};
                    $scope.baiViets.forEach(function(baiViet) {
                        var userId = baiViet.user.id;
                        baiVietUserCounts[userId] = (baiVietUserCounts[userId] || 0) + 1;
                    });

                    // Chuyển đổi dữ liệu thành dạng mảng cho Chart.js
                    var chartLabels = Object.keys(baiVietUserCounts); // Lấy danh sách user ID
                    var chartData = Object.values(baiVietUserCounts); // Lấy số lượng bài viết tương ứng

                    // Vẽ biểu đồ cột
                    var ctx = document.getElementById('userBaiVietChart').getContext('2d');
                    var myChart = new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: chartLabels, // User ID
                            datasets: [{
                                label: 'Số lượng bài viết theo UserID',
                                data: chartData,
                                barThickness: 10
                            }]
                        },
                        options: {
                            // ... (Các thiết lập cho biểu đồ)
                            scales: {
                                yAxes: [{
                                    ticks: {
                                        beginAtZero: true,
                                        stepSize: 1
                                    }
                                }]
                            },
                            responsive: true,
                            maintainAspectRatio: false
                        }
                    });
                });
        });

    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawAllUserPostCountChart);

    function drawAllUserPostCountChart() {
        $http.get(`${host_DangNhap}/api/quan-tri-vien/posts/count-by-month-all-users`)
            .then(function(response) {
                // Kiểm tra xem response.data có phải là array và không rỗng hay không
                if (Array.isArray(response.data) && response.data.length > 0) {
                    // Tạo DataTable và thêm dữ liệu
                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'Tháng');
                    data.addColumn('number', 'Tổng số bài viết');
                    response.data.forEach(function (row) {
                        // data.addRow([row.month.toString(), row.count]); // Ép kiểu row.month thành string
                        var monthYear = row.month.toString() + "/" + row.year.toString(); // Nối tháng và năm
                        data.addRow([monthYear, row.count]);
                    });
                    // Tùy chọn biểu đồ
                    var options = {
                        title: 'Thống kê số lượng bài viết theo tháng', // Thay đổi tiêu đề
                        width: 690, // Đặt chiều rộng
                        height: 300, // Đặt chiều cao
                        colors: ['red'], // Đặt màu cột
                        vAxis: {title: 'Tổng số bài viết'},
                        hAxis: {title: 'Tháng'}
                    };

                    // Vẽ biểu đồ
                    var chart = new google.visualization.ColumnChart(document.getElementById('allUserPostCountChart')); // Xóa .getContext('2d')
                    chart.draw(data, options);
                } else {
                    // Xử lý trường hợp response không phải là array hoặc array rỗng
                    console.error("API không trả về dữ liệu đúng định dạng hoặc dữ liệu rỗng.");
                    // Hiển thị thông báo lỗi cho người dùng hoặc vẽ biểu đồ với dữ liệu mặc định
                }
            })
            .catch(function(error) {
                console.error("API error:", error);
            });
    }

    // Lấy danh sách Admin và vẽ biểu đồ
    $http.get(`${host_DangNhap}/api/user/admin`)
        .then(function (response) {
            $scope.admins = response.data;

            Promise.all([
                $http.get(`${host_DangNhap}/api/user/admin`),
                $http.get(`${host_DangNhap}/api/user/user`),
                $http.get(`${host_DangNhap}/api/bai-viet`),
                $http.get(`${host_DangNhap}/api/quan-tri-vien/posts/count-by-month-all-users`) // Thêm API này vào Promise.all
            ])
                .then(function (results) {
                    $scope.users = results[0].data;
                    $scope.admins = results[1].data;
                    // Vẽ biểu đồ sau khi lấy được dữ liệu Admin
                    var ctx = document.getElementById('userChart').getContext('2d');
                    var myChart = new Chart(ctx, {
                        type: 'pie',
                        data: {
                            labels: ['User', 'Admin'],
                            datasets: [{
                                label: 'Số lượng',
                                data: [$scope.users.length, $scope.admins.length],
                                backgroundColor: [
                                    'rgba(54, 162, 235, 0.2)',
                                    'rgba(255, 99, 132, 0.2)'
                                ],
                                borderColor: [
                                    'rgba(54, 162, 235, 1)',
                                    'rgba(255, 99, 132, 1)'
                                ],
                                borderWidth: 1
                            }]
                        },
                        options: {
                            // Thiết lập tùy chọn cho biểu đồ
                        }

                    });
                });


            $scope.logout = function () {
                $http({
                    method: 'POST',
                    url: `${host_DangNhap}/api/user/dang-xuat`,
                    responseType: 'text'
                }).then(function (response) {
                    localStorage.removeItem('currentUser');
                    $window.location.href = `${host_DangNhap}/dang-nhap`;
                }, function (error) {
                    console.error("Lỗi khi đăng xuất:", error);
                });
            };

            $scope.editingUser = null; // Biến lưu trữ người dùng đang được sửa

            $scope.editUser = function (user) {
                $scope.editingUser = angular.copy(user); // Sao chép người dùng để sửa
            };

            $scope.saveUser = function () {
                $http.put(`${host_DangNhap}/api/user/${$scope.editingUser.id}`, $scope.editingUser)
                    .then(function (response) {
                        // Cập nhật danh sách người dùng sau khi sửa
                        $scope.users = $scope.users.map(function (u) {
                            if (u.id === $scope.editingUser.id) {
                                return response.data;
                            }
                            return u;
                        });
                        $scope.editingUser = null; // Ẩn form sửa
                    });
            };

            $scope.cancelEdit = function () {
                $scope.editingUser = null; // Ẩn form sửa
            };

            $scope.deleteUser = function (userId) {
                if (confirm("Bạn có chắc chắn muốn xóa người dùng này?")) {
                    $http.delete(`${host_DangNhap}/api/user/${userId}`)
                        .then(function (response) {
                            // Xóa người dùng khỏi danh sách sau khi xóa
                            $scope.users = $scope.users.filter(function (u) {
                                return u.id !== userId;
                            });
                        });
                }
            };

            $scope.openTab = function (evt, tabName) {
                var i, tabcontent, tablinks;
                tabcontent = document.getElementsByClassName("tabcontent");
                for (i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].style.display = "none";
                }
                tablinks = document.getElementsByClassName("tablinks");
                for (i = 0; i < tablinks.length; i++) {
                    tablinks[i].className = tablinks[i].className.replace(" active", "");
                }
                document.getElementById(tabName).style.display = "block";
                event.currentTarget.className += " active";
            }
            Promise.all([
                $http.get(`${host_DangNhap}/api/bai-viet`),
                $http.get(`${host_DangNhap}/api/binh-luan`),
                $http.get(`${host_DangNhap}/api/bai-viet/like`)
            ])
                .then(function (results) {
                    $scope.baiViets = results[0].data;
                    $scope.binhLuans = results[1].data;
                    $scope.likes = results[2].data;

                    // ... (Code tính toán số lượng bài viết theo trạng thái)

                    // Tính tổng số lượt thích
                    var tongLuotThich = $scope.likes.length;

                    // Tính tổng số bình luận
                    var tongBinhLuan = $scope.binhLuans.length;

            // Mở tab "Quản lý người dùng" mặc định
            document.getElementById("defaultOpen").click();
            // Cập nhật tổng số lượt thích và bình luận
            document.getElementById("tongLuotThich").innerText = tongLuotThich;
            document.getElementById("tongBinhLuan").innerText = tongBinhLuan;
        });

    $http.get(`${host_DangNhap}/api/bai-viet/like`)
        .then(function (response) {
            $scope.likes = response.data; // Lưu danh sách lượt thích vào $scope.likes
        });

            $scope.timKiem = function() {
                // Lọc người dùng
                $scope.users = $scope.users.filter(function(user) {
                    return user.tenNguoiDung.toLowerCase().includes($scope.searchKeyword.toLowerCase()) ||
                        user.email.toLowerCase().includes($scope.searchKeyword.toLowerCase());
                });

                // Lọc admin
                $scope.admins = $scope.admins.filter(function(admin) {
                    return admin.tenNguoiDung.toLowerCase().includes($scope.searchKeyword.toLowerCase()) ||
                        admin.email.toLowerCase().includes($scope.searchKeyword.toLowerCase());
                });

                // Lọc bài viết
                $scope.baiViets = $scope.baiViets.filter(function(baiViet) {
                    return baiViet.tieuDe.toLowerCase().includes($scope.searchKeyword.toLowerCase()) ||
                        baiViet.noiDung.toLowerCase().includes($scope.searchKeyword.toLowerCase()) ||
                        baiViet.user.tenNguoiDung.toLowerCase().includes($scope.searchKeyword.toLowerCase());
                });
            };
            $scope.locBaiViet = function(loaiLoc) {
                $http.get(`${host_DangNhap}/api/bai-viet`) // Lấy lại danh sách bài viết gốc
                    .then(function(response) {
                        $scope.baiViets = response.data;

                        if (loaiLoc === '2023') {
                            // Lọc bài viết năm 2023
                            $scope.baiViets = $scope.baiViets.filter(function(baiViet) {
                                var baiVietYear = new Date(baiViet.ngayTao).getFullYear();
                                return baiVietYear === 2023;
                            });
                        } else if (loaiLoc === '2024') {
                            // Lọc bài viết năm 2024
                            $scope.baiViets = $scope.baiViets.filter(function(baiViet) {
                                var baiVietYear = new Date(baiViet.ngayTao).getFullYear();
                                return baiVietYear === 2024;
                            });
                        } else if (loaiLoc === 'like') {
                            // Lọc bài viết nổi bật (được nhiều like)
                            $scope.baiViets.sort(function(a, b) {
                                // Sắp xếp giảm dần theo số lượt thích (giả sử mỗi bài viết có thuộc tính 'soLuotThich')
                                return b.soLuotThich - a.soLuotThich;
                            });
                            $scope.baiViets = $scope.baiViets.slice(0, 5); // Lấy 5 bài viết đầu tiên
                        }
                    });
            };
            $scope.locBaiViet = function(loaiLoc) {
                $http.get(`${host_DangNhap}/api/bai-viet`)
                    .then(function(response) {
                        $scope.baiViets = response.data;

                        if (loaiLoc === '2023') {
                            // Lọc bài viết năm 2023
                            $scope.baiViets = $scope.baiViets.filter(function(baiViet) {
                                var baiVietYear = new Date(baiViet.ngayTao).getFullYear(); // Thêm new Date(...) ở đây
                                return baiVietYear === 2023;
                            });
                        } else if (loaiLoc === '2024') {
                            // Lọc bài viết năm 2024
                            $scope.baiViets = $scope.baiViets.filter(function(baiViet) {
                                var baiVietYear = new Date(baiViet.ngayTao).getFullYear(); // Thêm new Date(...) ở đây
                                return baiVietYear === 2024;
                            });
                        } else if (loaiLoc === 'like') {
                            // Lọc bài viết nổi bật (được nhiều like)
                            $scope.baiViets.sort(function(a, b) {
                                // Sắp xếp giảm dần theo số lượt thích (giả sử mỗi bài viết có thuộc tính 'soLuotThich')
                                return b.soLuotThich - a.soLuotThich;
                            });
                            $scope.baiViets = $scope.baiViets.slice(0, 5); // Lấy 5 bài viết đầu tiên
                        }
                    });
            };
            // Mở tab "Quản lý người dùng" mặc định
            document.getElementById("defaultOpen").click();
        });
});
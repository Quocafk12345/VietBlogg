var mainApp = angular.module('mainApp', ['ngRoute']);
mainApp.controller('quanLyController', function($scope, $http, $window) {
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
            });

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
                            }
                        }
                    });
                });
        });

    // Lấy danh sách Admin và vẽ biểu đồ
    $http.get(`${host_DangNhap}/api/user/admin`)
        .then(function (response) {
            $scope.admins = response.data;

            Promise.all([
                $http.get(`${host_DangNhap}/api/user/user`),
                $http.get(`${host_DangNhap}/api/user/admin`)
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
                evt.currentTarget.className += " active";
            }

            // Mở tab "Quản lý người dùng" mặc định
            document.getElementById("defaultOpen").click();
        });
})
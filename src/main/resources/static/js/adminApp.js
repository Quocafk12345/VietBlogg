
var mainApp = angular.module('mainApp', ['ngRoute', 'ngSanitize']);

mainApp.controller('quanLyController', function ($scope, $http, $window, $sce) {
    $scope.currentUser = JSON.parse(localStorage.getItem('currentUser'));

    // Lấy danh sách tất cả người dùng (user và admin)
    $http.get(`${host_DangNhap}/api/user/all`)
        .then(function (response) {
            $scope.allUsers = response.data;

            // Phân loại user và admin
            $scope.users = $scope.allUsers.filter(user => user.vaiTro === 'USER');
            $scope.admins = $scope.allUsers.filter(user => user.vaiTro === 'ADMIN');
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy danh sách người dùng:", error);
        });

    // Lấy danh sách tất cả nhóm
    $http.get(`${host_DangNhap}/api/nhom/tat-ca`)
        .then(function (response) {
            $scope.nhoms = response.data;
            $scope.tongSoNhom = $scope.nhoms.length; // Tính tổng số nhóm
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy danh sách nhóm:", error);
        });

    // Lấy danh sách nhóm người dùng đã tham gia
    $http.get(`${host_DangNhap}/api/nhom/user/` + $scope.currentUser.id)
        .then(function (response) {
            $scope.nhomsThamGia = response.data;
            document.getElementById("tongNhomThamGia").innerText = $scope.nhomsThamGia.length;
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy danh sách nhóm người dùng đã tham gia:", error);
        });

    // Xóa nhóm
    $scope.deleteNhom = function (nhomId) {
        if (confirm("Bạn có chắc chắn muốn xóa nhóm này?")) {
            $http.delete(`${host_DangNhap}/api/nhom/` + nhomId)
                .then(function (response) {
                    $scope.nhoms = $scope.nhoms.filter(nhom => nhom.id !== nhomId);
                    alert("Xóa nhóm thành công!");
                })
                .catch(function (error) {
                    console.error("Lỗi khi xóa nhóm:", error);
                    alert("Xóa nhóm thất bại!");
                });
        }
    };
    // Lấy danh sách tất cả nhóm và vẽ biểu đồ cột
    $http.get(`${host_DangNhap}/api/nhom/tat-ca`)
        .then(function(response) {
            $scope.nhoms = response.data;
            $scope.tongSoNhom = $scope.nhoms.length; // Tính tổng số nhóm

            // Lấy số lượng thành viên cho mỗi nhóm
            var nhomLabels = [];
            var soLuongThanhVienData = [];
            $scope.nhoms.forEach(function(nhom) {
                nhomLabels.push(nhom.ten);
                soLuongThanhVienData.push(nhom.soLuongThanhVien);
            });

            // Vẽ biểu đồ cột sau khi đã có dữ liệu
            return new Promise(function(resolve, reject) {
                var ctx = document.getElementById('nhomThanhVienChart').getContext('2d');
                var myChart = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: nhomLabels,
                        datasets: [{
                            label: 'Số lượng thành viên trong nhóm',
                            data: soLuongThanhVienData,
                            backgroundColor: 'rgba(75, 192, 192, 0.2)',
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
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
                resolve(myChart);
            });
        })
        .then(function(myChart) {
            // Biểu đồ đã được vẽ, bạn có thể thực hiện các thao tác khác ở đây nếu cần
        })
        .catch(function(error) {
            console.error("Lỗi khi lấy danh sách nhóm:", error);
        });

    // Lấy danh sách bài viết và vẽ biểu đồ cột
    $http.get(`${host_DangNhap}/api/bai-viet`)
        .then(function (response) {
            $scope.baiViets = response.data;

            // Khởi tạo biến để lưu số lượng bài viết theo trạng thái
            var trangThaiCounts = {
                "NHAP": 0,
                "DA_DANG": 0,
                "CHO_DUYET": 0
            };

            // Duyệt qua danh sách bài viết
            $scope.baiViets.forEach(function (baiViet) {
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

            // ... (Code vẽ biểu đồ User và Admin - đã được tối ưu hóa)

            // Tạo object để lưu trữ số lượng bài viết của mỗi user
            var baiVietUserCounts = {};
            $scope.baiViets.forEach(function (baiViet) {
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
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy danh sách bài viết:", error);
        });


    google.charts.load('current', { 'packages': ['corechart'] });
    google.charts.setOnLoadCallback(drawAllUserPostCountChart);

    function drawAllUserPostCountChart() {
        $http.get(`${host_DangNhap}/api/quan-tri-vien/posts/count-by-month-all-users`)
            .then(function (response) {
                // ... (Code xử lý và vẽ biểu đồ)
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
            .catch(function (error) {
                console.error("API error:", error);
            });
    }

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

                // Cập nhật danh sách admin (nếu cần)
                $scope.admins = $scope.admins.map(function (a) {
                    if (a.id === $scope.editingUser.id) {
                        return response.data;
                    }
                    return a;
                });

                $scope.editingUser = null; // Ẩn form sửa
            })
            .catch(function (error) {
                console.error("Lỗi khi lưu người dùng:", error);
            });
    };

    $scope.cancelEdit = function () {
        $scope.editingUser = null; // Ẩn form sửa
    };

    $scope.openTab = function (evt, tabName) {
        // ... (Code xử lý tab)
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
    };

    Promise.all([
        $http.get(`${host_DangNhap}/api/nhom/danh-sach`),
        $http.get(`${host_DangNhap}/api/bai-viet`),
        $http.get(`${host_DangNhap}/api/binh-luan`),
        $http.get(`${host_DangNhap}/api/bai-viet/like`)
    ])
        .then(function (results) {
            // ... (Code xử lý dữ liệu)
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy dữ liệu:", error);
        });

    $http.get(`${host_DangNhap}/api/bai-viet/like`)
        .then(function (response) {
            $scope.likes = response.data; // Lưu danh sách lượt thích vào $scope.likes
        })
        .catch(function (error) {
            console.error("Lỗi khi lấy danh sách lượt thích:", error);
        });

    $scope.timKiem = function () {
        // ... (Code tìm kiếm)
    };

    $scope.locBaiViet = function (loaiLoc) {
        $http.get(`${host_DangNhap}/api/bai-viet`)
            .then(function (response) {
                $scope.baiViets = response.data;

                if (loaiLoc === '2023') {
                    // Lọc bài viết năm 2023
                    $scope.baiViets = $scope.baiViets.filter(function (baiViet) {
                        var baiVietYear = new Date(baiViet.ngayTao).getFullYear();
                        return baiVietYear === 2023;
                    });
                } else if (loaiLoc === '2024') {
                    // Lọc bài viết năm 2024
                    $scope.baiViets = $scope.baiViets.filter(function (baiViet) {
                        var baiVietYear = new Date(baiViet.ngayTao).getFullYear();
                        return baiVietYear === 2024;
                    });
                } else if (loaiLoc === 'like') {
                    // Lọc bài viết nổi bật (được nhiều like)
                    $scope.baiViets.sort(function (a, b) {
                        // Sắp xếp giảm dần theo số lượt thích (giả sử mỗi bài viết có thuộc tính 'soLuotThich')
                        return b.soLuotThich - a.soLuotThich;
                    });
                    $scope.baiViets = $scope.baiViets.slice(0, 5); // Lấy 5 bài viết đầu tiên
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi lọc bài viết:", error);
            });
    };

    $scope.deleteUser = function (userId) {
        if (confirm("Bạn có chắc chắn muốn xóa người dùng này?")) {
            $http.delete(`${host_DangNhap}/api/user/` + userId)
                .then(function (response) {
                    // Xóa người dùng thành công
                    $scope.users = $scope.users.filter(user => user.id !== userId);
                    // Cập nhật lại danh sách admin nếu người dùng bị xóa là admin
                    $scope.admins = $scope.admins.filter(admin => admin.id !== userId);
                    alert("Xóa người dùng thành công!");
                })
                .catch(function (error) {
                    console.error("Lỗi khi xóa người dùng:", error);
                    alert("Xóa người dùng thất bại!");
                });
        }
    };
    // Vẽ biểu đồ User và Admin
    Promise.all([
        $http.get(`${host_DangNhap}/api/user/admin`),
        $http.get(`${host_DangNhap}/api/user/user`)
    ])
        .then(function (results) {
            $scope.admins = results[0].data;
            $scope.users = results[1].data;

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
                    // Thiết lập tùy chọn cho biểu đồ (nếu cần)
                }
            });
        })
        .catch(function(error) {
            console.error("Lỗi khi lấy danh sách người dùng để vẽ biểu đồ:", error);
        });

});
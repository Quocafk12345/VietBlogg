// mainApp.js
var mainApp = angular.module('mainApp', []);

mainApp.service('timeService', function () {
    this.tinhThoiGianDang = function (ngayTao) {
        ngayTao = new Date(ngayTao); // Chuyển đổi chuỗi ngày tạo thành đối tượng Date
        var hienTai = new Date(); // Lấy thời gian hiện tại

        var chenhLech = hienTai - ngayTao; // Tính chênh lệch thời gian (ms)

        var giay = Math.floor(chenhLech / 1000);
        var phut = Math.floor(giay / 60);
        var gio = Math.floor(phut / 60);
        var ngay = Math.floor(gio / 24);
        var tuan = Math.floor(ngay / 7);
        var thang = Math.floor(ngay / 30);
        var nam = Math.floor(ngay / 365);

        if (nam > 0) {
            return nam + " năm trước";
        } else if (thang > 0) {
            return thang + " tháng trước";
        } else if (tuan > 0) {
            return tuan + " tuần trước";
        } else if (ngay > 0) {
            return ngay + " ngày trước";
        } else if (gio > 0) {
            return gio + " giờ trước";
        } else if (phut > 0) {
            return phut + " phút trước";
        } else {
            return giay + " giây trước";
        }
    };
});
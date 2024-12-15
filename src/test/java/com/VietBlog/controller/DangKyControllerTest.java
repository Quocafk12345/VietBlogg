package com.VietBlog.controller;

import com.VietBlog.service.UserService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DangKyControllerTest {

    @Autowired
    private UserService userService;

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        // Khởi tạo driver cho Chrome
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterEach
    void tearDown() {
        // Đóng trình duyệt sau khi test xong
        driver.quit();
    }

    @Test
    void testRegisterForm() {
        // Truy cập trang đăng ký
        driver.get("http://localhost:8080/dang-ky");

        // Thông tin đăng ký
        String email = "test" + System.currentTimeMillis() + "@gmail.com"; // Tạo email unique
        String tenDangNhap = "testuser" + System.currentTimeMillis(); // Tạo tên đăng nhập unique
        String tenNguoiDung = "Nguyễn Văn A";
        String matKhau = "password123";
        String gioiTinh = "true"; // Nam
        String ngaySinh = "2000-01-01";
        String dienThoai = "012345678" + (System.currentTimeMillis() % 10); // Tạo số điện thoại unique

        // Kiểm tra email trước khi điền vào form
        if (!userService.isValidEmail(email)) {
            fail("Địa chỉ email không hợp lệ");
        } else if (!userService.isValidPhone(dienThoai)) {
            fail("Số điện thoại không hợp lệ");
        } else if (userService.isEmailExists(email)) {
            fail("Email đã tồn tại");
        } else if (userService.isTenDangNhapExists(tenDangNhap)) {
            fail("Tên đăng nhập đã tồn tại");
        } else if (userService.isDienThoaiExists(dienThoai)) {
            fail("Số điện thoại đã tồn tại");
        } else {
            // Nếu email chưa tồn tại, điền thông tin vào form
            fillRegisterForm(driver, tenDangNhap, tenNguoiDung, email, matKhau, gioiTinh, ngaySinh, dienThoai);

            // Gửi form
            WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            submitButton.click();

            // Chờ cho đến khi chuyển hướng đến trang đăng nhập
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlToBe("http://localhost:8080/dang-nhap")); // Thay đổi URL nếu cần

            // Kiểm tra chuyển hướng đến trang đăng nhập
            String expectedUrl = "http://localhost:8080/dang-nhap"; // Thay đổi URL nếu cần
            assertEquals(expectedUrl, driver.getCurrentUrl());

            // Hiển thị thông báo thành công và đánh dấu test là pass
            System.out.println("Đăng ký thành công. Test Passed!");
        }
    }

    // Phương thức điền thông tin vào form đăng ký
    private void fillRegisterForm(WebDriver driver, String tenDangNhap, String tenNguoiDung, String email,
                                  String matKhau, String gioiTinh, String ngaySinh, String dienThoai) {
        WebElement tenDangNhapField = driver.findElement(By.name("tenDangNhap"));
        WebElement tenNguoiDungField = driver.findElement(By.name("tenNguoiDung"));
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement matKhauField = driver.findElement(By.name("matKhau"));
        WebElement gioiTinhField = driver.findElement(By.name("gioiTinh"));
        WebElement ngaySinhField = driver.findElement(By.name("ngaySinh"));
        WebElement dienThoaiField = driver.findElement(By.name("dienThoai"));

        // Điền thông tin vào các trường
        tenDangNhapField.sendKeys(tenDangNhap);
        tenNguoiDungField.sendKeys(tenNguoiDung);
        emailField.sendKeys(email);
        matKhauField.sendKeys(matKhau);
        gioiTinhField.sendKeys(gioiTinh); // Chọn "Nam" hoặc "Nữ"
        ngaySinhField.sendKeys(ngaySinh);
        dienThoaiField.sendKeys(dienThoai);
    }
}
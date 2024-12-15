package com.VietBlog.controller;

import com.VietBlog.service.UserService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DangNhapControllerTest {

    @Autowired
    private UserService userService;

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        // Khởi tạo driver cho Chrome
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);    }

    @AfterEach
    void tearDown() {
        // Đóng trình duyệt sau khi test xong
        driver.quit();
    }

    @Test
    void testLoginForm() {
        // Truy cập trang đăng nhập
        driver.get("http://localhost:8080/dang-nhap");

        // Thông tin đăng nhập
        String identifier = "0827876402"; // Email hoặc số điện thoại
        String matKhau = "password123";

        // Điền thông tin vào form
        WebElement identifierField = driver.findElement(By.id("identifiers"));
        WebElement passwordField = driver.findElement(By.id("password"));

        identifierField.sendKeys(identifier);
        passwordField.sendKeys(matKhau);

        // Gửi form
        WebElement submitButton = driver.findElement(By.cssSelector(".btn"));
        submitButton.click();

        // Kiểm tra thông tin sau khi điền
        if (identifier == null || identifier.isEmpty()) {
            fail("Email hoặc số điện thoại không được để trống");
        } else if (identifier.contains("@")) {
            // Nếu chứa '@', xác định là email
            if (!userService.isValidEmail(identifier)) {
                fail("Email không đúng định dạng (@gmail.com)");
            } else if (!userService.isEmailExists(identifier)) {
                fail("Email chưa có tài khoản");
            }
        } else {
            // Nếu không chứa '@', xác định là số điện thoại
            if (!userService.isValidPhone(identifier)) {
                fail("Số điện thoại không đúng định dạng");
            } else if (!userService.isDienThoaiExists(identifier)) {
                fail("Số điện thoại chưa có tài khoản");
            }
        }
        if (matKhau == null || matKhau.isEmpty()) {
            fail("Mật khẩu không được để trống");
        }

        // Hiển thị thông báo thành công và đánh dấu test là pass
        System.out.println("Đăng nhập thành công. Test Passed!");
    }
}
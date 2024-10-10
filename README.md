#Social-networking-website-VietBlog-Full-Stack-project
Đây là dự án Mạng Xã Hội tên VietBlog, xây dựng bằng Spring Boot, Thymeleaf và các thư viện trực thuộc Java.

#springboot-sample-app

## Yêu cầu
JDK 17, Spring Boot 3.3.2

# Chỗ này để note lại các thay đổi
Bất kỳ thay đổi nào về POM, về Code, hãy ghi theo cấu trúc sau.
### Tên file đã sửa + thao tác đã sửa
```
Code đã sửa
```
Thêm file mới vào thì ghi tên, ngày sửa, tên file, mô tả chức năng để làm gì.
Ghi tiêu đề thì dùng "#", càng nhiều dấu thăng, tiêu đề càng nhỏ.
Code thì để ở trong cặp ký tự "``` ```" (3 dấu `, nút nằm ở cuối cùng, tay trái hàng phím số)

## Danh sách các thư viện trong POM
Spring Data JPA
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
```
Spring Security
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
```
Thymeleaf
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
```
Spring Web
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
```
Spring Security tích hợp cho Thymeleaf
```
		<dependency>
			<groupId>org.thymeleaf.extras</groupId>
			<artifactId>thymeleaf-extras-springsecurity6</artifactId>
		</dependency>
```
Spring DevTool
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
```
MSSQL JDBC
```
		<dependency>
			<groupId>com.microsoft.sqlserver</groupId>
			<artifactId>mssql-jdbc</artifactId>
			<scope>runtime</scope>
		</dependency>
```
Lombok
```
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
```
Spring Boot Test
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
```
Thư viện test cho Spring Security
```
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
		</dependency>
```
Spring Fox, SpringDOc tích hợp với Swagger
```
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-boot-starter</artifactId>
			<version>3.0.0</version>
		</dependency>
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.6.0</version>
		</dependency>
```
Chức năng mail cho Spring Boot
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-mail</artifactId>
		</dependency>
		<!-- https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-core -->
```
OpenAPI
```
		<dependency>
			<groupId>org.springdoc</groupId>
    		<artifactId>springdoc-openapi-core</artifactId>
			<version>1.1.49</version>
		</dependency>
```




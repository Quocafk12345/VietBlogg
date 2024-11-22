package com.VietBlog.repository;

import com.VietBlog.entity.ThanhVien;
import com.VietBlog.entity.ThanhVienId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhVienRepository extends JpaRepository<ThanhVien, ThanhVienId> {
    List<ThanhVien> findByUser_Id(Long UserId);
    // Tìm theo idNhom
    List<ThanhVien> findByNhom_Id(Long NhomId);
    void deleteAllByNhom_Id(Long NhanId);

    //tối ưu câu truy vấn để chỉ lấy những thông tin tên và vai trò user , tránh việc fetch toàn bộ object ThanhVien
    @Query("SELECT u.tenNguoiDung, tv.vaiTro FROM ThanhVien tv JOIN tv.user u WHERE tv.nhom.id = :nhomId")
    List<Object[]> layTenNguoiDungVaVaiTro(@Param("nhomId") Long nhomId);

}

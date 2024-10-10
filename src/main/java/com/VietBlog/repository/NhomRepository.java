package com.VietBlog.repository;

import com.VietBlog.entity.Nhom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NhomRepository extends JpaRepository<Nhom, Integer> {
    @Query("SELECT COUNT(tv) FROM ThanhVien tv WHERE tv.id.idNhom = :idNhom")
    Long countThanhVienByNhomId(@Param("idNhom") Integer idNhom);
}

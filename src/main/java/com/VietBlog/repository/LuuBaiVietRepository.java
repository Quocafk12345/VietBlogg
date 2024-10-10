package com.VietBlog.repository;

import com.VietBlog.entity.LuuBaiViet;
import com.VietBlog.entity.LuuBaiViet_ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuuBaiVietRepository extends JpaRepository<LuuBaiViet, LuuBaiViet_ID> {

}

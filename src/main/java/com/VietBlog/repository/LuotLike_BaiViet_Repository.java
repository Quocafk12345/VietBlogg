package com.VietBlog.repository;

import com.VietBlog.entity.LuotLike_BaiViet;
import com.VietBlog.entity.LuotLike_BaiViet_ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuotLike_BaiViet_Repository extends JpaRepository<LuotLike_BaiViet, LuotLike_BaiViet_ID> {

}

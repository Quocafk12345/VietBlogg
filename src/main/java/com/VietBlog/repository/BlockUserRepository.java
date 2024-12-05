package com.VietBlog.repository;

import com.VietBlog.entity.BlockUser;
import com.VietBlog.entity.BlockUser_ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockUserRepository extends JpaRepository<BlockUser, BlockUser_ID> {
    void deleteById(@NonNull BlockUser_ID id);
    boolean existsById(@NonNull BlockUser_ID id);

}

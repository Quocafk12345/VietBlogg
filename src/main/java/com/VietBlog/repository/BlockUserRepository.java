package com.VietBlog.repository;

import com.VietBlog.entity.BlockUser;
import com.VietBlog.entity.BlockUserID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockUserRepository extends JpaRepository<BlockUser, BlockUserID> {
    void deleteById(@NonNull BlockUserID id);
    boolean existsById(@NonNull BlockUserID id);

}

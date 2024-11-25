package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BlockUserID implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @NotNull
    @Column(name = "User_Id", nullable = false)
    private Long user_Id;

    @NotNull
    @Column(name="Block_User_Id", nullable = false)
    private Long blockUser_Id;
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        BlockUserID entity = (BlockUserID) o;
        return Objects.equals(this.blockUser_Id, entity.user_Id) && Objects.equals(this.user_Id, entity.user_Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_Id, blockUser_Id);
    }
}

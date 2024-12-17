package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class LuotFollowId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @NotNull
    @Column(name = "User_Id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "User_Follow_Id", nullable = false)
    private Long userFollow_Id;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LuotFollowId entity = (LuotFollowId) o;
	    return Objects.equals(this.userId, entity.userId) &&
			    Objects.equals(this.userFollow_Id, entity.userFollow_Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userFollow_Id, userId);
    }

}
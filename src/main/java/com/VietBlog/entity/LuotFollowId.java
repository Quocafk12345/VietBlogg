package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Embeddable
public class LuotFollowId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 4743733207428332071L;
    @NotNull
    @Column(name = "User_Id", nullable = false)
    private Integer userId;

    @NotNull
    @Column(name = "Follower_Id", nullable = false)
    private Integer followerId;
}
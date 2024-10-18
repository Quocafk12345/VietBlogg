package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class LuotFollowId implements Serializable {
    @Serial
    private static final long serialVersionUID = 4743733207428332071L;
    @NotNull
    @Column(name = "User_Id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "User_Follow_Id", nullable = false)
    private Long userFollow_Id;
}
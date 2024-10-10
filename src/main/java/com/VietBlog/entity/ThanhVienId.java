package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class ThanhVienId implements Serializable {
    private static final long serialVersionUID = 8103918728952252262L;
    @NotNull
    @Column(name = "Id_Nhom", nullable = false)
    private Integer idNhom;

    @NotNull
    @Column(name = "User_Id", nullable = false)
    private Integer userId;
}
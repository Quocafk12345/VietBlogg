package com.VietBlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LuuBaiViet_ID implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "User_Id")
    private Integer userId;

    @Column(name = "Id_Bai_Viet")
    private Integer idBaiViet;

    // Constructors, getters và setters, equals và hashCode
}

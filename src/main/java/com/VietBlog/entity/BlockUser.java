package com.VietBlog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Block_User")
public class BlockUser {
    @EmbeddedId
    private BlockUser_ID id;

    @MapsId("user_Id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="User_Id", nullable = false)
    private User user;

    @MapsId("blockUser_Id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Block_User_Id", nullable = false)
    private User userBiChan;
}

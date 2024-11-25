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
    private BlockUserID id;

    @MapsId("user_Id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="User_Id", nullable = false)
    private User blocker;

    @MapsId("blockUser_Id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Block_User_Id", nullable = false)
    private User blocked;
}

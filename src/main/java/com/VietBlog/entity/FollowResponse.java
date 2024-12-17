package com.VietBlog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowResponse {
    private Long userId;
    private Boolean isFollowing;
}

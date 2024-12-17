package com.VietBlog.entity;

import lombok.Data;

@Data
public class FollowRequest {
    private Long followerId;
    private Long followedId;
}

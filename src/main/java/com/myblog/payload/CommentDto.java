package com.myblog.payload;

import com.myblog.entities.Post;
import lombok.Data;

@Data
public class CommentDto {

    private long id;
    private String name;
    private String email;
    private String body;
    private Post post;
}

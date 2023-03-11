package com.myblog.services;

import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;

import java.util.List;

public interface PostService {

    public PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePost(PostDto postDto, long id);

    void deletePostById(long id);
}

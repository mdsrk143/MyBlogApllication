package com.myblog.services.Impl;

import com.myblog.entities.Post;
import com.myblog.exception.ResourseNotFoundException;
import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.repositories.PostRepository;
import com.myblog.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    //
    @Override
    public PostDto createPost(PostDto postDto) {

        //convert DTO to entity
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);

        //convert Entity to DTO
        PostDto newPostDto = mapToDto(newPost);
        return newPostDto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> contents = posts.getContent();

        List<PostDto> postDtos = contents.stream().map(post -> mapToDto(post)).collect(Collectors.toList());


        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setTotalElements((int) posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setPageSize(posts.getSize());
        postResponse.setLast(posts.isLast());

        return postResponse;

    }

    @Override
    public PostDto getPostById(long id) {

       Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourseNotFoundException("Post", "id", id)
        );
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourseNotFoundException("post", "id", id)
        );

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatePost = postRepository.save(post);
        PostDto dto = mapToDto(updatePost);
        return dto;
    }

    @Override
    public void deletePostById(long id) {

        Post post = postRepository.findById(id).orElseThrow(

                () -> new ResourseNotFoundException("Post not found", "id", id)
        );

        postRepository.delete(post);

    }

    //convert Entity into DTO
   Post mapToEntity(PostDto postDto){
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }

    //convert DTO to Entity

   PostDto mapToDto(Post post){
        PostDto postDto = new PostDto();
        postDto.setId(postDto.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return postDto;
    }
}

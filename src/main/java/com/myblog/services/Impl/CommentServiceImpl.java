package com.myblog.services.Impl;

import com.myblog.entities.Comment;
import com.myblog.entities.Post;
import com.myblog.exception.BlogApiException;
import com.myblog.exception.ResourseNotFoundException;
import com.myblog.payload.CommentDto;
import com.myblog.repositories.CommentRepository;
import com.myblog.repositories.PostRepository;
import com.myblog.services.CommentService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepository;

    private CommentRepository commentRepository;

    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourseNotFoundException("Post", "id", postId)
        );

        comment.setPost(post);

        Comment newComment = commentRepository.save(comment);

        CommentDto dto = mapToDto(newComment);

        return dto;
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {

        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourseNotFoundException("post", "id", postId)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourseNotFoundException("Comment", "id", commentId)
        );

        if(comment.getPost().getId()!=post.getId()){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourseNotFoundException("post", "id", postId)
        );

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourseNotFoundException("comment", "id", id)
        );

        if(comment.getPost().getId()!=post.getId()){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Post not matching with comment");
        }
        return null;
    }

    private CommentDto mapToDto(Comment newComment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(newComment.getId());
        commentDto.setName(newComment.getName());
        commentDto.setEmail(newComment.getEmail());
        commentDto.setBody(newComment.getBody());
        commentDto.setPost(newComment.getPost());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment  = new Comment();
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;
    }
}

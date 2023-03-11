package com.myblog.controller;

import com.myblog.payload.CommentDto;
import com.myblog.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //http://localhost:8080/api/posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("postId") long postId, @RequestBody CommentDto commentDto){
        CommentDto dto = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/posts/1/comments
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentByPostId(
            @PathVariable("postId") long postId
    ){
        return commentService.getCommentsByPostId(postId);
    }

    //http://localhost:8080/api/posts/1/comments/1
    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable("postId") long postId,
            @PathVariable("id") long commentId
    ){
        CommentDto dto = commentService.getCommentById(postId, commentId);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable("postId") long postId,
            @PathVariable("id") long id,
            @RequestBody CommentDto commentDto
    ){
        CommentDto dto = commentService.updateComment(postId, id, commentDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}

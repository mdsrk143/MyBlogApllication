package com.myblog.repositories;

import com.myblog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PostRepository extends JpaRepository<Post, Long> {

}

package fi.academy.controllers;

import fi.academy.Error.ErrorPost;
import fi.academy.models.Post;
import fi.academy.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {

    @Autowired
    PostRepository postRepository;

   @GetMapping("/post")
   public List<Post> index() {
       return postRepository.findAll();
   }

   @GetMapping("/post/{_id}")
    public ResponseEntity<?> findOne(@PathVariable("_id") String _id) {
       Optional<Post> optionalPost = postRepository.findById(_id);
               if(optionalPost.isPresent()) {
           return ResponseEntity.ok(optionalPost.get());
               }
       HttpStatus status = HttpStatus.NOT_FOUND;
       return ResponseEntity
               .status(status)
               .body(new ErrorPost("No post with id" + _id, status.value()));

   }
   //TODO Kaikki responseEntityiksi
   @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
       postRepository.save(post);
       String id = post.getId();
       URI location = UriComponentsBuilder.newInstance()
               .scheme("http")
               .host("localhost")
               .port(8080)
               .path("/post/{id}")
               .buildAndExpand(id)
               .toUri();
       return ResponseEntity.created(location).build();
   }

   @DeleteMapping("/post/{_id}")
    public ResponseEntity<?> deletePost(@PathVariable("_id") String _id) {
       Optional <Post> p = postRepository.findById(_id);
       if((p.isPresent())) {
           postRepository.deleteById(_id);
           return ResponseEntity.notFound().build();
       } else {
           return ResponseEntity.notFound().build();
       }
   }

   @PutMapping("/post/{_id}")
    public ResponseEntity<?> updatePost(@PathVariable String _id, @RequestBody Post post) {
       Optional<Post> newPost = postRepository.findById(_id);
       newPost.get().setText(post.getText());
       newPost.get().setTitle(post.getTitle());
       postRepository.save(newPost.get());
       HttpStatus status = HttpStatus.OK;
       return ResponseEntity.ok(post);
   }
}

package fi.academy.controllers;

import fi.academy.Error.ErrorPost;
import fi.academy.models.Comment;
import fi.academy.models.Post;
import fi.academy.repositories.CommentRepository;
import fi.academy.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sun.security.util.AuthResources_de;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@EnableMongoAuditing
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;


    @GetMapping("/")
    public String index(Model model) {
        List<Post> posts = postRepository.findAllByOrderByDateDesc();

        model.addAttribute("showposts", posts);
        return "index";
    }

    @GetMapping("/archives")
    public String archives(Model model) {
        List<Post> posts = postRepository.findAllByOrderByDateDesc();
        model.addAttribute("showposts", posts);
        return "archives";
    }

    @GetMapping("/post")
    public String listposts(Model model) {
        Post post = new Post();
        model.addAttribute("addpost", post);

        return "addpost";
    }

    @GetMapping("/post/{_id}/comments")
    public List<Comment> comments(@PathVariable("_id") String _id) {
        Optional<Post> optionalPost = postRepository.findById(_id);
        return commentRepository.findAll();
    }

    @PostMapping("/post/{_id}/comments")
    public String postcomments(@ModelAttribute("addcomment") @RequestBody Comment comment, @PathVariable("_id") String _id) {
        Optional<Post> post = postRepository.findById(_id);
        if(post.get().getComments() == null) {
            post.get().setComments(new ArrayList<>());
        }
        List<Comment> comments = post.get().getComments();
        comments.add(comment);
        post.get().setComments(comments);
        postRepository.save(post.get());
        return "redirect:/";
    }

    @GetMapping("/post/{_id}")
    public String findOne(@PathVariable("_id") String _id, Model model) {
        List<Post> optionalPost = postRepository.getPostById(_id);
        model.addAttribute("showpost", optionalPost);
        Comment comment = new Comment();
        model.addAttribute("addcomment", comment);
        return "post";
    }

    //TODO Kaikki responseEntityiksi
    @PostMapping("/post")
    public String createPost(@ModelAttribute("addpost") @RequestBody Post post) {
        postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/post/delete/{_id}")
    public String deletePost(@PathVariable("_id") String _id) {
            postRepository.deleteById(_id);
      return "redirect:/";
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

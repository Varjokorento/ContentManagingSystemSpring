package fi.academy.controllers;

import fi.academy.helpfunctions.MonthGetter;
import fi.academy.helpfunctions.TagGetter;
import fi.academy.models.*;
import fi.academy.repositories.PostRepository;
import fi.academy.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@EnableMongoAuditing
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    TagRepository tagRepository;


    @GetMapping("/post")
    public String listposts(Model model) {
        Post post = new Post();
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        model.addAttribute("addpost", post);
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        model.addAttribute("popularposts", popularposts);
        return "addpost";
    }


    @PostMapping("/post/{_id}/comments")
    public String postcomments(@ModelAttribute("addcomment") @RequestBody Comment comment, @PathVariable("_id") String _id) {
        Optional<Post> post = postRepository.findById(_id);
        if (post.get().getComments() == null) {
            post.get().setComments(new ArrayList<>());
        }

        List<Comment> comments = post.get().getComments();
        comment.setPosted(new Date());
        comment.setPostedDate(new Date().toString());
        comments.add(comment);
        post.get().setComments(comments);
        postRepository.save(post.get());
        return "redirect:/post/{_id}";
    }

    @PostMapping("/post/{_id}/comments/delete")
    public String deleteComment(@ModelAttribute("deletecomment") Comment comment, @PathVariable("_id") String _id) {
        System.out.println(comment.getPostedDate());
        System.out.println(comment.getComment());
        List<Post> optionalPost = postRepository.getPostById(_id);
        List<Comment> kommentit = optionalPost.get(0).getComments();
        for (int i = 0; i < kommentit.size(); i++) {
            if (kommentit.get(i).getPostedDate().equals(comment.getPostedDate())) {
                kommentit.remove(i);
            }
        }
        optionalPost.get(0).setComments(kommentit);
        postRepository.save(optionalPost.get(0));
        return "redirect:/post/{_id}";
    }

    @GetMapping("/post/{_id}")
    public String findOne(@PathVariable("_id") String _id, Model model) {
        List<Post> optionalPost = postRepository.getPostById(_id);
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        optionalPost.get(0).addClicks();
        postRepository.save(optionalPost.get(0));
        List<Comment> comments = optionalPost.get(0).getComments();
        String id = optionalPost.get(0).getId();
        model.addAttribute("postid", id);
        model.addAttribute("showpost", optionalPost);
        model.addAttribute("popularposts", popularposts);
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        Comment comment = new Comment();
        Comment k = new Comment();
        model.addAttribute("deletecomment", k);
        model.addAttribute("addcomment", comment);
        model.addAttribute("showcomments", comments);
        return "post";
    }

    @GetMapping("/post/{_id}/edit")
    public String updatePost(@PathVariable("_id") String _id, Model model) {
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        model.addAttribute("popularposts", popularposts);
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        model.addAttribute("editpost", postRepository.findById(_id).get());
        return "edit";
    }

    // Tästä ei olla satavarmoja, että käyttääkö joku tätä. Poistetaan testauksen jälkeen, jos huomataan, ettei käytä.

    @GetMapping("/findpost/{title}")
    public String postfindbyname(@PathVariable("title") String title, Model model) {
        Post post = postRepository.findByTitleLike(title);
        Post posti = new Post();
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        model.addAttribute("showpost", posts);
        Comment comment = new Comment();
        model.addAttribute("addcomment", comment);
        return "post";
    }

    @PostMapping("/post")
    public String createPost(@ModelAttribute("addpost") @RequestBody Post post) {
        String text = post.getText();
        text = text.replaceAll("\n", "<br>");
        post.setText(text);
        post.setTags(post.getTags());
        post.setTitle(post.getTitle());
        post.setDate(new Date());
        post.setModifiedDatetoDisplay(post.getDate().toString());
        List<Comment> kommentit = new ArrayList<>();
        String[] splitattu = post.getTags().split("/");
        List<String> splitted = Arrays.asList(splitattu);
        post.setTagit(splitted);

        for (int i = 0; i < splitted.size(); i++) {
            Tag t = new Tag();
            t.setTag(splitted.get(i));
            tagRepository.save(t);
        }

        post.setComments(kommentit);
        post.setClicked(0);
        post.setLikes(1);
        postRepository.save(post);
        return "redirect:/";
    }

    @PostMapping("/post/edit")
    public String updatePost(@ModelAttribute("addpost") @RequestBody Post post) {
        Optional<Post> newPost = postRepository.findById((post.getId()));
        newPost.get().setTitle(post.getTitle());
        newPost.get().setText(post.getText());
        newPost.get().setModifiedDatetoDisplay(new Date().toString());
        postRepository.deleteById(post.getId());
        postRepository.save(newPost.get());
        return "redirect:/findpost/" + newPost.get().getTitle();
    }


    @GetMapping("/post/delete/{_id}")
    public String deletePost(@PathVariable("_id") String _id) {
        Optional<Post> post = postRepository.findById(_id);
        Tag.deleteTags(post, tagRepository);
        postRepository.deleteById(_id);
        return "redirect:/";
    }

    //Näillä sivupalkista pääsee tiettyihin postauksiin

    @PostMapping("/likes/{_id}")
    public String likeAPost(@PathVariable("_id") String _id) {
        Optional<Post> post = postRepository.findById(_id);
        post.get().addLikes();
        postRepository.save(post.get());
        return "redirect:/post/" + _id;
    }


    @GetMapping("/date/{month}")
    public String findByMonth(@PathVariable int month, Model model) {
        System.out.println("Hello date!");
        List<Post> posts = postRepository.findAll();
        System.out.println("Hello date" + posts.get(0).getDate().getMonth());
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        List<Post> thatMonthsPosts = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getDate().getMonth() == month) {
                thatMonthsPosts.add(posts.get(i));
            }
        }
        Post posti = new Post();
        model.addAttribute("showposts", thatMonthsPosts);
        model.addAttribute("addpost", posti);
        model.addAttribute("tagpost", posti);
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        model.addAttribute("popularposts", popularposts);
        return "archives";
    }


}

package fi.academy.controllers;

import fi.academy.helpfunctions.MonthGetter;
import fi.academy.helpfunctions.TagGetter;
import fi.academy.models.Comment;
import fi.academy.models.Post;
import fi.academy.models.PostService;
import fi.academy.repositories.PostRepository;
import fi.academy.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@EnableMongoAuditing
public class ArchivesController {


    @Autowired
    PostRepository postRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    PostService postService;


    @GetMapping("/archives")
    public String archives(Model model) {
        Post post = new Post();
        List<Post> posts = postRepository.findAllByOrderByDateDesc();
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        model.addAttribute("showposts", posts);
        model.addAttribute("addpost", post);
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("popularposts", popularposts);
        model.addAttribute("tagpost", post);
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        return "archives";
    }

    @GetMapping("/archives/{title}")
    public String archivesfindbyname(@PathVariable("title") String title, Model model) {
        Post post = postRepository.findByTitleLike(title);
        if (post == null) {
            return "redirect:/error";
        }
        Post posti = new Post();
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        model.addAttribute("showposts", posts);
        model.addAttribute("addpost", posti);
        model.addAttribute("tagpost", posti);
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("popularposts", popularposts);
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        return "archives";
    }

    @GetMapping("/archives/tag/{tag}")
    public String archivesfindbyTag(@PathVariable("tag") String tag, Model model) {
        List posts = postRepository.findByTagitContaining(tag);
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        if(posts.isEmpty()) {
            return "redirect:/archives";
        }
        Post posti = new Post();
        model.addAttribute("showposts", posts);
        model.addAttribute("addpost", posti);
        model.addAttribute("tagpost", posti);
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        model.addAttribute("popularposts", popularposts);
        return "archives";
    }


    @PostMapping("/archives")
    public String archivesfindbynames(@ModelAttribute("addpost") @RequestBody Post post, Model model) {
        Post posti = new Post();
        try {
            posti = postRepository.findByTitleLike(post.getTitle());
        } catch (Exception e) {
           return "redirect:/archives";
        }
        if(posti.getTitle().isEmpty()) {
            return "redirect:/archives";
        }
        System.out.println(posti);
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        List<Post> posts = new ArrayList<>();
        posts.add(posti);
        String titteli = post.getTitle();
        String url = "redirect:/archives/" + titteli;
        model.addAttribute("showposts", posts);
        model.addAttribute("addpost", posti);
        model.addAttribute("tagpost", posti);
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        model.addAttribute("popularposts", popularposts);
        return "archives";

    }


    @PostMapping("/archives/tags")
    public String archivesfindbyTags(@ModelAttribute("tagpost") @RequestBody Post post, Model model) {
        String[] searchParameters = post.getTitle().split("/");
        List postit = new ArrayList();
        List <List> superList = new ArrayList<>();
        for (int i = 0; i < searchParameters.length; i++ ){
            superList.add(postRepository.findByTagsContaining(searchParameters[i]));
            if(superList.isEmpty()) {
                return "redirect:/archives";
            }
        }
        for(int i = 0; i < superList.size(); i++ ){
            for(int a = 0; a < superList.get(i).size(); a++) {
                if(!(postit.contains(superList.get(i).get(a)))) {
                    postit.add(superList.get(i).get(a));
                }
            }
        }
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        Post posti = new Post();
        model.addAttribute("showposts", postit);
        model.addAttribute("addpost", posti);
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        model.addAttribute("popularposts", popularposts);
        model.addAttribute("tagpost", post);
        return "archives";
    }

//    @GetMapping("/findpost/{title}")
//    public String postfindbyname(@PathVariable("title") String title, Model model) {
//        Post post = postRepository.findByTitleLike(title);
//        Post posti = new Post();
//        List<Post> posts = new ArrayList<>();
//        posts.add(post);
//
//        model.addAttribute("showpost", posts);
//        Comment comment = new Comment();
//        model.addAttribute("addcomment", comment);
//        return "post";
//    }

}

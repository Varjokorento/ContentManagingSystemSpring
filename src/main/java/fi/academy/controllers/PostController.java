package fi.academy.controllers;

import fi.academy.models.Comment;
import fi.academy.models.Post;
import fi.academy.models.Tag;
import fi.academy.repositories.CommentRepository;
import fi.academy.repositories.PostRepository;
import fi.academy.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@Controller
@EnableMongoAuditing
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;


    @Autowired
    TagRepository tagRepository;


    @GetMapping("/")
    public String index(Model model) {
        List<Post> posts = postRepository.findAllByOrderByDateDesc();
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc();
        model.addAttribute("showposts", posts);
        model.addAttribute("popularposts", popularposts);
        model.addAttribute("alltags", findUniqueTags());
        return "index";
    }

    @GetMapping("/archives")
    public String archives(Model model) {
        Post post = new Post();
        List<Post> posts = postRepository.findAllByOrderByDateDesc();
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc();
        model.addAttribute("showposts", posts);
        model.addAttribute("addpost", post);
        model.addAttribute("popularposts", popularposts);
        return "archives";
    }

    @GetMapping("/archives/{title}")
    public String archivesfindbyname(@PathVariable("title") String title, Model model) {
        Post post = postRepository.findByTitleLike(title);
        Post posti = new Post();
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        System.out.println(posts);
        model.addAttribute("showposts", posts);
        model.addAttribute("addpost", posti);
        return "archives";
    }

    @GetMapping("/archives/tag/{tag}")
    public String archivesfindbyTag(@PathVariable("tag") String tag, Model model) {
        List posts = postRepository.findByTagitContaining(tag);
        Post posti = new Post();
        System.out.println(posts);
        model.addAttribute("showposts", posts);
        model.addAttribute("addpost", posti);
        return "archives";
    }


    @PostMapping("/archives")
    public String archivesfindbynames(@ModelAttribute("addpost") @RequestBody Post post) {
        Post posti = postRepository.findByTitleLike(post.getTitle());
        List<Post> posts = new ArrayList<>();
        posts.add(posti);
        System.out.println(posts);
        String titteli = post.getTitle();
        String url = "redirect:/archives/" + titteli;
        System.out.println(url);
        return url;
    }


    @PostMapping("/archives/tags")
    public String archivesfindbyTags(@ModelAttribute("addpost") @RequestBody Post post, Model model) {
        String[] searchParameters = post.getTitle().split("/");
        List postit = new ArrayList();
        List <List> superList = new ArrayList<>();
        for (int i = 0; i < searchParameters.length; i++ ){
            superList.add(postRepository.findByTagsContaining(searchParameters[i]));
        }
        for(int i = 0; i < superList.size(); i++ ){
            for(int a = 0; a < superList.get(i).size(); a++) {
                if(!(postit.contains(superList.get(i).get(a)))) {
                    postit.add(superList.get(i).get(a));
                }
            }
        }
        System.out.println(postit);
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc();
        model.addAttribute("showposts", postit);
        model.addAttribute("addpost", post);
        model.addAttribute("popularposts", popularposts);
        return "archives";
    }



    @GetMapping("/post")
    public String listposts(Model model) {
        Post post = new Post();
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc();
        model.addAttribute("addpost", post);
        model.addAttribute("popularposts", popularposts);
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
        if (post.get().getComments() == null) {
            post.get().setComments(new ArrayList<>());
        }
        List<Comment> comments = post.get().getComments();
        comment.setPosted(new Date());
        comments.add(comment);
        post.get().setComments(comments);
        postRepository.save(post.get());
        return "redirect:/post/{_id}";
    }

    @GetMapping("/post/{_id}")
    public String findOne(@PathVariable("_id") String _id, Model model) {
        List<Post> optionalPost = postRepository.getPostById(_id);
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc();
        optionalPost.get(0).addClicks();
        postRepository.save(optionalPost.get(0));
        List<Comment> comments = optionalPost.get(0).getComments();
        String id = optionalPost.get(0).getId();
        System.out.println(optionalPost.get(0).getClicked());
        model.addAttribute("postid", id);
        model.addAttribute("showpost", optionalPost);
        model.addAttribute("popularposts", popularposts);
        Comment comment = new Comment();
        model.addAttribute("addcomment", comment);
        model.addAttribute("showcomments", comments);
        return "post";
    }

    @GetMapping("/post/{_id}/edit")
    public String updatePost(@PathVariable("_id") String _id, Model model) {
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc();
        model.addAttribute("popularposts", popularposts);
        model.addAttribute("editpost", postRepository.findById(_id).get());
        return "edit";
    }

//    @GetMapping("/postfind/{title}")
//    public String findbyName(@PathVariable("title") String title, Model model) {
//        Post p = postRepository.findByTitleLike(title);
//        List<Post> optionalPost = new ArrayList<>();
//        optionalPost.add(p);
//        model.addAttribute("showpost", optionalPost);
//        Comment comment = new Comment();
//        model.addAttribute("addcomment", comment);
//        return "post";
//    }

    @GetMapping("/findpost/{title}")
    public String postfindbyname(@PathVariable("title") String title, Model model) {
        Post post = postRepository.findByTitleLike(title);
        Post posti = new Post();
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        System.out.println(posts);
        model.addAttribute("showpost", posts);
        Comment comment = new Comment();
        model.addAttribute("addcomment", comment);
        return "post";
    }


    //TODO Kaikki responseEntityiksi
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
        List <String> splitted = Arrays.asList(splitattu);
        post.setTagit(splitted);

        for( int i =0; i< splitted.size(); i++) {
            Tag t = new Tag();
            t.setTag(splitted.get(i));
            tagRepository.save(t);
        }

        post.setComments(kommentit);
        post.setClicked(0);
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

    public List<Tag> findUniqueTags() {
        List<Tag> tags = tagRepository.findAll();
        ArrayList<Tag> uniqueTags = new ArrayList<>();
        ArrayList<String> uniqueTagNames = new ArrayList<>();
        for(int i= 0; i < tags.size();i++) {
            if(!(uniqueTagNames.contains(tags.get(i).getTag()))) {
                uniqueTags.add(tags.get(i));
                uniqueTagNames.add(tags.get(i).getTag());
            } else {
            }
        }
        return uniqueTags;
    }
}

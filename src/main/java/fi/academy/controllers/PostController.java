package fi.academy.controllers;

import fi.academy.models.*;
import fi.academy.repositories.CommentRepository;
import fi.academy.repositories.PostRepository;
import fi.academy.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@EnableMongoAuditing
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;


    @Autowired
    TagRepository tagRepository;

    private static int currentPage = 1;
    private static int pageSize = 5;

    @Autowired
    private PostService postService;


    @GetMapping("/")
    public String index(Model model, @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        List<Post> posts = postRepository.findAllByOrderByDateDesc();
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);

        Page<Post> bookPage = postService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("bookPage", bookPage);

        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("showposts", posts);
        model.addAttribute("popularposts", popularposts);
        model.addAttribute("alltags", findUniqueTags());
        return "index";
    }

    @GetMapping("/archives")
    public String archives(Model model) {
        Post post = new Post();
        List<Post> posts = postRepository.findAllByOrderByDateDesc();
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        model.addAttribute("showposts", posts);
        model.addAttribute("addpost", post);
        model.addAttribute("alltags", findUniqueTags());
        model.addAttribute("popularposts", popularposts);
        model.addAttribute("tagpost", post);
        return "archives";
    }

    @GetMapping("/archives/{title}")
    public String archivesfindbyname(@PathVariable("title") String title, Model model) {
        Post post = postRepository.findByTitleLike(title);
        if(post == null) {
            return "redirect:/error";
        }
        Post posti = new Post();
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        model.addAttribute("showposts", posts);
        model.addAttribute("addpost", posti);
        return "archives";
    }

    @GetMapping("/archives/tag/{tag}")
    public String archivesfindbyTag(@PathVariable("tag") String tag, Model model) {
        List posts = postRepository.findByTagitContaining(tag);
        if(posts.isEmpty()) {
            return "redirect:/archives";
        }
        Post posti = new Post();
        model.addAttribute("showposts", posts);
        model.addAttribute("addpost", posti);
        return "archives";
    }


    @PostMapping("/archives")
    public String archivesfindbynames(@ModelAttribute("addpost") @RequestBody Post post) {
        Post posti = postRepository.findByTitleLike(post.getTitle());
        List<Post> posts = new ArrayList<>();
        posts.add(posti);
        String titteli = post.getTitle();
        String url = "redirect:/archives/" + titteli;
        return url;
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
        model.addAttribute("alltags", findUniqueTags());
        model.addAttribute("popularposts", popularposts);
        model.addAttribute("tagpost", post);
        return "archives";
    }



    @GetMapping("/post")
    public String listposts(Model model) {
        Post post = new Post();
        List<Post> popularposts = postRepository.findAllByOrderByClickedDesc(new PageRequest(0, 5));
        model.addAttribute("addpost", post);
        model.addAttribute("alltags", findUniqueTags());
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
        comment.setPostedDate(new Date().toString());
        comments.add(comment);
        commentRepository.save(comment);
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
        for (int i = 0; i < kommentit.size(); i++ ) {
            if(kommentit.get(i).getPostedDate().equals(comment.getPostedDate())) {
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
        model.addAttribute("alltags", findUniqueTags());
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
        model.addAttribute("alltags", findUniqueTags());
        model.addAttribute("editpost", postRepository.findById(_id).get());
        return "edit";
    }

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
        SimpleDateFormat modified = new SimpleDateFormat("dd-MM-yyyy");
        newPost.get().setModifiedDatetoDisplay(modified.format(new Date().toString()));
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

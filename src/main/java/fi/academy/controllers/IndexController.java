package fi.academy.controllers;

import fi.academy.helpfunctions.MonthGetter;
import fi.academy.helpfunctions.TagGetter;
import fi.academy.models.Post;
import fi.academy.models.PostService;
import fi.academy.repositories.PostRepository;
import fi.academy.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Controller
@EnableMongoAuditing
public class IndexController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    PostService postService;


    private static int currentPage = 1;
    private static int pageSize = 5;

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
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("addpost", posts);
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        return "index";
    }
}

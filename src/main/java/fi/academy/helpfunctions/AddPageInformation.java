package fi.academy.helpfunctions;

import fi.academy.models.Comment;
import fi.academy.models.Post;
import fi.academy.repositories.PostRepository;
import fi.academy.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.List;

public class AddPageInformation {


    public static void addInformation(TagRepository tagRepository, PostRepository postRepository, Model model, List posts, Post posti, List popularposts ) {

        model.addAttribute("showposts", posts);
        model.addAttribute("addpost", posti);
        model.addAttribute("tagpost", posti);
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        model.addAttribute("popularposts", popularposts);
    }

    public static void addInformation(TagRepository tagRepository, PostRepository postRepository, Model model, Post posti, List popularposts ) {

        model.addAttribute("addpost", posti);
        model.addAttribute("alltags", TagGetter.findUniqueTags(tagRepository));
        model.addAttribute("allmonths", MonthGetter.findMonths(postRepository));
        model.addAttribute("popularposts", popularposts);
    }


    public static void addInformation(TagRepository tagRepository, PostRepository postRepository, Model model, String id, List optionalPost, List popularposts, List comments)
    {
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
    }

}

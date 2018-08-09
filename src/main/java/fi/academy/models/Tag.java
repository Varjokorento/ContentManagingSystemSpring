package fi.academy.models;

import fi.academy.repositories.TagRepository;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Tämä on Tag-tietokannan alkio

public class Tag {
    @Id
    private String id;
    private String tag;


    public Tag() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tag='" + tag + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public static void deleteTags(Optional<Post> post, TagRepository tagRepository) {
        List<String> tagsOfPost = post.get().getTagit();
        List<String> tagsToBeSought = new ArrayList<>();
        List<List<Tag>> tags = new ArrayList<>();
        for(int x= 0; x < tagsOfPost.size(); x++) {
            tagsToBeSought.add(tagsOfPost.get(x));
        }
        for(int a = 0; a < tagsToBeSought.size(); a++) {
            tags.add(tagRepository.findByTag(tagsToBeSought.get(a)));
        }
        for(int i=0; i < tags.size(); i++) {
            for(int u = 0; u < 1; u++) {
                Optional<Tag> t = tagRepository.findById(tags.get(i).get(u).getId());
                System.out.println(t.toString());
                tagRepository.deleteById(tags.get(i).get(u).getId());
            }
        }
    }

    public void setId(String id) {
        this.id = id;
    }
}

package fi.academy.helpfunctions;

import fi.academy.models.Tag;
import fi.academy.repositories.TagRepository;

import java.util.ArrayList;
import java.util.List;

public class TagGetter {

    public static List<Tag> findUniqueTags(TagRepository tagRepository) {
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



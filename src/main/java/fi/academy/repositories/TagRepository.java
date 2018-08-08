package fi.academy.repositories;

import fi.academy.models.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TagRepository extends MongoRepository <Tag, String> {

    List<Tag> findDistinctByTagContaining(String jotain);
    List<Tag> findAllDistinctTagByTag();
    List<Tag> findByTag(String tag);
    Long deleteFirstTagByTag(String tag);

}

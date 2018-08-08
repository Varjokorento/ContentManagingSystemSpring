package fi.academy.repositories;

import fi.academy.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

List<Post> getPostById(String id);
//
List<Post> findAllByOrderByDateDesc();

Post findByTitleLike(String title);

List<Post> findAllByOrderByClickedDesc(Pageable pageSize);

List<Post> findByTagitContaining(String tagi);
List<Post> findByTagsContaining(String tagit);

}

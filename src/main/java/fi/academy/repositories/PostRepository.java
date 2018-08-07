package fi.academy.repositories;

import fi.academy.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

List<Post> getPostById(String id);
List<Post> findAllByOrderByDateDesc();

List<Post> findAllByOrderByDateDesc();


}

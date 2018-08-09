package fi.academy.models;

import fi.academy.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;


    public List<Post> findPosts(PostRepository postRepository) {
        List<Post> posts = postRepository.findAllByOrderByDateDesc();
        return posts;
    }


    public Page<Post> findPaginated(Pageable pageable) {
        List<Post> posts = findPosts(postRepository);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Post> list;

        if (posts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, posts.size());
            list = posts.subList(startItem, toIndex);
        }

        Page<Post> bookPage
                = new PageImpl<Post>(list, PageRequest.of(currentPage, pageSize), posts.size());

        return bookPage;
    }
}
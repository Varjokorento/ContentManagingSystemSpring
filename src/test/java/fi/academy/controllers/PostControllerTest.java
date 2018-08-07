package fi.academy.controllers;

import fi.academy.UltimatecmsApplicationTests;
import fi.academy.models.Post;
import fi.academy.repositories.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.hamcrest.core.IsEqual.equalTo;

import java.awt.*;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class PostControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    PostRepository postRepository;

    private static String baseurl = "/post";

    @Test
    public void testIfPostReturnsSomething() {
        ResponseEntity<List<Post>> response = restTemplate.exchange("http://localhost:8080/post", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Post>>() {});
        assertThat(response.getStatusCodeValue(), is(200));
        List<Post> posts = response.getBody();
        assertThat(posts, is(notNullValue()));
    }

    @Test
    public void testIfPostCantBeFoundById() {
        Iterable<Post> post = postRepository.findAll();
        Post findpost = post.iterator().next();

        ResponseEntity<Post> postResponseEntity = restTemplate.getForEntity("http://localhost:8080/post/{_id}", Post.class, findpost.getId());
        assertEquals(200, postResponseEntity.getStatusCodeValue());
        Post postt = postResponseEntity.getBody();
        assertThat(postt, is(notNullValue()));
        assertThat(postt.getId(), is(equalTo(findpost.getId())));
        assertThat(postt.getTitle(), is(equalTo((findpost.getTitle()))));
    }

    @Test
    public void testIfPostCanBeAdded() {
        // Ei mene läpi Actual   Expected: Expected :is <Post{id='5a', title='Heit', text='Hei', date=Hei}> Actual: <Post{id='5a', title='Heit', text='Hei', date=Hei}>
        Post p = new Post("5a", "Heit", "Hei", new Date());
        ResponseEntity<Post> response = restTemplate.postForEntity(URI.create("http://localhost:8080/post"), p, Post.class);
        assertThat(response.getStatusCode(), (equalTo(HttpStatus.CREATED)));
        String location = response.getHeaders().get("Location").get(0);
        String testLocation = URI.create(location).getPath();
        response = restTemplate.getForEntity(testLocation, Post.class);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.OK)));
        Post found = response.getBody();
        assertEquals(found, p);
    }


    @Test
    public void testIfPostCanBeDeleted() {
        List <Post> postit = postRepository.findAll();
        assertThat(postit.get(0), is(notNullValue()));
        String id = postit.get(0).getId();
        ResponseEntity<?> response = restTemplate.exchange("http://localhost:8080/post/{_id}", HttpMethod.DELETE, null, Post.class, id);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));

        response = restTemplate.getForEntity("http://localhost:8080/post/{_id}", Post.class, id);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));

        // Testi ei mene läpi. Syy: Expected scheme-specific part at index 5: http:
        response = restTemplate.exchange("http:localhost:8080/post/{_id}", HttpMethod.DELETE, null, Post.class, id);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testIfPostCantBeUpdated() throws Exception {
        List <Post> postit = postRepository.findAll();
        Post firstPost = postit.get(1);
        System.out.println(firstPost);
        String url = "http://localhost:8080/post/" + firstPost.getId();
        System.out.println(url);
        String oldText = firstPost.getText();
        String newText = oldText + "_modded";
        firstPost.setText(newText);
        RequestEntity<Post> requestEntity = new RequestEntity<Post>(firstPost, HttpMethod.PUT, new URI(url));
        ResponseEntity<Post> response = restTemplate.exchange(requestEntity, Post.class);
        System.out.println(response);
        Post found = response.getBody();
        assertThat(found.getText(), equalTo(newText));

        ResponseEntity<Post> postResponseEntity = restTemplate.getForEntity("http://localhost:8080/post/{_id}", Post.class, found.getId());
        assertThat(postResponseEntity.getBody(), is(equalTo(found)));

    }


}
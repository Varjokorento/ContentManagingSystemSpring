package fi.academy.models;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document(collection = "posts")
@EnableMongoAuditing
public class Post {

    @Id
    private String id;

    private String title;
    private String text;
    @LastModifiedDate
    private Date modifieddate;
    private Integer clicked;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @CreatedDate
    private Date date;

    private List<Comment> comments;

    public Post() {}

    public Post(String title, String text, Date date) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.clicked = 0;
    }

//    public Post(String title, String text, Date date) {
//        this.title = title;
//        this.text = text;
//        this.date = date;
//    }




    public Post(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Post(String title, String text, Date date, List<Comment> comments) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.comments = comments;
    }


    public Post(String id, String title, String text, Date date) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Date getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(Date modifieddate) {
        this.modifieddate = modifieddate;
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Post{");
        sb.append("id='").append(id).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", modifieddate=").append(modifieddate);
        sb.append(", clicked=").append(clicked);
        sb.append(", date=").append(date);
        sb.append(", comments=").append(comments);
        sb.append('}');
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
                Objects.equals(title, post.title) &&
                Objects.equals(text, post.text) &&
                Objects.equals(date, post.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, text, date);
    }

    public void addClicks() {
        clicked++;
    }
}

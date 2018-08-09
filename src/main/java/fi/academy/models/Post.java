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

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document(collection = "posts")
@EnableMongoAuditing
public class Post {

    @Id
    private String id;

    @NotEmpty(message = "Title cannot be empty!")
    private String title;
    private String text;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @LastModifiedDate
    private Date modifieddate;
    private String modifiedDatetoDisplay;
    private int clicked = 0;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @CreatedDate
    private Date date;
    private List<Comment> comments;
    private String tags;
    private List<String> tagit;
    private int likes;

    public Post() {}

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

    public String getModifiedDatetoDisplay() {
        return modifiedDatetoDisplay;
    }

    public void setModifiedDatetoDisplay(String modifiedDatetoDisplay) {
        this.modifiedDatetoDisplay = modifiedDatetoDisplay;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public List<String> getTagit() {
        return tagit;
    }

    public void setTagit(List<String> tagit) {
        this.tagit = tagit;
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

    public void addLikes() { likes++;}

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}

package fi.academy.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.Date;

@EnableMongoAuditing
public class Comment {

    @Id
    private String id;

    private String nickname;
    private String comment;
    @CreatedDate
    private Date posted;
    private String postedDate;
    public Comment() {}

    public Comment(String nickname, String comment, Date posted) {
        this.nickname = nickname;
        this.comment = comment;
        this.posted = posted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Comment:");
        sb.append("Nickname: ").append(nickname).append('\n');
        sb.append("Comment: ").append(comment).append('\n');
        sb.append("Date: ").append(posted);
        return sb.toString();
    }
}

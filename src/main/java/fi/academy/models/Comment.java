package fi.academy.models;

import org.springframework.data.annotation.Id;

import java.sql.Date;

public class Comment {

    @Id
    private String id;

    private String nickname;
    private String comment;
    private Date posted;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Comment{");
        sb.append("id='").append(id).append('\'');
        sb.append(", nickname='").append(nickname).append('\'');
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", posted=").append(posted);
        sb.append('}');
        return sb.toString();
    }
}

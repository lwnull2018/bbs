package com.bbs.remark.pojo;
import java.util.List;

public class Note {

    private String id;			//id

    private String nickname;	//昵称

    private String avatar;	    //头像

    private String content;		//内容

    private String createTime;		//发布时间

    private String respondent;	//回复对象昵称

    private String resId;		//回复对象id

    private String replyPost;	//根留言id

    private List<Note> follows; //回复列表，仅根留言有

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRespondent() {
        return respondent;
    }

    public void setRespondent(String respondent) {
        this.respondent = respondent;
    }

    public String getReplyPost() {
        return replyPost;
    }

    public void setReplyPost(String replyPost) {
        this.replyPost = replyPost;
    }

    public List<Note> getFollows() {
        return follows;
    }

    public void setFollows(List<Note> follows) {
        this.follows = follows;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", respondent='" + respondent + '\'' +
                ", resId='" + resId + '\'' +
                ", replyPost='" + replyPost + '\'' +
                ", follows=" + follows +
                '}';
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }
}

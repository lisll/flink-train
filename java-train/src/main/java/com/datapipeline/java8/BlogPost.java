package com.datapipeline.java8;

import java.util.Date;

public class BlogPost {
    String title;
    String author;
    BlogPostType type;
    int likes;
    Date date;

    public BlogPost() {
    }

    public BlogPost(String title, String author, BlogPostType type, int likes,Date date) {
        this.title = title;
        this.author = author;
        this.type = type;
        this.likes = likes;
        this.date = date;
    }

    @Override
    public String toString() {
        return "BlogPost{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", type=" + type +
                ", likes=" + likes +
                ", date=" + date +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BlogPostType getType() {
        return type;
    }

    public void setType(BlogPostType type) {
        this.type = type;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    enum BlogPostType {
        NEWS,
        REVIEW,
        GUIDE
    }

    public class Tuple {
        String author;

        BlogPostType type;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public BlogPostType getType() {
            return type;
        }

        public void setType(BlogPostType type) {
            this.type = type;
        }
    }
}

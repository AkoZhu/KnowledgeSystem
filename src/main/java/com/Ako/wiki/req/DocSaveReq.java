package com.Ako.wiki.req;

import javax.validation.constraints.NotNull;

import com.Ako.wiki.domain.Content;

public class DocSaveReq {
    private Long id;

    @NotNull(message = "[Ebook id] can't be null")
    private Long ebookId;

    @NotNull(message = "[Parent id] can't be null")
    private Long parent;

    @NotNull(message = "[Name] can't be null")
    private String name;

    @NotNull(message = "[Order] can't be null")
    private Integer sort;

    @NotNull(message = "[Conten] can't be null")
    private String content;

    private Integer viewCount;

    private Integer voteCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEbookId() {
        return ebookId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "DocSaveReq{" +
                "id=" + id +
                ", ebookId=" + ebookId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", content='" + content + '\'' +
                '}';
    }
}
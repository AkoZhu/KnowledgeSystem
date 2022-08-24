package com.Ako.wiki.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class PageReq {
    @NotNull(message="Page No. can't be null.")
    private int page;
    
    @NotNull(message="The number of Ebooks per page can't be null.")
    @Max(value = 1000, message="The number of Ebooks per page can't exceed 1000.")
    private int size;

    public void setSize(int size) {
        this.size = size;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString(){
        final StringBuffer sb = new StringBuffer("PageReq{");
        sb.append("page=").append(page);
        sb.append(", size=").append(size);
        sb.append("}");
        return sb.toString();
    }
}
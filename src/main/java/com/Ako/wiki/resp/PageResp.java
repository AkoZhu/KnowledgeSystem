package com.Ako.wiki.resp;

import java.util.List;

public class PageResp<T> {

    private Long total;
    
    private List<T> list;

    public void setTotal(Long total) {
        this.total = total;
    }
    
    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public List<T> getList() {
        return list;
    }


    @Override
    public String toString(){
        final StringBuffer sb = new StringBuffer("PageReq{");
        sb.append("total=").append(total);
        sb.append(", list=").append(list);
        sb.append("}");
        return sb.toString();
    }
}
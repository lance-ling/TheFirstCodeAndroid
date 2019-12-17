package com.lingsh.litepaltest;

import org.litepal.crud.LitePalSupport;

/**
 * 书籍分类
 * 包含分类名称 分类代码
 *
 * @author lingsh
 * @version 1.0
 * @date 2019/12/17 17:43
 **/


public class Category extends LitePalSupport {

    private int id;
    private String categoryName;
    private int categoryCode;

    public Category() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }
}

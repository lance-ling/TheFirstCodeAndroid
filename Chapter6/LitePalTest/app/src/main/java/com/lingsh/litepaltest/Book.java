package com.lingsh.litepaltest;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Bean类 用于ORM
 * 包括书名 作者 价格 书页数目 出版社
 *
 * @author lingsh
 * @version 1.0
 * @date 2019/12/17 17:21
 **/

public class Book extends LitePalSupport {
    @Column(unique = true)
    private int id;

    private String name;
    private String author;
    private double price;
    private int pages;
    private String press;

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", pages=" + pages +
                ", press='" + press + '\'' +
                '}';
    }
}

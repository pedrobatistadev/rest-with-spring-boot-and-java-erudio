package br.com.erudio.data.dto.v1;

import java.util.Date;
import java.util.Objects;

public class BookCreateDTO {

    private Long id;
    private String author;
    private Date launch_date;
    private Double price;
    private String title;

    public BookCreateDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunch_date() {
        return launch_date;
    }

    public void setLaunch_date(Date launch_date) {
        this.launch_date = launch_date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BookCreateDTO that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(author, that.author) && Objects.equals(launch_date, that.launch_date) && Objects.equals(price, that.price) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, launch_date, price, title);
    }
}

package gamo.villalba.sergio.models;

import gamo.villalba.sergio.enums.FormatMovie;
import jakarta.persistence.*;

@Entity
@Table(name = "movie")
public class MovieModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;
    String title;
    FormatMovie format;
    int year;
    double price;
    String director;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FormatMovie getFormat() {
        return format;
    }

    public void setFormat(FormatMovie format) {
        this.format = format;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}

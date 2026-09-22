package movie;

import java.util.List;

public class Movie {
    private String title;
    private int year;
    private VisualisationInfo info;

    public String getTitle() { return title; }
    public int getYear() { return year; }

    public Movie(String title, int year, VisualisationInfo info) {
        this.title = title;
        this.year = year;
        this.info = info;
    }
}

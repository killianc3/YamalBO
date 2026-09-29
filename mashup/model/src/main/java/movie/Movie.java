package movie;

public class Movie {
    private String title;
    private int year;
    private VisualisationInfo info;

    public Movie(String title, int year, VisualisationInfo info) {
        this.title = title;
        this.year = year;
        this.info = info;
    }

    public String getTitle() { return title; }
    public int getYear() { return year; }
    public VisualisationInfo getInfo() { return info; }
}

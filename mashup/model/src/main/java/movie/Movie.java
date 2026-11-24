package movie;

public class Movie {
    private String title;
    private int year;
    private VisualisationInfo visualisationInfo;

    public Movie(String title, int year, VisualisationInfo visualisationInfo) {
        this.title = title;
        this.year = year;
        this.visualisationInfo = visualisationInfo;
    }

    public String getTitle() { return title; }
    public int getYear() { return year; }
    public VisualisationInfo getVisualisationInfo() { return visualisationInfo; }
}

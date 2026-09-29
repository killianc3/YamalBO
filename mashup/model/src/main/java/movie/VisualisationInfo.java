package movie;

import java.util.Date;

public class VisualisationInfo {
    private Date date;
    private int score;

    public VisualisationInfo(Date date, int score) {
        this.date = date;
        this.score = score;
    }

    public Date getDate() { return date; }
    public int getScore() { return score; }
}

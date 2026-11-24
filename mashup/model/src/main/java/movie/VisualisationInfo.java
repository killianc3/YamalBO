package movie;

import java.util.Date;

public class VisualisationInfo {
    private Date visualisationDate;
    private int punctuation;

    public VisualisationInfo(Date visualisationDate, int punctuation) {
        this.visualisationDate = visualisationDate;
        this.punctuation = punctuation;
    }

    public Date getVisualisationDate() { return visualisationDate; }
    public int getPunctuation() { return punctuation; }
}

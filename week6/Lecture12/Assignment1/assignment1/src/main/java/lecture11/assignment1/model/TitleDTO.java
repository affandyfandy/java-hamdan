package lecture11.assignment1.model;

import lecture11.assignment1.model.Title;
import lombok.Data;

import java.util.Date;

@Data
public class TitleDTO {
    private String title;
    private Date fromDate;
    private Date toDate;

    public TitleDTO(Title title) {
        this.title = title.getTitle();
        this.fromDate = title.getId().getFromDate();
        this.toDate = title.getToDate();
    }
}

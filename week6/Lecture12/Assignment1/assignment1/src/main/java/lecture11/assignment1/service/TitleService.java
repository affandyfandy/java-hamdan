package lecture11.assignment1.service;

import lecture11.assignment1.model.Title;
import lecture11.assignment1.model.Title.TitleId;
import lecture11.assignment1.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TitleService {

    @Autowired
    private TitleRepository titleRepository;

    public List<Title> getTitlesByEmployeeId(int empNo) {
        return titleRepository.findByEmployeeEmpNo(empNo);
    }

    public Title addTitle(Title title) {
        return titleRepository.save(title);
    }

    public Title updateTitle(int empNo, Date fromDate, Title titleDetails) {
        TitleId id = new TitleId();
        id.setEmpNo(empNo);
        id.setFromDate(fromDate);
        Title title = titleRepository.findById(id).orElseThrow();
        title.setTitle(titleDetails.getTitle());
        title.setToDate(titleDetails.getToDate());
        return titleRepository.save(title);
    }
}

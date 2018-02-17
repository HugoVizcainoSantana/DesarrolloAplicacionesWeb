package daw.spring.service;

import daw.spring.model.Issue;
import daw.spring.model.Home;
import daw.spring.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    public Issue findOneById(Long id){

        return issueRepository.findOne(id);
    }

    public Issue findOneByHome(Home home){

        return issueRepository.findOne(home.getId());
    }

    public List<Issue> findAllIssues(){
        return issueRepository.findAll();
    }


    public void saveIssue(Issue notification){
        issueRepository.save(notification);
    }

    public void deleteIssue(Issue notification){
        issueRepository.delete(notification);
    }

    public Page<Issue> findAllIssuePage(PageRequest pageRequest){
        return issueRepository.findAll(pageRequest);
    }

    public void save(Issue issue1) {
        issueRepository.save(issue1);
    }
}

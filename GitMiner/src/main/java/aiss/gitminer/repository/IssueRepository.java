package aiss.gitminer.repository;

import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository  extends JpaRepository<Issue, String> {

    Page<Issue> findByState(String state,Pageable pageable);
    Page<Issue> findByAuthor(User author, Pageable pageable);
}

package aiss.gitminer.repository;

import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommitRepository  extends JpaRepository<Commit, String> {
    List<Commit> findByauthorEmail(String email);
    List<Commit> findBycommitterName(String committer_name);


}

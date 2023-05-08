package aiss.gitminer.repository;

import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommitRepository  extends JpaRepository<Commit, String> {
    Page<Commit> findByauthorEmail(String email, Pageable pageable);
    Page<Commit> findBycommitterName(String committer_name, Pageable pageable);


}

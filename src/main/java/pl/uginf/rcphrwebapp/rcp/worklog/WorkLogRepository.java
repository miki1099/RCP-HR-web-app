package pl.uginf.rcphrwebapp.rcp.worklog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;

@Repository
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

    Optional<WorkLog> findByStatusNotNullAndUser_Username(String username);

    @Query("select w from WorkLog w where ((w.from BETWEEN ?1 AND ?2) OR (w.to BETWEEN ?1 AND ?2)) AND w.user.username = ?3")
    List<WorkLog> findAllByBetweenFromAndToAndUserId(@NonNull Date from, @NonNull Date to, String username);
}

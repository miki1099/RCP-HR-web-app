package pl.uginf.rcphrwebapp.rcp.worklog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;

@Repository
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

    Optional<WorkLog> findByStatusNotNullAndUser_Username(String username);

    @Query("select w from WorkLog w where ((w.from BETWEEN ?2 AND ?3) OR (w.to BETWEEN ?2 AND ?3)) AND w.user.username = ?1")
    List<WorkLog> findAllByBetweenFromAndToAndUserId(String username, Date from, Date to);

    List<WorkLog> findAllByIsApprovedFalseAndUser_Username(String username);
}

package pl.uginf.rcphrwebapp.rcp.worklog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import pl.uginf.rcphrwebapp.rcp.worklog.model.WorkLog;

@Repository
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

    Optional<WorkLog> findByStatusNotNullAndUser_Username(String username);

    List<WorkLog> findByToAfterAndFromBeforeAndUser_UsernameOrderByFromAsc(@NonNull Date to, @NonNull Date from, String username);
}

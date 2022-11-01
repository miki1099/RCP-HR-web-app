package pl.uginf.rcphrwebapp.hr.workinfo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.uginf.rcphrwebapp.hr.user.model.User;

@Repository
public interface WorkInfoRepository extends JpaRepository<WorkInfo, Long> {

    List<WorkInfo> findAllByUserId_Username(String username);

    @Query("SELECT w FROM WorkInfo w WHERE ((w.from BETWEEN ?1 AND ?2) OR (w.to BETWEEN ?1 AND ?2)) AND w.userId = ?3")
    List<WorkInfo> getAllByConflictContractDateAndUserId(Date from, Date to, User userId);
    //TODO Maybe change query to method
}

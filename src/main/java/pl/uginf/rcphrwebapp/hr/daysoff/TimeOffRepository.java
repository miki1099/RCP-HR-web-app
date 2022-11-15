package pl.uginf.rcphrwebapp.hr.daysoff;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.uginf.rcphrwebapp.hr.daysoff.model.DaysOff;

@Repository
public interface TimeOffRepository extends JpaRepository<DaysOff, Long> {

    List<DaysOff> findAllByUser_UsernameAndApprovedIsFalse(String username);
    //TODO clear DB if not approved are outdated instead of getting after today date?

    @Query("SELECT w FROM DaysOff w WHERE ((w.startDate BETWEEN ?2 AND ?3) OR (w.endDate BETWEEN ?2 AND ?3)) AND w.user.username = ?1")
    List<DaysOff> getAllForUserAndBetweenPeriod(String username, Date from, Date to);
}

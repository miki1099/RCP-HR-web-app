package pl.uginf.rcphrwebapp.hr.workinfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkInfoRepository extends JpaRepository<WorkInfo, Long> {

    List<WorkInfo> findAllByUserId_Username(String username);
}

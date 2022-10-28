package pl.uginf.rcphrwebapp.hr.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPesel(String pesel);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}

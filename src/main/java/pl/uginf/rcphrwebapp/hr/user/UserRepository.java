package pl.uginf.rcphrwebapp.hr.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.uginf.rcphrwebapp.hr.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPesel(String pesel);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> getUserByUsername(String username);

}

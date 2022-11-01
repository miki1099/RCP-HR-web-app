package pl.uginf.rcphrwebapp.hr.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.uginf.rcphrwebapp.hr.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPesel(String pesel);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> getUserByUsername(String username);

}

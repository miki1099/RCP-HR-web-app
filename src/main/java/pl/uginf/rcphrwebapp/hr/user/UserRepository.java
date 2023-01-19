package pl.uginf.rcphrwebapp.hr.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.uginf.rcphrwebapp.hr.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPesel(String pesel);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findUserByUsername(String username);

    Optional<User> findAllByBoss_Username(String username);

    @Modifying
    @Query("update User u set u.password = :password where u.username = :username")
    void changePassword(@Param(value = "username") String username, @Param(value = "password") String password);

}

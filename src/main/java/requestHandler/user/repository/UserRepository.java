package requestHandler.user.repository;

import requestHandler.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User AS u WHERE u.username like %?1%")
    User findUserByUsernameLikeIgnoreCase(String username);

   Optional<User> findUserByUsername(String username);
}
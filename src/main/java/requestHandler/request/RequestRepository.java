package requestHandler.request;

import requestHandler.utils.RequestStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByUser_IdOrderByCreated(Long userId, Pageable pageable);

    List<Request> findAllByStatusLike(RequestStatus status, Pageable pageable);

    @Query("SELECT r FROM Request as r WHERE r.status = 'SENT' AND r.user.username like %?1% ")
    List<Request> findByUsername(String username, Pageable pageable);
}
package requestHandler.request.service;

import requestHandler.request.Request;
import requestHandler.request.RequestRepository;
import requestHandler.utils.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    @Override
    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    @Override
    public List<Request> getRequestsForOperator(RequestStatus requestStatus, Pageable pageable) {
        return requestRepository.findAllByStatusLike(requestStatus, pageable);
    }

    @Override
    public List<Request> getRequestsByUserName(String userName, Pageable pageable) {
        return requestRepository.findByUsername(userName, pageable);
    }

    @Override
    public List<Request> getRequestsById(Long userId, Pageable pageable) {
        return requestRepository.findAllByUser_IdOrderByCreated(userId, pageable);
    }
}
package requestHandler.request.service;

import requestHandler.request.Request;
import requestHandler.utils.RequestStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestService {
    Request saveRequest(Request request);

    List<Request> findAll();

    List<Request> getRequestsForOperator(RequestStatus requestStatus, Pageable pageRequest);

    List<Request> getRequestsByUserName(String username, Pageable pageRequest);

    List<Request> getRequestsById(Long userId, Pageable pageable);
}
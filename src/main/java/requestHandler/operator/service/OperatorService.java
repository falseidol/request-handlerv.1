package requestHandler.operator.service;

import requestHandler.request.Request;

import java.util.List;

public interface OperatorService {

    List<Request> getAllSentRequests(int from, int size, String sortBy);

    List<Request> getRequestsByUserName(String username, int from, int size, String sortBy);

    Request reviewRequest(Long requestId, String status);
}
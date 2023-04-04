package requestHandler.user.service;

import requestHandler.request.Request;
import requestHandler.user.User;

import java.util.List;

public interface UserService {
    Request createRequest(Request request);

    List<Request> getAll();

    Request editDraftRequest(Long userId, Long requestId, Request request);

    Request sendRequestToReview(Long userId, Long requestId);

    List<Request> getRequests(Long userId, int from, int size, String sortBy);

    User findUserByUsername(String username);

    List<User> getUsers();
}
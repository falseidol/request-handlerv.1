package requestHandler.user.service;

import requestHandler.exception.ObjectNotFoundException;
import requestHandler.request.Request;
import requestHandler.request.RequestRepository;
import requestHandler.request.service.RequestService;
import requestHandler.user.User;
import requestHandler.user.repository.UserRepository;
import requestHandler.utils.MyPageRequest;
import requestHandler.utils.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestService requestService;

    @Override
    public Request createRequest(Request request) {
        log.info("Создание заявки");
        Long userId = request.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        request.setUser(user);
        request.setCreated(LocalDateTime.now());
        request.setStatus(RequestStatus.DRAFT);  // По умолчанию заявка является черновиком
        return requestService.saveRequest(request);
    }

    @Override
    public List<Request> getAll() {
        return requestService.findAll();
    }

    @Override
    public Request editDraftRequest(Long userId, Long requestId, Request request) {
        log.info("Редактирование заявки");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        Request requestToUpdate = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Заявка не найдена"));
        if (requestToUpdate.getUser().getId() != null &&
                requestToUpdate.getStatus() != RequestStatus.DRAFT &&
                Objects.equals(requestToUpdate.getUser().getId(), userId)) {
            throw new ObjectNotFoundException("У пользователя нету заявок или он не является владельцем");
        }
        if (request.getText() != null) {
            requestToUpdate.setText(request.getText());
        }
        return requestRepository.save(requestToUpdate);
    }

    @Override
    public Request sendRequestToReview(Long userId, Long requestId) {
        log.info("Отправка заявки на рассмотрение");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Заявка не найдена"));
        if (!Objects.equals(user.getId(), request.getUser().getId())) {
            throw new ObjectNotFoundException("Вы не являетесь владельцем заявки");
        }
        request.setStatus(RequestStatus.SENT);
        return request;
    }

    @Override
    public List<Request> getRequests(Long userId, int from, int size, String sortBy) {
        Pageable pageRequest;
        log.info("Выводим заявки");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        if (Objects.equals(sortBy, "DESC")) {
            pageRequest = MyPageRequest.makePageRequest(from, size, Sort.by(Sort.Direction.DESC, "created"));
        } else {
            pageRequest = MyPageRequest.makePageRequest(from, size, Sort.by(Sort.Direction.ASC, "created"));
        }
        List<Request> requestList = requestService.getRequestsById(userId, pageRequest);
        for (Request request : requestList) {
            String textReplacer = request.getText().replaceAll("-", "");
            request.setText(textReplacer);
        }
        return requestList;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsernameLikeIgnoreCase(username);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
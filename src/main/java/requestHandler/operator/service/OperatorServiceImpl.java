package requestHandler.operator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import requestHandler.exception.ObjectNotFoundException;
import requestHandler.exception.ValidationException;
import requestHandler.request.Request;
import requestHandler.request.RequestRepository;
import requestHandler.request.service.RequestService;
import requestHandler.utils.MyPageRequest;
import requestHandler.utils.RequestStatus;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class OperatorServiceImpl implements OperatorService {
    private final RequestRepository requestRepository;
    private final RequestService requestService;

    @Override
    public List<Request> getAllSentRequests(int from, int size, String sortBy) {
        log.info("Возвращаем заявки поданные на рассмотрение");
        Pageable pageRequest;
        if (sortBy.equals("DESC")) {
            pageRequest = MyPageRequest.makePageRequest(from, size, Sort.by(Sort.Direction.DESC, "created"));
        } else {
            pageRequest = MyPageRequest.makePageRequest(from, size, Sort.by(Sort.Direction.ASC, "created"));
        }
        List<Request> requestList = requestService.getRequestsForOperator(RequestStatus.SENT, pageRequest);
        if (requestList.stream().anyMatch(request -> request.getText().contains("—"))) {
            return requestList;
        } else {
            splitText(requestList);
        }
        return requestList;
    }
    @Override
    public List<Request> getRequestsByUserName(String username, int from, int size, String sortBy) {
        if (username == null) {
            throw new ValidationException("Не введено имя пользователя");
        }
        log.info("Возвращаем заявки пользователя по имени");
        Pageable pageRequest;
        if (sortBy.equals("DESC")) {
            pageRequest = MyPageRequest.makePageRequest(from, size, Sort.by(Sort.Direction.DESC, "created"));
        } else {
            pageRequest = MyPageRequest.makePageRequest(from, size, Sort.by(Sort.Direction.ASC, "created"));
        }
        List<Request> requestList = requestService.getRequestsByUserName(username, pageRequest);
        if (requestList.stream().anyMatch(request -> request.getText().contains("—"))) {
            return requestList;
        } else {
            splitText(requestList);
        }
        return requestList;
    }

    @Override
    public Request reviewRequest(Long requestId, String status) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Заявка не найдена"));
        if (request.getStatus() != RequestStatus.SENT) {
            throw new ValidationException("Статус заявки не SENT");
        }
        if (status.equalsIgnoreCase(String.valueOf(RequestStatus.ACCEPTED))) {
            request.setStatus(RequestStatus.ACCEPTED);
        } else if (status.equalsIgnoreCase(String.valueOf(RequestStatus.REJECTED))) {
            request.setStatus(RequestStatus.REJECTED);
        } else {
            throw new ValidationException("Такого действия нету");
        }
        return requestRepository.save(request);
    }

    private List<Request> splitText(List<Request> requestList) {
        if (requestList.isEmpty()) {
            return Collections.emptyList();
        }
        for (Request requestik : requestList) {
            String[] bebra = requestik.getText().split("");
            StringBuilder abob = new StringBuilder();
            for (String s : bebra) {
                abob.append(s);
            }
            abob = new StringBuilder(abob.toString().replaceAll("", "—"));
            abob = new StringBuilder(abob.toString().replaceFirst("—", ""));
            StringBuilder stringBuilder;
            stringBuilder = new StringBuilder(abob.toString());
            stringBuilder.deleteCharAt(abob.length() - 1);
            requestik.setText(String.valueOf(stringBuilder));
        }
        return requestList;
    }
}
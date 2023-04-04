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
        return splitText(requestList);
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
        return requestService.getRequestsByUserName(username, pageRequest);
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
        for (Request requestik : requestList) {
            String[] bebra = requestik.getText().split("");
            String abob = "";
            for (int i = 0; i < bebra.length; i++) {
                abob += bebra[i];
            }
            abob = abob.replaceAll("", "-");
            abob = abob.replaceFirst("-", "");
            System.out.println(abob);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder = new StringBuilder(abob);
            System.out.println(abob.length());
            stringBuilder.deleteCharAt(abob.length() - 1);
            requestik.setText(String.valueOf(stringBuilder));
        }
        return requestList;
    }
}
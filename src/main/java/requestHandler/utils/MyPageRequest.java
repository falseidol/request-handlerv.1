package requestHandler.utils;

import requestHandler.exception.ObjectNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class MyPageRequest {
    public static Pageable makePageRequest(Integer from, Integer size, Sort sort) {
        if (from == null || size == null)
            return null;

        if (from < 0 || size <= 0)
            throw new ObjectNotFoundException("Неправильно указанны параметры для просмотра!");
        return PageRequest.of(from / size, size, sort);
    }
}
package requestHandler.operator.controller;

import requestHandler.operator.service.OperatorService;
import requestHandler.request.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * operator access only
 */
@RestController
@RequestMapping(path = "/operator")
@RequiredArgsConstructor
@Validated
public class OperatorController {
    private final OperatorService operatorService;

    @GetMapping("/requests")
    public List<Request> getAllSentRequests(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "desc") String sortBy) {
        return operatorService.getAllSentRequests(from, size, sortBy);
    }

    @GetMapping("/users/{username}")
    public List<Request> getUsersRequestsByUserName(
            @PathVariable String username,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(defaultValue = "desc") String sortBy) {

        return operatorService.getRequestsByUserName(username, from, size, sortBy);   // only SENT requests
    }

    @PatchMapping("/review/{requestId}")
    public Request requestReview(@PathVariable Long requestId, @RequestParam String status) {
        return operatorService.reviewRequest(requestId, status);
    }
}
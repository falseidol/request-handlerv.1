package requestHandler.user;

import requestHandler.request.Request;
import requestHandler.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * user access only
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/request")
    public Request createRequest(@RequestBody Request request) {
        return userService.createRequest(request);
    }

    @PreAuthorize("hasRole('USER')") // method only for tests
    @GetMapping("/requests")
    public List<Request> getAll() {
        return userService.getAll();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/edit/{userId}/{requestId}")
    public Request editDraftRequest(@PathVariable Long userId, @PathVariable Long requestId, @RequestBody Request request) {
        return userService.editDraftRequest(userId, requestId, request);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/send/{userId}/{requestId}")
    public Request sendRequestToReview(@PathVariable Long userId, @PathVariable Long requestId) {
        return userService.sendRequestToReview(userId, requestId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/requests/{userId}")
    public List<Request> getRequests(@PathVariable Long userId,
                                     @RequestParam(required = false, defaultValue = "0") int from,
                                     @RequestParam(required = false, defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "desc") String sortBy) {
        return userService.getRequests(userId, from, size, sortBy);
    }
}
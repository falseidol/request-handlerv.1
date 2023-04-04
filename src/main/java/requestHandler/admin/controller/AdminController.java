package requestHandler.admin.controller;

import requestHandler.admin.service.AdminService;
import requestHandler.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * admin access only
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<User> getUsers() {
        return adminService.getUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return adminService.getUserByUsername(username);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/users/{userId}")
    public User givePermission(@PathVariable Long userId) {
        return adminService.givePermission(userId);
    }
}
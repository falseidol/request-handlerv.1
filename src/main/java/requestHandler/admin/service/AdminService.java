package requestHandler.admin.service;

import requestHandler.user.User;

import java.util.List;

public interface AdminService {
    List<User> getUsers();

    User getUserByUsername(String username);

    User givePermission(Long userId);
}
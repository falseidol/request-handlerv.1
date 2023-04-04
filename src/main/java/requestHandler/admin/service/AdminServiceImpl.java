package requestHandler.admin.service;

import requestHandler.exception.ObjectNotFoundException;
import requestHandler.exception.ValidationException;
import requestHandler.user.User;
import requestHandler.user.repository.UserRepository;
import requestHandler.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public List<User> getUsers() {
        log.info("Возвращаем список пользователей");
        return userService.getUsers();
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("Возвращаем пользователя по имени");
        if (username == null) {
            throw new ValidationException("Имя пользователя == null");
        }
        return userService.findUserByUsername(username);
    }

    @Override
    public User givePermission(Long userId) {
        User userToGiveAccess = userRepository.findById(userId).
                orElseThrow(() -> new ObjectNotFoundException("Такого пользователя не существует"));
        if (userToGiveAccess.getRoles().matches("ROLE_OPERATOR")) {
            throw new ValidationException("Пользователь уже является оператором");
        } else {
            userToGiveAccess.setRoles("ROLE_OPERATOR");
            log.info(userToGiveAccess + " теперь оператор");
        }
        return userRepository.save(userToGiveAccess);
    }
}
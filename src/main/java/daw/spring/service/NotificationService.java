package daw.spring.service;

import daw.spring.model.Notification;
import daw.spring.model.User;
import daw.spring.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    public Notification findOneById(Long id) {
        return notificationRepository.findOne(id);
    }

    public Notification findOneByUser(User user) {
        return notificationRepository.findOne(user.getId());
    }

    public List<Notification> findAllNotifications() {
        return notificationRepository.findAll();
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    public Page<Notification> findAllNotificationPage(PageRequest pageRequest) {
        return notificationRepository.findAll(pageRequest);
    }

    public void alertAdmin(User user) {
        for (User admin : userService.getAllAdmins()) {
            notificationRepository.save(
                    new Notification(
                            "Security Alert",
                            "User " + user.getId() + " tried to access another element he doesn't control.",
                            new Date(),
                            admin)
            );
        }

    }
}

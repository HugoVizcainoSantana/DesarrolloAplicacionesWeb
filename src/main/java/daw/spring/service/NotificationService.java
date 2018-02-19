package daw.spring.service;

import daw.spring.model.Notification;
import daw.spring.model.User;
import daw.spring.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification findOneById(Long id){

        return notificationRepository.findOne(id);
    }

    public Notification findOneByUser(User user){

        return notificationRepository.findOne(user.getId());
    }

    public List<Notification> findAllNotifications(){
        return notificationRepository.findAll();
    }


    public void saveNotification(Notification notification){
        notificationRepository.save(notification);
    }

    public void deleteNotification(Notification notification){
        notificationRepository.delete(notification);
    }

    public Page<Notification> findAllNotificationPage(PageRequest pageRequest){
        return notificationRepository.findAll(pageRequest);
    }

    public void save(Notification notification1) {
        notificationRepository.save(notification1);
    }

}

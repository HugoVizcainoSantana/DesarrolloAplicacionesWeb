package daw.spring.component;

import daw.spring.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserComponent {

    private User user;

    public User getLoggedUser() {
        return user;
    }

    public boolean isLoggedUser() {
        return user != null;
    }

    public void setLoggedUser(User user) {
        if (user != null) {
            this.user = user;
        }
    }
}

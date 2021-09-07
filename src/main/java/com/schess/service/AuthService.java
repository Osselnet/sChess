package com.schess.service;

import com.schess.models.Role;
import com.schess.models.User;
import com.schess.repositories.UserRepository;
import com.schess.views.admin.AdminView;
import com.schess.views.home.HomeView;
import com.schess.views.logout.LogoutView;
import com.schess.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    public record AuthorizedRoute(String route, String name, Class<? extends Component> view) {

    }

    public class AuthException extends Exception {

    }

    private final UserRepository userRepository;
    //private final MailSender mailSender;

//    public AuthService(UserRepository userRepository, MailSender mailSender) {
//        this.mailSender = mailSender;
    public AuthService(UserRepository userRepository) {
            this.userRepository = userRepository;
    }

    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.getByUsername(username);
        if (user != null && user.checkPassword(password) && user.isActive()) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            createRoutes(user.getRole());
        } else {
            throw new AuthException();
        }
    }

    private void createRoutes(Role role) {
        getAuthorizedRoutes(role).stream()
                .forEach(route ->
                        RouteConfiguration.forSessionScope().setRoute(
                                route.route, route.view, MainLayout.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Role role) {
        var routes = new ArrayList<AuthorizedRoute>();

        if (role.equals(Role.USER)) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));

        } else if (role.equals(Role.ADMIN)) {
            routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            routes.add(new AuthorizedRoute("admin", "Admin", AdminView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));
        }

        return routes;
    }

    public void register(String email, String password) {
        User user = userRepository.save(new User(email, password, Role.USER));

        String text = "http://localhost:8080/activate?code=" + user.getActivationCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@example.com");
        message.setSubject("Confirmation email");
        message.setText(text);
        message.setTo(email);
       // mailSender.send(message);
    }

    public void activate(String activationCode) throws AuthException {
        User user = userRepository.getByActivationCode(activationCode);
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
        } else {
            throw new AuthException();
        }
    }

}

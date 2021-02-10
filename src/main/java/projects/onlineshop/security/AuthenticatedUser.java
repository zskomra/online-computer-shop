package projects.onlineshop.security;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

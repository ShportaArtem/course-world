package ua.nure.shporta.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ua.nure.shporta.constants.UserConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Component
@Configuration
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, Authentication authentication)
            throws RuntimeException
    {
        HttpSession session = httpServletRequest.getSession();
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.forEach(authority ->
        {
            if(authority.getAuthority().equals(UserConstants.MODERATOR_ROLE))
            {
                session.setAttribute("role", UserConstants.MODERATOR_ROLE);
                try
                {
                    httpServletResponse.sendRedirect("/CourseWorld/manage/courses");
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
            else if (authority.getAuthority().equals(UserConstants.USER_ROLE))
            {
                session.setAttribute("role", UserConstants.USER_ROLE);
                try
                {
                    httpServletResponse.sendRedirect("/CourseWorld/");
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
            else if(authority.getAuthority().equals(UserConstants.ADMIN_ROLE))
            {
                session.setAttribute("role", UserConstants.ADMIN_ROLE);
                try
                {
                    httpServletResponse.sendRedirect("/CourseWorld/admin");
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
package ua.nure.shporta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.nure.shporta.model.DefaultUserDetails;
import ua.nure.shporta.model.User;
import ua.nure.shporta.dao.UserDAO;

import java.util.Optional;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

    @Autowired
    UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userDAO.findByLogin(userName);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));
        return user.map(DefaultUserDetails::new).get();
    }
}

package kz.bitlab.springboot.group22.services;

import kz.bitlab.springboot.group22.entites.users.Users;
import kz.bitlab.springboot.group22.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Users user = userRepository.findByEmail(s);
        User securityUser = new User(user.getEmail(), user.getPassword(), user.getRoles());
        return securityUser;

    }
}

package gamo.villalba.sergio.services;

import gamo.villalba.sergio.dtos.SignUpDto;
import gamo.villalba.sergio.models.UserModel;
import gamo.villalba.sergio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return user;
    }

    public UserDetails signUp(SignUpDto data) throws Exception {
        if (repository.findByUsername(data.getUsername()) != null) {
            throw new Exception("Username already exists");
        }

        String encryptedPassword = passwordEncoder.encode(data.getPassword());
        UserModel newUser = new UserModel(data.getUsername(), encryptedPassword, data.getRole());
        return repository.save(newUser);
    }
}

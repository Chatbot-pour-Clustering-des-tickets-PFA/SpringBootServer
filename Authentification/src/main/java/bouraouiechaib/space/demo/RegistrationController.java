package bouraouiechaib.space.demo;

import bouraouiechaib.space.demo.model.MyUser;
import bouraouiechaib.space.demo.model.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
public class RegistrationController {

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register/user")
    public MyUser createUser(@RequestBody MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepository.save(user);
    }
}

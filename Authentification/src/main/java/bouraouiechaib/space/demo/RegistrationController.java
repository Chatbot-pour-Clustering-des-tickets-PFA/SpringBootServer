package bouraouiechaib.space.demo;

import bouraouiechaib.space.demo.model.MyUser;
import bouraouiechaib.space.demo.model.MyUserRepository;
import bouraouiechaib.space.demo.webtoken.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class RegistrationController {

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody RegisterForm request) {
        System.out.println("hello");
        if(myUserRepository.findByEmail(request.email()).isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body("Email already exists");
        }
        MyUser user = new MyUser();
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        myUserRepository.save(user);
        return ResponseEntity.ok("account created");
    }
}

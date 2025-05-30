package bouraouiechaib.space.demo;


import bouraouiechaib.space.demo.model.AuthResponse;
import bouraouiechaib.space.demo.model.MyUser;
import bouraouiechaib.space.demo.model.MyUserDetailService;
import bouraouiechaib.space.demo.model.MyUserRepository;
import bouraouiechaib.space.demo.webtoken.JwtService;
import bouraouiechaib.space.demo.webtoken.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class ContentController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private MyUserRepository myUserRepository;



    /*@PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginForm.username(), loginForm.password()
        ));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.username()));
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }*/

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> authenticateAndGetToken(@RequestBody LoginForm loginForm) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginForm.username(),
                            loginForm.password()
                    )
            );
            if (!authentication.isAuthenticated()) {
                throw new UsernameNotFoundException("Invalid credentials");
            }


            MyUser user   = myUserRepository
                    .findByUsername(loginForm.username())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found")
                    );
            String token  = jwtService.generateToken(
                    myUserDetailService.loadUserByUsername(loginForm.username())
            );
            Long userId   = user.getId();

            Integer clientId = myUserRepository
                    .findClientIdByUserIdNative(userId)
                    .orElse(null);

            Integer technicianId = myUserRepository
                    .findTechnicianIdByUserId(userId)
                    .orElse(null);


            AuthResponse body = new AuthResponse(token, userId, clientId, technicianId,user.getRole());
            return ResponseEntity.ok(body);        }
    }

package bouraouiechaib.space.demo;

import bouraouiechaib.space.demo.Repositories.ClientRepository;
import bouraouiechaib.space.demo.Requests.CreateUserRequest;
import bouraouiechaib.space.demo.Requests.RegisterRequest;
import bouraouiechaib.space.demo.model.Client;
import bouraouiechaib.space.demo.model.MyUser;
import bouraouiechaib.space.demo.model.MyUserRepository;
import bouraouiechaib.space.demo.model.Role;
import bouraouiechaib.space.demo.webtoken.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
public class RegistrationController {

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RestTemplate restTemplate;

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


    @PostMapping("/createUser")
    public ResponseEntity<?> createUserByAdmin(@RequestBody CreateUserRequest userRequest) {
        if (myUserRepository.findByEmail(userRequest.email()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));        }

        MyUser user = new MyUser();
        user.setEmail(userRequest.email());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setRole(userRequest.role());
        user.setFirstname(userRequest.firstName());
        user.setLastname(userRequest.LastName());
        user.setUsername(userRequest.firstName() + userRequest.LastName() + Instant.now().toEpochMilli());

        try {
            user = myUserRepository.save(user);

            if (user.getRole() == Role.Client) {
                Client client = new Client();
                client.setUser_id(user.getId());
                clientRepository.save(client);
            } else if (user.getRole() == Role.Technician) {
                System.out.println("categories "+userRequest.categories());
                callTechnicianMicroservice(user,userRequest.categories());
            }

            return ResponseEntity.ok(Map.of("message", "Account created successfully"));

        } catch (Exception ex) {

            myUserRepository.deleteById(user.getId());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error during user creation: " + ex.getMessage()));
        }
    }

    private void callTechnicianMicroservice(MyUser user,String category) {
        String technicianServiceUrl = "http://localhost:8083/api/v1/technician/create";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userID", user.getId());
        requestBody.put("category",category);

        ResponseEntity<String> response = restTemplate.postForEntity(technicianServiceUrl, requestBody, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create technician");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (myUserRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));        }

        MyUser user = new MyUser();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setFirstname(request.firstName());
        user.setLastname(request.LastName());
        user.setUsername(request.username());

        try {
            user = myUserRepository.save(user);

            if (user.getRole() == Role.Client) {
                Client client = new Client();
                client.setUser_id(user.getId());
                clientRepository.save(client);
            } else if (user.getRole() == Role.Technician) {
                System.out.println("categories "+request.categories());
                callTechnicianMicroservice(user,request.categories());
            }

            return ResponseEntity.ok(Map.of("message", "Account created successfully"));

        } catch (Exception ex) {

            myUserRepository.deleteById(user.getId());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error during user creation: " + ex.getMessage()));
        }
    }



}

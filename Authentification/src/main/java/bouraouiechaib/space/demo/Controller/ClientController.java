package bouraouiechaib.space.demo.Controller;

import bouraouiechaib.space.demo.Repositories.ClientList;
import bouraouiechaib.space.demo.Repositories.ClientRepository;
import bouraouiechaib.space.demo.model.Client;
import bouraouiechaib.space.demo.model.MyUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/")
public class ClientController {


    private final ClientRepository clientRepository;

    private final MyUserRepository myUserRepository;


    @GetMapping("ClientList")
    public ResponseEntity<List<ClientList>> getClientList() {
        return ResponseEntity.ok(myUserRepository.findClientInfo());
    }

    @GetMapping("userInfo/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable("userId") Long userId){
        return myUserRepository.findById(userId)
                .map(user -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", user.getId());
                    result.put("username", user.getUsername());
                    result.put("firstname", user.getFirstname());
                    result.put("lastname", user.getLastname());
                    result.put("email", user.getEmail());
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("clientProfile/{userId}")
    public ResponseEntity<?> getClientInfo(@PathVariable("userId") Long userId){
        System.out.println("userId: "+userId);
        return ResponseEntity.ok(myUserRepository.findById(userId));
    }

}

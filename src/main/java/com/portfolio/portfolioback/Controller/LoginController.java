package com.portfolio.portfolioback.Controller;

import com.portfolio.portfolioback.Configuration.AuthentificationService;
import com.portfolio.portfolioback.Configuration.UserPrincipal;
import com.portfolio.portfolioback.Dao.UserRepository;
import com.portfolio.portfolioback.JwUtil.JwtUtil;
import com.portfolio.portfolioback.Model.SignUpRequest;
import com.portfolio.portfolioback.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "https://localhost:8100")
@RequestMapping("/auth")
class LoginController {

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private AuthentificationService userDetailsService;

    private UserPrincipal userPrincipal;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;


    @CrossOrigin(origins = "")
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody @Valid SignUpRequest signUp) throws Exception {

        try {
            //vérifie si l'email existe déjà dans la base de donnée, si oui renvoie erreur
            if (userRepository.existsByEmail(signUp.getEmail())) {
                return ResponseEntity.badRequest().body("Error: Email is already in use!");
            }

            User user = new User();
            user.setFirstname(signUp.getFirstname());
            user.setName(signUp.getName());
            user.setEmail(signUp.getEmail());
            user.setPassword(encoder.encode(signUp.getPassword()));
            String role = "USER";
            user.setRole(role);
            user.setPassword(encoder.encode(signUp.getPassword()));
            user.setBirthday(signUp.getBirthday());
            user.setAdress(signUp.getAdress());
            user.setCity(signUp.getCity());


            userRepository.save(user);

            return ResponseEntity.ok("User successfully registred");
        } catch (BadCredentialsException e) {
            throw new Exception("An error happen during the registred process.", e);
        }
    }

}

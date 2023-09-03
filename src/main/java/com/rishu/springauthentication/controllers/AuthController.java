package com.rishu.springauthentication.controllers;

import com.rishu.springauthentication.models.ERole;
import com.rishu.springauthentication.models.Role;
import com.rishu.springauthentication.models.User;
import com.rishu.springauthentication.payload.request.LoginRequest;
import com.rishu.springauthentication.payload.request.SignupRequest;
import com.rishu.springauthentication.payload.response.MessageResponse;
import com.rishu.springauthentication.payload.response.UserInfoResponse;
import com.rishu.springauthentication.repository.RoleRepository;
import com.rishu.springauthentication.repository.UserRepository;
import com.rishu.springauthentication.security2.JwtService;
import com.rishu.springauthentication.security2.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;


//    @Autowired
//    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            final String jwtToken = jwtService.generateToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .body(new UserInfoResponse(
                            jwtToken,
                            userDetails.getId(),
                            userDetails.getUsername(),
                            userDetails.getEmail(),
                            roles));

        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }

//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//                .body(new UserInfoResponse(userDetails.getId(),
//                        userDetails.getUsername(),
//                        userDetails.getEmail(),
//                        roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));

        }
        // Create new user's account
        User user = new User(signupRequest.getUsername(),
                             signupRequest.getEmail(),
                             encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }

//    @PostMapping("/signout")
//    public ResponseEntity<?> logoutUser() {
//        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
//
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .body(new MessageResponse("You've been signed out!"));
//    }
}

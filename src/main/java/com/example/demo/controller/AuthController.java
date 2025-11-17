package com.example.demo.controller;

import com.example.demo.service.AuthService;
import com.example.demo.service.FirebaseLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String home() {
        return "login";
    }


    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/registers")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) {

        System.out.println("🔥 Controller reached!");

        try {
            authService.register(name, email, password);
            model.addAttribute("msg", "Registered Successfully!");
            return "login";

        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/google-login")
    @ResponseBody
    public Map<String, String> googleLogin(@RequestBody Map<String, String> body) {

        Map<String, String> response = new HashMap<>();

        try {
            String token = body.get("token");

            // 1. Verify Google Firebase Token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String email = decodedToken.getEmail();

            System.out.println("Google login email: " + email);

            // 2. Now check if user exists in Firebase Authentication
            try {
                UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);

                // ✔ User exists → LOGIN SUCCESS
                response.put("status", "OK");
                response.put("message", "Login successful!");
                response.put("uid", userRecord.getUid());
                return response;

            } catch (FirebaseAuthException e) {
                if (e.getErrorCode().equals("USER_NOT_FOUND")) {

                    // ❌ User does NOT exist → ASK TO REGISTER
                    response.put("status", "NOT_REGISTERED");
                    response.put("message", "User not registered. Please register first.");
                    return response;
                }

                // Other Firebase errors
                response.put("status", "ERROR");
                response.put("message", "Firebase error: " + e.getMessage());
                return response;
            }

        } catch (Exception e) {
            // ❌ Invalid token OR token verification failed
            response.put("status", "TOKEN_INVALID");
            response.put("message", "Invalid Google Login Token.");
            return response;
        }
    }


}

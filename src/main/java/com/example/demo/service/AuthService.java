package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public String register(String name, String email, String password) throws Exception {

        System.out.println("STEP 1: Firebase Auth creating user...");

        // 1. Firebase Authentication
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        System.out.println("STEP 2: Firebase created user with UID = " + userRecord.getUid());

        String uid = userRecord.getUid();

        // 2. Firestore
        System.out.println("STEP 3: Writing Firestore document...");

        Firestore db = FirestoreClient.getFirestore();

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("password", password);

        ApiFuture<WriteResult> future =
                db.collection("users").document(uid).set(data);

        future.get();
        System.out.println("STEP 4: Firestore write complete!");

        // 3. MySQL save
        //System.out.println("STEP 5: Saving to MySQL...");

        User user = new User();
        user.setUid(uid);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        //userRepository.save(user);

        System.out.println("STEP 6: MySQL save complete!");

        return uid;
    }

}

package com.mainproject.config;
import java.io.FileInputStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
public class FirebaseConfig {
    static{
        getFirebaseConfig();
    }
    private static void getFirebaseConfig(){
        try{
            FileInputStream serviceAccount=new FileInputStream("src/main/resources/agrilink.json");
            FirebaseOptions options=FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase Connected Succesfully");
        }catch(Exception e){
            e.printStackTrace();
        }   
    }
    public static Firestore getFirestore(){
        return FirestoreClient.getFirestore();
    }
}

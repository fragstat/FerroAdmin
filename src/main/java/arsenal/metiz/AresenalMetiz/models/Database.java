package arsenal.metiz.AresenalMetiz.models;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static final Database instance = new Database();

    int Times = 0;

    private Firestore connect() throws IOException {
        try {
            if (Times == 0) {
                InputStream serviceAccount = new FileInputStream("D:\\Idea\\Aresenal-Metiz\\src\\main\\java\\arsenal-metiz-firebase-adminsdk-figev-bbcf77fa1a.json");
                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(credentials)
                        .build();
                FirebaseApp.initializeApp(options);
                Times = 1;
            }
            return FirestoreClient.getFirestore();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Database getInstance() {
        return instance;
    }

    public Map<String, Object> getDocumentAsMap(String identifier) throws Exception {
        Firestore db = connect();
        if (db != null){
            DocumentReference docRef = db.collection("certificates").document(identifier);
            // asynchronously retrieve the document
            ApiFuture<DocumentSnapshot> future = docRef.get();
            // ...
            // future.get() blocks on response
            DocumentSnapshot document = future.get();

            Map <String, Object> map = new HashMap<>();
            map.put("id","Неверный код");

            return (document.exists()) ? document.getData() : map ;
        }else {
            return null;
        }

    }
}

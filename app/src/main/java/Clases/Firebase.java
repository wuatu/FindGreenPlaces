package Clases;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {
    public FirebaseDatabase database=FirebaseDatabase.getInstance();
    public DatabaseReference mDatabase=database.getReference();

    public Firebase() {
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getMDatabase() {
        return mDatabase;
    }
}

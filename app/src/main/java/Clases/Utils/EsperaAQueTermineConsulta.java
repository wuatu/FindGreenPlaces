package Clases.Utils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class EsperaAQueTermineConsulta {
    public static interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }

    public static void readData(DatabaseReference ref, final OnGetDataListener listener) {
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

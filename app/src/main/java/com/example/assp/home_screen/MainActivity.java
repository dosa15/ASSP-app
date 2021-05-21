package com.example.assp.home_screen;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assp.R;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DataSnapshot dataSnapshot;
    private Button button;
    static private boolean activatedHandSanitizer = false, activatedThermometer = false, activatedMaskOn = false, activatedRFID = false, activatedDoor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_screen);

        findViewById(R.id.RFIDButton).setClickable(false);
        findViewById(R.id.RFIDButton).setBackgroundColor(getResources().getColor(R.color.disabledButton));
        findViewById(R.id.doorButton).setClickable(false);
        findViewById(R.id.doorButton).setBackgroundColor(getResources().getColor(R.color.disabledButton));

/*

        //BUTTONS CLICK LISTENERS
        findViewById(R.id.handSanitizerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                activatedHandSanitizer = activateButton(R.id.handSanitizerButton, activatedHandSanitizer);
                userIsSafe();
            }
        });

        findViewById(R.id.thermometerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activatedThermometer = activateButton(R.id.thermometerButton, activatedThermometer);
                userIsSafe();
            }
        });

        findViewById(R.id.maskButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activatedMaskOn = activateButton(R.id.maskButton, activatedMaskOn);
                userIsSafe();
            }
        });

        findViewById(R.id.RFIDButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activatedRFID = activateButton(R.id.RFIDButton, activatedRFID);
                if (activatedRFID) {
                    findViewById(R.id.doorButton).setBackgroundColor(getResources().getColor(R.color.activatedButton));
                } else {
                    findViewById(R.id.doorButton).setBackgroundColor(getResources().getColor(R.color.deactivatedButton));
                }

            }
        });

 */

        FirebaseDatabase.getInstance().getReference().child("sensors").child("Fever").setValue("False");
        /*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sensors");
//        Log.d("FBDB", myRef.toString());
//        myRef.push().child("test").setValue("Hello, World!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("DSR", "about to read ahah: ");
                String value = dataSnapshot.getValue(String.class);
                Log.d("DSR", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DSE", "Failed to read value.", error.toException());
            }
        });
         */




        // https://console.firebase.google.com/u/1/project/assp-efa06/database/assp-efa06-default-rtdb/data/

//        firebaseDatabase = FirebaseDatabase.getInstance();
//        final ArrayList<String> list = new ArrayList<>();
//        firebaseDatabase.getReference().child("users").push().child("trial").setValue("trial data");

//        String fever = databaseReference.child("Fever").get().toString();
//        Log.d("children", dataSnapshot.getChildren().toString());
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                list.clear();
////                for (DataSnapshot snap : dataSnapshot.getChildren()) {
////                    list.add(snap.getValue().toString());
////                }
////                Log.println(Log.INFO, "ValueEventChange", list.toString());
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("ValueEventChange", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("ValueEventChange", "loadPost:onCancelled", error.toException());
//            }
//        });


//        Log.d("sensors", databaseReference.child("Fever").toString());
//
//        String path = databaseReference.toString();
//
//        Log.d("Fever", databaseReference.child("sensors").child("Fever").get().toString());
//
//        sensors.orderByChild(databaseReference.toString()){
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String sanitized = dataSnapshot.child("Hands Sanitized").getValue().toString();
//                boolean hasChi = dataSnapshot.hasChildren();
//                Log.d("papa", String.valueOf(hasChi));
//                if(sanitized == "true"){
//                    Log.d("sanitized", "Sanitized!");
//                    findViewById(R.id.handSanitizerButton).performClick();
//                }
//
//                String hasFever = dataSnapshot.child("Fever").getValue().toString();
//
//                if(hasFever.equals("False")){
//                    findViewById(R.id.thermometerButton).performClick();
//                }
//
//                String hasMask = dataSnapshot.child("Mask On").getValue().toString();
//
//                if(hasMask.equals("true")){
//                    findViewById(R.id.maskButton).performClick();
//                }
//
//                String doneRIFD = dataSnapshot.child("RFID").getValue().toString();
//
//                if(doneRIFD.equals("true")){
//                    findViewById(R.id.RFIDButton).performClick();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//

    }

    public boolean activateButton(int buttonID, boolean activated) {
        Button buttonView = findViewById(buttonID);
        if (!activated) {
            buttonView.setBackgroundColor(getResources().getColor(R.color.activatedButton)); // Integer.parseInt("008000", 16)
        } else {
            buttonView.setBackgroundColor(getResources().getColor(R.color.deactivatedButton)); // Integer.parseInt("E44646", 16)
        }
        return !activated;
    }

    public void userIsSafe() {
        if (activatedHandSanitizer && activatedThermometer && activatedMaskOn) {
            findViewById(R.id.RFIDButton).setClickable(true);
            findViewById(R.id.RFIDButton).setBackgroundColor(getResources().getColor(R.color.deactivatedButton));
            findViewById(R.id.doorButton).setBackgroundColor(getResources().getColor(R.color.deactivatedButton));
        } else {
            findViewById(R.id.RFIDButton).setClickable(false);
            findViewById(R.id.RFIDButton).setBackgroundColor(getResources().getColor(R.color.disabledButton));
            findViewById(R.id.doorButton).setBackgroundColor(getResources().getColor(R.color.disabledButton));
        }

    }
}
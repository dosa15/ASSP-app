package com.example.assp.home_screen;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assp.R;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    //        FirebaseDatabase.getInstance().getReference().child("sensors").child("Fever").setValue("false");
    //        FirebaseDatabase.getInstance().getReference().child("sensors").child("Hands Sanitized").setValue("false");
    //        FirebaseDatabase.getInstance().getReference().child("sensors").child("Mask On").setValue("false");
    //        FirebaseDatabase.getInstance().getReference().child("sensors").child("RFID").setValue("false");
    //        FirebaseDatabase.getInstance().getReference().child("sensors").child("Temperature").setValue("98.6");

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DataSnapshot dataSnapshot;
    private Button button;
    static private boolean activatedHandSanitizer = false, activatedThermometer = false, activatedMask = false, activatedRFID = false, activatedDoor = false;
    static String temperature = "100.8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_screen);

        Button handSanitizerButton = (Button) findViewById(R.id.handSanitizerButton);
        Button thermometerButton = (Button) findViewById(R.id.thermometerButton);
        Button maskButton = (Button) findViewById(R.id.maskButton);
        Button RFIDButton = (Button) findViewById(R.id.RFIDButton);
        Button doorButton = (Button) findViewById(R.id.doorButton);

//        handSanitizerButton.setClickable(false);
//        thermometerButton.setClickable(false);
//        maskButton.setClickable(false);
//        RFIDButton.setClickable(false);
//        doorButton.setClickable(false);

        handSanitizerButton.setBackgroundColor(getResources().getColor(R.color.deactivatedButton));
        thermometerButton.setBackgroundColor(getResources().getColor(R.color.deactivatedButton));
        maskButton.setBackgroundColor(getResources().getColor(R.color.deactivatedButton));
        RFIDButton.setBackgroundColor(getResources().getColor(R.color.disabledButton));
        doorButton.setBackgroundColor(getResources().getColor(R.color.disabledButton));

        //BUTTONS CLICK LISTENERS
        handSanitizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activatedHandSanitizer = activateButton(R.id.handSanitizerButton, activatedHandSanitizer);
                userIsSafe();
            }
        });

        thermometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activatedThermometer = activateButton(R.id.thermometerButton, activatedThermometer);
                if(activatedThermometer) {
                    thermometerButton.setText("No Fever. Temp: " + temperature + "ºF");
                } else {
                    thermometerButton.setText("Fever. Temp: " + temperature + "ºF");
                }
                userIsSafe();
            }
        });

        maskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activatedMask = activateButton(R.id.maskButton, activatedMask);
                if(activatedMask) {
                    maskButton.setText("Masked");
                } else {
                    maskButton.setText("No Mask");
                }
                userIsSafe();
            }
        });

        RFIDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activatedRFID = activateButton(R.id.RFIDButton, activatedRFID);
                if (activatedRFID && userIsSafe()) {
                    RFIDButton.setBackgroundColor(getResources().getColor(R.color.activatedButton));
                    doorButton.setBackgroundColor(getResources().getColor(R.color.activatedButton));
                    doorButton.setText("Door is Open");
                } else {
                    RFIDButton.setBackgroundColor(getResources().getColor(R.color.deactivatedButton));
                    doorButton.setBackgroundColor(getResources().getColor(R.color.deactivatedButton));
                    doorButton.setText("Door is Closed");
                }

            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d("DBREF", database.getReference().toString());
        DatabaseReference sensors = database.getReference("sensors");
        if (sensors != null) {

//            sensors.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//                    Log.d("DSR", "about to read ahah: ");
//                    String value = dataSnapshot.getValue(String.class);
//                    Log.d("DSR", "Value is: " + value);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
//                    Log.w("DSE", "Failed to read value.", error.toException());
//                }
//            });


            // https://console.firebase.google.com/u/1/project/assp-efa06/database/assp-efa06-default-rtdb/data/

            final HashMap<String, String> sensorData = new HashMap<>();
            sensors.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    sensorData.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        sensorData.put(snap.getKey(), snap.getValue().toString());
                    }
                    Log.println(Log.INFO, "ValueEventChange", sensorData.toString());
                    setButtonStates();
                }

                private void setButtonStates() {
                    if (sensorData.get("Hands Sanitized").equals("true") != activatedHandSanitizer) {
                        handSanitizerButton.performClick();
                    }
                    temperature = sensorData.get("Temperature");
                    if (sensorData.get("Fever").equals("false") != activatedThermometer) {
                        thermometerButton.performClick();
                    }
                    if (sensorData.get("Mask On").equals("true") != activatedMask) {
                        maskButton.performClick();
                    }
                    if(userIsSafe()) {
                        if (sensorData.get("RFID").equals("true") != activatedRFID) {
                            RFIDButton.performClick();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("ValueEventChange", "loadPost:onCancelled", error.toException());
                }
            });


//        Log.d("sensors", databaseReference.child("Fever").toString());
//
//        String path = databaseReference.toString();
//
//        Log.d("Fever", databaseReference.child("sensors").child("Fever").get().toString());
//

            /*
            sensors.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String sanitized = dataSnapshot.child("Hands Sanitized").getValue().toString();
                    boolean hasChi = dataSnapshot.hasChildren();
                    Log.d("papa", String.valueOf(hasChi));
                    if (sanitized == "true") {
                        Log.d("sanitized", "Sanitized!");
                        findViewById(R.id.handSanitizerButton).performClick();
                    }

                    String hasFever = dataSnapshot.child("Fever").getValue().toString();

                    if (hasFever.equals("False")) {
                        findViewById(R.id.thermometerButton).performClick();
                    }

                    String hasMask = dataSnapshot.child("Mask On").getValue().toString();

                    if (hasMask.equals("true")) {
                        findViewById(R.id.maskButton).performClick();
                    }

                    String doneRIFD = dataSnapshot.child("RFID").getValue().toString();

                    if (doneRIFD.equals("true")) {
                        findViewById(R.id.RFIDButton).performClick();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("DBERR", "Unable to retrieve sensor data.");
                }
            });
             */
        }

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

    public boolean userIsSafe() {
        if (activatedHandSanitizer && activatedThermometer && activatedMask) {
//            findViewById(R.id.RFIDButton).setClickable(true);
            findViewById(R.id.RFIDButton).setBackgroundColor(getResources().getColor(R.color.deactivatedButton));
            findViewById(R.id.doorButton).setBackgroundColor(getResources().getColor(R.color.deactivatedButton));
            return true;
        } else {
//            findViewById(R.id.RFIDButton).setClickable(false);
            findViewById(R.id.RFIDButton).setBackgroundColor(getResources().getColor(R.color.disabledButton));
            findViewById(R.id.doorButton).setBackgroundColor(getResources().getColor(R.color.disabledButton));
            return false;
        }
    }

}
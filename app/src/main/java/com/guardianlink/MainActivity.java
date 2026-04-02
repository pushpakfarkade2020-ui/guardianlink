package com.guardianlink;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.*;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    EditText nameInput, phoneInput;
    ArrayList<String> contactList;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        Button sosBtn = findViewById(R.id.sosBtn);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        SharedPreferences prefs = getSharedPreferences("data", MODE_PRIVATE);

        // 🔄 Load contacts
        contactList = new ArrayList<>(prefs.getStringSet("contacts", new HashSet<>()));

        // 🔥 RecyclerView setup
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(this, contactList, prefs);
        recyclerView.setAdapter(adapter);

        // ➕ ADD CONTACT
        LinearLayout inputContainer = findViewById(R.id.inputContainer);
        Button addBtn = findViewById(R.id.addBtn);

        addBtn.setOnClickListener(v -> {

            if (inputContainer.getVisibility() == View.GONE) {

                // SHOW INPUTS
                inputContainer.setAlpha(0f);
                inputContainer.setVisibility(View.VISIBLE);
                inputContainer.animate().alpha(1f).setDuration(200);
                addBtn.setText("Save Contact");

            } else {

                // SAVE CONTACT
                String name = nameInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(this, "Enter details", Toast.LENGTH_SHORT).show();
                    return;
                }

                String contact = name + " : " + phone;
                contactList.add(contact);

                prefs.edit().putStringSet("contacts", new HashSet<>(contactList)).apply();
                adapter.notifyDataSetChanged();

                // RESET UI
                nameInput.setText("");
                phoneInput.setText("");
                inputContainer.setVisibility(View.GONE);
                addBtn.setText("Add Contact");
            }
        });

        // 🚀 START FOREGROUND SERVICE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, NotificationService.class));
        } else {
            startService(new Intent(this, NotificationService.class));
        }

        // 🔴 SOS BUTTON
        sosBtn.setOnClickListener(v -> {

            // 📳 Vibration
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(300);
                }
            }

            // 📍 Permission check
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }

            SOSHelper.triggerSOS(this);
        });

        // 🔥 SOS ANIMATION (pulse)
        Handler handler = new Handler(Looper.getMainLooper());

        Runnable pulse = new Runnable() {
            @Override
            public void run() {

                sosBtn.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(700)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .withEndAction(() ->
                                sosBtn.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(700))
                        .start();

                handler.postDelayed(this, 1400);
            }
        };

        handler.post(pulse);
    }
}
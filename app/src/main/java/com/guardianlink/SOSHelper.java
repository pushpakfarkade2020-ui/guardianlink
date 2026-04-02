package com.guardianlink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Set;

public class SOSHelper {

    public static void triggerSOS(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        Set<String> contacts = prefs.getStringSet("contacts", null);

        if (contacts == null || contacts.isEmpty()) {
            Toast.makeText(context, "Add at least one contact", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder numbersBuilder = new StringBuilder();
        String firstNumber = null;

        for (String contact : contacts) {
            if (contact.contains(":")) {
                String phone = contact.split(":")[1].trim();

                if (firstNumber == null) firstNumber = phone;

                numbersBuilder.append(phone).append(";");
            }
        }


        String numbers = numbersBuilder.toString().replaceAll(";$", "");

        FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(context);

        try {
            final String finalFirstNumber = firstNumber;
            client.getLastLocation().addOnSuccessListener(location -> {

                if (location == null) {
                    Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show();
                    return;
                }

                String link = "https://maps.google.com/?q=" +
                        location.getLatitude() + "," + location.getLongitude();

                String message = "HELP! I am in danger. My location: " + link;


                // 📩 SMS
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.setData(Uri.parse("smsto:" + numbers));
                smsIntent.putExtra("sms_body", message);
                smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                context.startActivity(smsIntent);



            });

        } catch (SecurityException e) {
            Toast.makeText(context, "Location permission required", Toast.LENGTH_SHORT).show();
        }
    }
}
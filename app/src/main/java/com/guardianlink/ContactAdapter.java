package com.guardianlink;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.*;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    Context context;
    ArrayList<String> list;
    SharedPreferences prefs;

    public ContactAdapter(Context context, ArrayList<String> list, SharedPreferences prefs) {
        this.context = context;
        this.list = list;
        this.prefs = prefs;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contactName);
            phone = itemView.findViewById(R.id.contactPhone);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String contact = list.get(position);

        if (contact.contains(":")) {
            String[] parts = contact.split(":");
            holder.name.setText(parts[0].trim());
            holder.phone.setText(parts[1].trim());
        }

        // 🗑️ DELETE BUTTON CLICK
        holder.itemView.findViewById(R.id.deleteBtn).setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Contact")
                    .setMessage("Remove this contact?")
                    .setPositiveButton("Delete", (d, w) -> {
                        list.remove(position);
                        prefs.edit().putStringSet("contacts", new HashSet<>(list)).apply();
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
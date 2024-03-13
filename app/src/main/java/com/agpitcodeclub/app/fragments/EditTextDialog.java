package com.agpitcodeclub.app.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class EditTextDialog {

    private AlertDialog alertDialog;
    private EditText editText;

    public String getEnteredText() {
        return enteredText;
    }

    String enteredText;
    public EditTextDialog(Context context, DatabaseReference databaseReference, String app) {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Initialize the EditText
        editText = new EditText(context);

        // Set the EditText as the content view of the dialog
        builder.setView(editText);

        // Set the title for the dialog (optional)
        builder.setTitle("Add "+app+" url");
        builder.setMessage("paste profile sharing url from the "+app+" application .");
        // Set positive button with click listener
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Handle the positive button click, e.g., retrieve text from EditText
            enteredText = editText.getText().toString().trim();

            if(editText.getText().toString().trim().isEmpty()){
                editText.setError("Invalid Url");
            }else{
                databaseReference.setValue(enteredText);
                Toast.makeText(context,"url added",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }


            // Do something with the entered text
        });

        // Set negative button with click listener (optional)
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Handle the negative button click or dismiss the dialog
            dialog.dismiss();
        });

        // Create the AlertDialog
        alertDialog = builder.create();
    }

    // Show the dialog
    public void show() {
        alertDialog.show();
    }

}

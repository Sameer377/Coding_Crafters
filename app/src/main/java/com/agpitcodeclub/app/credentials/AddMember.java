package com.agpitcodeclub.app.credentials;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agpitcodeclub.app.R;

import java.util.ArrayList;

public class AddMember extends AppCompatActivity implements View.OnClickListener {
    private Spinner designationspinner;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        initui();
        initListeners();
        designationspinner();
    }


    private void initui() {
        designationspinner=findViewById(R.id.designation);
        back=findViewById(R.id.img_back_adduser);
    }

    private void initListeners() {
        back.setOnClickListener(this);
    }

    private String designation;
    private void designationspinner() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select designationspinner");
        arrayList.add("President");
        arrayList.add("Vice President");
        arrayList.add("Secretary");
        arrayList.add("Vice Secretary");
        arrayList.add("Renvenue Manger");
        arrayList.add("Assistant Renvenue Manger");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        designationspinner.setAdapter(arrayAdapter);
        designationspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation = parent.getItemAtPosition(position).toString();
                switch (designation)
                {
                    case "President":
                        break;
                    case "Vice President":
                        Toast.makeText(AddMember.this, "user created.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case "Secretary":
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back_adduser:
                finish();
                break;
        }
    }
}
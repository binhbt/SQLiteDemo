package com.demo.sqlitedemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by t430 on 10/18/2017.
 */

public class CreateOrEditActivity extends AppCompatActivity{
    private MySqliteHelper dbHelper;
    private EditText txtName;
    private EditText txtGender;
    private EditText txtAge;

    private Button btnSave;
    private Button btnEdit;
    private Button btnDelete;
    private View viewEditLayout;
    private int personId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        txtName = (EditText)findViewById(R.id.txt_name);
        txtGender = (EditText)findViewById(R.id.txt_gender);
        txtAge = (EditText)findViewById(R.id.txt_age);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        viewEditLayout = findViewById(R.id.btnLayout);

        personId = getIntent().getIntExtra(MainActivity.PERSON_ID, 0);
        dbHelper = new MySqliteHelper(this);
        if (personId >0){
            btnSave.setVisibility(View.GONE);
            viewEditLayout.setVisibility(View.VISIBLE);
            Cursor cursor = dbHelper.getPerson(personId);
            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(MySqliteHelper.PERSON_COLUMN_NAME));
                String gender = cursor.getString(cursor.getColumnIndex(MySqliteHelper.PERSON_COLUMN_GENDER));
                int age = cursor.getInt(cursor.getColumnIndex(MySqliteHelper.PERSON_COLUMN_AGE));
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                txtName.setText(name);
                txtName.setFocusable(false);
                txtName.setClickable(false);

                txtGender.setText(gender);
                txtGender.setFocusable(false);
                txtGender.setClickable(false);

                txtAge.setText(age + "");
                txtAge.setFocusable(false);
                txtAge.setClickable(false);
            }
        }else{
            btnSave.setVisibility(View.VISIBLE);
            viewEditLayout.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePerson();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave.setVisibility(View.VISIBLE);
                viewEditLayout.setVisibility(View.GONE);
                txtName.setFocusable(true);
                txtName.setClickable(true);
                txtName.setFocusableInTouchMode(true);
                txtGender.setFocusable(true);
                txtGender.setClickable(true);
                txtGender.setFocusableInTouchMode(true);
                txtAge.setFocusable(true);
                txtAge.setClickable(true);
                txtAge.setFocusableInTouchMode(true);

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateOrEditActivity.this);
                builder.setMessage("Do you want delete?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.deletePerson(personId);
                                Intent in = new Intent(CreateOrEditActivity.this, MainActivity.class);
                                startActivity(in);
                                finish();
                                Toast.makeText(CreateOrEditActivity.this, "Person deleted", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Do nothing
                            }
                        }).show();
            }
        });
    }

    private void savePerson(){
        if(personId >0){
            if(dbHelper.updatePerson(personId,
                    txtName.getText().toString(), txtGender.getText().toString(), Integer.parseInt(txtAge.getText().toString()))){
                Toast.makeText(this, "Update success", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Update failed", Toast.LENGTH_LONG).show();
            }
        }else{
            if(dbHelper.insertPerson(
                    txtName.getText().toString(), txtGender.getText().toString(), Integer.parseInt(txtAge.getText().toString()))){
                Toast.makeText(this, "Update success", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Update failed", Toast.LENGTH_LONG).show();
            }
        }

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}

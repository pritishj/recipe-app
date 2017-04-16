package com.recipes.app;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends Activity {

    EditText username;
    EditText password;
    Button login;
    String a;
    String b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



    }

    public void onClick(View view)

    {

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        a = username.getText().toString();
        b = password.getText().toString();
        if(a.matches("admin.*") && b.matches("admin123")) {
            Intent intent = new Intent(getApplicationContext(), RecipesList.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "wapas dekh be"+a+" /"+b , Toast.LENGTH_SHORT).show();
        }
    }


}

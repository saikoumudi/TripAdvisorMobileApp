package com.example.homework9_parta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText usermail,password;
    Button loginButton,signupButton;
    FirebaseAuth mAuth;
    String email;
    static int MSG_TYPE_TEXT=1;
    static int MSG_TYPE_PIC=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        mAuth= FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();

        usermail= (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.password);
        loginButton= (Button) findViewById(R.id.buttonLogin);
        signupButton= (Button) findViewById(R.id.buttonSignUp);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

        if(user!=null){
            Intent intent = new Intent(MainActivity.this, HomeScreen.class);
            startActivity(intent);
        }
        else {

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("demo","login button clicked");
                    if (usermail.getText().toString().trim().length() == 0 || password.getText().toString().trim().length() == 0) {
                        Toast.makeText(MainActivity.this, "Please enter user name and password", Toast.LENGTH_LONG).show();
                    } else {
                        final ProgressDialog pd=new ProgressDialog(MainActivity.this);
                        pd.setCancelable(false);
                        pd.show();
                        mAuth.signInWithEmailAndPassword(usermail.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pd.dismiss();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("demo","login success ");


                                    Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                                    //email=usermail.getText().toString().trim();
                                    //intent.putExtra("EMAIL",email);
                                    startActivity(intent);
                                    usermail.getText().clear();
                                    password.getText().clear();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}

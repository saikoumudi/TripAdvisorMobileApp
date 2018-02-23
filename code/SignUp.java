package com.example.homework9_parta;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    EditText firstNameEditText, lastNameEditText, emailEditText, repeatPswdEditText, choosePswdEditText;
    Button cancelButton, signUpButton,googleSignIn;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
     RadioGroup radioSexGroup;
     RadioButton radioSexButton;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    User user;
    GoogleApiClient mGoogleApiClient;
    public static final int RC_SIGN_IN=111;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                firstNameEditText.setText(acct.getGivenName());
                lastNameEditText.setText(acct.getFamilyName());
                emailEditText.setText(acct.getEmail());

            } else {
                // Signed out, show unauthenticated UI.
                Toast.makeText(SignUp.this,"Google signIn failed",Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("SignUp");
        mAuth= FirebaseAuth.getInstance();
        firstNameEditText= (EditText) findViewById(R.id.first_name);
        lastNameEditText= (EditText) findViewById(R.id.last_name);
        emailEditText= (EditText) findViewById(R.id.email_signup);
        repeatPswdEditText= (EditText) findViewById(R.id.password_repeat);
        choosePswdEditText= (EditText) findViewById(R.id.password_choose);
        cancelButton= (Button) findViewById(R.id.buttonCancel);
        signUpButton= (Button) findViewById(R.id.button_signup);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        googleSignIn= (Button) findViewById(R.id.googleSignInButton);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fname = firstNameEditText.getText().toString().trim();
                final String lName = lastNameEditText.getText().toString().trim();
                final String email = emailEditText.getText().toString().trim();
                final String cPwd = choosePswdEditText.getText().toString().trim();
                String rPwd = repeatPswdEditText.getText().toString().trim();
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) findViewById(selectedId);

                if(fname.length()==0 || lName.length()==0 || email.length()==0 || cPwd.length()==0 || rPwd.length()==0){
                    Toast.makeText(SignUp.this, "Enter valid Details", Toast.LENGTH_SHORT).show();
                }else if(!cPwd.equals(rPwd)){
                    Toast.makeText(SignUp.this, "Passwords didn't match. Enter same passwords!", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(email, cPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Log.d("demo","not success");
                                Toast.makeText(SignUp.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest pr= new UserProfileChangeRequest.Builder().setDisplayName(fname +" "+ lName).build();
                                firebaseUser.updateProfile(pr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(!task.isSuccessful()){
                                            Toast.makeText(SignUp.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }else{
                                            Log.d("demo","profile update completed");
                                            mAuth.signOut();
                                            Toast.makeText(SignUp.this, "User successfully created", Toast.LENGTH_SHORT).show();
                                            user =new User();
                                            user.setLname(lName);
                                            user.setEmail(email);
                                            user.setFname(fname);
                                            user.setGender(""+radioSexButton.getText());
                                            user.setUid(firebaseUser.getUid());
                                            databaseReference.child("Users").child(user.getUid()).setValue(user);
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

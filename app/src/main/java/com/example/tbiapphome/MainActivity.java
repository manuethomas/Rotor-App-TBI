package com.example.tbiapphome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001 ;
    TextView signInTextView;
    TextView signUpTextView;
    TextView bottomWordTextView;
    Button signUpButton;
    Button googleButton;
    EditText uNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    ProgressBar progressBarLoginScreen;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    Boolean signUpActive = true;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        signUpButton = findViewById(R.id.signUpButton);
        googleButton = findViewById(R.id.googleButton);
        signUpTextView = findViewById(R.id.signUpTextView);
        bottomWordTextView = findViewById(R.id.bottomWordTextView);
        uNameEditText = findViewById(R.id.uNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        progressBarLoginScreen = findViewById(R.id.progressBarLoginScreen);
        progressBarLoginScreen.setVisibility(View.INVISIBLE);

        //google sign in config
        //google sign in
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        if (mAuth.getCurrentUser() != null){
            login();
        }

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signUpActive){
                    if (emailEditText.getText().toString().equalsIgnoreCase("") || passwordEditText.getText().toString().equalsIgnoreCase("")){
                        Snackbar.make(findViewById(R.id.loginScreenMainConstraintLayout),"Invalid username or password", Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        progressBarLoginScreen.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progressBarLoginScreen.setVisibility(View.INVISIBLE);
                                            // Sign up success, update UI with the signed-in user's information
                                            Log.i("info", "createUserWithEmail:success");
                                            Snackbar.make(findViewById(R.id.loginScreenMainConstraintLayout), " Account created", Snackbar.LENGTH_SHORT).show();
                                            login();
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(uNameEditText.getText().toString())
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.i("info", "User profile updated.");
                                                            }
                                                        }
                                                    });
                                        } else {
                                            progressBarLoginScreen.setVisibility(View.INVISIBLE);
                                            // If sign up fails, display a message to the user.
                                            Log.i("info", "createUserWithEmail:failure", task.getException());
                                            Snackbar.make(findViewById(R.id.loginScreenMainConstraintLayout), " Account creation failed", Snackbar.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }

                }
                else {
                    progressBarLoginScreen.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressBarLoginScreen.setVisibility(View.INVISIBLE);
                                        // Sign in success, update UI with the signed-in user's information
                                        Snackbar.make(findViewById(R.id.loginScreenMainConstraintLayout), " Signed in", Snackbar.LENGTH_SHORT).show();
                                        Log.i("Info", "signInWithEmail:success");
                                    } else {
                                        progressBarLoginScreen.setVisibility(View.INVISIBLE);
                                        // If sign in fails, display a message to the user.
                                        Log.i("info", "signInWithEmail:failure", task.getException());
                                        Snackbar.make(findViewById(R.id.loginScreenMainConstraintLayout), " Sign in failed", Snackbar.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
        signInTextView = findViewById(R.id.signInTextView);
        signInTextView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpActive = false;
                    findViewById(R.id.fullNameTextView).setVisibility(View.GONE);
                    findViewById(R.id.uNameEditText).setVisibility(View.GONE);
                    signUpTextView.setText("Sign In");
                    //now changing the bottom line components
                    bottomWordTextView.setText("New user?");
                    signInTextView.setText("Sign Up");
                    //now changing the button texts
                    signUpButton.setText("Sign In");
                    googleButton.setText("Sign In with GOOGLE");
                    return true;
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void login(){
        Intent intent = new Intent(getApplicationContext(), MachineActivity.class);
        startActivity(intent);
    }
    
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.i("info", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.i("info", "Google sign in failed", e);
                Snackbar.make(findViewById(R.id.loginScreenMainConstraintLayout), " Google sign in failed", Snackbar.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]
        progressBarLoginScreen.setVisibility(View.VISIBLE);
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("info", "signInWithCredential:success");
                            Snackbar.make(findViewById(R.id.loginScreenMainConstraintLayout), " Signed in", Snackbar.LENGTH_SHORT).show();
                            login();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("info", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.loginScreenMainConstraintLayout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            login();
                        }

                        // [START_EXCLUDE]
                        progressBarLoginScreen.setVisibility(View.INVISIBLE);
                        // [END_EXCLUDE]
                    }
                });
    }
}

package bw.ac.biust.datingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private AuthStateListener firebaseAuthStateListener;
    private FirebaseAuth mAuth;
    private Button mBtnSign;
    private Button mLoginBtn,mLogToSign;
    private EditText mLoginName,mLoginPass;
    String name,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginBtn=(Button)findViewById(R.id.loginBtn);
        mLogToSign=(Button)findViewById(R.id.logToSign);
        mLoginName=(EditText)findViewById(R.id.loginName);
        mLoginPass=(EditText)findViewById(R.id.loginPass);



        this.mAuth = FirebaseAuth.getInstance();
        this.firebaseAuthStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                   startActivity(new Intent(LoginActivity.this, MainActivity.class));

                }
            }
        };


        mLogToSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                name= mLoginName.getText().toString();
                password= mLoginPass.getText().toString();
               if(name.isEmpty()&&password.isEmpty()){
                   mLoginName.setError("ENTER YOUR EMAIL");
                   mLoginPass.setError("ENTER YOUR PASSWORD");
                   Toast.makeText(LoginActivity.this, "YOU FORGOT TO ENTER YOUR EMAIL OR PASSWORD", Toast.LENGTH_SHORT).show();
               }else{

                LoginActivity.this.mAuth = FirebaseAuth.getInstance();
                LoginActivity.this.mAuth.signInWithEmailAndPassword(LoginActivity.this.mLoginName.getText().toString(), LoginActivity.this.mLoginPass.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "ERROR SIGNING IN", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }}
        });
    }
    public void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.firebaseAuthStateListener);
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        super.onStop();
        this.mAuth.removeAuthStateListener(this.firebaseAuthStateListener);
    }
}





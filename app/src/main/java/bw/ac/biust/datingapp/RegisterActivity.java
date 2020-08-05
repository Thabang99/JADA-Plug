package bw.ac.biust.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class RegisterActivity extends AppCompatActivity {

    private AuthStateListener firebaseAuthStateListener;
    private FirebaseAuth mAuth;

    private RadioGroup mRadioGroup;

    private Button mRegister;
    private EditText mRegisterPass,mConfirmPass,mRegisterName,mRealName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new AuthStateListener() {
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }
        };

        mRegister = (Button) findViewById(R.id.registerBtn);
        mRegisterPass = (EditText) findViewById(R.id.registerPass);
        mConfirmPass = (EditText) findViewById(R.id.confirmPass);
        mRadioGroup = (RadioGroup) findViewById(R.id.genderBtn);
        mRegisterName = (EditText) findViewById(R.id.registerName);
        mRealName = (EditText) findViewById(R.id.realName);
        mRegister.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final RadioButton radioButton = (RadioButton) findViewById(RegisterActivity.this.mRadioGroup.getCheckedRadioButtonId());
                if (radioButton.getText() != null) {
                    String email = mRegisterName.getText().toString();
                    String password = mRegisterPass.getText().toString();
                    final String name = mRealName.getText().toString();

                    if(email.isEmpty()&&password.isEmpty()&&name.isEmpty()){
                        mRegisterName.setError("ENTER EMAIL");
                        mRegisterPass.setError("ENTER PASSWORD");
                        mRealName.setError("ENTER NAME");
                    }else {

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
                            }else{
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                Map userInfo = new HashMap<>();
                                userInfo.put("name", name);
                                userInfo.put("sex", radioButton.getText().toString());
                                userInfo.put("profileImageUrl", "default");
                                userInfo.put("bio", "default");
                                currentUserDb.updateChildren(userInfo);


                                mAuth.signOut();
                                Toast.makeText(RegisterActivity.this, "successfully registered, proceed to log in", LENGTH_SHORT).show();
                                Intent i =new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }
            }}
        });

    }

    /* Access modifiers changed, original: protected */
    public void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.firebaseAuthStateListener);
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        super.onStop();
        this.mAuth.removeAuthStateListener(this.firebaseAuthStateListener);
    }


    public void back(View view) {
        Intent intent = new Intent(RegisterActivity.this , LoginActivity.class);
        startActivity(intent);
    }
}
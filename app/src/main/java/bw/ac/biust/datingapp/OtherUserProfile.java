package bw.ac.biust.datingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Map;

import bw.ac.biust.datingapp.Cards.cards;
import bw.ac.biust.datingapp.Chat.ChatActivity;
import bw.ac.biust.datingapp.Matches.MatchesActivity;



public class OtherUserProfile extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    ChipNavigationBar bottomNav;
    private FirebaseAuth mAuth;
    private DatabaseReference mProfileDatabase;
    private String userId;
    private TextView mName,mBio,mAge,mLocation,mHobbies;
    String name,bio,age,location,hobbies,profileImageUrl,oppositeId;
    private ImageView mProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        SharedPreferences preferences = getBaseContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        oppositeId=preferences.getString("profileId","none");


        mName= (TextView) findViewById(R.id.otherName);
        mBio= (TextView) findViewById(R.id.otherBio);
        mAge= (TextView) findViewById(R.id.otherAge);
        mLocation= (TextView) findViewById(R.id.otherLocation);
        mHobbies=(TextView) findViewById(R.id.otherHobbies);
        mProfileImage=(ImageView) findViewById(R.id.otherProfile);


        //Toast.makeText(this, oppositeId, Toast.LENGTH_SHORT).show();

        bottomNav= findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                switch (id) {

                    case R.id.home1:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(OtherUserProfile.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }, 500);
                        break;


                    case R.id.profile123:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent1 = new Intent(OtherUserProfile.this, ProfileActivity.class);
                                startActivity(intent1);
                            }
                        }, 500);
                        break;


                    case R.id.match:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent2 = new Intent(OtherUserProfile.this, MatchesActivity.class);
                                startActivity(intent2);
                            }
                        }, 500);
                        break;

                    case R.id.logout123:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                signOut();
                            }
                        }, 500);
                        break;
                }

            }
        });


         DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(oppositeId);

         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 Map<String,Object> map=(Map<String, Object>) snapshot.getValue();

                 if(map.get("name")!=null){
                     name=map.get("name").toString();
                     mName.setText(name);
                 }

                 if(map.get("bio")!=null){
                     bio=map.get("bio").toString();
                     mBio.setText(bio);
                 }

                 if(map.get("age")!=null){
                     age=map.get("age").toString();
                     mAge.setText(age);
                 }

                 if(map.get("location")!=null){
                     location=map.get("location").toString();
                     mLocation.setText(location);
                 }

                 if(map.get("hobbies")!=null){
                     hobbies=map.get("hobbies").toString();
                     mHobbies.setText(hobbies);
                 }

                 Glide.clear(mProfileImage);
                 if(map.get("profileImageUrl")!=null){
                     profileImageUrl=map.get("profileImageUrl").toString();
                     switch (profileImageUrl){
                         case "default":
                             Glide.with(getApplication()).load(R.drawable.person).into(mProfileImage);
                             break;

                         default:
                             Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                             break;
                     }
                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

    }

            public void signOut() {
                mAuth.signOut();
                Intent i = new Intent(OtherUserProfile.this, LoginActivity.class);
                startActivity(i);
                finish();
                return;
            }
        }

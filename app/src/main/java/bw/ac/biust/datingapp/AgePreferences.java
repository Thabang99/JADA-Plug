package bw.ac.biust.datingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import bw.ac.biust.datingapp.Cards.arrayAdapter;
import bw.ac.biust.datingapp.Cards.cards;
import bw.ac.biust.datingapp.Matches.MatchesActivity;

public class AgePreferences extends AppCompatActivity {

    private bw.ac.biust.datingapp.Cards.arrayAdapter arrayAdapter;
    private cards[] cards_data;
    private String currentUid;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private int i;
    ListView listView;
    private FirebaseAuth mAuth;
    private String oppositeUserSex;
    List<cards> rowItems;
    private String userSex,mPreference,preference;
    private DatabaseReference usersDb;
    private BottomNavigationView bottomNavigationView;
    ChipNavigationBar bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_preferences);

        bottomNav= findViewById(R.id.bottom_nav);



        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                switch (id){

                    case R.id.home1:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent(AgePreferences.this,MainActivity.class);
                                startActivity(intent);
                            }
                        },500);
                        break;


                    case R.id.profile123:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent1=new Intent(AgePreferences.this,ProfileActivity.class);
                                startActivity(intent1);
                            }
                        },500);
                        break;


                    case R.id.match:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent2=new Intent(AgePreferences.this, MatchesActivity.class);
                                startActivity(intent2);
                            }
                        },500);
                        break;

                    case R.id.logout123:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                signOut();
                            }
                        },500);
                        break;
                }


            }
        });


        final String oppositeUserSex="";
        String userSex;
        mAuth = FirebaseAuth.getInstance();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseAuth instance = FirebaseAuth.getInstance();
        currentUid = instance.getCurrentUser().getUid();

        checkUserSex();

        rowItems = new ArrayList<cards>();
        arrayAdapter = new arrayAdapter(this, R.layout.item, this.rowItems);
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentUid).setValue(true);
                Toast.makeText(AgePreferences.this, "Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yeps").child(currentUid).setValue(true);
                isConnectionMatch(userId);
                Toast.makeText(AgePreferences.this, "Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();

                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileId", userId);
                editor.apply();

                if(!userId.equals("")){
                    Intent intent= new Intent(AgePreferences.this,OtherUserProfile.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUid).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(AgePreferences.this, "new Connection", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUid).child("ChatId").setValue(key);
                    usersDb.child(currentUid).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });


    }


    public void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("preference").getValue() != null){
                        userSex = dataSnapshot.child("preference").getValue().toString();
                        switch (userSex){
                            case "Male":
                                oppositeUserSex = "Female";
                                break;
                            case "Female":
                                oppositeUserSex = "Male";
                                break;
                        }

                        if (dataSnapshot.exists()){
                            if (dataSnapshot.child("preference").getValue() != null){
                                mPreference = dataSnapshot.child("preference").getValue().toString();
                                switch (mPreference){
                                    case "young adult":
                                        preference = "young adult";
                                        break;
                                    case "adult":
                                        mPreference = "adult";
                                        break;

                                    case "older adult":
                                        mPreference = "older adult";
                                        break;
                                }

                            }
                        getOppositeUser();
                    }}


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void getOppositeUser(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("sex").getValue() != null) {
                    if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUid) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUid) && dataSnapshot.child("sex").getValue().toString().equals(oppositeUserSex)&&dataSnapshot.child("preference").getValue().toString().equals(mPreference)) {
                        String profileImageUrl = "default";
                        String bio="default";
                        if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")) {
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }

                        if (!dataSnapshot.child("bio").getValue().equals("default")) {
                            bio=dataSnapshot.child("bio").getValue().toString();
                        }
                        cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), bio,profileImageUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
        Intent i = new Intent(AgePreferences.this,LoginActivity.class);
        startActivity(i);
        finish();
        return;
    }


    public void goToProfile(View view) {

        Intent i = new Intent(AgePreferences.this,ProfileActivity.class);
        i.putExtra("userSex",userSex);

        startActivity(i);
    }

    public void toLocation(View view) {

        Intent intent= new Intent(AgePreferences.this, LocationPreferences.class);
        startActivity(intent);
    }

    public void toAgePreference(View view) {
        Intent intent= new Intent(AgePreferences.this, AgePreferences.class);
        startActivity(intent);
    }

    public void toSameSexPreference(View view) {
        Intent intent= new Intent(AgePreferences.this, SameGenderPreference.class);
        startActivity(intent);
    }
}
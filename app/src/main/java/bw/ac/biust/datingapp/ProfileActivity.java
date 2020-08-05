package bw.ac.biust.datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class ProfileActivity extends AppCompatActivity {

    private TextView mNameField , mBioFiled;
    private Button mConfirm, mBack;
    private ImageView mProfileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private String userId,name, bio, profileImageUrl,userSex,agePreference,location,hobbies,age;
    private Uri resultUri;
    private StorageReference mStorageReference;
    private RadioGroup mAgeRadio,mHobbiesRadio;
    private EditText mAge,mLoacation;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mStorageReference = FirebaseStorage.getInstance().getReference();

        //String userSex= getIntent().getExtras().getString("userSex");


        mNameField=(TextView) findViewById(R.id.profileName);
        mBioFiled= (TextView) findViewById(R.id.bio);
        mConfirm= (Button) findViewById(R.id.confirmProfile);
        mBack=(Button) findViewById(R.id.backBtnProfile);
        mProfileImage=(ImageView) findViewById(R.id.profileImageView);
        mAgeRadio=(RadioGroup) findViewById(R.id.agePreferenceBtn);
        mHobbiesRadio=(RadioGroup) findViewById(R.id.hobbiesBtn);
        mAge=(EditText)findViewById(R.id.userAge);
        mLoacation=(EditText)findViewById(R.id.locationField);

            progressDialog=new ProgressDialog(ProfileActivity.this);




        mAuth= FirebaseAuth.getInstance();
        userId= mAuth.getCurrentUser().getUid();




        mCustomerDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent , 1);
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("image is uploading...");
                progressDialog.show();
                saveUserInformation();
                Toast.makeText(ProfileActivity.this, "INFO UPDATED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
   }

   private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int selectedId = mAgeRadio.getCheckedRadioButtonId();
                int selected2Id = mHobbiesRadio.getCheckedRadioButtonId();
                if(dataSnapshot.exists()&& dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map= (Map<String, Object>)dataSnapshot.getValue();
                    if(map.get("name")!=null){

                        name=map.get("name").toString();
                        mNameField.setText(name);
                    }

                    if(map.get("bio")!=null){

                        bio=map.get("bio").toString();
                        mBioFiled.setText(bio);
                    }

                    if(map.get("sex")!=null){
                        userSex = map.get("sex").toString();
                    }


                    if(map.get("age")!=null){
                         age= map.get("age").toString();
                         mAge.setText(age);
                    }

                    if(map.get("location")!=null){
                        location= map.get("location").toString();
                        mLoacation.setText(location);
                    }

                    if(map.get("preference")!=null){
                        agePreference= map.get("preference").toString();
                        mAgeRadio.check(selectedId);
                    }

                    if(map.get("hobbies")!=null){
                        agePreference= map.get("hobbies").toString();
                        mHobbiesRadio.check(selected2Id);
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError DatabaseError) {

            }
        });

    }

    private void saveUserInformation() {

        name = mNameField.getText().toString();
        bio=mBioFiled.getText().toString();
        location=mLoacation.getText().toString();
     age=mAge.getText().toString();
       int selectedId = mAgeRadio.getCheckedRadioButtonId();RadioButton mAgeRadio =findViewById(selectedId);
        int selected2Id = mHobbiesRadio.getCheckedRadioButtonId();RadioButton mHobbiesRadio =findViewById(selected2Id);

        if(mAgeRadio.getText().equals("18-25")){
        agePreference="young adult";
       }
        else if(mAgeRadio.getText().equals("26-35")){
            agePreference="adult";
        }

       else if(mAgeRadio.getText().equals("36-45")){
           agePreference="older adult"; }

        ///////////////////////
      if(mHobbiesRadio.getText().equals("SINGING")){
           hobbies="SINGING";
       }

        else if(mHobbiesRadio.getText().equals("VIDEO GAMES")){
            hobbies="VIDEO GAMES";
        } else if(mHobbiesRadio.getText().equals("READING NOVELS")){
            hobbies="READING NOVELS";
        }


        Map userInfo= new HashMap();
        userInfo.put("name",name);
        userInfo.put("bio",bio);
       userInfo.put("location",location);
       userInfo.put("age",age);
        userInfo.put("preference",agePreference);
        userInfo.put("hobbies",hobbies);

        mCustomerDatabase.updateChildren(userInfo);

        if(resultUri!=null){
            StorageReference filepath= FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap=null;
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,20,baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask= filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!urlTask.isSuccessful());
                    Uri downloadUrl= urlTask.getResult();

                    Map userInfo=new HashMap();
                   userInfo.put("profileImageUrl",downloadUrl.toString());
                   mCustomerDatabase.updateChildren(userInfo);
                   finish();
                   return;
 }
            });


        }else {
            finish();
        }



   }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 1 && resultCode == Activity.RESULT_OK){
                final Uri imageUri = data.getData();
                resultUri = imageUri;
                mProfileImage.setImageURI(resultUri);
            }

    }

  public void signOut(View view) {
    mAuth.signOut();
    Intent i = new Intent(ProfileActivity.this,LoginActivity.class);
    startActivity(i);
    finish();
    return;
}


    public void goToProfile(View view) {

        Intent i = new Intent(ProfileActivity.this,ProfileActivity.class);
        i.putExtra("userSex",userSex);

        startActivity(i);}}
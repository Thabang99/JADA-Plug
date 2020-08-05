package bw.ac.biust.datingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserGuidesActivity extends AppCompatActivity {

    private Button getStartedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guides);

        getStartedBtn= (Button) findViewById(R.id.startBtn);

        SharedPreferences preferences= getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String FirstTime =preferences.getString("FirstTimeInstall","");


        if(FirstTime.equals("Yes")){

            Intent intent= new Intent(UserGuidesActivity.this,LoginActivity.class);
            startActivity(intent);

        }
        else {

            SharedPreferences.Editor editor= preferences.edit();
            editor.putString("FirstTimeInstall","Yes");
            editor.apply();

            getStartedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i= new Intent(UserGuidesActivity.this,LoginActivity.class);
                    startActivity(i);

                }
            });





        }


    }
}
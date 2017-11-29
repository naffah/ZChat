package app.naffah.zchat.LaunchScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import app.naffah.zchat.Activities.TabView;

/**
 * Created by Naffah Amin on 11/17/2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, TabView.class);
        startActivity(intent);
        finish();
    }
}

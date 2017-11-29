package app.naffah.zchat.PhoneAuth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.naffah.zchat.R;
import app.naffah.zchat.Activities.TabView;

public class PhoneAuthToTabView extends AppCompatActivity {

    private Button gotoTabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_auth_to_tab_view);

        gotoTabView = (Button) findViewById(R.id.gotoTabView);
        gotoTabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneAuthToTabView.this, TabView.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

package app.naffah.zchat.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;

import app.naffah.zchat.Adapters.SectionsPageAdapter;
import app.naffah.zchat.Fragments.ChatFragment;
import app.naffah.zchat.Fragments.ContactsFragment;
import app.naffah.zchat.R;

public class TabView extends AppCompatActivity {

    private static final String TAG = "FragementManager";
    private static final int REQUEST_PERMISSION = 2001;

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(
                    TabView.this,
                    new String[] {Manifest.permission.READ_CONTACTS},
                    REQUEST_PERMISSION
            );
        }

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        //Setup view pager with the sections adapter
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION)
        {
            if (grantResults.length != 0)
            {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                    finish();
            }
        }
    }

    private void setupViewPager (ViewPager viewPager)
    {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChatFragment(), "Chats");
        adapter.addFragment(new ContactsFragment(), "Contacts");
        viewPager.setAdapter(adapter);
    }
}

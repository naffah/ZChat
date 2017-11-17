package app.naffah.zchat.LaunchScreen;

import android.app.Application;
import android.os.SystemClock;

/**
 * Created by Naffah Amin on 11/17/2017.
 */

public class ScreenSpeed extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);
    }
}
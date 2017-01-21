package us.a7351.dynamicautonomousselector;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity implements OnMenuTabClickListener {

    private final String TAG = MainActivity.class.getName();
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static JsonHoster hoster;
    private Fragment variableEditorFragment;
    private Fragment autonomousGeneratorFragment;
    BottomBar bottomBar;

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        variableEditorFragment = new VariableEditorFragment();
        autonomousGeneratorFragment = new AutonomousGeneratorFragment();

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setMaxFixedTabs(1);
        bottomBar.setItems(R.menu.bottombar_items);

        bottomBar.setOnMenuTabClickListener(this);

        bottomBar.mapColorForTab(0, "#" + Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.tab1)));
        bottomBar.mapColorForTab(1, "#" + Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.tab2)));

        // We want to make sure that we can write to the hard drive
        verifyStoragePermissions(this);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hoster.stop();
    }

    @Override
    public void onMenuTabSelected(@IdRes int menuItemId) {
        switch (menuItemId) {
            case R.id.tab_variable_editor:
                switchFragment(variableEditorFragment);
                break;
            case R.id.tab_autonomous_generator:
                switchFragment(autonomousGeneratorFragment);
                break;
        }
        System.gc();
    }

    @Override
    public void onMenuTabReSelected(@IdRes int menuItemId) {

    }

    public void switchFragment(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contentContainer, fragment)
                .commit();
    }
}

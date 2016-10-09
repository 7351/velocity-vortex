package us.a7351.dynamicautonomousselector;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mainLayout;

    List<Option> allOptions;

    OptionTranslater translater;

    Button pushButton;

    HashMap<String, String> selectedItems;

    final static String TAG = "MainActivity";

    SharedPreferences preferences;

    JsonHoster hoster;

    CoordinatorLayout coordinatorLayout;

    AlertDialog manualDialog;

    // TODO: Add documentation and javadoc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (LinearLayout) findViewById(R.id.activity_main);

        translater = new OptionTranslater(mainLayout, getBaseContext());

        pushButton = (Button) findViewById(R.id.pushButton);
        pushButton.setOnClickListener(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.snackbarPosition);

        try {
            hoster = new JsonHoster();
        } catch (IOException e) {
            e.printStackTrace();
        }

        preferences = getSharedPreferences("selectorpreferences", MODE_WORLD_READABLE);

        initializeOptions();

    }

    private void initializeOptions() {
        // Total amount of options there are
        allOptions = new ArrayList<>();
        allOptions.addAll(Arrays.asList(UserOptions.options));
        translater.show(allOptions);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pushButton:
                selectedItems = new HashMap<>();
                // Deserialize the options
                for (Option generalOption : allOptions) {
                    if (generalOption.getType().equals("Number")) {
                        NumberOption currentNumberOption = (NumberOption) generalOption;
                        EditText numberEditText = currentNumberOption.editText;
                        String editTextValue = numberEditText.getText().toString();
                        if (!editTextValue.equals("")) {
                            Log.d(TAG, generalOption.OptionTitle + ": " + editTextValue);
                            selectedItems.put(generalOption.sharedPreferenceKey, editTextValue);
                        }
                    }
                    if (generalOption.getType().equals("Radio")) {
                        RadioOption currentRadioOption = (RadioOption) generalOption;
                        RadioGroup radioGroup = currentRadioOption.radioGroup;
                        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        if (checkedRadioButtonId != -1) {
                            RadioButton selectedRadioButton = (RadioButton) mainLayout.findViewById(checkedRadioButtonId);
                            //RadioButton selectedRadioButton = currentRadioOption.radioButtonList.get(checkedRadioButtonId);
                            String selectedOption = selectedRadioButton.getText().toString();
                            Log.d(TAG, generalOption.OptionTitle + ": " + selectedOption);
                            selectedItems.put(generalOption.sharedPreferenceKey, selectedOption);
                        }
                    }
                }

                // Host the hashmap json on a webserver if it's on the driver station
                if (appInstalledOrNot("com.qualcomm.ftcdriverstation")) {
                    postDataUsingWireless();
                }
                // Otherwise, save to sharedpreferences and let the OpMode open them
                if (appInstalledOrNot("com.qualcomm.ftcrobotcontroller")) {
                    postDataUsingPref();
                }
                if (!appInstalledOrNot("com.qualcomm.ftcrobotcontroller") && !appInstalledOrNot("com.qualcomm.ftcdriverstation")) {
                    Log.d(TAG, "Manual option");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    // set title
                    alertDialogBuilder.setTitle("Where is the app running on?");
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(true)
                            .setView(R.layout.choose_push_mode);
                    manualDialog = alertDialogBuilder.create();
                    manualDialog.show();
                }

                break;
            case R.id.robotButton:
                postDataUsingPref();
                manualDialog.dismiss();
                break;
            case R.id.driverStationButton:
                postDataUsingWireless();
                manualDialog.dismiss();
                break;
        }
    }

    private void postDataUsingPref() {
        // Save the data locally on the device (for Robot controller)
        Log.d(TAG, "Using sharedpreferences mode");
        JSONObject jsonObject = new JSONObject(selectedItems);
        String JsonData = jsonObject.toString();
        preferences.edit().putString("selectionjson", JsonData).apply();
        Snackbar.make(coordinatorLayout,
                "Data pushed! Now open the robot controller app " +  new String(Character.toChars(0x1F601)),
                Snackbar.LENGTH_SHORT).show();
    }

    private void postDataUsingWireless() {
        Log.d(TAG, "Using server mode on IP addr. " + getDottedDecimalIP(getLocalIPAddress()));
        JSONObject jsonObject = new JSONObject(selectedItems);
        String JsonData = jsonObject.toString();
        hoster.setData(JsonData);
        if (getLocalIPAddress() != null) {
            Snackbar.make(coordinatorLayout,
                    "Data pushed! You may now exit but do not kill this application " + new String(Character.toChars(0x1F601)),
                    Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(coordinatorLayout,
                    "You first must pair the robot controller and have a stable connection",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private byte[] getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if (inetAddress instanceof Inet4Address) { // fix for Galaxy Nexus. IPv4 is easy to use :-)
                            return inetAddress.getAddress();
                        }
                        //return inetAddress.getHostAddress().toString(); // Galaxy Nexus returns IPv6
                    }
                }
            }
        } catch (Exception ex) {
            //Log.e("AndroidNetworkAddressFactory", "getLocalIPAddress()", ex);
        }
        return null;
    }

    private String getDottedDecimalIP(byte[] ipAddr) {
        //convert to dotted decimal notation:
        String ipAddrStr = "";
        if (ipAddr != null) {
            for (int i = 0; i < ipAddr.length; i++) {
                if (i > 0) {
                    ipAddrStr += ".";
                }
                ipAddrStr += ipAddr[i] & 0xFF;
            }
        }
        return ipAddrStr;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hoster.stop();
    }
}

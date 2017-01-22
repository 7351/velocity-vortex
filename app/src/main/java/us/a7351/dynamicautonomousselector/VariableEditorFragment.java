package us.a7351.dynamicautonomousselector;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import static us.a7351.dynamicautonomousselector.MainActivity.hoster;

public class VariableEditorFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = VariableEditorFragment.class.getName();

    LinearLayout optionsLinearLayout;
    List<Option> allOptions;
    OptionTranslater translater;
    Button pushButton;
    HashMap<String, String> selectedItems;
    AlertDialog manualDialog;
    CoordinatorLayout coordinatorLayout;
    Context context;

    public VariableEditorFragment() {
        // Required empty public constructor
    }

    private void initializeOptions() {
        // Make a new ArrayList and add all of the options in UserOptions
        // We do this so we can iterate through all options easily
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
                            RadioButton selectedRadioButton = (RadioButton) optionsLinearLayout.findViewById(checkedRadioButtonId);
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
                if ((!appInstalledOrNot("com.qualcomm.ftcrobotcontroller") && !appInstalledOrNot("com.qualcomm.ftcdriverstation")) || (appInstalledOrNot("com.qualcomm.ftcrobotcontroller") && appInstalledOrNot("com.qualcomm.ftcdriverstation"))) {
                    Log.d(TAG, "Manual option");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_variable_editor, container, false);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();

        if (v != null) {

            coordinatorLayout = (CoordinatorLayout) v.getParent().getParent().getParent();

            optionsLinearLayout = (LinearLayout) v.findViewById(R.id.options_linear_layout);

            translater = new OptionTranslater(optionsLinearLayout, context);

            pushButton = (Button) v.findViewById(R.id.pushButton);
            pushButton.setOnClickListener(this);

            // Inject the options into the linearlayout
            initializeOptions();
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Get all layout objects and create translater to inject the options to the LinearLayout
        this.context = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void postDataUsingPref() {
        // Save the data locally on the device (for Robot controller)
        // Retro: Now it is named local file mode
        Log.d(TAG, "Using local file mode");
        JSONObject jsonObject = new JSONObject(selectedItems);
        String JsonData = jsonObject.toString();
        downloadAndStoreJson(JsonData);
        Snackbar.make(coordinatorLayout,
                "Data pushed! Now open the robot controller app " +  new String(Character.toChars(0x1F601)),
                Snackbar.LENGTH_SHORT).show();
    }

    private void postDataUsingWireless() {
        // Create a webserver on port 8080 over the wifi direct network and host the string
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
        PackageManager pm = context.getPackageManager();
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

    /**
     * Use this function to save the options to options.json file
     * @param jsonFileContent the string of the JSON content
     */
    private void downloadAndStoreJson(String jsonFileContent){

        byte[] jsonArray = jsonFileContent.getBytes();

        File fileToSaveJson = new File(Environment.getExternalStorageDirectory().getPath() + "/FIRST/" , "options.json");

        BufferedOutputStream bos;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(fileToSaveJson));
            bos.write(jsonArray);
            bos.flush();
            bos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            jsonArray = null;
            System.gc();
        }

    }

}

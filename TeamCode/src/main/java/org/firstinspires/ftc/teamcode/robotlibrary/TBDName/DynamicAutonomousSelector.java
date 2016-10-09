package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 10/8/2016.
 */

public class DynamicAutonomousSelector {

    public HashMap<String, String> selectorChoices;

    private SharedPreferences pref;

    public DynamicAutonomousSelector() {
        try {
            Context context = FtcRobotControllerActivity.getContext().createPackageContext("tk.leoforney.dynamicchooser", 0);
            pref = context.getSharedPreferences("selectorpreferences", Context.MODE_WORLD_READABLE);
            String JsonFromServer = getJsonFromServer("http://192.168.49.249:8080/");
            if (!JsonFromServer.equals("")) {
                Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
                selectorChoices = new Gson().fromJson(JsonFromServer, mapType);
            } else {
                String JsonFromPref = pref.getString("selectionjson", null);
                RobotLog.d(JsonFromPref);
                selectorChoices = new HashMap<>();
                /*
                if (JsonFromPref == null) {
                    selectorChoices = new HashMap<>();
                } else {
                    if (!JsonFromPref.equals("")) {
                        Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
                        selectorChoices = new Gson().fromJson(JsonFromPref, mapType);
                    }
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJsonFromServer(String url) throws IOException {

        BufferedReader inputStream = null;

        URL jsonUrl = new URL(url);
        URLConnection dc = jsonUrl.openConnection();

        dc.setConnectTimeout(5000);
        dc.setReadTimeout(5000);

        inputStream = new BufferedReader(new InputStreamReader(
                dc.getInputStream()));

        // read the JSON results into a string
        String jsonResult = inputStream.readLine();
        inputStream.close();
        return jsonResult;
    }


}

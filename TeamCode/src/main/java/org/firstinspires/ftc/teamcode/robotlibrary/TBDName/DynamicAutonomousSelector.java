package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by Leo on 10/8/2016.
 */

public class DynamicAutonomousSelector {

    public HashMap<String, String> selectorChoices;

    private final static Type mapType = new TypeToken<HashMap<String, String>>() {
    }.getType();

    public DynamicAutonomousSelector() {
        HashMap<String, String> serverHashMap = new HashMap<>();
        HashMap<String, String> fileHashMap = new HashMap<>();
        try {
            String JsonFromServer = getJsonFromServer("http://192.168.49.249:8080/");
            RobotLog.d("Json: " + JsonFromServer);
            serverHashMap = new Gson().fromJson(JsonFromServer, mapType);
        } catch (Exception e) {
            RobotLog.d("Driver station doesn't have the selections :(");
        }
        try {
            File OptionsFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/FIRST/",
                    "options.json");
            String JsonFromFile = getJsonFromFile(OptionsFile);
            fileHashMap = new Gson().fromJson(JsonFromFile, mapType);
            OptionsFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (serverHashMap.size() == 0) {
            selectorChoices = fileHashMap;
        } else {
            selectorChoices = serverHashMap;
        }

        RobotLog.d(fileHashMap.toString());
        RobotLog.d(serverHashMap.toString());

        if (selectorChoices == null) {
            selectorChoices = new HashMap<>();
        }
    }

    private static String getJsonFromServer(String url) throws Exception {

        BufferedReader inputStream;

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

    private static String getJsonFromFile(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        String str = "";
        int content;
        while ((content = fis.read()) != -1) {
            str += (char) content;
        }
        fis.close();
        return str;
    }


}

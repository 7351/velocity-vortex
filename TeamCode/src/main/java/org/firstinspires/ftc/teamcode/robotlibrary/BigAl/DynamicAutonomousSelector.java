package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Leo on 10/8/2016.
 */

public class DynamicAutonomousSelector {

    private final static Type mapType = new TypeToken<HashMap<String, String>>() {
    }.getType();
    private HashMap<String, String> selectorChoices;
    private ArrayList<Node> allNodes;

    public DynamicAutonomousSelector() {
        allNodes = new ArrayList<>();
        readAddresses();
        RobotLog.d("IP Addresses: " + allNodes.toString());
        HashMap<String, String> serverHashMap = new HashMap<>();
        HashMap<String, String> fileHashMap = new HashMap<>();
        try {
            String JsonFromServer = getJsonFromServer("http://" + allNodes.get(0).ip + ":8080/");
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
            RobotLog.d("Robot controller doesn't have the selections :(");
        }

        if (serverHashMap.size() == 0) {
            selectorChoices = fileHashMap;
        } else {
            selectorChoices = serverHashMap;
        }

        RobotLog.d("File: " + fileHashMap.toString());
        RobotLog.d("Server: " + serverHashMap.toString());

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

    public HashMap<String, String> getSelectorChoices() {
        return selectorChoices;
    }

    /*
     * Code borrowed from http://android-er.blogspot.com/2015/12/retrieve-ip-and-mac-addresses-from.html
     * for discovering all ip addresses and mac addresses on wifi network using arp.
     */
    private void readAddresses() {
        allNodes.clear();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    String mac = splitted[3];
                    if (mac.matches("..:..:..:..:..:..") && !mac.equals("00:00:00:00:00:00")) { // Making sure it isn't itself
                        Node thisNode = new Node(ip);
                        allNodes.add(thisNode);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Getter methods
    public double getNumberDouble(String key, double defaultNumber) {
        return ((selectorChoices.containsKey(key)) ? Double.valueOf(selectorChoices.get(key)) : defaultNumber);
    }

    public int getNumberInt(String key, int defaultNumber) {
        return ((selectorChoices.containsKey(key)) ? Integer.valueOf(selectorChoices.get(key)) : defaultNumber);
    }

    public String getRadio(String key, String defaultNumber) {
        return ((selectorChoices.containsKey(key)) ? String.valueOf(selectorChoices.get(key)) : defaultNumber);
    }

    public String get(String key, String defaultValue) {
        String returnValue = defaultValue;
        if (selectorChoices != null) {
            returnValue = selectorChoices.get(key);
        }
        return returnValue;
    }

    // Class for each device on the wifi direct network
    class Node {
        String ip;

        Node(String ip) {
            this.ip = ip;
        }

        @Override
        public String toString() {
            return ip;
        }
    }


}

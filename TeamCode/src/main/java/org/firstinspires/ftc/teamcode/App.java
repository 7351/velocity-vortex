package org.firstinspires.ftc.teamcode;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
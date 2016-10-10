package us.a7351.dynamicautonomousselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Leo on 10/9/2016.
 */

public class SplashActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8500);
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

}

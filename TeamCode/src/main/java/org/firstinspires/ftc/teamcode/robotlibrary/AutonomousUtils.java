package org.firstinspires.ftc.teamcode.robotlibrary;

import android.content.Context;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.R;

import java.text.DecimalFormat;

/**
 * Created by Dynamic Signals on 11/6/2016.
 */

public class AutonomousUtils {

    public static final double WAITTIME = 0.05;
    public static final int DEADBEEF = 666;
    public static final int COMPLETED = 555;
    public static final DecimalFormat df = new DecimalFormat("#.##");

    private ElapsedTime time;

    public AutonomousUtils() {
    }

    public AutonomousUtils(ElapsedTime time) {
        this.time = time;
    }
    public AutonomousUtils(int stage, ElapsedTime time){}


    public void waitTime(double time) {
        // Old way of waiting failed, must use the following
        /*
        if (stage == <Waiting stage number here>) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
         */
    }

    // I created this function to identify when a failsafe has been triggered.
    public static void failSafeError(HardwareMap hardwareMap) {
        SoundPlayer.getInstance().play(hardwareMap.appContext, R.raw.failsafeerrorsound);
    }
}

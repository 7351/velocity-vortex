package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Dynamic Signals on 11/6/2016.
 */
@Deprecated
public class AutonomousUtils {

    public static final double WAITTIME = 0.15;
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
}

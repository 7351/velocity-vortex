package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Dynamic Signals on 11/6/2016.
 */

public class AutonomousUtils {

    public final double WAITTIME = 0.15;
    int stage;
    ElapsedTime time;

    public AutonomousUtils(int stage, ElapsedTime time) {
        this.stage = stage;
        this.time = time;
    }

    public void waitTime(double timeWait) {
        if (time.time() > timeWait) {
            stage++;
            time.reset();
        }
    }

    public void waitTime() {
        waitTime(WAITTIME);
    }
}

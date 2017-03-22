package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;

/**
 * Created by leo on 3/18/17.
 */

public abstract class StateMachineOpMode extends OpMode implements StateMachine {

    public double stage = 0;
    public ElapsedTime time = new ElapsedTime();

    public void start() {
        time.reset();
    }

    public void next() {
        stage++;
        time.reset();
    }

    public void waitTime() {
        if (time.time() > AutonomousUtils.WAITTIME) {
            stage++;
        }
    }

    public void waitTime(Runnable runnable) {
        if (time.time() > AutonomousUtils.WAITTIME) {
            stage++;
            runnable.run();
        }
    }
}


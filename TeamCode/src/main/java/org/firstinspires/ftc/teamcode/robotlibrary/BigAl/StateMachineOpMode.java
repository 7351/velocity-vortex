package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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
}


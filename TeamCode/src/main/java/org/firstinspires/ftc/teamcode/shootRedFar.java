package org.firstinspires.ftc.teamcode;

/**
 * Created by Dynamic Signals on 3/27/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "shootRedFar", group = "AWorking")
public class shootRedFar extends capBallRedFar {

    // Completed on 3/27/17 12:20PM
    @Override
    public void init() {
        super.init();
        target = "Shoot only";
    }

}

package org.firstinspires.ftc.teamcode;

/**
 * Created by Dynamic Signals on 3/27/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "shootBlueFar", group = "AWorking")
public class shootBlueFar extends capBallBlueFar {

    // Completed on 3/27/17 12:28PM
    @Override
    public void init() {
        super.init();
        target = "Shoot only";
    }

}

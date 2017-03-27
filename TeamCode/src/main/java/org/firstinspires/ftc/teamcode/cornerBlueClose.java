package org.firstinspires.ftc.teamcode;

/**
 * Created by Dynamic Signals on 3/27/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "cornerBlueClose", group = "AWorking")
public class cornerBlueClose extends beaconBlueClose {
    // Completed on 3/27/17 3:05PM
    @Override
    public void init() {
        super.init();
        corner = true;
    }

}

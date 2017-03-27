package org.firstinspires.ftc.teamcode;

/**
 * Created by Dynamic Signals on 3/27/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "cornerRedClose", group = "AWorking")
public class cornerRedClose extends beaconRedClose {

    // Completed on 3/27/17 1:32PM

    @Override
    public void init() {
        super.init();
        corner = true;
    }

}

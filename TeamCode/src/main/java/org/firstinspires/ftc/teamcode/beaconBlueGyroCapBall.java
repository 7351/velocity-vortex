package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "beaconBlueGyroCapBall", group = "BNotWorking")
@Disabled
public class beaconBlueGyroCapBall extends beaconBlueGyro {

    @Override
    public void init() {
        super.init();
        capBallGet = true;
    }
}



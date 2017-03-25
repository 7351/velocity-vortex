package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "BeaconBlueCapBall", group = "BNotWorking")
public class BeaconBlueCapBall extends BeaconBlue {

    @Override
    public void init() {
        super.init();
        capBallGet = true;
    }
}



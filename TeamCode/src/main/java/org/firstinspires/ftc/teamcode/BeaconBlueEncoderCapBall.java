package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

/**
 * Created by Dynamic Signals on 3/24/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "BeaconBlueEncCapBall", group = "AWorking")
public class BeaconBlueEncoderCapBall extends BeaconBlueEncoder {

    @Override
    public void init() {
        super.init();
        capBallGet = true;
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

/**
 * Created by Dynamic Signals on 3/24/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "BeaconRedEncCapBall", group = "BNotWorking")
public class BeaconRedEncoderCapBall extends BeaconRedEncoder {

    @Override
    public void init() {
        super.init();
        capBallGet = true;
    }

}

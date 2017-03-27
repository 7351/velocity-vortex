package org.firstinspires.ftc.teamcode.gyro;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "beaconRedGyroCapBall", group = "BNotWorking")
@Disabled
public class beaconRedGyroCapBall extends beaconRedGyro {

    @Override
    public void init() {
        super.init();
        capBallGet = true;
    }
}
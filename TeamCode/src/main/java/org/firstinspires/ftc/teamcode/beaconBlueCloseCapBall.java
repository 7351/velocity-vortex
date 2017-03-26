package org.firstinspires.ftc.teamcode;

/**
 * Created by Dynamic Signals on 3/24/2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "BeaconBlueEncCapBall", group = "AWorking")
public class beaconBlueCloseCapBall extends beaconBlueClose {

    @Override
    public void init() {
        super.init();
        capBallGet = true;
    }
}

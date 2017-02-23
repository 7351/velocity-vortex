package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Dynamic Signals on 2/20/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpPID")
public class TeleOpNoPID extends TeleOp {

    public void init() {
        super.init();
        flyWheel.FlyWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


}

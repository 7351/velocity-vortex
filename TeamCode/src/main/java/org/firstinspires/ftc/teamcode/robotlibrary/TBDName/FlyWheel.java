package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Dynamic Signals on 10/31/2016.
 */

public class FlyWheel {

    public final double defaultStartingPower = 0.65; // This is the starting power that we click the left dpad
    public final double incrementValue = 0.05;
    public DcMotor FlyWheelMotor; // This is the big black wheel to launch balls (it goes fast)
    public double currentPower = 0;
    public boolean currentlyRunning = false; // By default, we want it off

    public FlyWheel(HardwareMap hardwareMap) {
        FlyWheelMotor = hardwareMap.dcMotor.get("FlyWheelMotor");
        FlyWheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FlyWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // We now are going to start using PID for our launches to make them more consistent
    }

    public void powerMotor() {
        if (currentlyRunning) {
            if (FlyWheelMotor.getPower() != currentPower) {
                FlyWheelMotor.setPower(currentPower);
            }
        } else {
            FlyWheelMotor.setPower(0);
        }
    }



}

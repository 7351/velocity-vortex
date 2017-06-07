package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Dynamic Signals on 10/31/2016.
 */

public class FlyWheel {

    public double defaultStartingPower = 0.83; // This is the starting power that we click the left dpad
    public final double incrementValue = 0.05;
    public DcMotor FlyWheelMotor; // This is the big black wheel to launch balls (it goes fast)
    public double currentPower = 0;
    public boolean currentlyRunning = false; // By default, we want it off

    public FlyWheel(HardwareMap hardwareMap) {
        FlyWheelMotor = hardwareMap.dcMotor.get("FlyWheelMotor");
        FlyWheelMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        FlyWheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FlyWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FlyWheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
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

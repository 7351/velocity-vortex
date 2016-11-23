package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Dynamic Signals on 10/31/2016.
 */

public class FlyWheel {

    public final double defaultStartingPower = 0.3;
    public final double incrementValue = 0.05;
    public DcMotor FlyWheelMotor; // This is the big black wheel to launch balls
    public double currentPower = 0;
    public boolean currentlyRunning = false;

    public FlyWheel(HardwareMap hardwareMap) {
        FlyWheelMotor = hardwareMap.dcMotor.get("FlyWheelMotor");
    }
}

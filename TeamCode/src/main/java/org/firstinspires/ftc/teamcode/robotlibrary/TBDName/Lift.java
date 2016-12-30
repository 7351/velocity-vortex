package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Dynamic Signals on 12/27/2016.
 */

public class Lift {

    public DcMotor LiftMotor;
    public Servo LiftServo;

    public double liftMotorPower = 0.75; // The power to move the lift motor up and down at

    public double minimum = 0.2; // The minimum (down) servo position
    public double maximum = 0.8; // The maximum (up) servo position
    public double servoIncrement = 0.05;
    public double currentServoPosition;

    public Lift(HardwareMap hardwareMap) {
        LiftMotor = hardwareMap.dcMotor.get("LiftMotor");
        LiftServo = hardwareMap.servo.get("LiftServo");

        currentServoPosition = maximum;
    }

    public void incrementServo() {

        currentServoPosition += servoIncrement;
    }

    public void decrementServo() {
        currentServoPosition -= servoIncrement;
    }

}

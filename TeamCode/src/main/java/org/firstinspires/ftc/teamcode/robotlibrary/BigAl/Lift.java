package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Dynamic Signals on 12/27/2016.
 */

public class Lift {

    public DcMotor LiftMotor1;
    public DcMotor LiftMotor2;
    public Servo LiftServo;

    public double minimum = 0; // The minimum (down) servo position
    public double ideal = 0.6; // The ideal (closed) position
    public double maximum = 1; // The maximum (up) servo position (released)
    public double servoIncrement = 0.05;
    public double currentServoPosition;

    public Lift(HardwareMap hardwareMap) {
        LiftMotor1 = hardwareMap.dcMotor.get("LiftMotor");
        LiftMotor2 = hardwareMap.dcMotor.get("LiftMotor2");
        LiftServo = hardwareMap.servo.get("LiftServo");

        currentServoPosition = minimum;

        LiftServo.setPosition(currentServoPosition);
    }

    public void setLiftPower(double power) {
        LiftMotor1.setPower(power);
        LiftMotor2.setPower(power);
    }

    public void incrementServo() {

        currentServoPosition += servoIncrement;
    }

    public void decrementServo() {
        currentServoPosition -= servoIncrement;
    }

}

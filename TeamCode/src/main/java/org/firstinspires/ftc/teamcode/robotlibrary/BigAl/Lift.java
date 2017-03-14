package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Dynamic Signals on 12/27/2016.
 */

public class Lift {

    public DcMotor LiftMotor;
    public DcMotor LiftMotor2;
    public Servo CapBallServoLeft;
    public Servo CapBallServoRight;
                                        // L     R
    public final static double[] closed = {0.72, 0.18};
    public final static double[] open = {0, 0.85};
    public final static double[] grab = {0.18, 0.68};

    public double leftPosition;
    public double rightPosition;

    public static double servoIncrement = 0.025;

    public Lift(HardwareMap hardwareMap) {
        LiftMotor = hardwareMap.dcMotor.get("LiftMotor");
        LiftMotor2 = hardwareMap.dcMotor.get("LiftMotor2");

        CapBallServoLeft = hardwareMap.servo.get("CapBallServoLeft");
        CapBallServoRight = hardwareMap.servo.get("CapBallServoRight");

        leftPosition = closed[0];
        rightPosition = closed[1];

        updateServo();
    }

    public void updateServo() {
        leftPosition = Range.clip(leftPosition, 0, 1);
        rightPosition = Range.clip(rightPosition, 0, 1);

        CapBallServoLeft.setPosition(leftPosition);
        CapBallServoRight.setPosition(rightPosition);
    }

    public void grabPositions() {
        leftPosition = CapBallServoLeft.getPosition();
        rightPosition = CapBallServoRight.getPosition();
    }

    public void setLiftPower(double power) {
        LiftMotor.setPower(power);
        LiftMotor2.setPower(power);
    }

}

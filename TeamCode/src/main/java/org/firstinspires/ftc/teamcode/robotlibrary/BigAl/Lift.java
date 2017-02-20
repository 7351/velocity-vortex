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

    public DcMotor LiftMotor1;
    public DcMotor LiftMotor2;
    public Servo CapBallServoLeft;
    public Servo CapBallServoRight;
                                        // L     R
    public final static double[] closed = {0.75, 0.18};
    public final static double[] open = {0, 1};

    public double servoIncrement = 0.025;

    public enum CapBallStatus {
        OPEN,
        CLOSED
    }

    public enum CapBallServo {
        LEFT,
        RIGHT
    }

    private CapBallStatus currentCapBallStatus;

    public Lift(HardwareMap hardwareMap) {
        LiftMotor1 = hardwareMap.dcMotor.get("LiftMotor");
        LiftMotor2 = hardwareMap.dcMotor.get("LiftMotor2");

        CapBallServoLeft = hardwareMap.servo.get("CapBallServoLeft");
        CapBallServoRight = hardwareMap.servo.get("CapBallServoRight");

        currentCapBallStatus = CapBallStatus.CLOSED; // Initialized closed
    }

    public void setServo(CapBallServo servo, CapBallStatus status) {
        Servo selectedServo = (servo.equals(CapBallServo.LEFT)) ? CapBallServoLeft : CapBallServoRight;
        int index = (servo.equals(CapBallServo.LEFT)) ? 0 : 1;
        switch (status) {
            case OPEN:
                selectedServo.setPosition(open[index]);
                break;
            case CLOSED:
                selectedServo.setPosition(closed[index]);
                break;
        }
    }

    public void setLiftPower(double power) {
        LiftMotor1.setPower(power);
        LiftMotor2.setPower(power);
    }

}

package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake.IntakeSpec.A;
import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake.IntakeSpec.B;

/**
 * Created by Dynamic Signals on 10/31/2016.
 */

public class Intake {

    private DcMotor IntakeA; // This is the top roller
    private CRServo IntakeB1, IntakeB2, IntakeB3; // This is the bottom roller
    private DcMotor IntakeB;
    // Kevin requested three servos for Intake B on 3/13/17

    public static boolean NewBServos = false;

    public Intake(HardwareMap hardwareMap) {
        IntakeA = hardwareMap.dcMotor.get("IntakeA");
        if (NewBServos) {
            IntakeB1 = hardwareMap.crservo.get("IntakeB1");
            IntakeB2 = hardwareMap.crservo.get("IntakeB2");
            IntakeB3 = hardwareMap.crservo.get("IntakeB3");
        } else {
            IntakeB = hardwareMap.dcMotor.get("IntakeB");
            IntakeB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        IntakeA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        stopIntake(IntakeSpec.BOTH);
    }

    public void setIntakePower(IntakeSpec spec, double power) {
        switch (spec) {
            case A:
                IntakeA.setPower(power);
                break;
            case B:
                if (NewBServos) {
                    IntakeB1.setPower(power);
                    IntakeB2.setPower(power);
                    IntakeB3.setPower(power);
                } else {
                    IntakeB.setPower(power);
                }
                break;
        }
    }

    public void setIntake(IntakeSpec spec, IntakeDirection direction) {
        double power = 0;
        switch (spec) {
            case A:
                power = (direction.equals(IntakeDirection.IN)) ? 1 : -1;
                setIntakePower(A, power);
                break;
            case B:
                power = (direction.equals(IntakeDirection.IN)) ? -1 : 1;
                setIntakePower(B, power);
                break;
            case BOTH:
                setIntake(A, direction);
                setIntake(B, direction);
                break;
        }
    }

    public void stopIntake(IntakeSpec spec) {
        switch (spec) {
            case A:
                setIntakePower(A, 0);
                break;
            case B:
                setIntakePower(B, 0);
                break;
            case BOTH:
                stopIntake(A);
                stopIntake(B);
        }
    }

    public enum IntakeSpec {
        A,
        B,
        BOTH
    }

    public enum IntakeDirection {
        IN,
        OUT
    }
}

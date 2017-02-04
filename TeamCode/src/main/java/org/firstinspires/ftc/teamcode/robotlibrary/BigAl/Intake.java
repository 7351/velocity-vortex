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

    public DcMotor IntakeA; // This is the blue roller
    public CRServo IntakeBServo1; // This is the silver roller
    public CRServo IntakeBServo2; // Second servo on port 5
    // Kevin requested two servos for Intake B on 1/8/17

    public Intake(HardwareMap hardwareMap) {
        IntakeA = hardwareMap.dcMotor.get("IntakeA");
        IntakeBServo1 = hardwareMap.crservo.get("IntakeServoB1");
        IntakeBServo2 = hardwareMap.crservo.get("IntakeServoB2");
        stopIntake(IntakeSpec.BOTH);
    }

    public void setIntakePower(IntakeSpec spec, double power) {
        switch (spec) {
            case A:
                IntakeA.setPower(power);
                break;
            case B:
                IntakeBServo1.setPower(power);
                IntakeBServo2.setPower(power);
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
                setIntakePower(B, -0.05);
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

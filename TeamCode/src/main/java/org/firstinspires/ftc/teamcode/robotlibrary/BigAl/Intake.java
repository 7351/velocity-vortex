package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

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

    public void stopIntake(IntakeSpec spec) {
        switch (spec) {
            case A:
                setIntakePower(IntakeSpec.A, 0);
                break;
            case B:
                setIntakePower(IntakeSpec.B, -0.05);
                break;
            case BOTH:
                stopIntake(IntakeSpec.A);
                stopIntake(IntakeSpec.B);
        }
    }

    public enum IntakeSpec {
        A,
        B,
        BOTH
    }
}

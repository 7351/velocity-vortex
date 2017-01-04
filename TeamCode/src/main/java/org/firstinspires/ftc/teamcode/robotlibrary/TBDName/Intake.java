package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Dynamic Signals on 10/31/2016.
 */

public class Intake {

    public DcMotor IntakeA; // This is the blue roller
    public CRServo IntakeBServo; // This is the silver roller

    public Intake(HardwareMap hardwareMap) {
        IntakeA = hardwareMap.dcMotor.get("IntakeA");
        IntakeBServo = hardwareMap.crservo.get("IntakeServo");
    }

    public void setIntakePower(IntakeSpec spec, double power) {
        switch (spec) {
            case A:
                IntakeA.setPower(power);
                break;
            case B:
                IntakeBServo.setPower(power);
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
        }
    }

    public enum IntakeSpec {
        A,
        B
    }
}

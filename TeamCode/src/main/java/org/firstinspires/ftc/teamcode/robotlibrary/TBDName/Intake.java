package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Dynamic Signals on 10/31/2016.
 */

public class Intake {

    public DcMotor IntakeA; // This is the blue roller
    public DcMotor IntakeB; // This is the silver roller

    public Intake(HardwareMap hardwareMap) {
        IntakeA = hardwareMap.dcMotor.get("IntakeA");
        IntakeB = hardwareMap.dcMotor.get("IntakeB");
    }

    public void setIntakePower(IntakeSpec spec, double power) {
        switch (spec) {
            case A:
                IntakeA.setPower(power);
                break;
            case B:
                IntakeB.setPower(power);
                break;
        }
    }

    public void stopIntake(IntakeSpec spec) {
        switch (spec) {
            case A:
                setIntakePower(IntakeSpec.A, 0);
                break;
            case B:
                setIntakePower(IntakeSpec.B, 0);
                break;
        }
    }

    public enum IntakeSpec {
        A,
        B
    }
}

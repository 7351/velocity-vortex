package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake.IntakeSpec.A;

/**
 * Created by Dynamic Signals on 10/31/2016.
 */

public class Intake {

    private DcMotor IntakeA;
    public Servo IntakeStopper;
    private double OpenPosition = .48;
    private double ClosePosition = .92;

    public Intake(HardwareMap hardwareMap) {
        IntakeA = hardwareMap.dcMotor.get("IntakeA");
        IntakeStopper = hardwareMap.servo.get("IntakeStopper");

        IntakeA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        stopIntake(IntakeSpec.A);
    }

    public void setIntakePower(IntakeSpec spec, double power) {
        switch (spec) {
            case A:
                IntakeA.setPower(power);
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
        }
    }

    public void stopIntake(IntakeSpec spec) {
        switch (spec) {
            case A:
                setIntakePower(A, 0);
                break;
        }
    }

    public void openStopper() {
        IntakeStopper.setPosition(OpenPosition);
    }

    public void closeStopper() {
        IntakeStopper.setPosition(ClosePosition);
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

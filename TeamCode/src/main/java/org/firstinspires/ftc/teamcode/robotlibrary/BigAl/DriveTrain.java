package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.Range;

public class DriveTrain {

    public final double DIFFERENCE = 0;
    public DcMotor LeftFrontMotor, RightFrontMotor, LeftBackMotor, RightBackMotor;
    public DcMotorController LeftMotorController, RightMotorController;
    public VoltageSensor LeftSensor, RightSensor;

    public DriveTrain(HardwareMap hardwareMap) {
        if (hardwareMap != null) {
            LeftFrontMotor = hardwareMap.dcMotor.get("LeftFrontMotor");
            RightFrontMotor = hardwareMap.dcMotor.get("RightFrontMotor");
            LeftBackMotor = hardwareMap.dcMotor.get("LeftBackMotor");
            RightBackMotor = hardwareMap.dcMotor.get("RightBackMotor");

            RightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            RightBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);

            LeftMotorController = LeftFrontMotor.getController();
            RightMotorController = RightFrontMotor.getController();

            LeftSensor = (VoltageSensor) LeftMotorController;
            RightSensor = (VoltageSensor) RightMotorController;
        }

    }

    public double getVoltage() {
        double sum = LeftSensor.getVoltage() + RightSensor.getVoltage();
        return sum/2;
    }

    public DriveTrain(OpMode opMode) {
        this(opMode.hardwareMap);
    }

    public void driveStraight(double startingPower, double difference) {
        if (startingPower > 0) {
            if (difference > 0) {
                powerLeft(startingPower);
                powerRight(startingPower - difference);
            }
            if (difference < 0) {
                powerLeft(startingPower - difference);
                powerRight(startingPower);
            }
        }
        if (startingPower < 0) {
            if (difference > 0) {
                powerLeft(startingPower);
                powerRight(startingPower + difference);
            }
            if (difference < 0) {
                powerLeft(startingPower + difference);
                powerRight(startingPower);
            }
        }
        if (difference == 0) {
            powerLeft(startingPower);
            powerRight(startingPower);
        }
    }

    public void driveStraight(double startingPower) {
        driveStraight(startingPower, DIFFERENCE);
    }

    public void driveStraight() {
        driveStraight(1);
    }

    public void powerLeft(double power) {
        double clippedPower = Range.clip(power, -1, 1);
        LeftFrontMotor.setPower(clippedPower);
        LeftBackMotor.setPower(clippedPower);

    }

    public void powerRight(double power) {//dont forget to turn encoders off
        double clippedPower = Range.clip(power, -1, 1);
        RightFrontMotor.setPower(clippedPower);
        RightBackMotor.setPower(clippedPower);

    }

    public void stopRobot() {
        powerLeft(0);
        powerRight(0);
    }

    public void setMode(DcMotor.RunMode mode) {
        LeftFrontMotor.setMode(mode);
        LeftBackMotor.setMode(mode);
        RightFrontMotor.setMode(mode);
        RightBackMotor.setMode(mode);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        LeftFrontMotor.setZeroPowerBehavior(behavior);
        LeftBackMotor.setZeroPowerBehavior(behavior);
        RightFrontMotor.setZeroPowerBehavior(behavior);
        RightBackMotor.setZeroPowerBehavior(behavior);
    }

    /*
    public double getVoltage() {
        double averageVoltage = 0;
        for (DcMotor motor : DcMotors) {
            VoltageSensor sensor = (VoltageSensor) motor;
            if (sensor != null) {
                averageVoltage += sensor.getVoltage();
            }
        }
        averageVoltage /= 4;
        return averageVoltage;
    }
    */

    public boolean isBusy() {
        return LeftFrontMotor.isBusy() || RightFrontMotor.isBusy();
    }

    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
        return LeftFrontMotor.getZeroPowerBehavior();
    }

    public void rotate(GyroUtils.Direction turnDirection, double power) {
        switch (turnDirection) {
            case CLOCKWISE:
                powerLeft(power);
                powerRight(-power);
                break;
            case COUNTERCLOCKWISE:
                powerLeft(-power);
                powerRight(power);
                break;
        }
    }

}



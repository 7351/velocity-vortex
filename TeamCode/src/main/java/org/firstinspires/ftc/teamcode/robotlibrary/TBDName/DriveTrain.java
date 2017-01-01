package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.Range;

public class DriveTrain {

    public final double DEFAULTDIFFERENCE = 0.04;
    public DcMotor LeftFrontMotor;
    public DcMotor RightFrontMotor;
    public DcMotor LeftBackMotor;
    public DcMotor RightBackMotor;
    private VoltageSensor LeftDriveTrainVoltage;
    private VoltageSensor RightDriveTrainVoltage;

    public DriveTrain(HardwareMap hardwareMap) {
        if (hardwareMap != null) {
            LeftFrontMotor = hardwareMap.dcMotor.get("LeftFrontMotor");
            RightFrontMotor = hardwareMap.dcMotor.get("RightFrontMotor");
            LeftBackMotor = hardwareMap.dcMotor.get("LeftBackMotor");
            RightBackMotor = hardwareMap.dcMotor.get("RightBackMotor");

            LeftFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            LeftBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);

            LeftDriveTrainVoltage = hardwareMap.voltageSensor.get("Left Drive Train");
            RightDriveTrainVoltage = hardwareMap.voltageSensor.get("Right Drive Train");
        }

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
        driveStraight(startingPower, DEFAULTDIFFERENCE);
    }

    public void driveStraight() {
        driveStraight(1);
    }

    public void powerLeft(double power) {
        double clippedPower = Range.clip(power, -1, 1);
        double previousPower = LeftFrontMotor.getPower();
        if (clippedPower != previousPower) {
            LeftFrontMotor.setPower(clippedPower);
            LeftBackMotor.setPower(clippedPower);
        }
    }

    public void powerRight(double power) {
        double clippedPower = Range.clip(power, -1, 1);
        double previousPower = RightFrontMotor.getPower();
        if (clippedPower != previousPower) {
            RightFrontMotor.setPower(clippedPower);
            RightBackMotor.setPower(clippedPower);
        }
    }

    public void stopRobot() {
        powerLeft(0);
        powerRight(0);
    }

    public double getVoltage() {
        double averageVoltage = 0;
        averageVoltage = LeftDriveTrainVoltage.getVoltage() + RightDriveTrainVoltage.getVoltage();
        averageVoltage /= 2;
        return averageVoltage;
    }


}

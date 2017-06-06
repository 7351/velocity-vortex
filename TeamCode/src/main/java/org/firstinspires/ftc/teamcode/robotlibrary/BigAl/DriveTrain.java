package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.Range;

public class DriveTrain {

    public final double DIFFERENCE = 0;
    public DcMotor LeftFrontMotor, RightFrontMotor, LeftBackMotor, RightBackMotor;
    public DcMotorController LeftMotorController, RightMotorController;
    public VoltageSensor LeftSensor, RightSensor;
    public double LeftPower = 0, RightPower = 0;
    boolean spoofMotors = false;

    public DriveTrain(HardwareMap hardwareMap) {
        if (hardwareMap != null) {
            if (!spoofMotors) {
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

    }

    public DriveTrain(OpMode opMode) {
        this(opMode.hardwareMap);
    }

    /**
     * Get voltage function for getting the voltage of the robot programatically
     *
     * @return the voltage as a double point number
     */
    public double getVoltage() {
        double sum = 0;
        if (!spoofMotors) {
            sum = LeftSensor.getVoltage() + RightSensor.getVoltage();
        }
        return sum / 2;
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
        if (!spoofMotors) {
            double clippedPower = Range.clip(power, -1, 1);
            LeftFrontMotor.setPower(clippedPower);
            LeftBackMotor.setPower(clippedPower);
        }
        LeftPower = power;
    }

    public void powerRight(double power) {
        if (!spoofMotors) {
            double clippedPower = Range.clip(power, -1, 1);
            RightFrontMotor.setPower(clippedPower);
            RightBackMotor.setPower(clippedPower);
        }
        RightPower = power;
    }

    public void stopRobot() {
        powerLeft(0);
        powerRight(0);
    }

    public void setMode(DcMotor.RunMode mode) {
        if (!spoofMotors) {
            LeftFrontMotor.setMode(mode);
            LeftBackMotor.setMode(mode);
            RightFrontMotor.setMode(mode);
            RightBackMotor.setMode(mode);
        }

    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        if (!spoofMotors) {
            LeftFrontMotor.setZeroPowerBehavior(behavior);
            LeftBackMotor.setZeroPowerBehavior(behavior);
            RightFrontMotor.setZeroPowerBehavior(behavior);
            RightBackMotor.setZeroPowerBehavior(behavior);
        }
    }

}



package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class DriveTrain {

    public final double DIFFERENCE = 0.04;
    public DcMotor LeftFrontMotor;
    public DcMotor RightFrontMotor;
    public DcMotor LeftBackMotor;
    public DcMotor RightBackMotor;

    public DriveTrain(HardwareMap hardwareMap) {
        if (hardwareMap != null) {
            LeftFrontMotor = hardwareMap.dcMotor.get("LeftFrontMotor");
            RightFrontMotor = hardwareMap.dcMotor.get("RightFrontMotor");
            LeftBackMotor = hardwareMap.dcMotor.get("LeftBackMotor");
            RightBackMotor = hardwareMap.dcMotor.get("RightBackMotor");

            LeftFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            LeftBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }

    }

    public void driveStraight(double difference) {
        if (difference > 0) {
            powerLeft(1);
            powerRight(1 - difference);
        }
        if (difference < 0) {
            powerLeft(1 - difference);
            powerRight(1);
        }
        if (difference == 0) {
            powerLeft(1);
            powerRight(1);
        }
    }

    public void driveStraight() {
        driveStraight(DIFFERENCE);
    }

    public void powerLeft(double power) {
        LeftFrontMotor.setPower(Range.clip(power, -1, 1));
        LeftBackMotor.setPower(Range.clip(power, -1, 1));
    }

    public void powerRight(double power) {
        RightFrontMotor.setPower(Range.clip(power, -1, 1));
        RightBackMotor.setPower(Range.clip(power, -1, 1));
    }

    public void stopRobot() {
        powerLeft(0);
        powerRight(0);
    }


}

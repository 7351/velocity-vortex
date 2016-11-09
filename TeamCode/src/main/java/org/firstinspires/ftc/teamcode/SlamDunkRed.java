package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;

/**
 * Created by DynamicSignals on 10/16/2016.
 */

@Autonomous(name = "SlamDunkRed", group = "Testing")
public class SlamDunkRed extends OpMode {

    ColorUtils colorutils;

    GyroSensor gyro;

    int stage = 0;

    public DcMotor LeftFrontMotor;
    public DcMotor RightFrontMotor;
    public DcMotor LeftBackMotor;
    public DcMotor RightBackMotor;

    ElapsedTime waitTime = new ElapsedTime();

    private int TOLERANCE = 1;

    public void powerLeft(double power) {
        LeftFrontMotor.setPower(Range.clip(power, -1, 1));
        LeftBackMotor.setPower(Range.clip(power, -1, 1));
    }

    public void powerRight(double power) {
        RightFrontMotor.setPower(Range.clip(power, -1, 1));
        RightBackMotor.setPower(Range.clip(power, -1, 1));
    }

    public boolean isGyroInTolerance(int degree) {
        boolean returnValue = false;
        if ((gyro.getHeading() <= degree + TOLERANCE) && (gyro.getHeading() >= degree - TOLERANCE)) {
            returnValue = true;
        }
        return returnValue;
    }

    private int spoofedZero(int zeroDegree) {
        int ActualDegree = gyro.getHeading();
        int degree = ActualDegree - zeroDegree;
        if (degree > 360) {
            degree = degree - 360;
        }
        if (degree < 0) {
            degree = degree + 360;
        }
        return degree;
    }

    public void rotateUsingSpoofed(int ZeroDegree, int TargetDegree, double DivisionNumber) {
        int CurrentSpoofedDegree = spoofedZero(ZeroDegree); //An expected 39 gyro value from fake zero
        if (!isGyroInTolerance(TargetDegree)) {
            double DegreesOff = Math.abs(TargetDegree - CurrentSpoofedDegree);
            double RawPower = Range.clip(DegreesOff / DivisionNumber, 0, 1);
            powerLeft(-RawPower);
            powerRight(RawPower);
        }
    }


    public void driveOnHeading(int desiredDegree, double power) {
        int gyroDegree = spoofedZero(desiredDegree);
        int targetDegrees = 0;
        double leftStartPower = power;
        double rightStartPower = power;
        double dividerNumber = 12.5;

        if (gyroDegree > 0 && gyroDegree <= 90) {
            int error_degrees = Math.abs(targetDegrees - gyroDegree);
            double subtractivePower = error_degrees / dividerNumber;
            DbgLog.msg(String.valueOf(subtractivePower + ", " + error_degrees));
            if (power > 0) {
                leftStartPower = Range.clip(1 - subtractivePower, -1, 1);
            }
            if (power < 0) {
                leftStartPower = Range.clip(1 + subtractivePower, -1, 1);
            }

        }

        if (gyroDegree >= 270 && gyroDegree < 360) {
            int error_degrees = Math.abs(90 - (gyroDegree - 270));
            double subtractivePower = error_degrees / dividerNumber;
            DbgLog.msg(String.valueOf(subtractivePower + ", " + error_degrees));
            if (power > 0) {
                rightStartPower = Range.clip(1 - subtractivePower, -1, 1);
            }
            if (power < 0) {
                rightStartPower = Range.clip(1 + subtractivePower, -1, 1);
            }

        }

        powerRight(rightStartPower);
        powerLeft(leftStartPower);
    }

    /*public void turnTowardsHeading(int target) throws InterruptedException
    {
        int zAccumulated = gyro.getHeading();  //Set variables to gyro readings
        double turnSpeed = 0.15;

        while (Math.abs(zAccumulated - target) > 3) {  //Continue while the robot direction is further than three degrees from the target
            if (zAccumulated > target) {  //if gyro is positive, we will turn right
                powerLeft(turnSpeed);
                powerRight(-turnSpeed);
            }

            if (zAccumulated < target) {  //if gyro is positive, we will turn left
                powerLeft(-turnSpeed);
                powerRight(turnSpeed);
            }

            waitOneFullHardwareCycle();

            zAccumulated = gyro.getHeading();  //Set variables to gyro readings
            telemetry.addData("1. accu", String.format("%03d", zAccumulated));
        }

        powerLeft(0);  //Stop the motors
        powerRight(0);

        waitOneFullHardwareCycle();
    }*/

    public void driveOnHeading(int desiredDegree) {
        driveOnHeading(desiredDegree, 1);
    }

    @Override
    public void init() {

        gyro = hardwareMap.gyroSensor.get("gyro");

        LeftFrontMotor = hardwareMap.dcMotor.get("LeftFrontMotor");
        RightFrontMotor = hardwareMap.dcMotor.get("RightFrontMotor");
        LeftBackMotor = hardwareMap.dcMotor.get("LeftBackMotor");
        RightBackMotor = hardwareMap.dcMotor.get("RightBackMotor");

        colorutils = new ColorUtils(hardwareMap);

        LeftFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        LeftBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        gyro.calibrate();

    }

    @Override
    public void start() {
        gyro.calibrate();
        waitTime.reset();
    }

    @Override
    public void loop() {

        if (stage == 0) {//calibrating
            if (!gyro.isCalibrating()) {
                stage++;
                waitTime.reset();
            }
            telemetry.addData("Calibrating", String.valueOf(gyro.isCalibrating()));
        }
        if (stage == 1) {
            if (waitTime.time() <= 0.5) {
                powerLeft(.5);
                powerRight(.5);
            } else {
                powerLeft(0);
                powerRight(0);
                stage++;
                waitTime.reset();
            }
        }
        if (stage == 2) {
            if (waitTime.time() > 0.25) {
                stage++;
            }
        }
        if (stage == 3) {
            if (waitTime.time() <= 2) {
                powerLeft(-.09);
                powerRight(.09);
            } else {
                powerLeft(0);
                powerRight(0);
                stage++;
            }
        }
        if (stage == 4) {
            if (waitTime.time() > 1) {
                stage++;
            }
        }
        if (stage == 5) {
            while(!colorutils.aboveWhiteLine())
            {
                powerLeft(.5);
                powerRight(.5);
            }
            powerLeft(0);
            powerRight(0);
            stage++;
        }



        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(waitTime.time()));

    }
}
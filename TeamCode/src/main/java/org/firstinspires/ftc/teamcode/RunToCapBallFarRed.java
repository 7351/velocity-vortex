package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "CapBallTimeFarRed", group = "Testing")
public class RunToCapBallFarRed extends OpMode {

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

        if (stage == 0) {
            if (!gyro.isCalibrating()) {
                stage++;
                waitTime.reset();
            }
            telemetry.addData("Calibrating", String.valueOf(gyro.isCalibrating()));
        }
        if (stage == 1) {
            if (waitTime.time() <= 0.5) {
                driveOnHeading(0);
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
                driveOnHeading(315, 1);
            } else {
                powerLeft(0);
                powerRight(0);
                stage++;
            }
        }

        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(waitTime.time()));

    }
}

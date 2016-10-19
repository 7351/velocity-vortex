package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Leo on 10/11/2016.
 */

public class GyroUtils {

    public GyroSensor gyro;
    private int TOLERANCE = 1;
    private DriveTrain driveTrain;
    private HardwareMap hardwareMap;
    Telemetry telemetry;

    public GyroUtils(HardwareMap hardwareMap, DriveTrain driveTrain, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.driveTrain = driveTrain;
        if (hardwareMap.gyroSensor.get("gyro") != null) {
            gyro = hardwareMap.gyroSensor.get("gyro");
        }
    }

    public void setTolerance(int tolerance) {
        this.TOLERANCE = tolerance;
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
            driveTrain.powerLeft(-RawPower);
            driveTrain.powerRight(RawPower);
        }
    }


    public void driveOnHeading(int desiredDegree, double power) {
        //TODO: Fix function
        int gyroDegree = spoofedZero(desiredDegree);
        int targetDegrees = 0;
        double leftStartPower = power;
        double rightStartPower = power;
        double dividerNumber = 12.5;

        if (gyroDegree > 0 && gyroDegree <= 90) {
            int error_degrees = Math.abs(targetDegrees - gyroDegree);
            double subtractivePower = error_degrees / dividerNumber;
            RobotLog.d(String.valueOf(subtractivePower + ", " + error_degrees));
            telemetry.addData("Drifting Right", String.valueOf(error_degrees));
            if (power > 0) {
                rightStartPower = Range.clip(1 - subtractivePower, -1, 1);
            }
            if (power < 0) {
                rightStartPower = Range.clip(1 + subtractivePower, -1, 1);
            }

        }

        if (gyroDegree >= 270 && gyroDegree < 360) {
            int error_degrees = Math.abs(90 - (gyroDegree - 270));
            double subtractivePower = error_degrees / dividerNumber;
            RobotLog.d(String.valueOf(subtractivePower + ", " + error_degrees));
            telemetry.addData("Drifting Left", String.valueOf(error_degrees));
            if (power > 0) {
                leftStartPower = Range.clip(1 - subtractivePower, -1, 1);
            }
            if (power < 0) {
                leftStartPower = Range.clip(1 + subtractivePower, -1, 1);
            }

        }

        driveTrain.powerRight(rightStartPower);
        driveTrain.powerLeft(leftStartPower);
    }

    public void driveOnHeading(int desiredDegree) {
        driveOnHeading(desiredDegree, 1);
    }
}

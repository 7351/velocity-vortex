package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "CapBallTimeFarRed", group = "Testing")
public class RunToCapBallFarRed extends OpMode {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    ColorUtils colorUtils;

    GyroSensor gyro;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        gyro = gyroUtils.gyro;
        gyro.calibrate();


    }

    @Override
    public void start() {
        gyro.calibrate();
        colorUtils.lineColorSensor.enableLed(true);
    }

    @Override
    public void loop() {

        if (stage == 0) { //Calibrates gyro to 0
            if (!gyro.isCalibrating()) {
                stage++;
                time.reset();
            }
            telemetry.addData("Calibrating", String.valueOf(gyro.isCalibrating()));
        }
        if (stage == 1) { //drives forward 0.25 seconds
            if (time.time() <= 0.25) {
                driveTrain.powerLeft(1);
                driveTrain.powerRight(1);
            } else {
                driveTrain.powerLeft(0);
                driveTrain.powerRight(0);
                stage++;
                time.reset();
            }
        }
        if (stage == 2) {
            if (time.time() > 0.15) {
                stage++;
                time.reset();
            }
        }
        if (stage == 3) {
            if (gyro.getHeading() < (135 - 25)) {
                driveTrain.powerLeft(0.3);
                driveTrain.powerRight(-0.3);
            } if (gyro.getHeading() >= (135 - 25)) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 4) {
            if (time.time() > 0.15) {
                stage++;
                time.reset();
            }
        }
        if (stage == 5) {
            if (!colorUtils.aboveRedLine()) {
                //driveTrain.powerLeft(-0.8);
                //driveTrain.powerRight(-0.8);
                gyroUtils.driveOnHeading(315, 1);
            } else {
                driveTrain.stopRobot();
                stage++;
            }
        }

        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));

    }
}

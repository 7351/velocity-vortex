package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;

/**
 * Created by Dynamic Signals on 10/23/2016.
 */
@Autonomous(name = "RunToCapBallTimeCloseBlue", group = "Autonomous")
public class RunToCapBallTimeCloseBlue extends OpMode {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    //ColorUtils colorUtils;

    GyroSensor gyro;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        //colorUtils = new ColorUtils(hardwareMap);
        gyro = gyroUtils.gyro;
        gyro.calibrate();


    }

    @Override
    public void start() {

        gyro.calibrate();

        //colorUtils.bottomColorSensor.enableLed(true);

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
        if (stage == 1) {
            if (time.time() < 0.3) {
                gyroUtils.driveOnHeading(0);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        /*if (stage == 2) {
            if (time.time() > 0.15) {
                stage++;
                time.reset();
            }
        }
        if (stage == 3) {
            if (time.time() < 1.2) {
                gyroUtils.driveOnHeading(5);
            } else {
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
            if (!gyroUtils.isGyroInTolerance(225)) {
                gyroUtils.rotateUsingSpoofed(0, 45, 162, GyroUtils.Direction.CLOCKWISE);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 6) {
            if (time.time() > 0.15) {
                stage++;
                time.reset();
            }
        }
        if (stage == 7) {
            /*
            if (!colorUtils.aboveBlueLine()) {
                gyroUtils.driveOnHeading(225);
            }
             */
            /*if (time.time() < 1.5){
                gyroUtils.driveOnHeading(225, -1);
            }

            else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
    */

        RobotLog.d(String.valueOf("Time: " + time.time() + " , Stage: " + stage + " , Gyro: " + gyroUtils.gyro.getHeading()));
        //telemetry.addData("Stage", String.valueOf(stage));
        RobotLog.d(String.valueOf("X: " + gyro.rawX() + " , Y: " + gyro.rawY() + " , Z: " + gyro.rawZ()));
        telemetry.addData("Gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));
        //telemetry.addData("Color", String.valueOf("White: " + colorUtils.aboveWhiteLine() + ", Blue: " + colorUtils.aboveBlueLine()));


    }
}

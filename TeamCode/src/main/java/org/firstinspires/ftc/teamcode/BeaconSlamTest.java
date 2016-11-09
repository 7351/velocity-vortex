package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "BeaconSlamTest", group = "Testing")
public class BeaconSlamTest extends OpMode {

    private int stage = 17;
    private ElapsedTime time = new ElapsedTime();
    private DriveTrain driveTrain;
    private GyroUtils gyroUtils;
    private ColorUtils colorUtils;
    private GyroSensor gyro;
    private Intake intake;
    private FlyWheel flyWheel;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);

        gyro = gyroUtils.gyro;
        gyro.calibrate();

    }

    @Override
    public void start() {
        gyro.calibrate();
        colorUtils.lineColorSensor.enableLed(true);
        time.reset();
    }

    @Override
    public void loop() {

        if (stage == 17) {
            if (time.time() < 0.75) {
                driveTrain.driveStraight(-0.5);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 18) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
        if (stage == 19) {
            if (time.time() < 0.1) {
                driveTrain.driveStraight(0.5);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 20) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
        if (stage == 21) {
            switch (colorUtils.beaconColor()) {
                case RED:
                    if (time.time() > 5.1) {
                        time.reset();
                        stage = 17;
                    }
                    break;
                case BLUE:
                    time.reset();
                    stage++;
                    break;
            }
        }
        if (stage == 22) {
            if (time.time() < 1) {
                driveTrain.driveStraight();
            } else {
                driveTrain.stopRobot();
                stage++;
            }
        }

        RobotLog.d(String.valueOf(stage + " Gyro: " + gyro.getHeading() + " Time:" + time.time()));
        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("White Line", String.valueOf(colorUtils.aboveWhiteLine()));
        telemetry.addData("Beacon", String.valueOf(colorUtils.beaconColor()));

    }
}

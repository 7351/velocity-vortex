package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DynamicAutonomousSelector;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;

/**
 * Created by Dynamic Signals on 10/18/2016.
 */

@Autonomous(name = "DriveStraightOrNot", group = "Testing")
public class DriveStraightTest extends OpMode {

    DriveTrain driveTrain;
    GyroUtils gyroUtils;

    int stage = 0;

    ElapsedTime driveTime = new ElapsedTime();

    DynamicAutonomousSelector das;

    @Override
    public void init() {

        das = new DynamicAutonomousSelector();

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);

        gyroUtils.gyro.calibrate();
        
        if (das.getSelectorChoices().containsKey("divider")) {
            gyroUtils.dividerNumber = Double.valueOf(das.getSelectorChoices().get("divider"));
        }

    }

    @Override
    public void start() {

        gyroUtils.gyro.calibrate();

    }

    @Override
    public void loop() {

        if (stage == 0) {
            if (!gyroUtils.gyro.isCalibrating()) {
                stage++;
                driveTime.reset();
            }
        }

        if (stage == 1) {
            // We drive for 2 seconds at full power drive on heading and stop after the 2 seconds
            final double time = 2;
            if (driveTime.time() <= time) {
                //gyroUtils.driveOnHeading(330);
                driveTrain.driveStraight();
                //gyroUtils.basicDriveWithGyro(0);
            } else {
                driveTrain.stopRobot();
            }
        }

        telemetry.addData("Gyro", String.valueOf(gyroUtils.gyro.getHeading()));
        //telemetry.addData("Divider", String.valueOf(gyroUtils.dividerNumber));

    }
}

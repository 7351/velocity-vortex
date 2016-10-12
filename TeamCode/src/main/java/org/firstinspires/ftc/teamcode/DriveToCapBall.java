package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TBDName;

/**
 * Created by Leo on 10/11/2016.
 */
@Autonomous(name = "DriveToCapBall", group = "Autonomous")
public class DriveToCapBall extends OpMode {

    private TBDName tbdName;
    private GyroUtils gyroUtils;
    int stage = 0;

    @Override
    public void init() {

        tbdName = new TBDName(hardwareMap, telemetry);
        gyroUtils = tbdName.gyroUtils;

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
            }
        }
        if (stage == 1) {
            gyroUtils.driveOnHeading(0);
        }


    }
}

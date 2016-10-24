package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;

/**
 * Created by Dynamic Signals on 10/23/2016.
 */

public class GyroProofOfConcept extends OpMode {

    GyroUtils gyroUtils;
    DriveTrain driveTrain;


    private int stage = 0;
    private ElapsedTime time = new ElapsedTime();

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);


        gyroUtils.gyro.calibrate();
    }

    @Override
    public void start() {
        gyroUtils.gyro.calibrate();
    }

    @Override
    public void loop() {


    }
}

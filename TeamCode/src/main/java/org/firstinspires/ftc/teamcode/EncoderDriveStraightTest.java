package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderDrive;

/**
 * Created by Dynamic Signals on 12/6/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "EncoderDriveTest", group = "Testing")
public class EncoderDriveStraightTest extends OpMode {

    DriveTrain driveTrain;

    int stage = 0;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);

    }

    /* Encoder drive sequence
     * 1. Reset
     * 2. Set target
     * 3. Set mode to RUN_TO_POSITION
     * 4. Set motor power
     * 5. Wait until motor isn't busy
     */

    @Override
    public void start() {


    }

    @Override
    public void loop() {

        if (stage == 0) {
            new EncoderDrive(driveTrain).run(1600, 1);
        }
        if (stage == 1) {
            if (!driveTrain.isBusy()) {
                driveTrain.stopRobot();
                stage++;
            }
        }

        DbgLog.msg(String.valueOf(": " + driveTrain.LeftBackMotor.getCurrentPosition() + " - " + driveTrain.LeftBackMotor.getTargetPosition()));

    }
}

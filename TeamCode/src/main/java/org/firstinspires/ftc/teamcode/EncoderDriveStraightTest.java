package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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
        driveTrain.RightFrontMotor.setTargetPosition(-1000);

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        driveTrain.powerLeft(-0.75);
        driveTrain.powerRight(-0.75);

    }

    @Override
    public void loop() {

        if (driveTrain.RightFrontMotor.getCurrentPosition() < -1000) {
            driveTrain.stopRobot();
        }

        telemetry.addData("Left Front Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());

        DbgLog.msg(String.valueOf(": " + driveTrain.LeftBackMotor.getCurrentPosition() + " - " + driveTrain.LeftBackMotor.getTargetPosition()));

    }
}

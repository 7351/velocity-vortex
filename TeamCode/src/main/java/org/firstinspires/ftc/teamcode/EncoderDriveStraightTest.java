package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;

/**
 * Created by Dynamic Signals on 12/6/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "EncoderDriveTest", group = "Testing")
public class EncoderDriveStraightTest extends OpMode {

    DriveTrain driveTrain;

    DcMotor testingMotor;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);

    }

    @Override
    public void start() {

        driveTrain.setDriveTrainRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        driveTrain.setTargetPosition(4000);

        driveTrain.setDriveTrainRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        driveTrain.powerLeft(1);
        driveTrain.powerRight(1);

        /*

        //driveTrain.LeftBackMotor.setTargetPosition(-8000);
        //driveTrain.LeftFrontMotor.setTargetPosition(-8000);
        //driveTrain.RightBackMotor.setTargetPosition(-8000);
        //driveTrain.RightFrontMotor.setTargetPosition(-8000);

        //driveTrain.LeftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //driveTrain.RightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        driveTrain.LeftFrontMotor.setPower(-0.5);
        //driveTrain.RightFrontMotor.setPower(-0.5);
        //driveTrain.LeftBackMotor.setPower(-0.5);
        //driveTrain.RightBackMotor.setPower(-0.5);

        //driveTrain.powerLeft(0.5);
        //driveTrain.powerRight(0.5);
        */

    }

    @Override
    public void loop() {

        if (!driveTrain.isBusy()) {
            driveTrain.stopRobot();
        }

        DbgLog.msg(String.valueOf(": " + driveTrain.LeftBackMotor.getCurrentPosition() + " - " + driveTrain.LeftBackMotor.getTargetPosition()));

    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RESET_ENCODERS;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
//import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderDrive;

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

        //driveTrain.LeftFrontMotor.setMode(STOP_AND_RESET_ENCODER);
        driveTrain.RightFrontMotor.setMode(STOP_AND_RESET_ENCODER);
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


        //driveTrain.RightFrontMotor.setTargetPosition(1000);
        //driveTrain.LeftFrontMotor.setTargetPosition(-1000);
        driveTrain.RightFrontMotor.setTargetPosition(1000);

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //driveTrain.LeftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        driveTrain.powerLeft(0.5);
        driveTrain.powerRight(0.5);
        //driveTrain.powerRight(0.75);

    }

    @Override
    public void loop() {

        telemetry.addData("Left Front Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());


        if (driveTrain.RightFrontMotor.getCurrentPosition() >= 1000) {
            driveTrain.stopRobot();
        }

        /*telemetry.addData("Busy", String.valueOf(driveTrain.isBusy()));
        telemetry.addData("Positions", String.valueOf(driveTrain.LeftBackMotor.getCurrentPosition() + " - " + driveTrain.LeftBackMotor.getTargetPosition()));

        DbgLog.msg(String.valueOf(": " + driveTrain.LeftBackMotor.getCurrentPosition() + " - " + driveTrain.LeftBackMotor.getTargetPosition()));*/

    }
}

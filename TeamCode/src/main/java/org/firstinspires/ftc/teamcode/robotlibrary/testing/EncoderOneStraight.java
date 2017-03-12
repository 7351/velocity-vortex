package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderDrive;

/**
 * Created by Dynamic Signals on 12/6/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "EncoderOneStraightTest", group = "Testing")
public class EncoderOneStraight extends OpMode {

    DriveTrain driveTrain;

    int stage = 0;

    EncoderDrive drive;

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
            driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            int startingPositionLeft = driveTrain.LeftFrontMotor.getCurrentPosition();
            driveTrain.LeftFrontMotor.setTargetPosition(1000 + startingPositionLeft);
            stage++;
        }
        if (stage == 1){
            if (driveTrain.LeftFrontMotor.getCurrentPosition() < 1000){
                driveTrain.powerLeft(.5);
                driveTrain.powerRight(.5);
            }
            else {
                driveTrain.stopRobot();
                stage++;
            }
        }
        if (stage == 2) {
            driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            int startingPositionRight = driveTrain.RightFrontMotor.getCurrentPosition();
            driveTrain.RightFrontMotor.setTargetPosition(1000 + startingPositionRight);
            stage++;
        }
        if (stage == 3){
            if (driveTrain.RightFrontMotor.getCurrentPosition() < 1000){
                driveTrain.powerLeft(.5);
                driveTrain.powerRight(.5);
            }
            else {
                driveTrain.stopRobot();
                stage++;
            }
        }
        if (stage == 4) {
            driveTrain.LeftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            driveTrain.LeftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            int startingPositionLeft = driveTrain.LeftBackMotor.getCurrentPosition();
            driveTrain.LeftBackMotor.setTargetPosition(1000 + startingPositionLeft);
            stage++;
        }
        if (stage == 5){
            if (driveTrain.LeftBackMotor.getCurrentPosition() < 1000) {
                driveTrain.powerLeft(.5);
                driveTrain.powerRight(.5);
            }
            else {
                driveTrain.stopRobot();
                stage++;
            }
        }
        if (stage == 6) {
            driveTrain.RightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            driveTrain.RightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            int startingPositionRight = driveTrain.RightBackMotor.getCurrentPosition();
            driveTrain.RightBackMotor.setTargetPosition(1000 + startingPositionRight);
            stage++;
        }
        if (stage == 7){
            if (driveTrain.RightBackMotor.getCurrentPosition() < 1000){
                driveTrain.powerLeft(.5);
                driveTrain.powerRight(.5);
            }
            else {
                driveTrain.stopRobot();
                stage++;
            }
        }


        telemetry.addData("Left Front Position: ", driveTrain.LeftFrontMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());

        DbgLog.msg(String.valueOf(": " + driveTrain.LeftBackMotor.getCurrentPosition() + " - " + driveTrain.LeftBackMotor.getTargetPosition()));

    }
}

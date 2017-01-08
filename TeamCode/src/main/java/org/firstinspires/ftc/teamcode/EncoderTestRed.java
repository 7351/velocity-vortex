package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake.IntakeSpec;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderDrive;

/**
 * Created by Dynamic Signals on 12/6/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "EncoderTestRed", group = "Testing")
public class EncoderTestRed extends OpMode {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();

    DriveTrain driveTrain;

    GyroUtils gyroUtils;

    ColorUtils colorUtils;

    GyroSensor gyro;

    DcMotor FlyWheelMotor;

    DcMotor IntakeA;

    Intake intake;

    EncoderDrive drive;

    EncoderTurn turn;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        intake = new Intake(hardwareMap);
        FlyWheelMotor = hardwareMap.dcMotor.get("FlyWheelMotor");
        IntakeA = hardwareMap.dcMotor.get("IntakeA");

        gyro = gyroUtils.gyro;
        gyro.calibrate();

    }

    @Override
    public void start() {
        gyro.calibrate();
        colorUtils.lineColorSensor.enableLed(true);
    }

    @Override
    public void loop() {

        if (stage == 0) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 460, 0.5);
                drive.run();
            }
            if (drive.isCompleted()) {
                drive.completed();
                stage++;
            }
        }
        if (stage == 1) {
            if (time.time() > 3) {
                driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                drive = null;
                stage++;
            }
        }
        /*if (stage == 2) {
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 45, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage++;
            }
        }

        if (stage == 3){
            if(time.time() < 1)
            {
                driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                turn = null;
                stage = 100;
            }
        }
        if (stage == 4) { //drives forward 0.25 seconds
            if (time.time() <= 0.325) {
                driveTrain.driveStraight();
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 5)
        {
            if (time.time() < .8)
            {
                driveTrain.driveStraight();
            }
            else
            {
                driveTrain.stopRobot();
                stage++;
                time.reset();
                FlyWheelMotor.setPower(.3);
            }
        }

        if(stage == 6)
        {
            if (time.time() > 2)
            {
                time.reset();
                stage++;
            }
        }

        if (stage == 7)
        {
            if(time.time() < 2.5) {
                intake.setIntakePower(IntakeSpec.B, -1);
            }
            else
            {
                time.reset();
                stage++;
            }
        }

        if (stage == 8)
        {
            if (time.time() < .35)
                IntakeA.setPower(1);
            else
            {
                time.reset();
                stage++;
            }
        }

        if (stage == 9)
        {
            if(time.time() > 2.5)
            {
                intake.stopIntake(Intake.IntakeSpec.B);
                IntakeA.setPower(0);
                FlyWheelMotor.setPower(0);
                time.reset();
                stage++;
            }
        }

        if (stage == 10)
        {
            if (time.time() > .25)
            {
                time.reset();
                stage++;
            }
        }


        if (stage == 11) {
            if (!colorUtils.aboveRedLine() && time.time() < .7) {
                driveTrain.driveStraight(.8);
            } else {
                driveTrain.stopRobot();
                stage++;
            }
        }*/

        telemetry.addData("Left Front Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());/*
        telemetry.addData("Color: ", String.valueOf(colorUtils.lineColorSensor.red()));
        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));*/
        // telemetry.addData("Fly Wheel Power: ", String.valueOf())

    }
}

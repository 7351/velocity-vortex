package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "EncoderCapBallRed", group = "Testing")
public class EncoderCapBallRed extends OpMode {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    ColorUtils colorUtils;
    GyroSensor gyro;
    Intake intake;
    FlyWheel flyWheel;
    EncoderDrive drive;



    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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

        if (stage == 0) {//calibrates to 0
            if (!gyro.isCalibrating()) {
                stage++;
                time.reset();
            }
            telemetry.addData("Calibrating", String.valueOf(gyro.isCalibrating()));
        }

        if (stage == 1) { //drives forward 0.25 seconds
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 500, 0.5);
                drive.run();
            }
            if (drive.isCompleted()) {
                drive.completed();
                time.reset();
                stage++;
            }

        }

        if (stage == 2) {
            if (time.time() > 0.15) {
                stage++;
                time.reset();
            }
        }

        if (stage == 3){
            if(gyro.getHeading() > 334 || gyro.getHeading() < 10)
            {
                driveTrain.powerLeft(-.15);
                driveTrain.powerRight(.15);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 4) {
            if (time.time() > 0.15) {
                stage++;
                time.reset();
            }
        }

        if (stage == 5)
        {
            if (time.time() < 1.05)
            {
                driveTrain.driveStraight();
            }
            else
            {
                driveTrain.stopRobot();
                stage++;
                time.reset();
                flyWheel.FlyWheelMotor.setPower(.3);
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
            if(time.time() < 2.5)
            {
                intake.setIntakePower(Intake.IntakeSpec.B, 0.7);
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
                intake.setIntakePower(Intake.IntakeSpec.A, 1);
            else
            {
                time.reset();
                stage++;
            }
        }

        if (stage == 9)
        {
            if(time.time() > 2)
            {
                intake.stopIntake(Intake.IntakeSpec.A);
                intake.stopIntake(Intake.IntakeSpec.B);
                flyWheel.FlyWheelMotor.setPower(0);
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
            if (!colorUtils.aboveRedLine() && time.time() < 1) {
                driveTrain.driveStraight();
            } else {
                driveTrain.stopRobot();
                stage++;
            }
        }

        telemetry.addData("Left Front Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());
        /*telemetry.addData("Color: ", String.valueOf(colorUtils.lineColorSensor.red()));
        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));*/
        // telemetry.addData("Fly Wheel Power: ", String.valueOf())

    }
}
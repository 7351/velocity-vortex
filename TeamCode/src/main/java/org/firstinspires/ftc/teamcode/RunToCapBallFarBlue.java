package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "CapBallBlue", group = "Testing")
public class RunToCapBallFarBlue extends OpMode {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();

    DriveTrain driveTrain;

    GyroUtils gyroUtils;

    ColorUtils colorUtils;

    GyroSensor gyro;

    DcMotor FlyWheelMotor;

    DcMotor IntakeB;

    DcMotor IntakeA;



    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        FlyWheelMotor = hardwareMap.dcMotor.get("FlyWheelMotor");
        IntakeB = hardwareMap.dcMotor.get("IntakeB");
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

        if (stage == 0) {//calibrates to 0
            if (!gyro.isCalibrating()) {
                stage++;
                time.reset();
            }
            telemetry.addData("Calibrating", String.valueOf(gyro.isCalibrating()));
        }

        if (stage == 1) { //drives forward 0.25 seconds
            if (time.time() <= 0.40) {
                driveTrain.driveStraight();
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 2) {
            if (time.time() > 0.15) {
                stage++;
                time.reset();
            }
        }

        if (stage == 3){
            if(gyro.getHeading() > 350 || gyro.getHeading() < 31)
            {
                driveTrain.powerLeft(.15);
                driveTrain.powerRight(-.15);
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
                FlyWheelMotor.setPower(.47);
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
                IntakeB.setPower(.7);
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
            if(time.time() > 2)
            {
                IntakeB.setPower(0);
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
            if (!colorUtils.aboveBlueLine() && time.time() < 1) {
                driveTrain.driveStraight();
            } else {
                driveTrain.stopRobot();
                stage++;
            }
        }

        telemetry.addData("Above Blue Line?: ", String.valueOf(colorUtils.aboveBlueLine()));
        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));
        // telemetry.addData("Fly Wheel Power: ", String.valueOf())

    }
}
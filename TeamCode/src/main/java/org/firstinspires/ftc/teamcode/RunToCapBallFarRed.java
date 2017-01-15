package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake.IntakeSpec;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "CapBallRed", group = "Testing")
@Disabled
public class RunToCapBallFarRed extends OpMode {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();

    DriveTrain driveTrain;

    GyroUtils gyroUtils;

    ColorUtils colorUtils;

    GyroSensor gyro;

    DcMotor FlyWheelMotor;

    DcMotor IntakeA;

    Intake intake;



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

        if (stage == 0) {//calibrates to 0
            if (!gyro.isCalibrating()) {
                stage++;
                time.reset();
            }
            telemetry.addData("Calibrating", String.valueOf(gyro.isCalibrating()));
        }

        if (stage == 1) { //drives forward 0.25 seconds
        if (time.time() <= 0.325) {
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
        if(gyro.getHeading() > 335.5 || gyro.getHeading() < 10)
        {
        driveTrain.powerLeft(-.3);
        driveTrain.powerRight(.3);
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
        }

        telemetry.addData("Color: ", String.valueOf(colorUtils.lineColorSensor.red()));
        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));
        // telemetry.addData("Fly Wheel Power: ", String.valueOf())

        }
        }
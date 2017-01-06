package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "EncoderBeaconBlue", group = "Encoder")
public class EncoderBeaconBlue extends OpMode {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    ColorUtils colorUtils;
    GyroSensor gyro;
    Intake intake;
    FlyWheel flyWheel;
    EncoderDrive drive;
    EncoderTurn turn;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);

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
        if (stage == 0) { //calibrates to 0
            if (!gyro.isCalibrating()) {
                nextStage();
            }
            telemetry.addData("Calibrating", String.valueOf(gyro.isCalibrating()));
        } if (stage == 1) { //drives forward 0.25 seconds
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 50, 0.75);
                drive.run();
            }
            if (drive.isCompleted()) {
                drive.completed();
                stage++;
            }

        }
        if (stage == 2) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                drive = null;
                nextStage();
            }
        }

        /*if (stage == 2) {
            flyWheel.currentlyRunning = true;
            flyWheel.currentPower = flyWheel.defaultStartingPower; // Start the fly wheel with default launch power
            nextStage();
        } if (stage == 3) {
            if (time.time() > 1.5) nextStage(); // Wait 1.5 seconds
        } if (stage == 4) {
            if (time.time() < 3) {
                intake.setIntakePower(Intake.IntakeSpec.B, -1);
            } else {
                nextStage();
            }
        } if (stage == 5) {
            if (time.time() < 4) {
                intake.setIntakePower(Intake.IntakeSpec.A, 1);
            } else {
                nextStage();
            }
        } if (stage == 6) {
            for (Intake.IntakeSpec spec: Intake.IntakeSpec.values()) {
                intake.stopIntake(spec);
            }
            flyWheel.currentlyRunning = false;
            nextStage();
        } if (stage == 7) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                nextStage();
            }
        } if (stage == 8) {

        }*/

        telemetry.addData("F", driveTrain.LeftFrontMotor.getCurrentPosition() + ":" + driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("B", driveTrain.LeftBackMotor.getCurrentPosition() + ":" + driveTrain.RightBackMotor.getCurrentPosition());
        telemetry.addData("Right Power: ", driveTrain.RightFrontMotor.getPower());
        telemetry.addData("Left Power: ", driveTrain.LeftFrontMotor.getPower());

        flyWheel.powerMotor();
    }

    public void nextStage() {
        stage++;
        time.reset();
    }
}
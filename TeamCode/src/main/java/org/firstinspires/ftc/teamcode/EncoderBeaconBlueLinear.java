package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

@Autonomous(name = "EncoderBeaconBlueLinear", group = "Encoder")
public class EncoderBeaconBlueLinear extends LinearOpMode {

    private final static int WAITTIME = 250; // .25 seconds (this is in ms)

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    ColorUtils colorUtils;
    GyroSensor gyro;
    Intake intake;
    FlyWheel flyWheel;

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);

        gyro = gyroUtils.gyro;
        gyro.calibrate();
        waitForStart();
        colorUtils.lineColorSensor.enableLed(true);
        while(opModeIsActive() && gyro.isCalibrating()) {

        }
        int target = 700; // This is within 1 inch of the tile border
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.RightFrontMotor.setTargetPosition(target);
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrain.powerLeft(0.75);
        driveTrain.powerRight(0.75);
        while (opModeIsActive()) {
            if (-driveTrain.RightFrontMotor.getCurrentPosition() > target) break;
            telemetry.addData("F", driveTrain.LeftFrontMotor.getCurrentPosition() + ":" + driveTrain.RightFrontMotor.getCurrentPosition());
            telemetry.addData("B", driveTrain.LeftBackMotor.getCurrentPosition() + ":" + driveTrain.RightBackMotor.getCurrentPosition());
            telemetry.addData("Right Power: ", driveTrain.RightFrontMotor.getPower());
            telemetry.addData("Left Power: ", driveTrain.LeftFrontMotor.getPower());
            telemetry.update();
        }
        driveTrain.stopRobot();
        sleep(WAITTIME);
        flyWheel.currentlyRunning = true; // Startup the flywheel
        flyWheel.currentPower = flyWheel.defaultStartingPower;
        flyWheel.powerMotor(); // Update the value
        sleep(2000); // Wait 2 seconds
        intake.setIntakePower(Intake.IntakeSpec.B, -1); // Clear top ball
        sleep(3000); // Then 3
        intake.setIntakePower(Intake.IntakeSpec.A, 1); // Clear bottom ball
        sleep(4000); // Then 4
        intake.stopIntake(Intake.IntakeSpec.A);
        intake.stopIntake(Intake.IntakeSpec.B);
        flyWheel.currentlyRunning = false;
        flyWheel.powerMotor(); // Update everything shutting off
        driveTrain.LeftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(WAITTIME + 500); // Everything works up till here
        target = 700;
        driveTrain.LeftBackMotor.setTargetPosition(target);
        driveTrain.LeftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrain.powerLeft(0.3);
        driveTrain.powerRight(-0.3);
        while(opModeIsActive()) { //TODO: Fix this janky turn. It works barely but the encoder counts need to be correctly found
            //FIXME
            if (driveTrain.LeftBackMotor.getCurrentPosition() < -target) break;
            telemetry.addData("F", driveTrain.LeftFrontMotor.getCurrentPosition() + ":" + driveTrain.RightFrontMotor.getCurrentPosition());
            telemetry.addData("B", driveTrain.LeftBackMotor.getCurrentPosition() + ":" + driveTrain.RightBackMotor.getCurrentPosition());
            telemetry.addData("Right Power: ", driveTrain.RightFrontMotor.getPower());
            telemetry.addData("Left Power: ", driveTrain.LeftFrontMotor.getPower());
            telemetry.update();
        }
        driveTrain.stopRobot();
        sleep(WAITTIME);

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
        } */
}
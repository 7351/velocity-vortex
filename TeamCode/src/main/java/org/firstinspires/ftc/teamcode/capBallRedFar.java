package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.BeaconUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.NewEncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.NewEncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.StateMachineOpMode;
/**
 * Created by Dynamic Signals on 10/16/2016.
 */

@Autonomous(name = "capBallRedFar", group = "AWorking")
public class capBallRedFar extends StateMachineOpMode {
    public String target = "Cap Ball"; // or Corner
    // Completed on 3/26/17 12:45PM
    DriveTrain driveTrain;
    ColorUtils colorUtils;
    GyroUtils gyroUtils;
    Intake intake;
    FlyWheel flyWheel;
    EncoderDrive drive;
    EncoderTurn turn;
    private String alliance = "Red";
    private int shoot = 2;
    private int moveType = 0;
    //0 is for just encoders
    //1 is for encoders and gyro
    //2 is for encoders and navX

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        new Lift(hardwareMap);
        new BeaconUtils(hardwareMap, colorUtils);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        gyroUtils.gyro.calibrate();
        intake.openStopper();

    }

    @Override
    public void loop() {

        if (stage == 0) {
            next(); // Save this for where the gyro should go
        }

        if (stage == 1) {
            if(moveType == 0)
            {
                if (drive == null) {
                    drive = new EncoderDrive(driveTrain, 390, .75);//410, .75);
                    drive.run();
                }
                if (drive.isCompleted()) {
                    driveTrain.stopRobot();
                    next();
                }
            }
        }

        if (stage == 2) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }
        if (stage == 3) {
            if(moveType == 0){
                if (turn == null) {
                    turn = new EncoderTurn(driveTrain, 80, GyroUtils.Direction.COUNTERCLOCKWISE);
                    turn.run();
                }
                if (((gyroUtils.gyro.getHeading() < 350) && (gyroUtils.gyro.getHeading() > 45)) || turn.isCompleted()) {
                    driveTrain.stopRobot();
                    stage++;
                    time.reset();
                }
            }
        }
        if (stage == 4) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }

        if (stage == 5) {
            if (moveType == 0) {
                if (drive == null) {
                    drive = new EncoderDrive(driveTrain, 1250, 0.5);
                    drive.run();
                    if (shoot > 0) {
                        flyWheel.currentPower = flyWheel.defaultStartingPower;
                        flyWheel.currentlyRunning = true;
                    }
                }
                if (drive.isCompleted()) {
                    driveTrain.stopRobot();
                    next();
                }
            }
        }

        flyWheel.powerMotor(); // Update flywheel values

        if (stage == 6) {
            if (time.time() > .25) {
                if (shoot == 1) {
                    intake.setIntakePower(Intake.IntakeSpec.A, 1);
                }
                if (shoot == 2) {
                    intake.setIntakePower(Intake.IntakeSpec.A, 1);
                }
                if (time.time() > 3.5 || shoot <= 0) {
                    next();
                    intake.stopIntake(Intake.IntakeSpec.A);
                    intake.setIntakePower(Intake.IntakeSpec.A, -1);
                    flyWheel.currentlyRunning = false;
                }
            }
        }
        if (target.equals("Cap Ball")) {
            if (moveType == 0){
                if (stage == 7) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 2300, 0.5);
                        drive.run();
                    }
                    if (drive.isCompleted() || colorUtils.aboveRedLine()) {
                        drive.completed();
                        next();
                    }
                }

                if (stage == 8) {
                    if (time.time() > 2) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        next();
                    }
                }
            }

        }

        if (target.equals("Shoot only")) {
            if (moveType == 0) {
                if (stage == 7) {
                    if (time.time() > 2) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        next();
                    }
                }
            }
        }

        if (target.equals("Corner")) {
            if (moveType ==0){
                if (stage == 7) {
                    if (time.time() > 10) {
                        next();
                    }
                }

                if (stage == 8) {
                    if(moveType == 0){
                        if (turn == null) {
                            turn = new EncoderTurn(driveTrain, 70, GyroUtils.Direction.COUNTERCLOCKWISE);
                            turn.run();
                        }
                        if (((gyroUtils.gyro.getHeading() < 350) && (gyroUtils.gyro.getHeading() > 90)) || turn.isCompleted()) {
                            driveTrain.stopRobot();
                            stage++;
                            time.reset();
                        }
                    }
                    //NewEncoderTurn.createTurn(this, 70, GyroUtils.Direction.COUNTERCLOCKWISE);
                }

                if (stage == 9) {
                    NewEncoderDrive.createDrive(this, 2900);
                }

                if (stage == 10) {
                    if(moveType == 0){
                        if (turn == null) {
                            turn = new EncoderTurn(driveTrain, 47, GyroUtils.Direction.COUNTERCLOCKWISE);
                            turn.run();
                        }
                        if (((gyroUtils.gyro.getHeading() < 350) && (gyroUtils.gyro.getHeading() > 135)) || turn.isCompleted()) {
                            driveTrain.stopRobot();
                            stage++;
                            time.reset();
                        }
                    }
                    //NewEncoderTurn.createTurn(this, 47, GyroUtils.Direction.COUNTERCLOCKWISE);
                }

                if (stage == 11) {
                    NewEncoderDrive.createDrive(this, 2900);
                }

                if (stage == 12) {
                    if (time.time() > 2) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        next();
                    }
                }
            }
        }


        telemetry.addData("Left Front Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());
        telemetry.addData("FlyWheel", String.valueOf(flyWheel.FlyWheelMotor.getPower()));
        telemetry.addData("Stage", String.valueOf(stage));
    }

    @Override
    public void next() {
        super.next();
        turn = null;
        drive = null;
    }
}
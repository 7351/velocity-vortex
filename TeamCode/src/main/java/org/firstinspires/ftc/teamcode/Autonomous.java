package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TBDName;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Dynamic Signals on 11/10/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group = "Autonomous")
public class Autonomous extends OpMode {

    TBDName tbdName; // Robot object

    private int stage = -1; // First stage is -1 (wait time)
    private ElapsedTime time = new ElapsedTime(); // regular match time variable minus gyro cal. and wait
    private ElapsedTime delayTime = new ElapsedTime(); // delay time elapsed time

    // Selector variables
    private double delay;
    private String alliance;
    private int shoot;
    private String target;

    private DriveTrain driveTrain;
    private GyroUtils gyroUtils;
    private ColorUtils colorUtils;
    private GyroSensor gyro;
    private Intake intake;
    private FlyWheel flyWheel;

    //TODO: Implement shoot variable for each shooting method

    @Override
    public void init() {

        tbdName = new TBDName(hardwareMap, telemetry, false);

        driveTrain = tbdName.driveTrain;
        gyroUtils = tbdName.gyroUtils;
        colorUtils = tbdName.colorUtils;
        gyro = gyroUtils.gyro;
        intake = tbdName.intake;
        flyWheel = tbdName.flyWheel;

        delay = tbdName.das.getNumberDouble("delay", 0); // 0 default wait time
        alliance = tbdName.das.getRadio("alliance", "ns"); // Not selected - ns
        shoot = tbdName.das.getNumberInt("shoot", 2); // We want to shoot twice by default
        target = tbdName.das.getRadio("target", "ns"); // Not selected - ns
    }

    @Override
    public void init_loop() {

        Iterator it = tbdName.das.getSelectorChoices().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            telemetry.addData((String) pair.getKey(), pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

    }

    @Override
    public void start() {

        tbdName.start();
        time.reset();
        delayTime.reset();

    }

    @Override
    public void loop() {

        if (stage == -1) {
            if (delayTime.time() > delay) {
                stage++;
            }
        }
        if (stage == 0) {
            if (!tbdName.gyroUtils.gyro.isCalibrating()) {
                stage++;
                time.reset();
            }
            telemetry.addData("Calibrating", String.valueOf(tbdName.gyroUtils.gyro.isCalibrating()));
        }
        if (alliance.equals("Red")) {
            if (target.equals("Beacon")) {
                if (stage == 1) { //drives forward 33 inches in seconds
                    if (time.time() <= 0.64) {
                        driveTrain.driveStraight();
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 2) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        if (shoot == 0) {
                            stage = 10;
                        } else {
                            stage++;
                            time.reset();
                        }

                    }
                }

                if (stage == 3) {
                    double flyWheelLaunchPower = 0.25;
                    flyWheel.FlyWheelMotor.setPower(flyWheelLaunchPower);
                    stage++;
                }

                if (stage == 4) {
                    if (time.time() > 3) {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 5) {
                    if (time.time() < 2) {
                        intake.setIntakePower(Intake.IntakeSpec.B, 1);
                    } else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 6) {
                    if (shoot == 2) {
                        if (time.time() > 1.2) {
                            stage++;
                            time.reset();
                        }
                    } else if (shoot == 1) {
                        stage = 8;
                        time.reset();
                    }

                }

                if (stage == 7) {
                    if (time.time() < .35)
                        intake.setIntakePower(Intake.IntakeSpec.A, 1);
                    else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 8) {
                    if (time.time() > 2) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        intake.stopIntake(Intake.IntakeSpec.B);
                        flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 9) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 10) { // Turn to 90
                    int difference = 13;
                    int angle = 270;
                    if (gyro.getHeading() > (angle + difference) || gyro.getHeading() < 10) {
                        driveTrain.powerLeft(-.15);
                        driveTrain.powerRight(.15);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 11) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }


                if (stage == 12) { //drives forward 33 inches in seconds // OUTDATED LENGTH
                    if (time.time() <= 0.8) {
                        driveTrain.driveStraight(1);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 13) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 14) { // Turn to 145
                    int difference = 9;
                    int angle = 317;
                    if (!gyroUtils.isGyroInTolerance(angle, difference)) {
                        driveTrain.powerLeft(.15);
                        driveTrain.powerRight(-.15);
                    } else {
                        RobotLog.d("13." + "Statement true at gyro degree " + gyro.getHeading());
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 15) {
                    if (!colorUtils.aboveWhiteLine()) {
                        driveTrain.driveStraight(.3);
                    } else {
                        driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }
                if (stage == 16) {
                    if (time.time() < AutonomousUtils.WAITTIME) {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 17) {
                    if (time.time() < .3) {
                        driveTrain.driveStraight(-.3);
                    } else {
                        driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 18) { // Turn to 90
                    int difference = 13;
                    int angle = 270;
                    if (gyro.getHeading() > (angle + difference) || gyro.getHeading() < 10) {
                        driveTrain.powerLeft(-.15);
                        driveTrain.powerRight(.15);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 19) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 20) {
                    if (time.time() < 1) {
                        driveTrain.driveStraight(.3);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 21) {
                    if (time.time() > .5) {
                        stage++;
                        time.reset();
                    }
                }

                // Initialize the beacon subroutine from BeaconSlamTest

                if (stage == 22) {
                    if (time.time() < 1) {
                        driveTrain.driveStraight(0.3);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 23) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 24) {
                    if (time.time() < 0.15) {
                        driveTrain.driveStraight(-0.5);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 25) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 26) {
                    switch (colorUtils.beaconColor()) {
                        case RED:
                            switch (alliance) {
                                case "Blue":
                                    if (time.time() > 5.1) {
                                        time.reset();
                                        stage = 21;
                                    }
                                    break;
                                case "Red":
                                    time.reset();
                                    stage++;
                                    break;
                            }
                            break;
                        case BLUE:
                            switch (alliance) {
                                case "Blue":
                                    time.reset();
                                    stage++;
                                    break;
                                case "Red":
                                    if (time.time() > 5.1) {
                                        time.reset();
                                        stage = 21;
                                    }
                                    break;
                            }
                            break;
                    }
                }

                if (stage == 27) {
                    if (time.time() < 0.5) {
                        driveTrain.driveStraight(-0.5);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 28) {
                    int difference = 13;
                    int angle = 0;
                    if (gyro.getHeading() > 210) {
                        driveTrain.powerLeft(-.15);
                        driveTrain.powerRight(.15);
                    } else {
                        driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 29) {
                    if (time.time() < 1.25) {
                        driveTrain.powerRight(1);
                        driveTrain.powerLeft(1);
                    } else {
                        driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }
            }
            if (target.equals("Cap ball")) {
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

                if (stage == 3) {
                    if (gyro.getHeading() > 334 || gyro.getHeading() < 10) {
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

                if (stage == 5) {
                    if (time.time() < 1.05) {
                        driveTrain.driveStraight();
                    } else {
                        if (shoot == 0) {
                            stage = 10;
                            time.reset();
                        } else {
                            driveTrain.stopRobot();
                            stage++;
                            time.reset();
                            flyWheel.FlyWheelMotor.setPower(.3);
                        }
                    }
                }

                if (stage == 6) {
                    if (time.time() > 2) {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 7) {
                    if (time.time() < 2.5) {
                        intake.setIntakePower(Intake.IntakeSpec.B, .7);
                    } else {
                        time.reset();
                        if (shoot == 1) {
                            time.reset();
                            stage = 9;
                        }
                        if (shoot == 2) {
                            stage++;
                            time.reset();
                        }

                    }
                }

                if (stage == 8) {
                    if (time.time() < .35) {
                        intake.setIntakePower(Intake.IntakeSpec.A, 1);
                    } else {
                        time.reset();
                        stage++;
                    }

                }

                if (stage == 9) {
                    if (time.time() > 2) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        intake.stopIntake(Intake.IntakeSpec.B);
                        flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 10) {
                    if (time.time() > .25) {
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
            }
        }
        if (alliance.equals("Blue")) {
            if (target.equals("Beacon")) {

                if (stage == 1) { //drives forward 33 inches in seconds
                    if (time.time() <= 0.64) {
                        driveTrain.driveStraight();
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 2) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        if (shoot == 0) {
                            stage = 10;
                        } else {
                            stage++;
                            time.reset();
                        }

                    }
                }

                if (stage == 3) {
                    double flyWheelLaunchPower = 0.25;
                    flyWheel.FlyWheelMotor.setPower(flyWheelLaunchPower);
                    stage++;
                }

                if (stage == 4) {
                    if (time.time() > 3) {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 5) {
                    if (time.time() < 2) {
                        intake.setIntakePower(Intake.IntakeSpec.B, 1);
                    } else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 6) {
                    if (shoot == 2) {
                        if (time.time() > 1.2) {
                            time.reset();
                            stage++;
                        }
                    } else if (shoot == 1) {
                        stage = 8;
                        time.reset();
                    }

                }

                if (stage == 7) {
                    if (time.time() < .35)
                        intake.setIntakePower(Intake.IntakeSpec.A, 1);
                    else {
                        time.reset();
                        stage++;
                    }

                }

                if (stage == 8) {
                    if (time.time() > 2) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        intake.stopIntake(Intake.IntakeSpec.B);
                        flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 9) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 10) { // Turn to 90
                    int difference = 13;
                    int angle = 80;
                    if (gyro.getHeading() < (angle - difference) || gyro.getHeading() > 350) {
                        driveTrain.powerLeft(.15);
                        driveTrain.powerRight(-.15);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 11) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }


                if (stage == 12) { //drives forward 33 inches in seconds // OUTDATED LENGTH
                    if (time.time() <= 0.85) {
                        driveTrain.driveStraight(1);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 13) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 14) { // Turn to 145
                    int difference = 9;
                    int angle = 30;
                    if (!gyroUtils.isGyroInTolerance(angle, difference)) {
                        driveTrain.powerLeft(-.15);
                        driveTrain.powerRight(.15);
                    } else {
                        RobotLog.d("13." + "Statement true at gyro degree " + gyro.getHeading());
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 15) {
                    if (!colorUtils.aboveWhiteLine()) {
                        driveTrain.driveStraight(.3);
                    } else {
                        driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }
                if (stage == 16) {
                    if (time.time() < AutonomousUtils.WAITTIME) {
                        time.reset();
                        stage = 18;
                    }
                }

        /*if (stage == 17) {
            if (time.time() < .3) {
                driveTrain.driveStraight(-.3);
            } else {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }*/

                if (stage == 18) { // Turn to 90
                    int difference = 13;
                    int angle = 80;
                    if (gyro.getHeading() < (angle - difference) || gyro.getHeading() > 350) {
                        driveTrain.powerLeft(.15);
                        driveTrain.powerRight(-.15);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 19) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 20) {
                    if (time.time() < 1) {
                        driveTrain.driveStraight(.3);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 21) {
                    if (time.time() > .5) {
                        stage++;
                        time.reset();
                    }
                }

                // Initialize the beacon subroutine from BeaconSlamTest

                if (stage == 22) {
                    if (time.time() < 1) {
                        driveTrain.driveStraight(0.3);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 23) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 24) {
                    if (time.time() < 0.15) {
                        driveTrain.driveStraight(-0.5);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 25) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 26) {
                    switch (colorUtils.beaconColor()) {
                        case RED:
                            switch (alliance) {
                                case "Blue":
                                    if (time.time() > 5.1) {
                                        time.reset();
                                        stage = 21;
                                    }
                                    break;
                                case "Red":
                                    time.reset();
                                    stage++;
                                    break;
                            }
                            break;
                        case BLUE:
                            switch (alliance) {
                                case "Blue":
                                    time.reset();
                                    stage++;
                                    break;
                                case "Red":
                                    if (time.time() > 5.1) {
                                        time.reset();
                                        stage = 21;
                                    }
                                    break;
                            }
                            break;
                    }
                }

                if (stage == 27) {
                    if (time.time() < 0.5) {
                        driveTrain.driveStraight(-0.5);
                    } else {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 28) {
                    int difference = 13;
                    int angle = 0;
                    if (gyro.getHeading() < 150) {
                        driveTrain.powerLeft(.15);
                        driveTrain.powerRight(-.15);
                    } else {
                        driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 29) {
                    if (time.time() < 1.25) {
                        driveTrain.powerRight(1);
                        driveTrain.powerLeft(1);
                    } else {
                        driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }
            }
            if (target.equals("Cap ball")) {
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

                if (stage == 3) {
                    if (gyro.getHeading() > 350 || gyro.getHeading() < 29) {
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

                if (stage == 5) {
                    if (time.time() < .90) {
                        driveTrain.driveStraight();
                    } else {
                        if (shoot == 0) {
                            stage = 10;
                            time.reset();
                        } else {
                            driveTrain.stopRobot();
                            stage++;
                            time.reset();
                            flyWheel.FlyWheelMotor.setPower(.3);
                        }
                    }
                }

                if (stage == 6) {
                    if (time.time() > 2) {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 7) {
                    if (time.time() < 2.5) {
                        intake.setIntakePower(Intake.IntakeSpec.B, .7);
                    } else {
                        time.reset();
                        if (shoot == 1) {
                            time.reset();
                            stage = 9;
                        }
                        if (shoot == 2) {
                            stage++;
                            time.reset();
                        }

                    }
                }

                if (stage == 8) {
                    if (time.time() < .35)
                        intake.setIntakePower(Intake.IntakeSpec.A, 1);
                    else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 9) {
                    if (time.time() > 2) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        intake.stopIntake(Intake.IntakeSpec.B);
                        flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 10) {
                    if (time.time() > .25) {
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
            }
        }


        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(tbdName.gyroUtils.gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));
        telemetry.addData("Beacon Color: ", String.valueOf(tbdName.colorUtils.beaconColor()));

    }
}

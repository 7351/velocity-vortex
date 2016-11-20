package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TBDName;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Dynamic Signals on 11/10/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group = "Autonomous")
public class Autonomous extends OpMode {

    TBDName tbdName;

    private int stage = -1;
    private ElapsedTime time = new ElapsedTime();
    private ElapsedTime delayTime = new ElapsedTime();

    private double flyWheelLaunchPower = 0.4;

    private double delay;
    private String alliance;
    private int shoot;
    private String target;

    @Override
    public void init() {

        tbdName = new TBDName(hardwareMap, telemetry, false);

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
                        tbdName.driveTrain.driveStraight();
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 2) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 3) {
                    tbdName.flyWheel.FlyWheelMotor.setPower(flyWheelLaunchPower);
                    stage++;
                }

                if (stage == 4) {
                    if (time.time() > 2) {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 5) {
                    double DurationTime = 0;
                    if (shoot == 0) {
                        DurationTime = 0;
                    } else {
                        DurationTime = 2;
                    }
                    if (time.time() < DurationTime) { // 2 Seconds for 2 balls
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.B, 1);
                    } else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 6) {
                    double DurationTime = 0;
                    if (shoot == 0 || shoot == 1) {
                        DurationTime = 0;
                    }
                    if (shoot == 2) {
                        DurationTime = 0.35;
                    }
                    if (time.time() < DurationTime) // 0.35 for 2 balls
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.A, 1);
                    else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 7) {
                    if (time.time() > 2) { // Let it run for 2 seconds
                        tbdName.intake.stopIntake(Intake.IntakeSpec.A);
                        tbdName.intake.stopIntake(Intake.IntakeSpec.B);
                        tbdName.flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 8) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 9) { // Turn to 90
                    int difference = 11;
                    int angle = 90;
                    if (tbdName.gyroUtils.gyro.getHeading() < (angle - difference) || tbdName.gyroUtils.gyro.getHeading() > 350) {
                        tbdName.driveTrain.powerLeft(.15);
                        tbdName.driveTrain.powerRight(-.15);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 10) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }


                if (stage == 11) { //drives forward 33 inches in seconds // OUTDATED LENGTH
                    if (time.time() <= 0.8) {
                        tbdName.driveTrain.driveStraight(-1);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 12) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 13) { // Turn to 145
                    int difference = 9;
                    int angle = 145;
                    if (!tbdName.gyroUtils.isGyroInTolerance(angle, difference)) {
                        tbdName.driveTrain.powerLeft(.15);
                        tbdName.driveTrain.powerRight(-.15);
                    } else {
                        RobotLog.d("13." + "Statement true at gyro degree " + tbdName.gyroUtils.gyro.getHeading());
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 14) {
                    if (!tbdName.colorUtils.aboveWhiteLine()) {
                        tbdName.driveTrain.driveStraight(-.3);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }
                if (stage == 15) {
                    if (time.time() < AutonomousUtils.WAITTIME) {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 16) {
                    if (time.time() < .15) {
                        tbdName.driveTrain.driveStraight(.3);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 17) {
                    int difference = 9;
                    int angle = 90;
                    if (!tbdName.gyroUtils.isGyroInTolerance(angle, difference)) {
                        tbdName.driveTrain.powerLeft(-.15);
                        tbdName.driveTrain.powerRight(.15);
                    } else {
                        RobotLog.d("15." + "Statement true at gyro degree " + tbdName.gyroUtils.gyro.getHeading());
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 18) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 19) {
                    if (time.time() < 0.75) {
                        tbdName.driveTrain.driveStraight(-.3);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 20) {
                    if (time.time() > .5) {
                        stage++;
                        time.reset();
                    }
                }

                // Initialize the beacon subroutine from BeaconSlamTest

                if (stage == 21) {
                    if (time.time() < 0.75) {
                        tbdName.driveTrain.driveStraight(-0.5);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 22) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 23) {
                    if (time.time() < 0.1) {
                        tbdName.driveTrain.driveStraight(0.5);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 24) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 25) {
                    switch (tbdName.colorUtils.beaconColor()) {
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
            }
            if (target.equals("Cap ball")) {
                if (stage == 1) { //drives forward 0.25 seconds
                    if (time.time() <= 0.3) {
                        tbdName.driveTrain.driveStraight();
                    } else {
                        tbdName.driveTrain.stopRobot();
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
                    if (tbdName.gyroUtils.gyro.getHeading() > 332 || tbdName.gyroUtils.gyro.getHeading() < 10) {
                        tbdName.driveTrain.powerLeft(-.15);
                        tbdName.driveTrain.powerRight(.15);
                    } else {
                        tbdName.driveTrain.stopRobot();
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
                        tbdName.driveTrain.driveStraight();
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                        tbdName.flyWheel.FlyWheelMotor.setPower(flyWheelLaunchPower);
                    }
                }

                if (stage == 6) {
                    if (time.time() > 2) {
                        time.reset();
                        stage++;
                    }
                }
                if (stage == 7) {
                    double DurationTime = 0;
                    if (shoot == 0) {
                        DurationTime = 0;
                    } else {
                        DurationTime = 2.5;
                    }
                    if (time.time() < DurationTime) { // 2 Seconds for 2 balls
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.B, .7);
                    } else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 8) {
                    double DurationTime = 0;
                    if (shoot == 0 || shoot == 1) {
                        DurationTime = 0;
                    }
                    if (shoot == 2) {
                        DurationTime = 0.35;
                    }
                    if (time.time() < DurationTime) // 0.35 for 2 balls
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.A, 1);
                    else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 9) {
                    if (time.time() > 2) {
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.B, 0);
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.A, 0);
                        tbdName.flyWheel.FlyWheelMotor.setPower(0);
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
                    if (!tbdName.colorUtils.aboveRedLine() && time.time() < 1) {
                        tbdName.driveTrain.driveStraight();
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                    }
                }
            }
        }
        if (alliance.equals("Blue")) {
            if (target.equals("Beacon")) {
                if (stage == 1) { //drives forward 33 inches in seconds
                    telemetry.addData("Config", "BlueBeacon");
                    if (time.time() <= 0.64) {
                        tbdName.driveTrain.driveStraight();
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 2) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 3) {

                    tbdName.flyWheel.FlyWheelMotor.setPower(flyWheelLaunchPower);
                    stage++;
                }

                if (stage == 4) {
                    if (time.time() > 2) {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 5) {
                    double DurationTime = 0;
                    if (shoot == 0) {
                        DurationTime = 0;
                    } else {
                        DurationTime = 2;
                    }
                    if (time.time() < DurationTime) { // 2 Seconds for 2 balls
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.B, 1);
                    } else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 6) {
                    double DurationTime = 0;
                    if (shoot == 0 || shoot == 1) {
                        DurationTime = 0;
                    }
                    if (shoot == 2) {
                        DurationTime = 0.35;
                    }
                    if (time.time() < DurationTime) // 0.35 for 2 balls
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.A, 1);
                    else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 7) {
                    if (time.time() > 2) {
                        tbdName.intake.stopIntake(Intake.IntakeSpec.A);
                        tbdName.intake.stopIntake(Intake.IntakeSpec.B);
                        tbdName.flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 8) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 9) { // Turn to 270
                    int difference = 11;
                    int angle = 270;
                    if (tbdName.gyroUtils.gyro.getHeading() > (angle + difference) || tbdName.gyroUtils.gyro.getHeading() < 10) {
                        tbdName.driveTrain.powerLeft(-.15);
                        tbdName.driveTrain.powerRight(.15);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 10) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }


                if (stage == 11) { //drives forward 33 inches in seconds // OUTDATED LENGTH
                    if (time.time() <= 1.2) {
                        tbdName.driveTrain.driveStraight(-1);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 12) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 13) { // Turn to 225
                    int difference = 9;
                    int angle = 225;
                    if (!tbdName.gyroUtils.isGyroInTolerance(angle, difference)) {
                        tbdName.driveTrain.powerLeft(-.15);
                        tbdName.driveTrain.powerRight(.15);
                    } else {
                        RobotLog.d("13." + "Statement true at gyro degree " + tbdName.gyroUtils.gyro.getHeading());
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 14) {
                    if (!tbdName.colorUtils.aboveWhiteLine()) {
                        tbdName.driveTrain.driveStraight(-.3);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        //autonomousUtils.waitTime(.5);
                        time.reset();
                        stage++;
                    }
                }
                if (stage == 15) {
                    if (time.time() < AutonomousUtils.WAITTIME) {
                        time.reset();
                        stage++;
                    }
                }
                if (stage == 16) {
                    if (time.time() < .15) {
                        tbdName.driveTrain.driveStraight(.3);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 17) {
                    int difference = 9;
                    int angle = 270;
                    if (!tbdName.gyroUtils.isGyroInTolerance(angle, difference)) {
                        tbdName.driveTrain.powerLeft(.15);
                        tbdName.driveTrain.powerRight(-.15);
                    } else {
                        RobotLog.d("15." + "Statement true at gyro degree " + tbdName.gyroUtils.gyro.getHeading());
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 18) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 19) {
                    if (time.time() < 0.75) {
                        tbdName.driveTrain.driveStraight(-.3);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 20) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                // Initialize the beacon subroutine from BeaconSlamTest

                if (stage == 21) {
                    if (time.time() < 0.75) {
                        tbdName.driveTrain.driveStraight(-0.5);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 22) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 23) {
                    if (time.time() < 0.1) {
                        tbdName.driveTrain.driveStraight(0.5);
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 24) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 25) {
                    switch (tbdName.colorUtils.beaconColor()) {
                        case RED:
                            RobotLog.d("Red color detected");
                            switch (alliance) {
                                case "Blue":
                                    RobotLog.d(alliance + " alliance, Beacon color: " + tbdName.colorUtils.beaconColor());
                                    if (time.time() > 5.1) {
                                        time.reset();
                                        stage = 21;
                                    }
                                    break;
                                case "Red":
                                    RobotLog.d(alliance + " alliance, Beacon color: " + tbdName.colorUtils.beaconColor());
                                    time.reset();
                                    stage++;
                                    break;
                            }
                            break;
                        case BLUE:
                            RobotLog.d("Blue color detected");
                            switch (alliance) {
                                case "Blue":
                                    RobotLog.d(alliance + " alliance, Beacon color: " + tbdName.colorUtils.beaconColor());
                                    time.reset();
                                    stage++;
                                    break;
                                case "Red":
                                    RobotLog.d(alliance + " alliance, Beacon color: " + tbdName.colorUtils.beaconColor());
                                    if (time.time() > 5.1) {
                                        time.reset();
                                        stage++;
                                    }
                                    break;
                            }
                            break;
                    }
                }
            }
            if (target.equals("Cap ball")) {
                if (stage == 1) { //drives forward 0.25 seconds
                    if (time.time() <= 0.35) {
                        tbdName.driveTrain.driveStraight();
                    } else {
                        tbdName.driveTrain.stopRobot();
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
                    if (tbdName.gyroUtils.gyro.getHeading() > 350 || tbdName.gyroUtils.gyro.getHeading() < 27) {
                        tbdName.driveTrain.powerLeft(.15);
                        tbdName.driveTrain.powerRight(-.15);
                    } else {
                        tbdName.driveTrain.stopRobot();
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
                        tbdName.driveTrain.driveStraight();
                    } else {
                        tbdName.driveTrain.stopRobot();
                        stage++;
                        time.reset();
                        tbdName.flyWheel.FlyWheelMotor.setPower(flyWheelLaunchPower);
                    }
                }

                if (stage == 6) {
                    if (time.time() > 2) {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 7) {
                    double DurationTime = 0;
                    if (shoot == 0) {
                        DurationTime = 0;
                    } else {
                        DurationTime = 2.5;
                    }
                    if (time.time() < DurationTime) { // 2 Seconds for 2 balls
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.B, .7);
                    } else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 8) {
                    double DurationTime = 0;
                    if (shoot == 0 || shoot == 1) {
                        DurationTime = 0;
                    }
                    if (shoot == 2) {
                        DurationTime = 0.35;
                    }
                    if (time.time() < DurationTime) // 0.35 for 2 balls
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.A, 1);
                    else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 9) {
                    if (time.time() > 2) {
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.B, 0);
                        tbdName.intake.setIntakePower(Intake.IntakeSpec.A, 0);
                        tbdName.flyWheel.FlyWheelMotor.setPower(0);
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
                    if (!tbdName.colorUtils.aboveBlueLine() && time.time() < 1) {
                        tbdName.driveTrain.driveStraight();
                    } else {
                        tbdName.driveTrain.stopRobot();
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

package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.BeaconUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake;

/**
 * Created by Dynamic Signals on 2/2/2017.
 */

@Autonomous(name = "ShootingTest", group = "Testing")
public class ShootingTest extends OpMode {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    FlyWheel flyWheel;
    Intake intake;
    DriveTrain driveTrain;
    BeaconUtils beaconUtils;
    EncoderDrive drive;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, new ColorUtils(hardwareMap));

    }

    @Override
    public void start() {
        time.reset();
    }

    @Override
    public void loop() {

        if (stage == 0) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 1400, 0.5);
                drive.run();
                flyWheel.currentPower = flyWheel.defaultStartingPower;
                flyWheel.currentlyRunning = true;
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        flyWheel.powerMotor(); // Update flywheel values

        if (stage == 1) {
            intake.setIntake(Intake.IntakeSpec.BOTH, Intake.IntakeDirection.IN);
            if (time.time() > 2.5) {
                stage++;
                intake.stopIntake(Intake.IntakeSpec.BOTH);
                flyWheel.currentlyRunning = false;
            }
        }



        telemetry.addData("Stage", String.valueOf(stage));

    }
}

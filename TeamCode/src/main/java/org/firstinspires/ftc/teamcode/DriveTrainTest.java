package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DynamicAutonomousSelector;

/**
 * Created by Dynamic Signals on 10/30/2016.
 */

@Autonomous(name = "DriveTrainTest", group = "Testing")
public class DriveTrainTest extends OpMode {

    DriveTrain driveTrain;
    DynamicAutonomousSelector das;

    @Override
    public void init() {
        driveTrain = new DriveTrain(hardwareMap);
        das = new DynamicAutonomousSelector();
    }

    @Override
    public void loop() {

        if (this.time > 0 && this.time <= 2) {
            if (das.getSelectorChoices().get("MotorMode").equals("Front")) { // Left FRONT Right Back
                driveTrain.LeftFrontMotor.setPower(1);
                driveTrain.RightBackMotor.setPower(1);
            } else { // Left BACK Right Front
                driveTrain.LeftBackMotor.setPower(1);
                driveTrain.RightFrontMotor.setPower(1);
            }
        }  else {
            driveTrain.stopRobot();
        }

    }
}

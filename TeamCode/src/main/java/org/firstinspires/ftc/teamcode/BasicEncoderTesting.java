package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DynamicAutonomousSelector;

/**
 * Created by Dynamic Signals on 10/30/2016.
 */

@Autonomous(name = "BasicEncoder", group = "Testing")
public class BasicEncoderTesting extends OpMode {

    DriveTrain driveTrain;

    @Override
    public void init() {
        driveTrain = new DriveTrain(hardwareMap);
    }

    @Override
    public void start() {
        driveTrain.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


    }

    @Override
    public void loop() {

    }
}

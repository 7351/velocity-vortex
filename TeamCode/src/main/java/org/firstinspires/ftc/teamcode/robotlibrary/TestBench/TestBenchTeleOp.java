package org.firstinspires.ftc.teamcode.robotlibrary.TestBench;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TeleOpUtils;

/**
 * Created by Dynamic Signals on 1/15/2017.
 */

@TeleOp(name = "TeleOpTestBench")
public class TestBenchTeleOp extends OpMode {

    DriveTrain driveTrain;
    TeleOpUtils teleOpUtils;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        teleOpUtils = new TeleOpUtils(gamepad1, gamepad2);

    }

    @Override
    public void loop() {

        driveTrain.powerLeft(teleOpUtils.scaleInput(-gamepad1.left_stick_y));
        driveTrain.powerRight(teleOpUtils.scaleInput(-gamepad1.right_stick_y));
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;

/**
 * Created by Dynamic Signals on 12/6/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "EncoderTurnTest", group = "Testing")
public class EncoderTurnTest extends OpMode {

    DriveTrain driveTrain;

    GyroUtils.Direction turnDirection = GyroUtils.Direction.CLOCKWISE;
    int targetPosition = 7500;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);

    }

    /* Encoder drive sequence
     * 1. Reset
     * 2. Set target
     * 3. Set mode to RUN_TO_POSITION
     * 4. Set motor power
     * 5. Wait until motor isn't busy
     */

    @Override
    public void start() {

        double power = 0.5;

        driveTrain.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int directionMultiplier = (turnDirection.equals(GyroUtils.Direction.CLOCKWISE)) ? 1 : -1; // For the left side

        driveTrain.LeftFrontMotor.setTargetPosition(directionMultiplier * targetPosition);
        driveTrain.RightFrontMotor.setTargetPosition(-directionMultiplier * targetPosition);

        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        driveTrain.powerLeft(directionMultiplier * power);
        driveTrain.powerRight(-directionMultiplier * power);

    }

    @Override
    public void loop() {

        if (!driveTrain.isBusy()) driveTrain.stopRobot();

        telemetry.addData("Position", String.valueOf(driveTrain.LeftFrontMotor.getCurrentPosition() + " - " + driveTrain.LeftFrontMotor.getTargetPosition()));

        DbgLog.msg(String.valueOf(": " + driveTrain.LeftFrontMotor.getCurrentPosition() + " - " + driveTrain.LeftFrontMotor.getTargetPosition()));

    }
}

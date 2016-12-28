package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

    double turnPerDegree = (40 * 1.5) / 360;


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
        int turnThisMuch = 90 * (int) ((7 * 4 * 40 * 1.5) / 360);

        double power = 0.5;

        driveTrain.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int directionMultiplier = (turnDirection.equals(GyroUtils.Direction.CLOCKWISE)) ? 1 : -1; // For the left side

        driveTrain.RightFrontMotor.setTargetPosition(-100);
        //driveTrain.RightFrontMoto r.setTargetPosition(-directionMultiplier * turnThisMuch);

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        driveTrain.powerLeft(directionMultiplier * power);
        driveTrain.powerRight(-directionMultiplier * power);
    }

    @Override
    public void loop() {

        if (!driveTrain.LeftFrontMotor.isBusy())
            driveTrain.stopRobot();

        DbgLog.msg(String.valueOf(": " + driveTrain.LeftBackMotor.getCurrentPosition() + " - " + driveTrain.LeftBackMotor.getTargetPosition()));

        telemetry.addData("Busy", String.valueOf(driveTrain.LeftFrontMotor.isBusy() + ":" + driveTrain.RightFrontMotor.isBusy()));
        telemetry.addData("Encoders", String.valueOf(driveTrain.LeftFrontMotor.getCurrentPosition() + ":" + driveTrain.RightFrontMotor.getCurrentPosition()));
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderTurn;

/**
 * Created by Dynamic Signals on 12/6/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "EncoderTurnTest", group = "Testing")
public class EncoderTurnTest extends OpMode {

    DriveTrain driveTrain;
    int startingLeftPosition;
    int startingRightPosition;


    int stage = 0;
    double turnPerDegree = (40 * 1.5) / 360;


    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        /*this.gyroUtils = gyroUtils;
        gyroSensor = initialgyro;*/
        // We use this way instead of resetting the encoders because we can relatively move the robot
        startingLeftPosition = driveTrain.LeftFrontMotor.getCurrentPosition();
        startingRightPosition = driveTrain.RightFrontMotor.getCurrentPosition();


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


    }

    @Override
    public void loop() {

        if (stage == 0) {
            int turnThisMuch = 90 * (int)turnPerDegree;

            driveTrain.LeftFrontMotor.setTargetPosition(turnThisMuch + startingLeftPosition);
            driveTrain.RightFrontMotor.setTargetPosition(startingRightPosition - turnThisMuch);

            driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveTrain.LeftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveTrain.RightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            driveTrain.powerLeft((.5 > 0 && (turnThisMuch + startingLeftPosition) < 0) ? -1 * .5 : .5);
            driveTrain.powerRight((-.5 < 0 && (startingRightPosition - turnThisMuch) > 0) ? 1 * -.5 : -.5);


            /*EncoderTurn yo = new EncoderTurn(driveTrain);
            yo.run(90, 1, true);*/
            // Currently, this just TURNS 90 degrees, not turns to 90 degrees.
        }
        if (stage == 1) {
            if (!driveTrain.isBusy()) {
                driveTrain.stopRobot();
                stage++;
            }
        }

        DbgLog.msg(String.valueOf(": " + driveTrain.LeftBackMotor.getCurrentPosition() + " - " + driveTrain.LeftBackMotor.getTargetPosition()));

    }
}

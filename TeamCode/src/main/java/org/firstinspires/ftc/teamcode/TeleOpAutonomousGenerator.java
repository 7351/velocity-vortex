package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake.IntakeSpec;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.TeleOpUtils;

/**
 * Created by Dynamic Signals on 10/21/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "AutonomousGenerator")
public class TeleOpAutonomousGenerator extends OpMode {

    private DriveTrain driveTrain;
    private TeleOpUtils teleOpUtils;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        teleOpUtils = new TeleOpUtils(gamepad1, gamepad2);

        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    @Override
    public void loop() {

        /*
         * Driving controls
         * Right Joystick - Arcade driving takes both x and y - full speed
         * Left Joystick - Arcade driving takes both x and y - half speed
         */

        float right = 0;
        float left = 0;

        // throttle: right_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: right_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle1 = -gamepad1.right_stick_y;

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float direction2 = gamepad1.left_stick_x;

        if ((direction2 != 0) && (throttle1 == 0)) { // If the second joystick is moving only
            right = -direction2;
            left = direction2;
        }

        if ((throttle1 != 0) && (direction2 == 0)) { // If the first joystick is moving only
            right = throttle1;
            left = throttle1;
        }

        if ((throttle1 != 0) && (direction2 != 0)) { // We will prefer the right joystick
            right = throttle1;
            left = throttle1;
        }

        if ((throttle1 == 0) && (direction2 == 0)) {
            right = 0;
            left = 0;
        }

        /*
        right = throttle - direction;
        left = throttle + direction;
        */

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float) teleOpUtils.scaleInput(right);
        left = (float) teleOpUtils.scaleInput(left);

        if (gamepad1.a) {
            driveTrain.stopRobot();
            driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } else {
            // write the values to the motors
            driveTrain.powerLeft(left);
            driveTrain.powerRight(right);
        }


        /* Controller 1 telemetry data */
        telemetry.addData("Drive power", "L: " + String.valueOf(left) + ", R: " + String.valueOf(right));
        telemetry.addData("Encoder Front", "L: " + String.valueOf(driveTrain.LeftFrontMotor.getCurrentPosition()) +
                ", R: " + driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Time", String.valueOf(time));

    }
}
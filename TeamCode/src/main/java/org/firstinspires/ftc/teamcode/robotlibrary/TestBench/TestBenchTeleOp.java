package org.firstinspires.ftc.teamcode.robotlibrary.TestBench;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TeleOpUtils;

/**
 * Created by Dynamic Signals on 1/15/2017.
 */

@TeleOp(name = "TeleOpTestBench")
public class TestBenchTeleOp extends OpMode {

    DriveTrain driveTrain;
    TeleOpUtils teleOpUtils;
    boolean twoMotors = true;
    DcMotor LeftMotor;
    DcMotor RightMotor;

    boolean RightDPad = false;

    @Override
    public void init() {

        if (!twoMotors) {
            driveTrain = new DriveTrain(hardwareMap);
        } else {
            LeftMotor = hardwareMap.dcMotor.get("LeftMotor");
            RightMotor = hardwareMap.dcMotor.get("RightMotor");
            RightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        teleOpUtils = new TeleOpUtils(gamepad1, gamepad2);


    }

    @Override
    public void init_loop() {
        if (gamepad1.dpad_right) {
            if (!RightDPad) {
                if (twoMotors) {
                    twoMotors = false;
                } else {
                    twoMotors = true;
                }
                RightDPad = true;
            }
        } else {
            RightDPad = false;
        }
        telemetry.addData("Motor mode", (twoMotors ? "Two Motors": "Four Motors"));
        telemetry.addData("", "Change motor mode by right dpad");
    }

    @Override
    public void loop() {

        // throttle: right_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: right_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle = -gamepad1.right_stick_y;
        float direction = gamepad1.right_stick_x;
        if (twoMotors) {
            direction = -direction;
        }
        float right = throttle - direction;
        float left = throttle + direction;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float) teleOpUtils.scaleInput(right);
        left = (float) teleOpUtils.scaleInput(left);

        // write the values to the motors
        if (!twoMotors) {
            driveTrain.powerLeft(left);
            driveTrain.powerRight(right);
        } else {
            LeftMotor.setPower(Range.clip(left, -1, 1));
            RightMotor.setPower(Range.clip(right, -1, 1));
        }

        /* Controller 1 telemetry data */
        telemetry.addData("Drive power", "L: " + String.valueOf(left) + ", R: " + String.valueOf(right));
    }
}

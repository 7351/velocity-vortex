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
    boolean twoMotors = false;
    boolean tankDrive = false;
    DcMotor LeftMotor;
    DcMotor RightMotor;
    int selectionStage = 0;
    double scalingPower = 1;
    double delta = 10;

    boolean RightDPad = false;
    boolean DPadDown = false;
    boolean DPadUp = false;


    @Override
    public void init() {

        teleOpUtils = new TeleOpUtils(gamepad1, gamepad2);

    }

    @Override
    public void init_loop() {
        if (selectionStage == 0) {
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
            if (gamepad1.dpad_up) {
                selectionStage++;
            }

            telemetry.addData("Motor mode", (twoMotors ? "Two Motors" : "Four Motors"));
            telemetry.addData("", "Change motor mode by right dpad");
            telemetry.addData(".", "Dpad up to go to next stage");
        }
        if (selectionStage == 1) {
            if (gamepad1.dpad_right) {
                if (!RightDPad) {
                    if (tankDrive) {
                        tankDrive = false;
                    } else {
                        tankDrive = true;
                    }
                    RightDPad = true;
                }
            } else {
                RightDPad = false;
            }
            telemetry.addData("Drive mode", (tankDrive ? "Tank" : "Arcade"));
            telemetry.addData("", "Change drive mode by right dpad");
        }
    }

    @Override
    public void start() {
        if (!twoMotors) {
            driveTrain = new DriveTrain(hardwareMap);
        } else {
            LeftMotor = hardwareMap.dcMotor.get("leftMotor");
            RightMotor = hardwareMap.dcMotor.get("rightMotor");
            RightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_down) {
            if (!DPadDown) {
                scalingPower = Range.clip(scalingPower - (delta / 100), 0.1, 1);
                DPadDown = true;
            }
        } else {
            DPadDown = false;
        }

        if (gamepad1.dpad_up) {
            if (!DPadUp) {
                scalingPower = Range.clip(scalingPower + (delta / 100), 0.1, 1);
                DPadUp = true;
            }
        } else {
            DPadUp = false;
        }

        float left = 0;
        float right = 0;
        if (!tankDrive) {
            // throttle: right_stick_y ranges from -1 to 1, where -1 is full up, and
            // 1 is full down
            // direction: right_stick_x ranges from -1 to 1, where -1 is full left
            // and 1 is full right
            float throttle = -gamepad1.right_stick_y;
            float direction = gamepad1.right_stick_x;
            if (twoMotors) {
                direction = -direction;
            }
            right = throttle - direction;
            left = throttle + direction;

            // clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);

            // scale the joystick value to make it easier to control
            // the robot more precisely at slower speeds.
            right = (float) teleOpUtils.scaleInput(right);
            left = (float) teleOpUtils.scaleInput(left);
        } else {
            left = (float) teleOpUtils.scaleInput(-gamepad1.left_stick_y);
            right = (float) teleOpUtils.scaleInput(-gamepad1.right_stick_y);
        }


        // write the values to the motors
        if (!twoMotors) {
            driveTrain.powerLeft(left * scalingPower);
            driveTrain.powerRight(right * scalingPower);
        } else {
            LeftMotor.setPower(Range.clip(left * scalingPower, -1, 1));
            RightMotor.setPower(Range.clip(right * scalingPower, -1, 1));
        }

        /* Controller 1 telemetry data */
        telemetry.addData("Drive power", "L: " + String.valueOf(left) + ", R: " + String.valueOf(right));
        telemetry.addData("Scaling power", Math.round(scalingPower * 100) + "%");
    }
}

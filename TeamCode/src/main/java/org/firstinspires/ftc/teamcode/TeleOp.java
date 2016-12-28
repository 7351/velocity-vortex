package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake.IntakeSpec;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TeleOpUtils;

/**
 * Created by Dynamic Signals on 10/21/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "TeleOp")
public class TeleOp extends OpMode {

    boolean DPadUp = false;
    boolean DPadDown = false;
    boolean DPadRight = false;
    boolean DPadLeft = false;
    private DriveTrain driveTrain;
    private FlyWheel flyWheel;
    private Intake intake;
    private Lift lift;
    private TeleOpUtils teleOpUtils;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        lift = new Lift(hardwareMap);
        teleOpUtils = new TeleOpUtils(gamepad1, gamepad2);
    }

    @Override
    public void loop() {

        /*
         * Controller 1 Controls --------------------------------------------------
         */

        /*
         * Intake controls
         * Left Trigger - Intake A sets power to -1
         * Right Trigger - Intake A sets power to 1
         * Left Bumper - Intake B sets power to -1
         * Right Bumper - Intake B sets power to 1
         */

        if (gamepad1.left_trigger >= 0.5) {
            intake.setIntakePower(IntakeSpec.A, -1);
        }

        if (gamepad1.right_trigger >= 0.5) {
            intake.setIntakePower(IntakeSpec.A, 1);
        }

        if (gamepad1.left_trigger < 0.5 && gamepad1.right_trigger < 0.5) {
            intake.stopIntake(IntakeSpec.A);
        }

        if (gamepad1.left_bumper) {
            intake.setIntakePower(IntakeSpec.B, -1);
        }

        if (gamepad1.right_bumper) {
            intake.setIntakePower(IntakeSpec.B, 1);
        }

        if (!gamepad1.left_bumper && !gamepad1.right_bumper) {
            intake.stopIntake(IntakeSpec.B);
        }

        /*
         * Fly wheel controls
         * D-pad up - Increment the power by 0.05
         * D-pad down - Decrease the power by 0.05
         * D-pad right - Kill the motor
         * D-pad left - Set the power to default no-matter the position
         */

        boolean DPadUpPressed = gamepad1.dpad_up;
        boolean DPadDownPressed = gamepad1.dpad_down;
        boolean DPadRightPressed = gamepad1.dpad_right;
        boolean DPadLeftPressed = gamepad1.dpad_left;

        if (DPadDownPressed) {
            if (!DPadDown) {
                flyWheel.currentlyRunning = true;
                flyWheel.currentPower -= flyWheel.incrementValue;
                DPadDown = true;
            }
        } else {
            DPadDown = false;
        }

        if (DPadUpPressed) {
            if (!DPadUp) {
                flyWheel.currentlyRunning = true;
                flyWheel.currentPower += flyWheel.incrementValue;
                DPadUp = true;
            }
        } else {
            DPadUp = false;
        }

        if (DPadRightPressed) {
            if (!DPadRight) {
                flyWheel.currentlyRunning = false;
                DPadRight = true;
            }
        } else {
            DPadRight = false;
        }

        if (DPadLeftPressed) {
            if (!DPadLeft) {
                flyWheel.currentlyRunning = true;
                flyWheel.currentPower = flyWheel.defaultStartingPower;
                DPadLeft = true;
            }
        } else {
            DPadLeft = false;
        }

        flyWheel.currentPower = Range.clip(flyWheel.currentPower, 0, 1);

        if (flyWheel.currentPower == 0) {
            flyWheel.currentlyRunning = false;
        }

        if (flyWheel.currentlyRunning) {
            flyWheel.FlyWheelMotor.setPower(flyWheel.currentPower);
        } else {
            flyWheel.FlyWheelMotor.setPower(0);
        }

        telemetry.addData("FlyWheel", String.valueOf(flyWheel.FlyWheelMotor.getPower()));


        /*
         * Driving controls
         * Right Joystick - Arcade driving takes both x and y
         */

        // throttle: right_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: right_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle = -gamepad1.right_stick_y;
        float direction = gamepad1.right_stick_x;
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
        driveTrain.powerLeft(left);
        driveTrain.powerRight(right);

        /* Controller 1 telemetry data */
        telemetry.addData("Drive power", "L: " + String.valueOf(left) + ", R: " + String.valueOf(right));

        /*
         * Controller 2 Controls --------------------------------------------------
         */

        /*
         * Lift controls
         * Right joystick Y Up - Lift positive power
         * Right joystick Y Down - Lift negative power
         * D-pad up (without the top bumper) - Put the lift servo up
         * D-pad down (without the top bumper) - Put the lift servo down
         */

        double right_joystick_y = -gamepad2.right_stick_y;
        boolean liftManualMode = gamepad2.left_bumper;

        if (right_joystick_y > 0.5) {
            lift.LiftMotor.setPower(lift.liftMotorPower);
        } else if(right_joystick_y < 0.5) {
            lift.LiftMotor.setPower(-lift.liftMotorPower);
        } else {
            lift.LiftMotor.setPower(0);
        }

        if (liftManualMode) {
            if (gamepad2.dpad_up) {
                lift.LiftServo.setPosition(lift.maximumServo);
            } if (gamepad2.dpad_down) {
                lift.LiftServo.setPosition(lift.minimumServo);
            }
        } else {
            //TODO: Implement stepper here
        }




    }
}
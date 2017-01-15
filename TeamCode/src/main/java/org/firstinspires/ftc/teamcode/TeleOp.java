package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake.IntakeSpec;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.TeleOpUtils;

/**
 * Created by Dynamic Signals on 10/21/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "TeleOp")
public class TeleOp extends OpMode {

    private boolean DPadUp = false;
    private boolean DPadDown = false;
    private boolean DPadRight = false;
    private boolean DPadLeft = false;
    private boolean DPadUp2 = false;
    private boolean DPadDown2 = false;
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
            intake.setIntakePower(IntakeSpec.B, 1);
        }

        if (gamepad1.right_bumper) {
            intake.setIntakePower(IntakeSpec.B, -1);
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
         * Right Joystick - Arcade driving takes both x and y - full speed
         * Left Joystick - Arcade driving takes both x and y - half speed
         */

        float right = 0;
        float left = 0;
        double scalePower = 1;
        double slowerSpeed = 0.5;

        // throttle: right_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: right_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle1 = -gamepad1.right_stick_y;
        float direction1 = gamepad1.right_stick_x;

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle2 = -gamepad1.left_stick_y;
        float direction2 = gamepad1.left_stick_x;

        if ((throttle2 != 0 || direction2 != 0) && (throttle1 == 0 && direction1 == 0)) { // If the second joystick is moving only
            right = throttle2 - direction2;
            left = throttle2 + direction2;
            scalePower = slowerSpeed;
        }

        if ((throttle1 != 0 || direction1 != 0) && (throttle2 == 0 && direction2 == 0)) { // If the first joystick is moving only
            right = throttle1 - direction1;
            left = throttle1 + direction1;
            scalePower = 1;
        }

        if ((throttle1 != 0 || direction1 != 0) && (throttle2 != 0 || direction2 != 0)) { // We will prefer the first joystick for faster
            right = throttle1 - direction1;
            left = throttle1 + direction1;
            scalePower = 1;
        }

        /*
        right = throttle - direction;
        left = throttle + direction;
        */

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right * ((float) scalePower), -1, 1);
        left = Range.clip(left * ((float) scalePower), -1, 1);

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
         * D-pad up (with top bumper) - Increment the servo up
         * D-pad down (with top bumper) - Increment the servo down
         */

        double right_joystick_y = gamepad2.right_stick_y;
        boolean liftManualMode = gamepad2.left_bumper;

        lift.setLiftPower(teleOpUtils.scaleInput(right_joystick_y));

        if (!liftManualMode) { // If it's automatic
            if (gamepad2.dpad_up) {
                lift.currentServoPosition = lift.maximum;
            } if (gamepad2.dpad_down) {
                lift.currentServoPosition = lift.minimum;
            } if (gamepad2.dpad_left) {
                lift.currentServoPosition = lift.ideal;
            }
        } else {
            if (gamepad2.dpad_up) {
                if (!DPadUp2) {
                    lift.incrementServo();
                    DPadUp2 = true;
                }
            } else {
                DPadUp2 = false;
            }
            if (gamepad2.dpad_down) {
                if (!DPadDown2) {
                    lift.decrementServo();
                    DPadDown2 = true;
                }
            } else {
                DPadDown2 = false;
            }
        }
        lift.currentServoPosition = Range.clip(lift.currentServoPosition, -1, 1);
        lift.LiftServo.setPosition(lift.currentServoPosition);


        telemetry.addData("Lift Servo", String.valueOf(lift.currentServoPosition));

    }
}
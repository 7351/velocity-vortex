package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.BeaconUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake.IntakeSpec;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.TeleOpUtils;

/**
 * Created by Dynamic Signals on 10/21/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends OpMode {

    DriveTrain driveTrain;
    FlyWheel flyWheel;
    Intake intake;
    Lift lift;
    TeleOpUtils teleOpUtils;
    BeaconUtils beaconUtils;
    ColorUtils colorUtils;
    private boolean DPadUp = false;
    private boolean DPadDown = false;
    private boolean DPadRight = false;
    private boolean DPadLeft = false;
    private boolean DPadRight2NoStep = false;
    private boolean DPadLeft2NoStep = false;
    private boolean DPadDown2NoStep = false;
    private boolean DPadLeft2StepL = false;
    private boolean DPadLeft2StepR = false;
    private boolean DPadRight2StepL = false;
    private boolean DPadRight2StepR = false;
    private boolean DPadDown2Step = false;
    private boolean DPadUp2Step = false;
    private ElapsedTime capBallServoTime = new ElapsedTime();
    public boolean Mecanum = false;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        lift = new Lift(hardwareMap);
        teleOpUtils = new TeleOpUtils(gamepad1, gamepad2);
        colorUtils = new ColorUtils(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils, "NS");

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

        if (!Mecanum) {
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
        }

        if (Mecanum) {

            // Detecting which is moving
            boolean LeftStickMovement = gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0;
            boolean RightStickMovement = gamepad1.right_stick_x != 0 || gamepad1.right_stick_y != 0;

            // Scaling the joystick inputs
            double scaledY1 = teleOpUtils.scaleInput(-gamepad1.left_stick_y);
            double scaledX1 = teleOpUtils.scaleInput(gamepad1.left_stick_x);
            double scaledY2 = teleOpUtils.scaleInput(-gamepad1.right_stick_y);
            double scaledX2 = teleOpUtils.scaleInput(gamepad1.right_stick_x);

            // Power temporary values
            double RFLB = 0;
            double LFRB = 0;

            // Right joystick for rotating clockwise and counterclockwise
            if (RightStickMovement) {
                RFLB = Range.clip(-scaledX2/2, -1, 1);
                LFRB = Range.clip(scaledX2/2, -1, 1);
            }
            // Left joystick driving forward, backwards, left, and right
            if (LeftStickMovement) { // We prefer the left joystick over the right
                RFLB = Range.clip((scaledY1 - scaledX1)/2, -1, 1);
                LFRB = Range.clip((-scaledY1 - scaledX1)/2, -1, 1);
            }

            // Power the motors
            driveTrain.RightFrontMotor.setPower(RFLB);
            driveTrain.LeftBackMotor.setPower(RFLB);
            driveTrain.LeftFrontMotor.setPower(LFRB);
            driveTrain.RightBackMotor.setPower(LFRB);
        }



        /*
         * Controller 2 Controls --------------------------------------------------
         */

        /*
         * Lift controls
         * Right joystick Y Up - Lift positive power
         * Right joystick Y Down - Lift negative power
         * D-pad left (without a top bumper) - Open the cap ball
         * D-pad right (without a top bumper) - Close the cap ball
         * D-pad down (without a top bumper) - Grab the ball
         *
         * D-pad left (with a top bumper) - Move servo left (based on which bumper is pressed)
         * D-pad right (with a top bumper) - Move servo right (based on which bumper is pressed)
         * D-pad down (with a top bumper) - Step both servos out at the same time
         * D-pad up (with a top bumper) - Step both servos in at the same time
         */

        double right_joystick_y = gamepad2.right_stick_y;
        boolean liftManualModeLeft = gamepad2.left_bumper;
        boolean liftManualModeRight = gamepad2.right_bumper;

        lift.setLiftPower(teleOpUtils.scaleInput(right_joystick_y));

        if (!liftManualModeLeft && !liftManualModeRight) {
            if (gamepad2.dpad_left) {
                if (!DPadLeft2NoStep && !DPadRight2NoStep) {
                    capBallServoTime.reset();
                    DPadLeft2NoStep = true;
                }
            }
            if (gamepad2.dpad_right) {
                if (!DPadRight2NoStep && !DPadLeft2NoStep) {
                    capBallServoTime.reset();
                    DPadRight2NoStep = true;
                }
            }
            if (gamepad2.dpad_down) {
                if (!DPadDown2NoStep) {
                    lift.leftPosition = Lift.grab[0];
                    lift.rightPosition = Lift.grab[1];
                    DPadDown2NoStep = true;
                } else {
                    DPadDown2NoStep = false;
                }
            }
        }
        if (liftManualModeLeft && !liftManualModeRight) { // Manual left
            if (gamepad2.dpad_left) {
                DPadLeft2NoStep = false;
                DPadRight2NoStep = false;
                DPadDown2NoStep = false;
                if (!DPadLeft2StepL) {
                    lift.leftPosition -= Lift.servoIncrement;
                    DPadLeft2StepL = true;
                }
            } else {
                DPadLeft2StepL = false;
            }
            if (gamepad2.dpad_right) {
                DPadLeft2NoStep = false;
                DPadRight2NoStep = false;
                DPadDown2NoStep = false;
                if (!DPadRight2StepL) {
                    lift.leftPosition += Lift.servoIncrement;
                    DPadRight2StepL = true;
                }
            } else {
                DPadRight2StepL = false;
            }
        }
        if (liftManualModeRight && !liftManualModeLeft) { // Manual right
            if (gamepad2.dpad_left) {
                DPadLeft2NoStep = false;
                DPadRight2NoStep = false;
                DPadDown2NoStep = false;
                if (!DPadLeft2StepR) {
                    lift.rightPosition -= Lift.servoIncrement;
                    DPadLeft2StepR = true;
                }
            } else {
                DPadLeft2StepR = false;
            }
            if (gamepad2.dpad_right) {
                DPadLeft2NoStep = false;
                DPadRight2NoStep = false;
                DPadDown2NoStep = false;
                if (!DPadRight2StepR) {
                    lift.rightPosition += Lift.servoIncrement;
                    DPadRight2StepR = true;
                }
            } else {
                DPadRight2StepR = false;
            }
        }

        if (liftManualModeLeft || liftManualModeRight) {
            if (gamepad2.dpad_up) { // Step in
                DPadLeft2NoStep = false;
                DPadRight2NoStep = false;
                DPadDown2NoStep = false;
                if (!DPadUp2Step) {
                    lift.leftPosition += Lift.servoIncrement;
                    lift.rightPosition -= Lift.servoIncrement;
                    DPadUp2Step = true;
                }
            } else {
                DPadUp2Step = false;
            }
            if (gamepad2.dpad_down) { // Step out
                DPadLeft2NoStep = false;
                DPadRight2NoStep = false;
                DPadDown2NoStep = false;
                if (!DPadDown2Step) {
                    lift.leftPosition -= Lift.servoIncrement;
                    lift.rightPosition += Lift.servoIncrement;
                    DPadDown2Step = true;
                }
            } else {
                DPadDown2Step = false;
            }
        }

        if (DPadLeft2NoStep) { // Open
            if (capBallServoTime.time() < 0.2) {
                lift.leftPosition = Lift.open[0];
            } else {
                lift.rightPosition = Lift.open[1];
                DPadLeft2NoStep = false; // Completed with routine
            }
        }

        if (DPadRight2NoStep) { // Close
            if (capBallServoTime.time() < 0.3) {
                lift.rightPosition = Lift.closed[1];
            } else {
                lift.leftPosition = Lift.closed[0];
                DPadRight2NoStep = false; // Completed with routine
            }
        }

        lift.updateServo();

        telemetry.addData("Cap Ball",
                "L: " + TeleOpUtils.df.format(lift.leftPosition) +
                        " R: " + TeleOpUtils.df.format(lift.rightPosition));

        /*
         * Beacon sunglasses control
         * X left rotate servo left
         * B right rotate servo right
         */

        if (gamepad2.x && !gamepad2.b) {
            beaconUtils.rotateServo(BeaconUtils.ServoPosition.TRIGGER_LEFT);
        }
        if (gamepad2.b && !gamepad2.x) {
            beaconUtils.rotateServo(BeaconUtils.ServoPosition.TRIGGER_RIGHT);
        }
        if (!gamepad2.x && !gamepad2.b) {
            beaconUtils.rotateServo(BeaconUtils.ServoPosition.CENTER);
        }

        telemetry.addData("Beacon Servo", beaconUtils.BeaconServo.getPosition());

    }

    @Override
    public void stop() {
        driveTrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
}
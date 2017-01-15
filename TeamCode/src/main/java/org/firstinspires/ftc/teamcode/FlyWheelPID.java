package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.FlyWheel;

/**
 * Created by Dynamic Signals on 1/1/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "FlyWheelPID", group = "Testing")
@Disabled
public class FlyWheelPID extends OpMode {

    private boolean DPadUp = false;
    private boolean DPadDown = false;
    private boolean DPadRight = false;
    private boolean DPadLeft = false;
    DriveTrain driveTrain;
    int lastRPM;
    int lastEncoder = 0;
    FlyWheel flyWheel;
    ElapsedTime testingTime = new ElapsedTime();


    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);

        flyWheel.FlyWheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        flyWheel.FlyWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void init_loop() {
        boolean DPadUpPressed = gamepad1.dpad_up;
        boolean DPadDownPressed = gamepad1.dpad_down;
        boolean DPadRightPressed = gamepad1.dpad_right;
        boolean DPadLeftPressed = gamepad1.dpad_left;

        if (DPadDownPressed) {
            if (!DPadDown) {
                flyWheel.currentPower -= flyWheel.incrementValue;
                DPadDown = true;
            }
        } else {
            DPadDown = false;
        }

        if (DPadUpPressed) {
            if (!DPadUp) {
                flyWheel.currentPower += flyWheel.incrementValue;
                DPadUp = true;
            }
        } else {
            DPadUp = false;
        }

        telemetry.addData("FlyWheel", flyWheel.currentPower);
    }

    @Override
    public void start() {
        testingTime.reset();
        flyWheel.currentlyRunning = true;
    }

    @Override
    public void loop() {

        if (testingTime.time() < 15) {

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
                    flyWheel.currentPower -= flyWheel.incrementValue;
                    DPadDown = true;
                }
            } else {
                DPadDown = false;
            }

            if (DPadUpPressed) {
                if (!DPadUp) {
                    flyWheel.currentPower += flyWheel.incrementValue;
                    DPadUp = true;
                }
            } else {
                DPadUp = false;
            }

            if (DPadRightPressed) {
                if (!DPadRight) {
                    if (!flyWheel.currentlyRunning) {
                        flyWheel.currentlyRunning = true;
                    } else {
                        flyWheel.currentlyRunning = false;
                    }
                    DPadRight = true;
                }
            } else {
                DPadRight = false;
            }

            if (DPadLeftPressed) {
                if (!DPadLeft) {
                    flyWheel.currentPower = flyWheel.defaultStartingPower;
                    DPadLeft = true;
                }
            } else {
                DPadLeft = false;
            }

        }

        if (testingTime.time() > 15) {
            flyWheel.currentlyRunning = false;
        }
        lastEncoder = flyWheel.FlyWheelMotor.getCurrentPosition();
        lastRPM = (lastEncoder / 560) * 4;
        telemetry.addData("FlyWheel", flyWheel.currentPower);
        telemetry.addData("Time", String.valueOf(testingTime.time()));
        telemetry.addData("RPM", lastRPM);
        telemetry.addData("Enc", lastEncoder);

        flyWheel.currentPower = Range.clip(flyWheel.currentPower, 0, 1);

        if (flyWheel.currentPower == 0) {
            flyWheel.currentlyRunning = false;
        }

        if (flyWheel.currentlyRunning) {
            flyWheel.FlyWheelMotor.setPower(flyWheel.currentPower);
        } else {
            flyWheel.FlyWheelMotor.setPower(0);
        }

    }
}

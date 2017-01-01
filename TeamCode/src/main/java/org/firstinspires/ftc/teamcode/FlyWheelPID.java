package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.FlyWheel;

/**
 * Created by Dynamic Signals on 1/1/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "FlyWheelPID", group = "Testing")
public class FlyWheelPID extends OpMode {

    private boolean DPadUp = false;
    private boolean DPadDown = false;
    private boolean DPadRight = false;
    private boolean DPadLeft = false;
    DriveTrain driveTrain;
    FlyWheel flyWheel;


    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);

        flyWheel.FlyWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void loop() {

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

        String RPM = String.valueOf((flyWheel.FlyWheelMotor.getCurrentPosition()/time)*60);

        telemetry.addData("FlyWheel", String.valueOf(flyWheel.FlyWheelMotor.getPower()));
        telemetry.addData("RPM", RPM);
        if (Math.round(time) % 15 == 0) {
            DbgLog.msg("RPM: " + RPM);
        }

    }
}

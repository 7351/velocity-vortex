package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;

/**
 * Created by Dynamic Signals on 10/21/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "TeleOp")
@Disabled
public class TeleOp extends OpMode {
    public DcMotor flyWheel;
    public DcMotor intakeA;
    public DcMotor intakeB;
    DriveTrain driveTrain;

    boolean buttonPressed = false;
    double startingFlyPower = .5;
    double curentFlyPower;
    double amountpressed = 0;
    double incrementpower = .05;

    @Override
    public void init() {
        flyWheel = hardwareMap.dcMotor.get("flyWheel");
        driveTrain = new DriveTrain(hardwareMap);

    }

    @Override
    public void loop() {

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
        right = (float) scaleInput(right);
        left = (float) scaleInput(left);

        // write the values to the motors

        driveTrain.powerLeft(left);
        driveTrain.powerRight(right);


        telemetry.addData("drive power", "L: " + String.valueOf(left) + ", R: " + String.valueOf(right));
        //incrments power by incrementalpower
        if (gamepad1.dpad_up) {
            if (!buttonPressed) {
                curentFlyPower = startingFlyPower + (incrementpower * amountpressed);//incresses power by set amount if first presss amount press =0
                curentFlyPower = Range.clip(curentFlyPower, 0, 1);// keep current between 0 and 1
                flyWheel.setPower(curentFlyPower);
                buttonPressed = true;
                amountpressed++;
                telemetry.addData("curentFlyPower", curentFlyPower);


            }
        }
        if (!gamepad1.dpad_up) {
            buttonPressed = false;
        }
        //decrements power by incrementalpower
        if (gamepad1.dpad_down)
            if (!buttonPressed) {
                amountpressed--;
                curentFlyPower = startingFlyPower - (incrementpower * amountpressed);
                curentFlyPower = Range.clip(curentFlyPower, 0, 1);
                flyWheel.setPower(curentFlyPower);
                buttonPressed = true;
                telemetry.addData("curentFlyPower", curentFlyPower);


            }
        if (!gamepad1.dpad_down) {
            buttonPressed = false;

        }
        //kill fly power
        if (gamepad1.dpad_right) {
            flyWheel.setPower(0);

        }
        //d pad left resets
        if (gamepad1.dpad_left) {
            curentFlyPower = startingFlyPower;
            amountpressed = 0;
        }//intake a in
        if (gamepad1.right_trigger > 0) {
            intakeA.setPower(1);

        }
        if (gamepad1.right_trigger == 0) {
            intakeA.setPower(0);
        }//intake a out
        if (gamepad1.left_trigger > 0) {
            intakeA.setPower(-1);
        }
        if (gamepad1.left_trigger == 0) {
            intakeA.setPower(0);
        }//intake b out
        if (gamepad1.left_bumper) {
            intakeB.setPower(-1);
        }//intake b in
        if (gamepad1.right_bumper) {
            intakeB.setPower(1);
        }
    }


    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};
        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }
        return dScale;
    }
}

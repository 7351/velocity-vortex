package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Dynamic Signals on 2/19/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "StepperProgram")
public class StepperProgram extends OpMode {

    Servo capBallLeft;
    Servo capBallRight;

    boolean DPadLeft = false;
    boolean DPadRight = false;

    double LeftPosition = 0;
    double RightPosition = 1;
    double increment = 0.01;

    @Override
    public void init() {

        capBallLeft = hardwareMap.servo.get("CapBallServoLeft");
        capBallRight = hardwareMap.servo.get("CapBallServoRight");

    }

    @Override
    public void loop() {

        if (gamepad1.dpad_left) { // Left
            if (!DPadLeft) {
                if (gamepad1.left_bumper) { // Left servo
                    LeftPosition -= increment;
                }
                if (gamepad1.right_bumper) { // Right servo
                    RightPosition -= increment;
                }
                DPadLeft = true;
            }
        } else {
            DPadLeft = false;
        }

        if (gamepad1.dpad_right) { // Move right
            if (!DPadRight) {
                if (gamepad1.left_bumper) { // Left servo
                    LeftPosition += increment;
                }
                if (gamepad1.right_bumper) { // Right servo
                    RightPosition += increment;
                }
                DPadRight = true;
            }
        } else {
            DPadRight = false;
        }

        LeftPosition = Range.clip(LeftPosition, 0, 1);
        RightPosition = Range.clip(RightPosition, 0, 1);

        capBallLeft.setPosition(LeftPosition);
        capBallRight.setPosition(RightPosition);

        telemetry.addData("Position", "L: " + LeftPosition + ", R: " + RightPosition);

    }
}

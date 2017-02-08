package org.firstinspires.ftc.teamcode.robotlibrary;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TeleOpDemoRobot")
public class TeleOpDemoRobot extends OpMode {

    // Specifying motors has been moved to HardwareAccess.class

    // Initial scaling power
    double scalePower = 0.65;

    // Declare left motor
    DcMotor motorLeft;
    // Declare right motor
    DcMotor motorRight;

    TeleOpUtils teleOpUtils;

	/*
	 * Code to run when the op mode is initialized goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {

        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        teleOpUtils = new TeleOpUtils(gamepad1, gamepad2);

	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

        float leftTrigger = gamepad1.left_trigger;

        if (leftTrigger != 0) {
            scalePower = 0.55;
        } if (leftTrigger == 0) {
            scalePower = 0.95;
        }

		/*
		 * Gamepad 1
		 * 
		 * Gamepad 1 controls the motors via the right stick
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
		right = (float)teleOpUtils.scaleInput(right * scalePower);
		left =  (float)teleOpUtils.scaleInput(left * scalePower);

		// write the values to the motors
        setDrivePower((double)left, (double)right);

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
		telemetry.addData("Text", "*** Robot Data***");
		telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
		telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

    /*
    Setting drive power for both motors with one command. Run with setDrivePower() function
     */
    public void setDrivePower(double power1, double power2) {
        motorLeft.setPower(power1);
        motorRight.setPower(power2);
    }

    /*
    Stop all drive motor power in one simple command
     */
    public void stopDriveMotors(){
        setDrivePower(0, 0);
    }


}
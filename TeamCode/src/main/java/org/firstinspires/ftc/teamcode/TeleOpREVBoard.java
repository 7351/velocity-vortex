package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
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

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpREVBoard")
public class TeleOpREVBoard extends OpMode {


    public DcMotor motor_1;
    public Servo servo_1;
    public ColorSensor color;
    double servoPostion1 = 0.0;
    double servoPostion2 = 1;
    double motorPower = 0;


    @Override
    public void init() {

        motor_1 = hardwareMap.dcMotor.get("Motor_1");
        motor_1.setDirection(DcMotor.Direction.REVERSE);
        servo_1 = hardwareMap.servo.get("Servo_1");
        color = hardwareMap.colorSensor.get("Color");
    }

    @Override
    public void loop() {


        /*
         * Controller 1 Controls --------------------------------------------------
         */
        boolean RightStickMovement = gamepad1.right_stick_x != 0 || gamepad1.right_stick_y != 0;
        if (RightStickMovement) { // We prefer the left joystick over the right
            motorPower = ((-gamepad1.right_stick_x) - (gamepad1.right_stick_y));
        }
        motor_1.setPower(motorPower);

        if(gamepad1.a){
            servo_1.setPosition(servoPostion1);
        }
        if (gamepad1.y){
            servo_1.setPosition(servoPostion2);
        }



        //telemetry.addData("Beacon Servo", beaconUtils.BeaconServo.getPosition());
        telemetry.addData("Servo:", servo_1.getPosition());
        telemetry.addData("Motor:", motor_1.getPower());
        telemetry.addData("Color Red:", color.red());
        telemetry.addData("Color Green:", color.green());
        telemetry.addData("Color Blue:", color.blue());
    }
}
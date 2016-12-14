package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Dynamic Signals on 10/21/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpSuperSimple", group = "TeleOp")
public class TeleOpSuperSimple extends OpMode {

    public DcMotor LeftFrontMotor;
    public DcMotor RightFrontMotor;
    public DcMotor LeftBackMotor;
    public DcMotor RightBackMotor;

    @Override
    public void init() {

        LeftFrontMotor = hardwareMap.dcMotor.get("LeftFrontMotor");
        RightFrontMotor = hardwareMap.dcMotor.get("RightFrontMotor");
        LeftBackMotor = hardwareMap.dcMotor.get("LeftBackMotor");
        RightBackMotor = hardwareMap.dcMotor.get("RightBackMotor");

        LeftFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        LeftBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    @Override
    public void loop() {

        LeftFrontMotor.setPower(gamepad1.left_stick_y);
        LeftBackMotor.setPower(gamepad1.left_stick_y);
        RightBackMotor.setPower(gamepad1.right_stick_y);
        RightFrontMotor.setPower(gamepad1.right_stick_y);


    }
}
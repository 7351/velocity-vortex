package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;

/**
 * Created by Dynamic Signals on 12/6/2016.
 */

@Autonomous(name = "EncodeModern", group = "Testing")
public class EncodeDriveModernRobotics extends LinearOpMode {
    DriveTrain driveTrain;

    int target = 500;
    int startpositionRight;
    int startpositionLeft;

    @Override
    public void runOpMode() throws InterruptedException{
        driveTrain = new DriveTrain(hardwareMap);


        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RESET_ENCODERS);
        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RESET_ENCODERS);
        waitOneFullHardwareCycle();
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        startpositionRight = driveTrain.RightFrontMotor.getCurrentPosition();
        startpositionLeft = driveTrain.LeftFrontMotor.getCurrentPosition();

        waitForStart();

        driveTrain.powerLeft(.15);
        driveTrain.powerRight(.15);
        driveTrain.RightFrontMotor.setTargetPosition(target + startpositionRight);
        driveTrain.LeftFrontMotor.setTargetPosition(target + startpositionLeft);

        while (opModeIsActive()){
            telemetry.addData("Left Front Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
            telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
            telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
            telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());

            if ((Math.abs(target - driveTrain.LeftFrontMotor.getCurrentPosition()) <= 10) && Math.abs(target - driveTrain.RightFrontMotor.getCurrentPosition()) <= 10)
            {
                driveTrain.stopRobot();
            }
        }
    }
}
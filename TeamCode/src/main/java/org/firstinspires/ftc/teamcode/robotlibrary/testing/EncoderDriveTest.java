package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.NewEncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.StateMachineOpMode;

/**
 * Created by Dynamic Signals on 12/6/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "EncoderDriveTest", group = "Testing")
public class EncoderDriveTest extends StateMachineOpMode {

    DriveTrain driveTrain;

    @Override
    public void init() {

        driveTrain = new DriveTrain(this);

    }

    /* Encoder drive sequence
     * 1. Reset
     * 2. Set target
     * 3. Set mode to RUN_TO_POSITION
     * 4. Set motor power
     * 5. Wait until motor isn't busy
     */

    @Override
    public void loop() {


        if (stage == 0) {
            NewEncoderDrive.createDrive(this, NewEncoderDrive.DriveMode.HOLONOMIC_Z, 500, 0.45);
        }


        telemetry.addData("Stage", stage);
        telemetry.addData("Left Front Position: ", driveTrain.LeftFrontMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());

    }
}

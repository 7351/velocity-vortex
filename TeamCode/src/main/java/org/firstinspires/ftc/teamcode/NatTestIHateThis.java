package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderDrive;

/**
 * Created by Dynamic Signals on 12/6/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "IHateThis", group = "Testing")
public class NatTestIHateThis extends OpMode {

    DriveTrain driveTrain;

    int stage = 0;

    ElapsedTime time = new ElapsedTime();
    EncoderDrive drive;

    @Override
    public void init() {
        driveTrain = new DriveTrain(hardwareMap);
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

        if (stage == 0){
            if (drive == null)
            {
                drive = new EncoderDrive(driveTrain, 300, 0.75);
                drive.run();
            }
            if (drive.isCompleted()) {
                drive.completed();
                stage++;
            }
        }
        if (stage == 1)
        {
            if (time.time() > 3) {
                driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                stage++;
            }
        }
        if (stage == 2){
            if (drive == null)
            {
                drive = new EncoderDrive(driveTrain, -200, 0.75);
                drive.run();
            }
            if (drive.isCompleted()) {
                drive.completed();
                stage++;
            }
        }


        telemetry.addData("Left Front Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());

        DbgLog.msg(String.valueOf(": " + driveTrain.LeftBackMotor.getCurrentPosition() + " - " + driveTrain.LeftBackMotor.getTargetPosition()));

    }
}
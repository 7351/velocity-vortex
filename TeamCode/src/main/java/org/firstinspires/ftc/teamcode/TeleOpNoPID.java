package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake.IntakeSpec;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TeleOpUtils;

/**
 * Created by Dynamic Signals on 10/21/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpNoPID", group = "TeleOp")
public class TeleOpNoPID extends TeleOp {

    @Override
    public void init() {

        super.init();
        flyWheel.FlyWheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    @Override
    public void loop() {

        super.loop();

    }
}
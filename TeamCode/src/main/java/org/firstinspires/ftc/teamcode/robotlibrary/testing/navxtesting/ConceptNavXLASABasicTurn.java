package org.firstinspires.ftc.teamcode.robotlibrary.testing.navxtesting;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;

/**
 * Created by Dynamic Signals on 2/26/2017.
 */

@Autonomous(name = "ConceptNavXLASABasicTurn")
public class ConceptNavXLASABasicTurn extends OpMode {

    DriveTrain driveTrain;
    AHRS navx;
    int stage = 0;

    double drivePower = 0.05;

    BasicGyroTurn gyroTurn;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        navx = AHRS.getInstance(hardwareMap);

    }

    @Override
    public void loop() {

        if (stage == 0) {
            if (!navx.isCalibrating()) {
                navx.zeroYaw();
                stage++;
            }
        }

        if (stage == 1) {
            if (gyroTurn == null) {
                gyroTurn = new BasicGyroTurn(driveTrain, navx, 90);
            }
            gyroTurn.run();
            if (gyroTurn.isCompleted()) {
                stage++;
            }
            telemetry.addData("Degrees left", gyroTurn.detail.degreesOff);
            telemetry.addData("Direction", gyroTurn.detail.turnDirection.toString().toLowerCase());
            telemetry.addData("Power", "L: " + driveTrain.LeftBackMotor.getPower() + " , R: " + driveTrain.RightBackMotor.getPower());
        }

        telemetry.addData("Stage", stage);

    }
}

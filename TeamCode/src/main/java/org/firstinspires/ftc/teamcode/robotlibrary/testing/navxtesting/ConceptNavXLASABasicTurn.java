package org.firstinspires.ftc.teamcode.robotlibrary.testing.navxtesting;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

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

    GyroUtils.GyroDetail detail;
    double drivePower = 0.3;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        navx = AHRS.getInstance(hardwareMap);
        detail = new GyroUtils.GyroDetail(navx);

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
            detail.setData(90);
            if (detail.degreesOff > 2) {
                double leftPower = (detail.turnDirection.equals(GyroUtils.Direction.COUNTERCLOCKWISE)) ? -drivePower : drivePower;
                driveTrain.powerLeft(leftPower);
                driveTrain.powerRight(-leftPower);
            }
        }

    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TBDName;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Dynamic Signals on 11/10/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group = "Autonomous")
public class Autonomous extends OpMode {

    TBDName tbdName;

    private int stage = -1;
    private ElapsedTime time = new ElapsedTime();
    private ElapsedTime delayTime = new ElapsedTime();

    private double flyWheelLaunchPower = 0.4;

    private double delay;
    private String alliance;
    private int shoot;
    private String target;

    @Override
    public void init() {

        tbdName = new TBDName(hardwareMap, telemetry, false);

        delay = tbdName.das.getNumberDouble("delay", 0); // 0 default wait time
        alliance = tbdName.das.getRadio("alliance", "ns"); // Not selected - ns
        shoot = tbdName.das.getNumberInt("shoot", 2); // We want to shoot twice by default
        target = tbdName.das.getRadio("target", "ns"); // Not selected - ns
    }

    @Override
    public void init_loop() {

        Iterator it = tbdName.das.getSelectorChoices().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            telemetry.addData((String) pair.getKey(), pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

    }

    @Override
    public void start() {

        tbdName.start();
        time.reset();
        delayTime.reset();

    }

    @Override
    public void loop() {

        if (stage == -1) {
            if (delayTime.time() > delay) {
                stage++;
            }
        }
        if (stage == 0) {
            if (!tbdName.gyroUtils.gyro.isCalibrating()) {
                stage++;
                time.reset();
            }
            telemetry.addData("Calibrating", String.valueOf(tbdName.gyroUtils.gyro.isCalibrating()));
        }
        if (alliance.equals("Red")) {
            if (target.equals("Beacon")) {

            }
            if (target.equals("Cap ball")) {

            }
        }
        if (alliance.equals("Blue")) {
            if (target.equals("Beacon")) {

            }
            if (target.equals("Cap ball")) {

            }
        }


        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(tbdName.gyroUtils.gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));
        telemetry.addData("Beacon Color: ", String.valueOf(tbdName.colorUtils.beaconColor()));

    }
}

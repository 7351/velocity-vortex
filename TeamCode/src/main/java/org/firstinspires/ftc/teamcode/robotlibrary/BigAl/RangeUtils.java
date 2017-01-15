package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Leo on 1/15/2017.
 */

public class RangeUtils {

    public ModernRoboticsI2cRangeSensor rangeSensor;

    public RangeUtils(HardwareMap hardwareMap) {

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "beaconRange");

    }

    /*
     * Due to the weird value spikes that happen in this sensor, this function cleans them out.
     * -1 will be the distance that indicates a spike.
     */
    public double getDistance(DistanceUnit unit) {
        double rawDistance = rangeSensor.getDistance(unit);
        double distance = -1;
        switch (unit) {
            case INCH:
                if (rawDistance < 95) distance = rawDistance;
                break;
            case CM:
                if (rawDistance < 230) distance = rawDistance;
                break;
            default:
                break;
        }
        return distance;
    }
}

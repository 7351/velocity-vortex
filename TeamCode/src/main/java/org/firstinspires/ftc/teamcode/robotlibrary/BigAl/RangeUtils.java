package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Dynamic Signals on 1/15/2017.
 */

public class RangeUtils {

    public ModernRoboticsI2cRangeSensor rangeSensor;

    public RangeUtils(HardwareMap hardwareMap) {

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "beaconRange");

    }

    /*
     * Due to the weird value spikes that happen in this sensor, this function cleans them out.
     * You have to pass it a number to indicate a sensor error
     */
    public double getDistance(DistanceUnit unit, int randomValueNumber) {
        double rawDistance = rangeSensor.getDistance(unit);
        double distance = randomValueNumber;
        switch (unit) {
            case INCH:
                if (rawDistance < 95) distance = rawDistance;
                break;
            case CM:
                if (rawDistance < 230) distance = rawDistance;
                break;
        }
        return distance;
    }

    public double getDistance(DistanceUnit unit) {
        return getDistance(unit, -1);
    }
}

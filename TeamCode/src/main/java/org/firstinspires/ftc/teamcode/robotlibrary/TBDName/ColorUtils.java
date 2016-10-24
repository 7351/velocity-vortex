package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Dynamic Signals on 10/23/2016.
 */

public class ColorUtils {

    private HardwareMap hardwareMap;

    public ColorSensor bottomColorSensor;

    public ColorUtils(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        bottomColorSensor = hardwareMap.colorSensor.get("bottomColorSensor");
        bottomColorSensor.enableLed(false);
    }

    public boolean aboveWhiteLine() {
        boolean returnValue = false;
        if ((bottomColorSensor.red() >= 2) && (bottomColorSensor.green() >= 2) && (bottomColorSensor.blue() >= 2)) {
            returnValue = true;
        }
        return returnValue;
    }

    public boolean aboveBlueLine() {
        boolean returnValue = false;
        if ((bottomColorSensor.blue() > bottomColorSensor.red()) && (bottomColorSensor.blue() > bottomColorSensor.green())) {
            returnValue = true;
        }
        return returnValue;
    }

    public boolean aboveRedLine() {
        boolean returnValue = false;
        if ((bottomColorSensor.red() > bottomColorSensor.green() + 2) && (bottomColorSensor.red() > bottomColorSensor.blue() + 2)) {
            returnValue = true;
        }
        return returnValue;
    }


}

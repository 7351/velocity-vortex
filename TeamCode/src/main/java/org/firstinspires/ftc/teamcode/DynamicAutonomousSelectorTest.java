package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DynamicAutonomousSelector;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TBDName;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Leo on 10/8/2016.
 */

@Autonomous(name = "SelectorTest", group = "concept")
public class DynamicAutonomousSelectorTest extends OpMode {

    HashMap<String, String> selectorHashmap;

    DynamicAutonomousSelector das;

    @Override
    public void init() {

        das = new DynamicAutonomousSelector();
        selectorHashmap = das.getSelectorChoices();

    }

    @Override
    public void loop() {

        Iterator it = selectorHashmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            telemetry.addData((String) pair.getKey(), pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

    }

}

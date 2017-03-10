package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DynamicAutonomousSelector;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Leo on 10/8/2016.
 */

@Autonomous(name = "SelectorTest", group = "concept")
public class DynamicAutonomousSelectorTest extends OpMode {

    DynamicAutonomousSelector das;

    @Override
    public void init() {

        das = new DynamicAutonomousSelector(true);
    }

    @Override
    public void loop() {

        das.displayTelemetry(this);

    }

}

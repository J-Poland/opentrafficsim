package org.opentrafficsim.demo.web.circularroad;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.DurationUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.base.modelproperties.BooleanProperty;
import org.opentrafficsim.base.modelproperties.CompoundProperty;
import org.opentrafficsim.base.modelproperties.ContinuousProperty;
import org.opentrafficsim.base.modelproperties.IntegerProperty;
import org.opentrafficsim.base.modelproperties.ProbabilityDistributionProperty;
import org.opentrafficsim.base.modelproperties.Property;
import org.opentrafficsim.base.modelproperties.SelectionProperty;
import org.opentrafficsim.core.dsol.OTSSimulatorInterface;
import org.opentrafficsim.road.modelproperties.IDMPropertySet;
import org.opentrafficsim.simulationengine.SimpleAnimator;

import nl.tudelft.simulation.dsol.jetty.test.sse.DSOLWebServer;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class CircularRoadlWebApplication extends DSOLWebServer
{
    /**
     * @param title the tile for the model
     * @param simulator the simulator
     * @throws Exception on jetty error
     */
    public CircularRoadlWebApplication(final String title, final OTSSimulatorInterface simulator) throws Exception
    {
        super(title, simulator, new Rectangle2D.Double(-400, -400, 800, 800));
    }

    /**
     * @param args String[]; arguments, expected to be empty
     * @throws Exception on error
     */
    public static void main(final String[] args) throws Exception
    {
        List<Property<?>> properties = new ArrayList<>();
        properties.add(new ProbabilityDistributionProperty("TrafficComposition", "Traffic composition",
                "<html>Mix of passenger cars and trucks</html>", new String[] { "passenger car", "truck" },
                new Double[] { 0.8, 0.2 }, false, 10));
        properties.add(new SelectionProperty("CarFollowingModel", "Car following model",
                "<html>The car following model determines " + "the acceleration that a vehicle will make taking into account "
                        + "nearby vehicles, infrastructural restrictions (e.g. speed limit, "
                        + "curvature of the road) capabilities of the vehicle and personality " + "of the driver.</html>",
                new String[] { "IDM", "IDM+" }, 1, false, 1));
        properties.add(
                IDMPropertySet.makeIDMPropertySet("IDMCar", "Car", new Acceleration(1.0, AccelerationUnit.METER_PER_SECOND_2),
                        new Acceleration(1.5, AccelerationUnit.METER_PER_SECOND_2), new Length(2.0, LengthUnit.METER),
                        new Duration(1.0, DurationUnit.SECOND), 2));
        properties.add(IDMPropertySet.makeIDMPropertySet("IDMTruck", "Truck",
                new Acceleration(0.5, AccelerationUnit.METER_PER_SECOND_2),
                new Acceleration(1.25, AccelerationUnit.METER_PER_SECOND_2), new Length(2.0, LengthUnit.METER),
                new Duration(1.0, DurationUnit.SECOND), 3));
        properties.add(new SelectionProperty("LaneChanging", "Lane changing",
                "<html>The lane change strategies vary in politeness.<br>"
                        + "Two types are implemented:<ul><li>Egoistic (looks only at personal gain).</li>"
                        + "<li>Altruistic (assigns effect on new and current follower the same weight as "
                        + "the personal gain).</html>",
                new String[] { "Egoistic", "Altruistic" }, 0, false, 500));
        properties.add(new SelectionProperty("TacticalPlanner", "Tactical planner",
                "<html>The tactical planner determines if a lane change is desired and possible.</html>",
                new String[] { "IDM", "MOBIL/IDM", "DIRECTED/IDM", "LMRS", "Toledo" }, 1, false, 600));
        properties.add(new IntegerProperty("TrackLength", "Track length", "Circumference of the track", 2000, 500, 6000,
                "Track length %dm", false, 10));
        properties.add(new ContinuousProperty("MeanDensity", "Mean density", "Number of vehicles per km", 40.0, 5.0, 45.0,
                "Density %.1f veh/km", false, 11));
        properties.add(new ContinuousProperty("DensityVariability", "Density variability",
                "Variability of the number of vehicles per km", 0.0, 0.0, 1.0, "%.1f", false, 12));
        List<Property<?>> outputProperties = new ArrayList<>();
        for (int lane = 1; lane <= 2; lane++)
        {
            int index = lane - 1;
            String laneId = String.format("Lane %d ", lane);
            outputProperties.add(index, new BooleanProperty(laneId + "Density", laneId + " Density",
                    laneId + "Density contour plot", true, false, 0));
            index += lane;
            outputProperties.add(index,
                    new BooleanProperty(laneId + "Flow", laneId + " Flow", laneId + "Flow contour plot", true, false, 1));
            index += lane;
            outputProperties.add(index,
                    new BooleanProperty(laneId + "Speed", laneId + " Speed", laneId + "Speed contour plot", true, false, 2));
            index += lane;
            outputProperties.add(index, new BooleanProperty(laneId + "Acceleration", laneId + " Acceleration",
                    laneId + "Acceleration contour plot", true, false, 3));
            index += lane;
            outputProperties.add(index, new BooleanProperty(laneId + "Trajectories", laneId + " Trajectories",
                    laneId + "Trajectory (time/distance) diagram", true, false, 4));
            // index += lane;
            // outputProperties.add(index, new BooleanProperty(laneId + "Variable Sample Rate Trajectories",
            // laneId + " VSR Trajectories", laneId + "Trajectory (time/distance) diagram", true, false, 5));
        }
        outputProperties.add(new BooleanProperty("Fundamental diagram aggregated", "Fundamental diagram aggregated",
                "Fundamental diagram aggregated", true, false, 5));
        outputProperties
                .add(new BooleanProperty("Fundamental diagram", "Fundamental diagram", "Fundamental diagram", true, false, 5));
        properties.add(new CompoundProperty("OutputGraphs", "Output graphs", "Select the graphical output", outputProperties,
                true, 1000));

        CircularRoadModel model = new CircularRoadModel(properties);
        SimpleAnimator simulator = new SimpleAnimator(Time.ZERO, Duration.ZERO, new Duration(1.0, DurationUnit.HOUR), model);
        new CircularRoadlWebApplication("Circular Road model", simulator);
    }

}
package org.opentrafficsim.imb.demo;

import static org.opentrafficsim.core.gtu.GTUType.CAR;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.naming.NamingException;
import javax.swing.SwingUtilities;

import org.djunits.unit.DurationUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.UNITS;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.djunits.value.vdouble.scalar.Time;
import org.djutils.exceptions.Throw;
import org.jgrapht.GraphPath;
import org.opentrafficsim.base.modelproperties.CompoundProperty;
import org.opentrafficsim.base.modelproperties.ProbabilityDistributionProperty;
import org.opentrafficsim.core.compatibility.Compatible;
import org.opentrafficsim.core.dsol.OTSModelInterface;
import org.opentrafficsim.core.dsol.OTSSimulationException;
import org.opentrafficsim.core.dsol.OTSSimulatorInterface;
import org.opentrafficsim.core.geometry.OTSGeometryException;
import org.opentrafficsim.core.geometry.OTSPoint3D;
import org.opentrafficsim.core.graphs.AbstractPlot;
import org.opentrafficsim.core.graphs.ContourDataSource;
import org.opentrafficsim.core.graphs.ContourPlotAcceleration;
import org.opentrafficsim.core.graphs.ContourPlotDensity;
import org.opentrafficsim.core.graphs.ContourPlotFlow;
import org.opentrafficsim.core.graphs.ContourPlotSpeed;
import org.opentrafficsim.core.graphs.TrajectoryPlot;
import org.opentrafficsim.core.graphs.imb.GraphTransceiver;
import org.opentrafficsim.core.gtu.GTUDirectionality;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.core.gtu.GTUType;
import org.opentrafficsim.core.gtu.RelativePosition;
import org.opentrafficsim.core.gtu.colorer.GTUColorer;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.core.network.OTSNetwork;
import org.opentrafficsim.core.network.OTSNode;
import org.opentrafficsim.graphs.GraphLaneUtil;
import org.opentrafficsim.imb.IMBException;
import org.opentrafficsim.imb.connector.OTSIMBConnector;
import org.opentrafficsim.imb.transceiver.urbanstrategy.GTUTransceiver;
import org.opentrafficsim.imb.transceiver.urbanstrategy.LaneGTUTransceiver;
import org.opentrafficsim.imb.transceiver.urbanstrategy.LinkGTUTransceiver;
import org.opentrafficsim.imb.transceiver.urbanstrategy.NetworkTransceiver;
import org.opentrafficsim.imb.transceiver.urbanstrategy.NodeTransceiver;
import org.opentrafficsim.imb.transceiver.urbanstrategy.SensorGTUTransceiver;
import org.opentrafficsim.imb.transceiver.urbanstrategy.SimulatorTransceiver;
import org.opentrafficsim.kpi.sampling.KpiLaneDirection;
import org.opentrafficsim.road.gtu.colorer.DefaultCarAnimation;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.gtu.lane.LaneBasedIndividualGTU;
import org.opentrafficsim.road.gtu.lane.tactical.LaneBasedCFLCTacticalPlannerFactory;
import org.opentrafficsim.road.gtu.lane.tactical.following.GTUFollowingModelOld;
import org.opentrafficsim.road.gtu.lane.tactical.following.IDMOld;
import org.opentrafficsim.road.gtu.lane.tactical.following.IDMPlusFactory;
import org.opentrafficsim.road.gtu.lane.tactical.following.IDMPlusOld;
import org.opentrafficsim.road.gtu.lane.tactical.lanechangemobil.AbstractLaneChangeModel;
import org.opentrafficsim.road.gtu.lane.tactical.lanechangemobil.Altruistic;
import org.opentrafficsim.road.gtu.lane.tactical.lanechangemobil.Egoistic;
import org.opentrafficsim.road.gtu.lane.tactical.lmrs.DefaultLMRSPerceptionFactory;
import org.opentrafficsim.road.gtu.lane.tactical.lmrs.LMRSFactory;
import org.opentrafficsim.road.gtu.lane.tactical.toledo.ToledoFactory;
import org.opentrafficsim.road.gtu.strategical.LaneBasedStrategicalPlanner;
import org.opentrafficsim.road.gtu.strategical.LaneBasedStrategicalPlannerFactory;
import org.opentrafficsim.road.gtu.strategical.route.LaneBasedStrategicalRoutePlannerFactory;
import org.opentrafficsim.road.modelproperties.IDMPropertySet;
import org.opentrafficsim.road.network.animation.SensorAnimation;
import org.opentrafficsim.road.network.factory.LaneFactory;
import org.opentrafficsim.road.network.lane.CrossSectionElement;
import org.opentrafficsim.road.network.lane.DirectedLanePosition;
import org.opentrafficsim.road.network.lane.Lane;
import org.opentrafficsim.road.network.lane.LaneDirection;
import org.opentrafficsim.road.network.lane.LaneType;
import org.opentrafficsim.road.network.lane.object.LaneBasedObject;
import org.opentrafficsim.road.network.lane.object.sensor.AbstractSensor;
import org.opentrafficsim.road.network.sampling.RoadSampler;
import org.opentrafficsim.simulationengine.SimpleAnimator;
import org.opentrafficsim.swing.gui.AbstractOTSSwingApplication;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameter;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterBoolean;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterDouble;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterException;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterInteger;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterSelectionList;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.swing.gui.TablePanel;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * Circular road simulation demo.
 * <p>
 * Copyright (c) 2013-2018 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2016-08-24 13:50:36 +0200 (Wed, 24 Aug 2016) $, @version $Revision: 2144 $, by $Author: pknoppers $,
 * initial version 21 nov. 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class CircularRoadIMB extends AbstractOTSSwingApplication implements UNITS
{
    /** */
    private static final long serialVersionUID = 1L;

    /** The model. */
    private RoadSimulationModelIMB model;

    /**
     * Create a CircularRoad simulation.
     * @throws InputParameterException if key for a property used more than once
     */
    public CircularRoadIMB() throws InputParameterException
    {
        this.inputParameterMap.add(new InputParameterSelectionList("LaneChanging", "Lane changing",
                "<html>The lane change strategies vary in politeness.<br>"
                        + "Two types are implemented:<ul><li>Egoistic (looks only at personal gain).</li>"
                        + "<li>Altruistic (assigns effect on new and current follower the same weight as "
                        + "the personal gain).</html>",
                new String[] { "Egoistic", "Altruistic" }, 0, false, 500));
        this.inputParameterMap.add(new InputParameterSelectionList("TacticalPlanner", "Tactical planner",
                "<html>The tactical planner determines if a lane change is desired and possible.</html>",
                new String[] { "MOBIL", "LMRS", "Toledo" }, 0, false, 600));
        this.inputParameterMap.add(new InputParameterInteger("TrackLength", "Track length", "Circumference of the track", 2000,
                500, 6000, "Track length %dm", false, 10));
        this.inputParameterMap.add(new InputParameterDouble("MeanDensity", "Mean density", "Number of vehicles per km", 40.0,
                5.0, 45.0, "Density %.1f veh/km", false, 11));
        this.inputParameterMap.add(new InputParameterDouble("DensityVariability", "Density variability",
                "Variability of the number of vehicles per km", 0.0, 0.0, 1.0, "%.1f", false, 12));
        List<InputParameter<?, ?>> outputProperties = new ArrayList<>();
        for (int lane = 1; lane <= 2; lane++)
        {
            String laneId = String.format("Lane %d ", lane);
            outputProperties.add(new InputParameterBoolean(laneId + "Density", laneId + " Density",
                    laneId + "Density contour plot", true, false, 0));
            outputProperties.add(
                    new InputParameterBoolean(laneId + "Flow", laneId + " Flow", laneId + "Flow contour plot", true, false, 1));
            outputProperties.add(new InputParameterBoolean(laneId + "Speed", laneId + " Speed", laneId + "Speed contour plot",
                    true, false, 2));
            outputProperties.add(new InputParameterBoolean(laneId + "Acceleration", laneId + " Acceleration",
                    laneId + "Acceleration contour plot", true, false, 3));
            outputProperties.add(new InputParameterBoolean(laneId + "Trajectories", laneId + " Trajectories",
                    laneId + "Trajectory (time/distance) diagram", true, false, 4));
        }
        this.inputParameterMap.add(new CompoundProperty("OutputGraphs", "Output graphs", "Select the graphical output",
                outputProperties, true, 1000));
    }

    /** {@inheritDoc} */
    @Override
    public final void stopTimersThreads()
    {
        super.stopTimersThreads();
        this.model = null;
    }

    /**
     * Main program.
     * @param args String[]; the command line arguments (not used)
     * @throws SimRuntimeException should never happen
     */
    public static void main(final String[] args) throws SimRuntimeException
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    CircularRoadIMB circularRoad = new CircularRoadIMB();
                    List<InputParameter<?, ?>> propertyList = circularRoad.getProperties();
                    try
                    {
                        propertyList.add(new ProbabilityDistributionProperty("TrafficComposition", "Traffic composition",
                                "<html>Mix of passenger cars and trucks</html>", new String[] { "passenger car", "truck" },
                                new Double[] { 0.8, 0.2 }, false, 10));
                    }
                    catch (InputParameterException exception)
                    {
                        exception.printStackTrace();
                    }
                    propertyList.add(new InputParameterSelectionList("CarFollowingModel", "Car following model",
                            "<html>The car following model determines "
                                    + "the acceleration that a vehicle will make taking into account "
                                    + "nearby vehicles, infrastructural restrictions (e.g. speed limit, "
                                    + "curvature of the road) capabilities of the vehicle and personality "
                                    + "of the driver.</html>",
                            new String[] { "IDM", "IDM+" }, 1, false, 1));
                    propertyList.add(IDMPropertySet.makeIDMPropertySet("IDMCar", "Car",
                            new Acceleration(1.0, METER_PER_SECOND_2), new Acceleration(1.5, METER_PER_SECOND_2),
                            new Length(2.0, METER), new Duration(1.0, SECOND), 2));
                    propertyList.add(IDMPropertySet.makeIDMPropertySet("IDMTruck", "Truck",
                            new Acceleration(0.5, METER_PER_SECOND_2), new Acceleration(1.25, METER_PER_SECOND_2),
                            new Length(2.0, METER), new Duration(1.0, SECOND), 3));

                    circularRoad.buildAnimator(Time.ZERO, Duration.ZERO, new Duration(3600.0, SECOND), propertyList, null,
                            true);
                }
                catch (SimRuntimeException | NamingException | OTSSimulationException | InputParameterException exception)
                {
                    exception.printStackTrace();
                }
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    protected final OTSModelInterface makeModel()
    {
        System.out.println("CircularRoadIMB.makeModel called");
        this.model = new RoadSimulationModelIMB(getSavedUserModifiedProperties(), getColorer(),
                new OTSNetwork("circular road simulation network"));
        return this.model;
    }

    /**
     * @return the saved user properties for a next run
     */
    private List<InputParameter<?, ?>> getSavedUserModifiedProperties()
    {
        return this.savedUserModifiedProperties;
    }

    /** {@inheritDoc} */
    @Override
    protected final Rectangle2D.Double makeAnimationRectangle()
    {
        return new Rectangle2D.Double(-350, -350, 700, 700);
    }

    /** {@inheritDoc} */
    @Override
    protected final void addTabs(final OTSSimulatorInterface simulator) throws OTSSimulationException, InputParameterException
    {
        // Make the tab with the plots
        InputParameter<?, ?> output = new CompoundProperty("", "", "", this.properties, false, 0).findByKey("OutputGraphs");
        if (null == output)
        {
            throw new Error("Cannot find output properties");
        }
        ArrayList<InputParameterBoolean> graphs = new ArrayList<>();
        if (output instanceof CompoundProperty)
        {
            CompoundProperty outputProperties = (CompoundProperty) output;
            for (InputParameter<?, ?> ap : outputProperties.getValue())
            {
                if (ap instanceof InputParameterBoolean)
                {
                    InputParameterBoolean bp = (InputParameterBoolean) ap;
                    if (bp.getValue())
                    {
                        graphs.add(bp);
                    }
                }
            }
        }
        else
        {
            throw new Error("output properties should be compound");
        }

        int graphCount = graphs.size();
        int columns = (int) Math.ceil(Math.sqrt(graphCount));
        int rows = 0 == columns ? 0 : (int) Math.ceil(graphCount * 1.0 / columns);
        TablePanel charts = new TablePanel(columns, rows);

        GraphPath<KpiLaneDirection> path01;
        GraphPath<KpiLaneDirection> path0;
        GraphPath<KpiLaneDirection> path1;
        try
        {
            List<String> names = new ArrayList<>();
            names.add("Left lane");
            names.add("Right lane");
            List<LaneDirection> start = new ArrayList<>();
            start.add(new LaneDirection(this.model.getPath(0).get(0), GTUDirectionality.DIR_PLUS));
            start.add(new LaneDirection(this.model.getPath(1).get(0), GTUDirectionality.DIR_PLUS));
            path01 = GraphLaneUtil.createPath(names, start);
            path0 = GraphLaneUtil.createPath(names.get(0), start.get(0));
            path1 = GraphLaneUtil.createPath(names.get(1), start.get(1));
        }
        catch (NetworkException exception)
        {
            throw new RuntimeException("Could not create a path as a lane has no set speed limit.", exception);
        }
        RoadSampler sampler = new RoadSampler(simulator);
        ContourDataSource dataPool0 = new ContourDataSource(sampler, path0);
        ContourDataSource dataPool1 = new ContourDataSource(sampler, path1);
        Duration updateInterval = Duration.createSI(10.0);

        for (int i = 0; i < graphCount; i++)
        {
            String graphName = graphs.get(i).getKey();
            AbstractPlot plot = null;
            GraphPath<KpiLaneDirection> path = null;
            ContourDataSource dataPool = null;
            if (!graphName.contains("Fundamental diagram"))
            {
                int pos = graphName.indexOf(' ') + 1;
                String laneNumberText = graphName.substring(pos, pos + 1);
                int lane = Integer.parseInt(laneNumberText) - 1;
                path = lane == 0 ? path01 : path1;
                dataPool = lane == 0 ? dataPool0 : dataPool1;
            }

            if (graphName.contains("Trajectories"))
            {
                plot = new TrajectoryPlot(graphName, updateInterval, simulator, sampler, path);
            }
            else
            {
                if (graphName.contains("Density"))
                {
                    plot = new ContourPlotDensity(graphName, simulator, dataPool);
                }
                else if (graphName.contains("Speed"))
                {
                    plot = new ContourPlotSpeed(graphName, simulator, dataPool);
                }
                else if (graphName.contains("Flow"))
                {
                    plot = new ContourPlotFlow(graphName, simulator, dataPool);
                }
                else if (graphName.contains("Acceleration"))
                {
                    plot = new ContourPlotAcceleration(graphName, simulator, dataPool);
                }
                else
                {
                    throw new Error("Unhandled type of contourplot: " + graphName);
                }
            }
            // Add the container to the matrix
            charts.setCell(plot.getContentPane(), i % columns, i / columns);

            // Publish all the graphs to IMB at the moment, at a 640x480 resolution, every 5 seconds.
            try
            {
                new GraphTransceiver(this.model.imbConnector, simulator, this.model.getNetwork(), 640, 480, plot,
                        new Duration(5.0, DurationUnit.SECOND));
            }
            catch (IMBException exception)
            {
                exception.printStackTrace();
            }
        }
        addTab(getTabCount(), "statistics", charts);
    }

    /** {@inheritDoc} */
    @Override
    public final String shortName()
    {
        return "Circular Road simulation";
    }

    /** {@inheritDoc} */
    @Override
    public final String description()
    {
        return "<html><h1>Circular Road simulation</h1>" + "Vehicles are unequally distributed over a two lane ring road.<br>"
                + "When simulation starts, all vehicles begin driving, some lane changes will occurr and some "
                + "shockwaves should develop.<br>"
                + "Trajectories and contourplots are generated during the simulation for both lanes.</html>";
    }

}

/**
 * Simulate traffic on a circular, two-lane road.
 * <p>
 * Copyright (c) 2013-2018 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2016-08-24 13:50:36 +0200 (Wed, 24 Aug 2016) $, @version $Revision: 2144 $, by $Author: pknoppers $,
 * initial version 1 nov. 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
class RoadSimulationModelIMB extends AbstractOTSModel implements UNITS
{
    /** */
    private static final long serialVersionUID = 20141121L;

    /** The simulator. */
    private OTSSimulatorInterface simulator;

    /** Number of cars created. */
    private int carsCreated = 0;

    /** The car following model, e.g. IDM Plus for cars. */
    private GTUFollowingModelOld carFollowingModelCars;

    /** The car following model, e.g. IDM Plus for trucks. */
    private GTUFollowingModelOld carFollowingModelTrucks;

    /** The probability that the next generated GTU is a passenger car. */
    private double carProbability;

    /** The lane change model. */
    private AbstractLaneChangeModel laneChangeModel;

    /** Minimum distance. */
    private Length minimumDistance = new Length(0, METER);

    /** The speed limit. */
    private Speed speedLimit = new Speed(100, KM_PER_HOUR);

    /** User settable properties. */
    private List<InputParameter<?, ?>> properties = null;

    /** The sequence of Lanes that all vehicles will follow. */
    private List<List<Lane>> paths = new ArrayList<>();

    /** The random number generator used to decide what kind of GTU to generate. */
    private StreamInterface randomGenerator = new MersenneTwister(12345);

    /** The GTUColorer for the generated vehicles. */
    private final GTUColorer gtuColorer;

    /** Strategical planner generator for cars. */
    private LaneBasedStrategicalPlannerFactory<LaneBasedStrategicalPlanner> strategicalPlannerGeneratorCars = null;

    /** Strategical planner generator for cars. */
    private LaneBasedStrategicalPlannerFactory<LaneBasedStrategicalPlanner> strategicalPlannerGeneratorTrucks = null;

    /** the network as created by the AbstractWrappableIMBAnimation. */
    private final OTSNetwork network;

    /** Connector to the IMB hub. */
    OTSIMBConnector imbConnector;

    /**
     * @param properties List&lt;Property&lt;?&gt;&gt;; the properties
     * @param gtuColorer GTUColorer; the default and initial GTUColorer, e.g. a DefaultSwitchableTUColorer.
     * @param network OTSNetwork; the network
     */
    RoadSimulationModelIMB(final List<InputParameter<?, ?>> properties, final GTUColorer gtuColorer, final OTSNetwork network)
    {
        this.properties = properties;
        this.gtuColorer = gtuColorer;
        this.network = network;
    }

    /** {@inheritDoc} */
    @Override
    public final OTSNetwork getNetwork()
    {
        return this.network;
    }

    /**
     * @param index int; the rank number of the path
     * @return List&lt;Lane&gt;; the set of lanes for the specified index
     */
    public List<Lane> getPath(final int index)
    {
        return this.paths.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel()
            throws SimRuntimeException
    {
        System.out.println("CircularRoadIMB: constructModel called; Connecting to IMB");
        SimpleAnimator imbAnimator = (SimpleAnimator) theSimulator;
        try
        {
            CompoundProperty imbSettings = null;
            for (InputParameter<?, ?> property : this.properties)
            {
                if (property.getKey().equals(OTSIMBConnector.PROPERTY_KEY))
                {
                    imbSettings = (CompoundProperty) property;
                }
            }
            Throw.whenNull(imbSettings, "IMB Settings not found in properties");
            this.imbConnector = OTSIMBConnector.create(imbSettings, "OTS");
            new NetworkTransceiver(this.imbConnector, imbAnimator, this.network);
            new NodeTransceiver(this.imbConnector, imbAnimator, this.network);
            new LinkGTUTransceiver(this.imbConnector, imbAnimator, this.network);
            new LaneGTUTransceiver(this.imbConnector, imbAnimator, this.network);
            new GTUTransceiver(this.imbConnector, imbAnimator, this.network);
            new SensorGTUTransceiver(this.imbConnector, imbAnimator, this.network);
            new SimulatorTransceiver(this.imbConnector, imbAnimator);
        }
        catch (IMBException exception)
        {
            throw new SimRuntimeException(exception);
        }

        final int laneCount = 2;
        for (int laneIndex = 0; laneIndex < laneCount; laneIndex++)
        {
            this.paths.add(new ArrayList<Lane>());
        }
        this.simulator = (OTSSimulatorInterface) theSimulator;
        double radius = 6000 / 2 / Math.PI;
        double headway = 40;
        double headwayVariability = 0;
        try
        {
            // Get car-following model name
            String carFollowingModelName = null;
            CompoundProperty propertyContainer = new CompoundProperty("", "", "", this.properties, false, 0);
            InputParameter<?, ?> cfmp = propertyContainer.findByKey("CarFollowingModel");
            if (null == cfmp)
            {
                throw new Error("Cannot find \"Car following model\" property");
            }
            if (cfmp instanceof InputParameterSelectionList)
            {
                carFollowingModelName = ((InputParameterSelectionList) cfmp).getValue();
            }
            else
            {
                throw new Error("\"Car following model\" property has wrong type");
            }

            // Get car-following model parameter
            for (InputParameter<?, ?> ap : new CompoundProperty("", "", "", this.properties, false, 0))
            {
                if (ap instanceof CompoundProperty)
                {
                    CompoundProperty cp = (CompoundProperty) ap;
                    // System.out.println("Checking compound property " + cp);
                    if (ap.getKey().contains("IDM"))
                    {
                        System.out.println("Car following model name appears to be " + ap.getKey());
                        Acceleration a = IDMPropertySet.getA(cp);
                        Acceleration b = IDMPropertySet.getB(cp);
                        Length s0 = IDMPropertySet.getS0(cp);
                        Duration tSafe = IDMPropertySet.getTSafe(cp);
                        GTUFollowingModelOld gtuFollowingModel = null;
                        if (carFollowingModelName.equals("IDM"))
                        {
                            gtuFollowingModel = new IDMOld(a, b, s0, tSafe, 1.0);
                        }
                        else if (carFollowingModelName.equals("IDM+"))
                        {
                            gtuFollowingModel = new IDMPlusOld(a, b, s0, tSafe, 1.0);
                        }
                        else
                        {
                            throw new Error("Unknown gtu following model: " + carFollowingModelName);
                        }
                        if (ap.getKey().contains("Car"))
                        {
                            this.carFollowingModelCars = gtuFollowingModel;
                        }
                        else if (ap.getKey().contains("Truck"))
                        {
                            this.carFollowingModelTrucks = gtuFollowingModel;
                        }
                        else
                        {
                            throw new Error("Cannot determine gtu type for " + ap.getKey());
                        }
                    }
                }
            }

            // Get lane change model
            cfmp = propertyContainer.findByKey("LaneChanging");
            if (null == cfmp)
            {
                throw new Error("Cannot find \"Lane changing\" property");
            }
            if (cfmp instanceof InputParameterSelectionList)
            {
                String laneChangeModelName = ((InputParameterSelectionList) cfmp).getValue();
                if ("Egoistic".equals(laneChangeModelName))
                {
                    this.laneChangeModel = new Egoistic();
                }
                else if ("Altruistic".equals(laneChangeModelName))
                {
                    this.laneChangeModel = new Altruistic();
                }
                else
                {
                    throw new Error("Lane changing " + laneChangeModelName + " not implemented");
                }
            }
            else
            {
                throw new Error("\"Lane changing\" property has wrong type");
            }

            // Get remaining properties
            for (InputParameter<?, ?> ap : new CompoundProperty("", "", "", this.properties, false, 0))
            {
                if (ap instanceof InputParameterSelectionList)
                {
                    InputParameterSelectionList sp = (InputParameterSelectionList) ap;
                    if ("TacticalPlanner".equals(sp.getKey()))
                    {
                        String tacticalPlannerName = sp.getValue();
                        if ("MOBIL".equals(tacticalPlannerName))
                        {
                            this.strategicalPlannerGeneratorCars = new LaneBasedStrategicalRoutePlannerFactory(
                                    new LaneBasedCFLCTacticalPlannerFactory(this.carFollowingModelCars, this.laneChangeModel));
                            this.strategicalPlannerGeneratorTrucks =
                                    new LaneBasedStrategicalRoutePlannerFactory(new LaneBasedCFLCTacticalPlannerFactory(
                                            this.carFollowingModelTrucks, this.laneChangeModel));
                        }
                        else if ("LMRS".equals(tacticalPlannerName))
                        {
                            // provide default parameters with the car-following model
                            this.strategicalPlannerGeneratorCars = new LaneBasedStrategicalRoutePlannerFactory(new LMRSFactory(
                                    new IDMPlusFactory(this.randomGenerator), new DefaultLMRSPerceptionFactory()));
                            this.strategicalPlannerGeneratorTrucks =
                                    new LaneBasedStrategicalRoutePlannerFactory(new LMRSFactory(
                                            new IDMPlusFactory(this.randomGenerator), new DefaultLMRSPerceptionFactory()));
                        }
                        else if ("Toledo".equals(tacticalPlannerName))
                        {
                            this.strategicalPlannerGeneratorCars =
                                    new LaneBasedStrategicalRoutePlannerFactory(new ToledoFactory());
                            this.strategicalPlannerGeneratorTrucks =
                                    new LaneBasedStrategicalRoutePlannerFactory(new ToledoFactory());
                        }
                        else
                        {
                            throw new Error("Don't know how to create a " + tacticalPlannerName + " tactical planner");
                        }
                    }
                }
                else if (ap instanceof ProbabilityDistributionProperty)
                {
                    ProbabilityDistributionProperty pdp = (ProbabilityDistributionProperty) ap;
                    if (ap.getKey().equals("TrafficComposition"))
                    {
                        this.carProbability = pdp.getValue()[0];
                    }
                }
                else if (ap instanceof InputParameterInteger)
                {
                    InputParameterInteger ip = (InputParameterInteger) ap;
                    if ("TrackLength".equals(ip.getKey()))
                    {
                        radius = ip.getValue() / 2 / Math.PI;
                    }
                }
                else if (ap instanceof InputParameterDouble)
                {
                    InputParameterDouble cp = (InputParameterDouble) ap;
                    if (cp.getKey().equals("MeanDensity"))
                    {
                        headway = 1000 / cp.getValue();
                    }
                    if (cp.getKey().equals("DensityVariability"))
                    {
                        headwayVariability = cp.getValue();
                    }
                }
                else if (ap instanceof CompoundProperty)
                {
                    if (ap.getKey().equals("OutputGraphs"))
                    {
                        continue; // Output settings are handled elsewhere
                    }
                }
            }
            GTUType gtuType = CAR;
            LaneType laneType = LaneType.TWO_WAY_LANE;
            OTSNode start = new OTSNode(this.network, "Start", new OTSPoint3D(radius, 0, 0));
            OTSNode halfway = new OTSNode(this.network, "Halfway", new OTSPoint3D(-radius, 0, 0));

            OTSPoint3D[] coordsHalf1 = new OTSPoint3D[127];
            for (int i = 0; i < coordsHalf1.length; i++)
            {
                double angle = Math.PI * (1 + i) / (1 + coordsHalf1.length);
                coordsHalf1[i] = new OTSPoint3D(radius * Math.cos(angle), radius * Math.sin(angle), 0);
            }
            Lane[] lanes1 = LaneFactory.makeMultiLane(this.network, "FirstHalf", start, halfway, coordsHalf1, laneCount,
                    laneType, this.speedLimit, this.simulator);
            OTSPoint3D[] coordsHalf2 = new OTSPoint3D[127];
            for (int i = 0; i < coordsHalf2.length; i++)
            {
                double angle = Math.PI + Math.PI * (1 + i) / (1 + coordsHalf2.length);
                coordsHalf2[i] = new OTSPoint3D(radius * Math.cos(angle), radius * Math.sin(angle), 0);
            }
            Lane[] lanes2 = LaneFactory.makeMultiLane(this.network, "SecondHalf", halfway, start, coordsHalf2, laneCount,
                    laneType, this.speedLimit, this.simulator);
            for (int laneIndex = 0; laneIndex < laneCount; laneIndex++)
            {
                this.paths.get(laneIndex).add(lanes1[laneIndex]);
                this.paths.get(laneIndex).add(lanes2[laneIndex]);
            }
            int sensorNr = 0;
            for (Lane lane : lanes1)
            {
                new SimpleSilentSensor("sensor " + ++sensorNr, lane, new Length(10.0, LengthUnit.METER), RelativePosition.FRONT,
                        imbAnimator);
            }
            for (Lane lane : lanes2)
            {
                new SimpleSilentSensor("sensor" + ++sensorNr, lane, new Length(20.0, LengthUnit.METER), RelativePosition.REAR,
                        imbAnimator);
            }
            // Put the (not very evenly spaced) cars on the track
            double variability = (headway - 20) * headwayVariability;
            System.out.println("headway is " + headway + " variability limit is " + variability);
            Random random = new Random(12345);
            for (int laneIndex = 0; laneIndex < laneCount; laneIndex++)
            {
                double lane1Length = lanes1[laneIndex].getLength().getSI();
                double trackLength = lane1Length + lanes2[laneIndex].getLength().getSI();
                for (double pos = 0; pos <= trackLength - headway - variability;)
                {
                    Lane lane = pos >= lane1Length ? lanes2[laneIndex] : lanes1[laneIndex];
                    // Actual headway is uniformly distributed around headway
                    double laneRelativePos = pos > lane1Length ? pos - lane1Length : pos;
                    double actualHeadway = headway + (random.nextDouble() * 2 - 1) * variability;
                    // System.out.println(lane + ", len=" + lane.getLength() + ", pos=" + laneRelativePos);
                    generateCar(new Length(laneRelativePos, METER), lane, gtuType);
                    pos += actualHeadway;
                }
            }
        }
        catch (SimRuntimeException | NamingException | NetworkException | GTUException | OTSGeometryException
                | InputParameterException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Generate cars at a fixed rate (implemented by re-scheduling this method).
     * @param initialPosition Length; the initial position of the new cars
     * @param lane Lane; the lane on which the new cars are placed
     * @param gtuType GTUType; the type of the new cars
     * @throws NamingException on ???
     * @throws SimRuntimeException cannot happen
     * @throws NetworkException on network inconsistency
     * @throws GTUException when something goes wrong during construction of the car
     * @throws OTSGeometryException when the initial position is outside the center line of the lane
     */
    protected final void generateCar(final Length initialPosition, final Lane lane, final GTUType gtuType)
            throws NamingException, NetworkException, SimRuntimeException, GTUException, OTSGeometryException
    {

        // GTU itself
        boolean generateTruck = this.randomGenerator.nextDouble() > this.carProbability;
        Length vehicleLength = new Length(generateTruck ? 15 : 4, METER);
        LaneBasedIndividualGTU gtu =
                new LaneBasedIndividualGTU("" + (++this.carsCreated), gtuType, vehicleLength, new Length(1.8, METER),
                        new Speed(200, KM_PER_HOUR), vehicleLength.multiplyBy(0.5), this.simulator, this.network);

        // strategical planner
        LaneBasedStrategicalPlanner strategicalPlanner;
        if (!generateTruck)
        {
            strategicalPlanner = this.strategicalPlannerGeneratorCars.create(gtu, null, null, null);
        }
        else
        {
            strategicalPlanner = this.strategicalPlannerGeneratorTrucks.create(gtu, null, null, null);
        }

        // init
        Set<DirectedLanePosition> initialPositions = new LinkedHashSet<>(1);
        initialPositions.add(new DirectedLanePosition(lane, initialPosition, GTUDirectionality.DIR_PLUS));
        Speed initialSpeed = new Speed(0, KM_PER_HOUR);
        gtu.init(strategicalPlanner, initialPositions, initialSpeed, DefaultCarAnimation.class, this.gtuColorer);
    }

    /** {@inheritDoc} */
    @Override
    public SimulatorInterface<Time, Duration, SimTimeDoubleUnit> getSimulator()
    {
        return this.simulator;
    }

    /**
     * @return minimumDistance
     */
    public final Length getMinimumDistance()
    {
        return this.minimumDistance;
    }

    /**
     * Stop simulation and throw an Error.
     * @param theSimulator DEVSSimulatorInterface.TimeDoubleUnit; the simulator
     * @param errorMessage String; the error message
     */
    public void stopSimulator(final DEVSSimulatorInterface.TimeDoubleUnit theSimulator, final String errorMessage)
    {
        System.out.println("Error: " + errorMessage);
        try
        {
            if (theSimulator.isRunning())
            {
                theSimulator.stop();
            }
        }
        catch (SimRuntimeException exception)
        {
            exception.printStackTrace();
        }
        throw new Error(errorMessage);
    }

    /**
     * Simple sensor that does not provide output, but is drawn on the Lanes.
     * <p>
     * Copyright (c) 2013-2018 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="http://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
     * </p>
     * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
     * initial version Sep 18, 2016 <br>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    public static class SimpleSilentSensor extends AbstractSensor
    {
        /** */
        private static final long serialVersionUID = 20150130L;

        /**
         * @param lane Lane; the lane of the sensor.
         * @param position Length; the position of the sensor
         * @param triggerPosition RelativePosition.TYPE; the relative position type (e.g., FRONT, REAR) of the vehicle that
         *            triggers the sensor.
         * @param id String; the id of the sensor.
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; the simulator to enable animation.
         * @throws NetworkException when the position on the lane is out of bounds w.r.t. the center line of the lane
         * @throws OTSGeometryException when the geometry of the sensor cannot be calculated, e.g. when the lane width is zero,
         *             or the position is beyond or before the lane length
         */
        public SimpleSilentSensor(final String id, final Lane lane, final Length position,
                final RelativePosition.TYPE triggerPosition, final DEVSSimulatorInterface.TimeDoubleUnit simulator)
                throws NetworkException, OTSGeometryException
        {
            super(id, lane, position, triggerPosition, simulator, LaneBasedObject.makeGeometry(lane, position),
                    Compatible.EVERYTHING);
            try
            {
                new SensorAnimation(this, position, simulator, Color.RED);
            }
            catch (RemoteException | NamingException exception)
            {
                exception.printStackTrace();
            }
        }

        /** {@inheritDoc} */
        @Override
        public final void triggerResponse(final LaneBasedGTU gtu)
        {
            // do nothing.
        }

        /** {@inheritDoc} */
        @Override
        public final String toString()
        {
            return "SimpleSilentSensor [Lane=" + this.getLane() + "]";
        }

        /** {@inheritDoc} */
        @Override
        @SuppressWarnings("checkstyle:designforextension")
        public SimpleSilentSensor clone(final CrossSectionElement newCSE, final SimulatorInterface.TimeDoubleUnit newSimulator,
                final boolean animation) throws NetworkException
        {
            Throw.when(!(newCSE instanceof Lane), NetworkException.class, "sensors can only be cloned for Lanes");
            Throw.when(!(newSimulator instanceof DEVSSimulatorInterface.TimeDoubleUnit), NetworkException.class,
                    "simulator should be a DEVSSimulator");
            try
            {
                return new SimpleSilentSensor(getId(), (Lane) newCSE, getLongitudinalPosition(), getPositionType(),
                        (DEVSSimulatorInterface.TimeDoubleUnit) newSimulator);
            }
            catch (OTSGeometryException exception)
            {
                throw new NetworkException(exception);
            }
        }

    }
}
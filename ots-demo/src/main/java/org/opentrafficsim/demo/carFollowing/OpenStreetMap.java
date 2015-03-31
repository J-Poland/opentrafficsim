package org.opentrafficsim.demo.carFollowing;

import java.awt.FileDialog;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.naming.NamingException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

import org.opentrafficsim.core.dsol.OTSDEVSSimulatorInterface;
import org.opentrafficsim.core.dsol.OTSModelInterface;
import org.opentrafficsim.core.dsol.OTSSimTimeDouble;
import org.opentrafficsim.core.network.Network;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.core.network.Node;
import org.opentrafficsim.core.network.geotools.NodeGeotools;
import org.opentrafficsim.core.network.geotools.NodeGeotools.STR;
import org.opentrafficsim.core.network.lane.CrossSectionLink;
import org.opentrafficsim.core.network.lane.Lane;
import org.opentrafficsim.core.unit.AccelerationUnit;
import org.opentrafficsim.core.unit.LengthUnit;
import org.opentrafficsim.core.unit.TimeUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar.Abs;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar.Rel;
import org.opentrafficsim.importexport.osm.Tag;
import org.opentrafficsim.importexport.osm.events.ProgressEvent;
import org.opentrafficsim.importexport.osm.events.ProgressListener;
import org.opentrafficsim.importexport.osm.events.ProgressListenerImpl;
import org.opentrafficsim.importexport.osm.events.WarningListener;
import org.opentrafficsim.importexport.osm.events.WarningListenerImpl;
import org.opentrafficsim.importexport.osm.input.ReadOSMFile;
import org.opentrafficsim.importexport.osm.output.Convert;
import org.opentrafficsim.simulationengine.AbstractProperty;
import org.opentrafficsim.simulationengine.ControlPanel;
import org.opentrafficsim.simulationengine.IDMPropertySet;
import org.opentrafficsim.simulationengine.ProbabilityDistributionProperty;
import org.opentrafficsim.simulationengine.PropertyException;
import org.opentrafficsim.simulationengine.SelectionProperty;
import org.opentrafficsim.simulationengine.SimpleSimulator;
import org.opentrafficsim.simulationengine.SimulatorFrame;
import org.opentrafficsim.simulationengine.WrappableSimulation;

/**
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Feb 10, 2015 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author Moritz Bergmann
 */
public class OpenStreetMap implements WrappableSimulation
{
    /** */
    private org.opentrafficsim.importexport.osm.Network networkOSM;

    /** */
    private Network<String, CrossSectionLink<?, ?>> networkOTS;

    /** The properties of this simulation. */
    private ArrayList<AbstractProperty<?>> properties = new ArrayList<AbstractProperty<?>>();

    /** */
    private ProgressListener progressListener;

    /** */
    private WarningListener warningListener;

    /**
     * Construct the OpenStreetMap demo
     */
    public OpenStreetMap()
    {
        // The real work is done in buildSimulator.
    }

    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    OpenStreetMap osm = new OpenStreetMap();
                    ArrayList<AbstractProperty<?>> localProperties = osm.getProperties();
                    try
                    {
                        localProperties.add(new ProbabilityDistributionProperty("Traffic composition",
                                "<html>Mix of passenger cars and trucks</html>",
                                new String[]{"passenger car", "truck"}, new Double[]{0.8, 0.2}, false, 10));
                    }
                    catch (PropertyException exception)
                    {
                        exception.printStackTrace();
                    }
                    localProperties.add(new SelectionProperty("Car following model",
                            "<html>The car following model determines "
                                    + "the acceleration that a vehicle will make taking into account "
                                    + "nearby vehicles, infrastructural restrictions (e.g. speed limit, "
                                    + "curvature of the road) capabilities of the vehicle and personality "
                                    + "of the driver.</html>", new String[]{"IDM", "IDM+"}, 1, false, 1));
                    localProperties.add(IDMPropertySet.makeIDMPropertySet("Car",
                            new DoubleScalar.Abs<AccelerationUnit>(1.0, AccelerationUnit.METER_PER_SECOND_2),
                            new DoubleScalar.Abs<AccelerationUnit>(1.5, AccelerationUnit.METER_PER_SECOND_2),
                            new DoubleScalar.Rel<LengthUnit>(2.0, LengthUnit.METER), new DoubleScalar.Rel<TimeUnit>(
                                    1.0, TimeUnit.SECOND), 2));
                    localProperties.add(IDMPropertySet.makeIDMPropertySet("Truck",
                            new DoubleScalar.Abs<AccelerationUnit>(0.5, AccelerationUnit.METER_PER_SECOND_2),
                            new DoubleScalar.Abs<AccelerationUnit>(1.25, AccelerationUnit.METER_PER_SECOND_2),
                            new DoubleScalar.Rel<LengthUnit>(2.0, LengthUnit.METER), new DoubleScalar.Rel<TimeUnit>(
                                    1.0, TimeUnit.SECOND), 3));
                    new SimulatorFrame("OpenStreetMap animation", osm.buildSimulator(localProperties).getPanel());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    /** {@inheritDoc} */
    @Override
    public SimpleSimulator buildSimulator(final ArrayList<AbstractProperty<?>> usedProperties)
            throws SimRuntimeException, RemoteException, NetworkException
    {
        JFrame frame = new JFrame();
        FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        // fd.setDirectory("C:\\");
        fd.setFile("*.osm");
        fd.setVisible(true);
        File[] file = fd.getFiles();
        if (file.length == 0)
        {
            return null;
        }
        String filename = fd.getFile();
        String filepath = null;
        try
        {
            filepath = file[0].toURI().toURL().toString();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        if (filename == null)
        {
            System.out.println("You cancelled the choice");
            return null;
        }
        System.out.println("Opening file " + filename);
        ArrayList<org.opentrafficsim.importexport.osm.Tag> wt =
                new ArrayList<org.opentrafficsim.importexport.osm.Tag>();
        wt.add(new Tag("highway", "primary"));
        wt.add(new Tag("highway", "secondary"));
        wt.add(new Tag("highway", "tertiary"));
        wt.add(new Tag("highway", "cycleway"));
        wt.add(new Tag("highway", "trunk"));
        wt.add(new Tag("highway", "path"));
        wt.add(new Tag("cyclway", "lane"));
        wt.add(new Tag("highway", "residential"));
        wt.add(new Tag("highway", "service"));
        wt.add(new Tag("highway", "motorway"));
        wt.add(new Tag("highway", "bus_stop"));
        wt.add(new Tag("highway", "motorway_link"));
        wt.add(new Tag("highway", "unclassified"));
        wt.add(new Tag("highway", "footway"));
        wt.add(new Tag("cycleway", "track"));
        wt.add(new Tag("highway", "road"));
        wt.add(new Tag("highway", "pedestrian"));
        wt.add(new Tag("highway", "track"));
        wt.add(new Tag("highway", "living_street"));
        wt.add(new Tag("highway", "tertiary_link"));
        wt.add(new Tag("highway", "secondary_link"));
        wt.add(new Tag("highway", "primary_link"));
        wt.add(new Tag("highway", "trunk_link"));
        ArrayList<String> ft = new ArrayList<String>();
        try
        {
            System.out.println(filepath);
            this.progressListener = new ProgressListenerImpl();
            this.warningListener = new WarningListenerImpl();
            ReadOSMFile osmf = new ReadOSMFile(filepath, wt, ft, this.progressListener);
            org.opentrafficsim.importexport.osm.Network net = osmf.getNetwork();
            // net.makeLinks();
            // net.removeRedundancy();
            this.networkOSM = new org.opentrafficsim.importexport.osm.Network(net);
            this.networkOTS = new Network<String, CrossSectionLink<?, ?>>(this.networkOSM.getName());
            for (org.opentrafficsim.importexport.osm.Node osmNode : this.networkOSM.getNodes().values())
            {
                try
                {
                    this.networkOTS.addNode(Convert.convertNode(osmNode));
                }
                catch (NetworkException ne)
                {
                    System.out.println(ne.getMessage());
                }
            }
            for (org.opentrafficsim.importexport.osm.Link osmLink : this.networkOSM.getLinks())
            {
                this.networkOTS.add(Convert.convertLink(osmLink));
            }
        }
        catch (URISyntaxException | IOException exception)
        {
            exception.printStackTrace();
            return null;
        }
        OSMModel model = new OSMModel(usedProperties, this.networkOSM, this.warningListener, this.progressListener);
        Iterator<Node<?, ?>> count = this.networkOTS.getNodeSet().iterator();
        Rectangle2D area = null;
        while (count.hasNext())
        {
            NodeGeotools.STR node = (STR) count.next();
            if (null == area)
            {
                area = new Rectangle2D.Double(node.getX(), node.getY(), 0, 0);
            }
            else
            {
                area = area.createUnion(new Rectangle2D.Double(node.getX(), node.getY(), 0, 0));
            }
        }
        SimpleSimulator result =
                new SimpleSimulator(new DoubleScalar.Abs<TimeUnit>(0.0, TimeUnit.SECOND),
                        new DoubleScalar.Rel<TimeUnit>(0.0, TimeUnit.SECOND), new DoubleScalar.Rel<TimeUnit>(1800.0,
                                TimeUnit.SECOND), model, area);
        new ControlPanel(result);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String shortName()
    {
        return "Open Street Map Simulation";
    }

    /** {@inheritDoc} */
    @Override
    public String description()
    {
        return "Depicts a Map imported from Open Street Maps";
    }

    /** {@inheritDoc} */
    @Override
    public ArrayList<AbstractProperty<?>> getProperties()
    {
        return new ArrayList<AbstractProperty<?>>(this.properties);
    }

}

/**
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Feb 10, 2015 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author Moritz Bergmann
 */
class OSMModel implements OTSModelInterface
{
    /** */
    private static final long serialVersionUID = 20150227L;

    /** the simulator. */
    private OTSDEVSSimulatorInterface simulator;

    /** User settable properties. */
    private ArrayList<AbstractProperty<?>> properties = null;

    /** Provided Network. */
    private org.opentrafficsim.importexport.osm.Network network;

    /** OTS Network created from provided OSM Network. */
    private Network<String, CrossSectionLink<?, ?>> networkOTS;

    /** Provided lanes. */
    private ArrayList<Lane> lanes;

    /** */
    private ProgressListener progressListener;

    /** */
    private WarningListener warningListener;

    /**
     * @param properties
     * @param net
     * @param wL
     * @param pL
     */
    public OSMModel(final ArrayList<AbstractProperty<?>> properties,
            final org.opentrafficsim.importexport.osm.Network net, final WarningListener wL, final ProgressListener pL)
    {
        this.network = net;
        this.warningListener = wL;
        this.progressListener = pL;
        /*
         * ArrayList<org.opentrafficsim.importexport.osm.Tag> wt = new
         * ArrayList<org.opentrafficsim.importexport.osm.Tag>(); org.opentrafficsim.importexport.osm.Tag t1 = new
         * org.opentrafficsim.importexport.osm.Tag("highway", "primary"); wt.add(t1);
         * org.opentrafficsim.importexport.osm.Tag t2 = new org.opentrafficsim.importexport.osm.Tag("highway",
         * "secondary"); wt.add(t2); org.opentrafficsim.importexport.osm.Tag t3 = new
         * org.opentrafficsim.importexport.osm.Tag("highway", "tertiary"); wt.add(t3);
         * org.opentrafficsim.importexport.osm.Tag t4 = new org.opentrafficsim.importexport.osm.Tag("highway",
         * "cycleway"); wt.add(t4); org.opentrafficsim.importexport.osm.Tag t5 = new
         * org.opentrafficsim.importexport.osm.Tag("highway", "trunk"); wt.add(t5);
         * org.opentrafficsim.importexport.osm.Tag t6 = new org.opentrafficsim.importexport.osm.Tag("highway", "path");
         * wt.add(t6); org.opentrafficsim.importexport.osm.Tag t8 = new
         * org.opentrafficsim.importexport.osm.Tag("cyclway", "lane"); wt.add(t8);
         * org.opentrafficsim.importexport.osm.Tag t9 = new org.opentrafficsim.importexport.osm.Tag("highway",
         * "residential"); wt.add(t9); org.opentrafficsim.importexport.osm.Tag t10 = new
         * org.opentrafficsim.importexport.osm.Tag("highway", "service"); wt.add(t10);
         * org.opentrafficsim.importexport.osm.Tag t11 = new org.opentrafficsim.importexport.osm.Tag("highway",
         * "motorway"); wt.add(t11); org.opentrafficsim.importexport.osm.Tag t12 = new
         * org.opentrafficsim.importexport.osm.Tag("highway", "bus_stop"); wt.add(t12);
         * org.opentrafficsim.importexport.osm.Tag t13 = new org.opentrafficsim.importexport.osm.Tag("highway",
         * "motorway_link"); wt.add(t13); org.opentrafficsim.importexport.osm.Tag t14 = new
         * org.opentrafficsim.importexport.osm.Tag("highway", "unclassified"); wt.add(t14);
         * org.opentrafficsim.importexport.osm.Tag t15 = new org.opentrafficsim.importexport.osm.Tag("highway",
         * "footway"); wt.add(t15); org.opentrafficsim.importexport.osm.Tag t16 = new
         * org.opentrafficsim.importexport.osm.Tag("cycleway", "track"); wt.add(t16);
         * org.opentrafficsim.importexport.osm.Tag t17 = new org.opentrafficsim.importexport.osm.Tag("highway", "road");
         * wt.add(t17); org.opentrafficsim.importexport.osm.Tag t18 = new
         * org.opentrafficsim.importexport.osm.Tag("highway", "pedestrian"); wt.add(t18);
         * org.opentrafficsim.importexport.osm.Tag t19 = new org.opentrafficsim.importexport.osm.Tag("highway",
         * "track"); wt.add(t19); org.opentrafficsim.importexport.osm.Tag t20 = new
         * org.opentrafficsim.importexport.osm.Tag("highway", "living_street"); wt.add(t20);
         * org.opentrafficsim.importexport.osm.Tag t21 = new org.opentrafficsim.importexport.osm.Tag("highway",
         * "tertiary_link"); wt.add(t21); org.opentrafficsim.importexport.osm.Tag t22 = new
         * org.opentrafficsim.importexport.osm.Tag("highway", "secondary_link"); wt.add(t22);
         * org.opentrafficsim.importexport.osm.Tag t23 = new org.opentrafficsim.importexport.osm.Tag("highway",
         * "primary_link"); wt.add(t23); org.opentrafficsim.importexport.osm.Tag t24 = new
         * org.opentrafficsim.importexport.osm.Tag("highway", "trunk_link"); wt.add(t24);
         * org.opentrafficsim.importexport.osm.Tag t25 = new org.opentrafficsim.importexport.osm.Tag("waterway",
         * "river"); wt.add(t25); org.opentrafficsim.importexport.osm.Tag t26 = new
         * org.opentrafficsim.importexport.osm.Tag("waterway", "riverbank"); wt.add(t26);
         * org.opentrafficsim.importexport.osm.Tag t27 = new org.opentrafficsim.importexport.osm.Tag("waterway",
         * "stream"); wt.add(t27); org.opentrafficsim.importexport.osm.Tag t28 = new
         * org.opentrafficsim.importexport.osm.Tag("waterway", "wadi"); wt.add(t28);
         * org.opentrafficsim.importexport.osm.Tag t29 = new org.opentrafficsim.importexport.osm.Tag("waterway",
         * "canal"); wt.add(t29); org.opentrafficsim.importexport.osm.Tag t30 = new
         * org.opentrafficsim.importexport.osm.Tag("waterway", "drain"); wt.add(t30);
         * org.opentrafficsim.importexport.osm.Tag t31 = new org.opentrafficsim.importexport.osm.Tag("waterway",
         * "ditch"); wt.add(t31); org.opentrafficsim.importexport.osm.Tag t32 = new
         * org.opentrafficsim.importexport.osm.Tag("highway", "steps"); wt.add(t32); ArrayList<String> ft = new
         * ArrayList<String>(); try { ReadOSMFile osmf = new ReadOSMFile("file:///C:/amsterdam_netherlands.osm.bz2", wt,
         * ft); org.opentrafficsim.importexport.osm.Network net = osmf.getNetwork(); net.makeLinks(); //
         * net.removeRedundancy(); this.network = new org.opentrafficsim.importexport.osm.Network(net);
         */
        this.networkOTS = new Network<String, CrossSectionLink<?, ?>>(this.network.getName());
        try
        {
            this.network.makeLinks(this.warningListener, this.progressListener);
        }
        catch (IOException exception1)
        {
            exception1.printStackTrace();
        }
        for (org.opentrafficsim.importexport.osm.Node osmNode : this.network.getNodes().values())
        {
            try
            {
                this.networkOTS.addNode(Convert.convertNode(osmNode));
            }
            catch (NetworkException ne)
            {
                System.out.println(ne.getMessage());
            }
        }
        for (org.opentrafficsim.importexport.osm.Link osmLink : this.network.getLinks())
        {
            this.networkOTS.add(Convert.convertLink(osmLink));
        }
        this.lanes = new ArrayList<Lane>();
        this.properties = new ArrayList<AbstractProperty<?>>(properties);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Abs<TimeUnit>, Rel<TimeUnit>, OTSSimTimeDouble> theSimulator)
            throws SimRuntimeException, RemoteException
    {
        this.network = Convert.findSinksandSources(this.network, this.progressListener);
        this.progressListener.progress(new ProgressEvent(this.network, "Starting lane creation."));
        double total = this.network.getLinks().size();
        double counter = 0;
        double nextPercentage = 5.0D;
        for (org.opentrafficsim.importexport.osm.Link l : this.network.getLinks())
        {
            try
            {
                this.lanes.addAll(Convert.makeLanes(l, (OTSDEVSSimulatorInterface) theSimulator, this.warningListener,
                        this.progressListener));
            }
            catch (NetworkException | NamingException exception)
            {
                exception.printStackTrace();
            }
            counter++;
            double currentPercentage = counter / total * 100;
            if (currentPercentage >= nextPercentage)
            {
                this.progressListener.progress(new ProgressEvent(this, nextPercentage + "% Progress"));
                nextPercentage += 5.0D;
            }
        }
        /*
         * System.out.println("Number of Links: " + this.network.getLinks().size());
         * System.out.println("Number of Nodes: " + this.network.getNodes().size());
         * System.out.println("Number of Lanes: " + this.lanes.size());
         */

    }

    /**
     * @param theSimulator
     * @param errorMessage
     */
    public void stopSimulator(final OTSDEVSSimulatorInterface theSimulator, final String errorMessage)
    {
        System.out.println("Error: " + errorMessage);
        try
        {
            if (theSimulator.isRunning())
            {
                theSimulator.stop();
            }
        }
        catch (RemoteException | SimRuntimeException exception)
        {
            exception.printStackTrace();
        }
        throw new Error(errorMessage);
    }

    /** {@inheritDoc} */
    @Override
    public SimulatorInterface<Abs<TimeUnit>, Rel<TimeUnit>, OTSSimTimeDouble> getSimulator() throws RemoteException
    {
        return this.simulator;
    }

}
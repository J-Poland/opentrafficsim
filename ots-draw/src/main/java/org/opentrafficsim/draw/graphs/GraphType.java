package org.opentrafficsim.draw.graphs;

/**
 * <p>
 * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public enum GraphType
{
    /** Trajectory. */
    TRAJECTORY,

    /** Contour plot: speed. */
    SPEED_CONTOUR,

    /** Contour plot: acceleration. */
    ACCELERATION_CONTOUR,

    /** Contour plot: density. */
    DENSITY_CONTOUR,

    /** Contour plot: flow. */
    FLOW_CONTOUR,

    /** Contour plot: delay. */
    DELAY_CONTOUR,

    /** Fundamental diagram. */
    FUNDAMENTAL_DIAGRAM,

    /** Any other graph. */
    OTHER,
}

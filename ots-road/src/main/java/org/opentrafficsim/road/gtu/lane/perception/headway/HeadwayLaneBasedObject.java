package org.opentrafficsim.road.gtu.lane.perception.headway;

import org.opentrafficsim.road.network.lane.Lane;

/**
 * Headway of a lane-based object.
 * <p>
 * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public interface HeadwayLaneBasedObject extends Headway
{

    /**
     * Lane at which the object is located.
     * @return Lane; lane at which the object is located
     */
    Lane getLane();

}

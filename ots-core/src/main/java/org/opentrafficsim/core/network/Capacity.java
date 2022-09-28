package org.opentrafficsim.core.network;

import org.djunits.value.vdouble.scalar.Frequency;

/**
 * <p>
 * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public interface Capacity
{
    /** @return link capacity. */
    Frequency getCapacity();

    /**
     * Set the link capacity.
     * @param capacity Frequency; the new capacity of the link as a frequency in GTUs per time unit.
     */
    void setCapacity(Frequency capacity);

}

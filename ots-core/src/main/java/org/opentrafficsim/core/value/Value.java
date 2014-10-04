package org.opentrafficsim.core.value;

import org.opentrafficsim.core.unit.Unit;

/**
 * Value is a static interface that implements a couple of unit-related static methods.
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Aug 18, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <U> the unit type.
 */
public interface Value<U extends Unit<U>>
{
    /**
     * Retrieve the unit of this Value.
     * @return U; the unit of this Value
     */
    U getUnit();

    /**
     * Convert a value to the standard SI unit.
     * @param value double; the value to convert to the standard SI unit
     * @return double; the value in the standard SI unit
     */
    double expressAsSIUnit(final double value);

    /**
     * Indicate whether this is an Absolute Value.
     * @return boolean
     */
    boolean isAbsolute();

    /**
     * Indicate whether this is a Relative Value.
     * @return boolean
     */
    boolean isRelative();

    /**
     * Create a deep copy of this Value.
     * @return Value&lt;U&gt;
     */
    Value<U> copy();
    
}

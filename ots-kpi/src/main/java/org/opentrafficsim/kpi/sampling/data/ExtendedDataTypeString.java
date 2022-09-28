package org.opentrafficsim.kpi.sampling.data;

import org.opentrafficsim.kpi.interfaces.GtuDataInterface;

/**
 * <p>
 * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <G> gtu data type
 */
public abstract class ExtendedDataTypeString<G extends GtuDataInterface> extends ExtendedDataTypeList<String, G>
{

    /**
     * @param id String; id
     */
    public ExtendedDataTypeString(final String id)
    {
        super(id, String.class);
    }

    /** {@inheritDoc} */
    @Override
    public String formatValue(final String format, final String value)
    {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public String parseValue(final String string)
    {
        return string;
    }

}

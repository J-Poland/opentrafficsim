package org.opentrafficsim.road.gtu.animation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.opentrafficsim.core.gtu.GTU;
import org.opentrafficsim.core.gtu.animation.ColorInterpolator;
import org.opentrafficsim.core.gtu.animation.GTUColorer;
import org.opentrafficsim.road.gtu.lane.tactical.util.lmrs.Tailgating;

/**
 * Colorer for social pressure.
 * <p>
 * Copyright (c) 2013-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version $Revision$, $LastChangedDate$, by $Author$, initial version 9 mrt. 2018 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class SocialPressureColorer implements GTUColorer
{

    /** The legend. */
    private static final List<LegendEntry> LEGEND;

    /** No pressure color. */
    private final static Color NONE = Color.WHITE;

    /** Full pressure color. */
    private final static Color FULL = Color.RED;

    /** Not applicable color. */
    private final static Color NA = Color.YELLOW;

    static
    {
        LEGEND = new ArrayList<>(3);
        LEGEND.add(new LegendEntry(NONE, "None", "None: 0.0"));
        LEGEND.add(new LegendEntry(FULL, "Full", "Full: 1.0"));
        LEGEND.add(new LegendEntry(NA, "N/A", "N/A"));
    }

    /** {@inheritDoc} */
    @Override
    public Color getColor(final GTU gtu)
    {
        try
        {
            double rho = gtu.getParameters().getParameter(Tailgating.RHO);
            return ColorInterpolator.interpolateColor(NONE, FULL, rho);
        }
        catch (@SuppressWarnings("unused") Exception exception)
        {
            return NA;
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<LegendEntry> getLegend()
    {
        return LEGEND;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Social pressure";
    }

}

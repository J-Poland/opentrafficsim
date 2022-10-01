package org.opentrafficsim.kpi.sampling;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Length;
import org.djutils.exceptions.Throw;
import org.opentrafficsim.kpi.interfaces.LaneDataInterface;

/**
 * Store one position and lane of a GTU.
 * <p>
 * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 */
public class KpiLanePosition implements Serializable
{
    /** */
    private static final long serialVersionUID = 20151111L;

    /** The lane for the position. */
    private final LaneDataInterface lane;

    /** The position on the lane, relative to the cross section link (design line). */
    private final Length position;

    /**
     * Construct a new KpiLanePosition.
     * @param lane LaneDataInterface; the lane for the position
     * @param position Length; the position on the lane, relative to the cross section link (design line)
     */
    public KpiLanePosition(final LaneDataInterface lane, final Length position)
    {
        Throw.whenNull(lane, "lane is null");
        Throw.whenNull(position, "position is null");
        this.lane = lane;
        this.position = position;
    }

    /**
     * Retrieve the lane.
     * @return LaneDataInterface; the lane for the position
     */
    public final LaneDataInterface getLaneData()
    {
        return this.lane;
    }

    /**
     * Retrieve the position on the lane.
     * @return Length; the position on the lane, relative to the cross section link (design line)
     */
    public final Length getPosition()
    {
        return this.position;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.lane == null) ? 0 : this.lane.hashCode());
        result = prime * result + ((this.position == null) ? 0 : this.position.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KpiLanePosition other = (KpiLanePosition) obj;
        if (this.lane == null)
        {
            if (other.lane != null)
                return false;
        }
        else if (!this.lane.equals(other.lane))
            return false;
        if (this.position == null)
        {
            if (other.position != null)
                return false;
        }
        else if (!this.position.equals(other.position))
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "DirectedLanePosition [lane=" + this.lane + ", position=" + this.position + "]";
    }

}

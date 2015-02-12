package org.opentrafficsim.core.gtu.lane.changing;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

import org.opentrafficsim.core.gtu.RelativePosition;
import org.opentrafficsim.core.gtu.following.AccelerationStep;
import org.opentrafficsim.core.gtu.following.FollowAcceleration;
import org.opentrafficsim.core.gtu.following.HeadwayGTU;
import org.opentrafficsim.core.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.core.network.LateralDirectionality;
import org.opentrafficsim.core.network.NetworkException;
import org.opentrafficsim.core.network.lane.Lane;
import org.opentrafficsim.core.unit.AccelerationUnit;
import org.opentrafficsim.core.unit.LengthUnit;
import org.opentrafficsim.core.unit.SpeedUnit;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar;
import org.opentrafficsim.core.value.vdouble.scalar.DoubleScalar.Rel;

/**
 * Common code for a family of lane change models like in M. Treiber and A. Kesting <i>Traffic Flow Dynamics</i>,
 * Springer-Verlag Berlin Heidelberg 2013, pp 239-244
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version 4 nov. 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public abstract class AbstractLaneChangeModel implements LaneChangeModel
{
    /** {@inheritDoc} */
    @Override
    public final LaneMovementStep computeLaneChangeAndAcceleration(final LaneBasedGTU<?> gtu,
            final Collection<HeadwayGTU> sameLaneGTUs, final Collection<HeadwayGTU> preferredLaneGTUs,
            final Collection<HeadwayGTU> nonPreferredLaneGTUs, final DoubleScalar.Abs<SpeedUnit> speedLimit,
            final DoubleScalar.Rel<AccelerationUnit> preferredLaneRouteIncentive,
            final DoubleScalar.Rel<AccelerationUnit> laneChangeThreshold,
            final DoubleScalar.Rel<AccelerationUnit> nonPreferredLaneRouteIncentive) throws RemoteException
    {
        try
        {
            // System.out.println(String.format(
            // "Route desire to merge to preferredLane: %s, route desire to merge to overtakingLane: %s",
            // preferredLaneRouteIncentive, nonPreferredLaneRouteIncentive));
            if (gtu.getId().toString().equals("14"))
            {
                System.out.println("LaneMovementStep for " + gtu);
            }
            Map<Lane, Rel<LengthUnit>> positions = gtu.positions(RelativePosition.REFERENCE_POSITION);
            Lane lane = positions.keySet().iterator().next();
            DoubleScalar.Rel<LengthUnit> longitudinalPosition = positions.get(lane);
            // TODO make this driving side dependent; i.e. implement a general way to figure out on which side of the
            // road cars are supposed to drive
            final LateralDirectionality preferred = LateralDirectionality.RIGHT;
            final LateralDirectionality nonPreferred = LateralDirectionality.LEFT;
            Lane nonPreferredLane =
                    lane.bestAccessibleAdjacentLane(nonPreferred, longitudinalPosition, gtu.getGTUType());
            Lane preferredLane = lane.bestAccessibleAdjacentLane(preferred, longitudinalPosition, gtu.getGTUType());
            AccelerationStep[] straightAccelerationSteps =
                    FollowAcceleration.acceleration(gtu, sameLaneGTUs, speedLimit);
            DoubleScalar.Abs<AccelerationUnit> straightA =
                    DoubleScalar.plus(applyDriverPersonality(straightAccelerationSteps), laneChangeThreshold)
                            .immutable();
            AccelerationStep[] nonPreferrredAccelerationSteps =
                    null == nonPreferredLane ? null : FollowAcceleration.acceleration(gtu, nonPreferredLaneGTUs,
                            speedLimit);
            DoubleScalar.Abs<AccelerationUnit> nonPreferredA =
                    null == nonPreferredLane ? null : applyDriverPersonality(nonPreferrredAccelerationSteps);
            AccelerationStep[] preferredAccelerationSteps =
                    null == preferredLane ? null : FollowAcceleration.acceleration(gtu, preferredLaneGTUs, speedLimit);
            DoubleScalar.Abs<AccelerationUnit> preferredA =
                    null == preferredLane ? null : applyDriverPersonality(preferredAccelerationSteps);
            if (null == preferredA)
            {
                // Lane change to the preferred lane is not possible
                if (null == nonPreferredA)
                {
                    // No lane change possible; this is definitely the easy case
                    return new LaneMovementStep(straightAccelerationSteps[0], null);
                }
                else
                {
                    // Merge to nonPreferredLane is possible; merge to preferredLane is NOT possible
                    if (DoubleScalar.plus(nonPreferredA, nonPreferredLaneRouteIncentive).getSI() > straightA.getSI())
                    {
                        // Merge to the nonPreferred lane; i.e. start an overtaking procedure
                        return new LaneMovementStep(nonPreferrredAccelerationSteps[0], nonPreferred);
                    }
                    else
                    {
                        // Stay in the current lane
                        return new LaneMovementStep(straightAccelerationSteps[0], null);
                    }
                }
            }
            // A merge to the preferredLane is possible
            if (null == nonPreferredA)
            {
                // Merge to preferredLane is possible; merge to nonPreferred lane is NOT possible
                if (DoubleScalar.plus(preferredA, preferredLaneRouteIncentive).getSI() > straightA.getSI())
                {
                    // Merge to the preferred lane; i.e. finish (or cancel) an overtaking procedure
                    return new LaneMovementStep(preferredAccelerationSteps[0], preferred);
                }
                else
                {
                    // Stay in current lane
                    return new LaneMovementStep(straightAccelerationSteps[0], null);
                }
            }
            // All merges are possible
            DoubleScalar.Rel<AccelerationUnit> preferredAttractiveness =
                    DoubleScalar.minus(DoubleScalar.plus(preferredA, preferredLaneRouteIncentive).immutable(),
                            straightA).immutable();
            DoubleScalar.Rel<AccelerationUnit> nonPreferredAttractiveness =
                    DoubleScalar.minus(DoubleScalar.plus(nonPreferredA, nonPreferredLaneRouteIncentive).immutable(),
                            straightA).immutable();
            if (preferredAttractiveness.getSI() <= 0 && nonPreferredAttractiveness.getSI() < 0)
            {
                // Stay in current lane
                return new LaneMovementStep(straightAccelerationSteps[0], null);

            }
            if (preferredAttractiveness.getSI() > 0
                    && preferredAttractiveness.getSI() > nonPreferredAttractiveness.getSI())
            {
                // Merge to the preferred lane; i.e. finish (or cancel) an overtaking procedure
                return new LaneMovementStep(preferredAccelerationSteps[0], preferred);
            }
            // Merge to the adjacent nonPreferred lane; i.e. start an overtaking procedure
            return new LaneMovementStep(nonPreferrredAccelerationSteps[0], nonPreferred);
        }
        catch (NetworkException exception)
        {
            exception.printStackTrace();
        }
        throw new Error(
                "Cannot happen: computeLaneChangeAndAcceleration failed to decide whether or not to change lane");
    }

    /**
     * Return the weighted acceleration as described by the personality. This incorporates the personality of the driver
     * to the lane change decisions.
     * @param accelerationSteps AccelerationStep[]; the AccelerationStep that the reference GTU will make (in position 0
     *            of the array) and the AccelerationStep that the (new) follower GTU will make (in position 1 of the
     *            array)
     * @return DoubleScalar.Abs&lt;AccelerationUnit&gt;; the acceleration that the personality of the driver uses (in a
     *         comparison to a similarly computed acceleration in the non-, or different-lane-changed state) to decide
     *         if a lane change should be performed
     */
    public abstract DoubleScalar.Abs<AccelerationUnit> applyDriverPersonality(AccelerationStep[] accelerationSteps);
}

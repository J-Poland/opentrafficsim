package org.opentrafficsim.road.gtu.perception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.SpeedUnit;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.junit.Test;
import org.opentrafficsim.core.dsol.OTSSimulator;
import org.opentrafficsim.core.gtu.GtuException;
import org.opentrafficsim.core.gtu.GtuType;
import org.opentrafficsim.road.gtu.lane.perception.headway.GtuStatus;
import org.opentrafficsim.road.gtu.lane.perception.headway.Headway;
import org.opentrafficsim.road.gtu.lane.perception.headway.HeadwayGtuSimple;
import org.opentrafficsim.road.network.OTSRoadNetwork;

/**
 * Test the HeadwayGtu class and the EnumType in the Headway interface.
 * <p>
 * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class HeadwayGtuTest
{
    /** The network. */
    private OTSRoadNetwork network = new OTSRoadNetwork("test network", true, new OTSSimulator("Simulator for HeadwayGtuTest"));

    /**
     * Test the constructor and the getters.
     * @throws GtuException when something fails; if uncaught; this test has failed
     */
    @Test
    public final void constructorTest() throws GtuException
    {
        // Make two headway GTUs with different values to prove that HeadwayGtus do not share static fields.
        String id1 = "id1";
        GtuType gtuType1 = new GtuType("type1", this.network.getGtuType(GtuType.DEFAULTS.CAR));
        Length distance1 = new Length(123, LengthUnit.METER);
        String id2 = "id2";
        GtuType gtuType2 = new GtuType("type2", this.network.getGtuType(GtuType.DEFAULTS.CAR));
        Length distance2 = new Length(234, LengthUnit.METER);
        HeadwayGtuSimple hg1 = new HeadwayGtuSimple(id1, gtuType1, distance1, Length.ZERO, Length.ZERO, (Speed) null,
                (Acceleration) null, null);
        HeadwayGtuSimple hg2 = new HeadwayGtuSimple(id2, gtuType2, distance2, Length.ZERO, Length.ZERO, (Speed) null,
                (Acceleration) null, null);
        verifyFields(hg1, null, distance1, gtuType1, id1, Headway.ObjectType.GTU, null, null, null, null, true, false, false,
                false, false, false, false, false);
        verifyFields(hg2, null, distance2, gtuType2, id2, Headway.ObjectType.GTU, null, null, null, null, true, false, false,
                false, false, false, false, false);
        Length overlapFront = new Length(2, LengthUnit.METER);
        Length overlap = new Length(3, LengthUnit.METER);
        Length overlapRear = new Length(4, LengthUnit.METER);
        hg2 = new HeadwayGtuSimple(id2, gtuType2, overlapFront, overlap, overlapRear, Length.ZERO, Length.ZERO, (Speed) null,
                (Acceleration) null, null);
        verifyFields(hg2, null, null, gtuType2, id2, Headway.ObjectType.GTU, overlap, overlapFront, overlapRear, null, false,
                false, false, false, false, false, false, true);
        Speed speed2 = new Speed(50, SpeedUnit.KM_PER_HOUR);
        Acceleration acceleration2 = new Acceleration(1.234, AccelerationUnit.METER_PER_SECOND_2);
        hg2 = new HeadwayGtuSimple(id2, gtuType2, overlapFront, overlap, overlapRear, Length.ZERO, Length.ZERO, speed2,
                acceleration2, null);
        verifyFields(hg2, acceleration2, null, gtuType2, id2, Headway.ObjectType.GTU, overlap, overlapFront, overlapRear,
                speed2, false, false, false, false, false, false, false, true);
        // Test all combinations of two GtuStatus values.
        for (GtuStatus gtuStatus1 : GtuStatus.values())
        {
            for (GtuStatus gtuStatus2 : GtuStatus.values())
            {
                hg2 = new HeadwayGtuSimple(id2, gtuType2, distance2, Length.ZERO, Length.ZERO, speed2, acceleration2, null,
                        gtuStatus1, gtuStatus2);
                boolean honking = GtuStatus.HONK == gtuStatus1 || GtuStatus.HONK == gtuStatus2;
                boolean braking = GtuStatus.BRAKING_LIGHTS == gtuStatus1 || GtuStatus.BRAKING_LIGHTS == gtuStatus2;
                boolean leftIndicator =
                        GtuStatus.LEFT_TURNINDICATOR == gtuStatus1 || GtuStatus.LEFT_TURNINDICATOR == gtuStatus2;
                boolean rightIndicator =
                        GtuStatus.RIGHT_TURNINDICATOR == gtuStatus1 || GtuStatus.RIGHT_TURNINDICATOR == gtuStatus2;
                boolean hazardLights = GtuStatus.EMERGENCY_LIGHTS == gtuStatus1 || GtuStatus.EMERGENCY_LIGHTS == gtuStatus2;
                verifyFields(hg2, acceleration2, distance2, gtuType2, id2, Headway.ObjectType.GTU, null, null, null, speed2,
                        true, false, braking, hazardLights, honking, leftIndicator, rightIndicator, false);

            }
        }
        // Verify that toString returns something
        assertTrue("toString returns something", hg1.toString().length() > 10);
        assertTrue("toString returns something", hg2.toString().length() > 10);
        try
        {
            new HeadwayGtuSimple(null, gtuType1, distance1, Length.ZERO, Length.ZERO, Speed.ZERO);
            fail("null for id should have thrown a GTUException");
        }
        catch (GtuException e)
        {
            // Ignore expected exception
        }
        try
        {
            new HeadwayGtuSimple(id1, gtuType1, null, Length.ZERO, Length.ZERO, Speed.ZERO);
            fail("null for distance should have thrown a GTUException");
        }
        catch (GtuException e)
        {
            // Ignore expected exception
        }
        assertTrue("ObjectType is a GTU", hg1.getObjectType().isGtu());
        assertFalse("ObjectType is traffic light", hg1.getObjectType().isTrafficLight());
        assertFalse("ObjectType is some other object", hg1.getObjectType().isObject());
        assertFalse("ObjectType is distance only", hg1.getObjectType().isDistanceOnly());
    }

    /**
     * Verify all fields in a HeadwayGtu.
     * @param headwayGTU HeadwayGtu; the HeadwayGtu to check
     * @param acceleration Acceleration; the expected return value for getAcceleration
     * @param distance Length; the expected return value for getDistance
     * @param gtuType GtuType; the expected return value for getGtuType
     * @param id String; the expected return value for getId
     * @param objectType Headway.ObjectType; the expected return value for getObjectType
     * @param overlap Length; the expected return value for getOverlap
     * @param overlapFront Length; the expected return value for getOverlapFront
     * @param overlapRear Length; the expected return value for getOverlapRear
     * @param speed Speed; the expected return value for getSpeed
     * @param ahead boolean; the expected return value for isAhead
     * @param behind boolean; the expected return value for isBehind
     * @param breakingLights boolean; the expected return value for isBreakingLightsOn
     * @param hazardLights boolean; the expected return value for isEmergencyLightsOn
     * @param honk boolean; the expected return value for isHonking
     * @param leftIndicator boolean; the expected return value for isLeftTurnIndicatorOn
     * @param rightIndicator boolean; the expected return value for isRightTurnIndicatorOn
     * @param parallel boolean; the expected return value for isParallel
     */
    private void verifyFields(final HeadwayGtuSimple headwayGTU, final Acceleration acceleration, final Length distance,
            final GtuType gtuType, final String id, final Headway.ObjectType objectType, final Length overlap,
            final Length overlapFront, final Length overlapRear, final Speed speed, final boolean ahead, final boolean behind,
            final boolean breakingLights, final boolean hazardLights, final boolean honk, final boolean leftIndicator,
            final boolean rightIndicator, final boolean parallel)
    {
        assertNotNull("headwayGTU should not be null", headwayGTU);
        if (null == acceleration)
        {
            assertNull("acceleration should be null", headwayGTU.getAcceleration());
        }
        else
        {
            assertEquals("acceleration should be " + acceleration, acceleration.si, headwayGTU.getAcceleration().si,
                    acceleration.si / 99999);
        }
        if (null == distance)
        {
            assertNull("distance should be null", headwayGTU.getDistance());
        }
        else
        {
            assertEquals("distance should be " + distance, distance.si, headwayGTU.getDistance().si, distance.si / 99999);
        }
        assertEquals("GTU type should be " + gtuType, gtuType, headwayGTU.getGtuType());
        assertEquals("Id should be " + id, id, headwayGTU.getId());
        assertEquals("Object type should be " + objectType, objectType, headwayGTU.getObjectType());
        if (null == overlap)
        {
            assertNull("overlap should be null", headwayGTU.getOverlap());
        }
        else
        {
            assertEquals("overlap should be " + overlap, overlap.si, headwayGTU.getOverlap().si, overlap.si / 99999);
        }
        if (null == overlapFront)
        {
            assertNull("overlapFront should be null", headwayGTU.getOverlapFront());
        }
        else
        {
            assertEquals("overlapFront should be " + overlapFront, overlapFront.si, headwayGTU.getOverlapFront().si,
                    overlapFront.si / 99999);
        }
        if (null == overlap)
        {
            assertNull("overlapRear should be null", headwayGTU.getOverlapRear());
        }
        else
        {
            assertEquals("overlapRear should be " + overlapRear, overlapRear.si, headwayGTU.getOverlapRear().si,
                    overlapRear.si / 99999);
        }
        if (null == speed)
        {
            assertNull("speed should be null", headwayGTU.getSpeed());
        }
        else
        {
            assertEquals("Speed should be " + speed, speed.si, headwayGTU.getSpeed().si, speed.si / 99999);
        }
        if (ahead)
        {
            assertTrue("ahead should be true", headwayGTU.isAhead());
        }
        else
        {
            assertFalse("ahead should be false", headwayGTU.isAhead());
        }
        if (behind)
        {
            assertTrue("behind should be true", headwayGTU.isBehind());
        }
        else
        {
            assertFalse("behind should be false", headwayGTU.isBehind());
        }
        if (breakingLights)
        {
            assertTrue("breaking lights should be on", headwayGTU.isBrakingLightsOn());
        }
        else
        {
            assertFalse("breaking lights should be off", headwayGTU.isBrakingLightsOn());
        }
        if (hazardLights)
        {
            assertTrue("hazard lights should be on", headwayGTU.isEmergencyLightsOn());
        }
        else
        {
            assertFalse("hazard lights should be off", headwayGTU.isEmergencyLightsOn());
        }
        if (honk)
        {
            assertTrue("GTU should be honking", headwayGTU.isHonking());
        }
        else
        {
            assertFalse("GTU should not be honking", headwayGTU.isHonking());
        }
        if (leftIndicator)
        {
            assertTrue("Left indicator lights should be on", headwayGTU.isLeftTurnIndicatorOn());
        }
        else
        {
            assertFalse("Left indicator lights should be off", headwayGTU.isLeftTurnIndicatorOn());
        }
        if (rightIndicator)
        {
            assertTrue("Right indicator lights should be on", headwayGTU.isRightTurnIndicatorOn());
        }
        else
        {
            assertFalse("Right indicator lights should be off", headwayGTU.isRightTurnIndicatorOn());
        }
        if (parallel)
        {
            assertTrue("Parallel should be true", headwayGTU.isParallel());
        }
        else
        {
            assertFalse("Parallel should be false", headwayGTU.isParallel());
        }
    }

}
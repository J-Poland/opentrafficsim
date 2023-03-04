package org.opentrafficsim.core.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djunits.unit.AngleUnit;
import org.djunits.unit.DirectionUnit;
import org.djunits.value.ValueRuntimeException;
import org.djunits.value.storage.StorageType;
import org.djunits.value.vdouble.scalar.Angle;
import org.djunits.value.vdouble.scalar.Direction;
import org.djunits.value.vdouble.vector.base.DoubleVector;
import org.junit.Test;

/**
 * Test the Direction3d and Angle3d classes. These classes are extremely similar. An Angle3d is a relative, a Direction is
 * absolute.
 * <p>
 * Copyright (c) 2013-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 */
public class Rotation3dTest
{

    /**
     * Test the constructors and getters.
     * @throws ValueRuntimeException should not happen; test has failed if it does happen
     */
    @Test
    public final void constructorTest() throws ValueRuntimeException
    {
        double roll = Math.toRadians(10);
        double pitch = Math.toRadians(20);
        double yaw = Math.toRadians(30);
        Direction3d r3d = new Direction3d(roll, pitch, yaw, DirectionUnit.EAST_RADIAN);
        checkRotation3d(r3d, roll, pitch, yaw);
        r3d = new Direction3d(
                DoubleVector.instantiate(new double[] {roll, pitch, yaw}, DirectionUnit.EAST_RADIAN, StorageType.DENSE));
        checkRotation3d(r3d, roll, pitch, yaw);
        r3d = new Direction3d(
                DoubleVector.instantiate(new double[] {roll, pitch, yaw}, DirectionUnit.EAST_RADIAN, StorageType.SPARSE));
        checkRotation3d(r3d, roll, pitch, yaw);
        r3d = new Direction3d(new Direction(roll, DirectionUnit.EAST_RADIAN), new Direction(pitch, DirectionUnit.EAST_RADIAN),
                new Direction(yaw, DirectionUnit.EAST_RADIAN));
        checkRotation3d(r3d, roll, pitch, yaw);
        try
        {
            new Direction3d(DoubleVector.instantiate(new double[] {roll, pitch}, DirectionUnit.EAST_RADIAN, StorageType.DENSE));
            fail("Short vector should have thrown an exception");
        }
        catch (ValueRuntimeException ve)
        {
            // Ignore expected exception
        }
        try
        {
            new Direction3d(DoubleVector.instantiate(new double[] {roll, pitch, yaw, pitch}, DirectionUnit.EAST_RADIAN,
                    StorageType.DENSE));
            fail("Long vector should have thrown an exception");
        }
        catch (ValueRuntimeException ve)
        {
            // Ignore expected exception
        }
        try
        {
            new Direction3d(
                    DoubleVector.instantiate(new double[] {roll, pitch}, DirectionUnit.EAST_RADIAN, StorageType.SPARSE));
            fail("Short vector should have thrown an exception");
        }
        catch (ValueRuntimeException ve)
        {
            // Ignore expected exception
        }
        try
        {
            new Direction3d(DoubleVector.instantiate(new double[] {roll, pitch, yaw, pitch}, DirectionUnit.EAST_RADIAN,
                    StorageType.SPARSE));
            fail("Long vector should have thrown an exception");
        }
        catch (ValueRuntimeException ve)
        {
            // Ignore expected exception
        }
        assertTrue("toString contains class name", r3d.toString().contains("Rotation3d"));
        Angle3d a3d = new Angle3d(roll, pitch, yaw, AngleUnit.RADIAN);
        checkRotation3d(a3d, roll, pitch, yaw);
        a3d = new Angle3d(DoubleVector.instantiate(new double[] {roll, pitch, yaw}, AngleUnit.RADIAN, StorageType.DENSE));
        checkRotation3d(a3d, roll, pitch, yaw);
        a3d = new Angle3d(DoubleVector.instantiate(new double[] {roll, pitch, yaw}, AngleUnit.RADIAN, StorageType.SPARSE));
        checkRotation3d(a3d, roll, pitch, yaw);
        a3d = new Angle3d(new Angle(roll, AngleUnit.RADIAN), new Angle(pitch, AngleUnit.RADIAN),
                new Angle(yaw, AngleUnit.RADIAN));
        checkRotation3d(a3d, roll, pitch, yaw);
        try
        {
            new Angle3d(DoubleVector.instantiate(new double[] {roll, pitch}, AngleUnit.RADIAN, StorageType.DENSE));
            fail("Short vector should have thrown an exception");
        }
        catch (ValueRuntimeException ve)
        {
            // Ignore expected exception
        }
        try
        {
            new Angle3d(DoubleVector.instantiate(new double[] {roll, pitch, yaw, pitch}, AngleUnit.RADIAN, StorageType.DENSE));
            fail("Long vector should have thrown an exception");
        }
        catch (ValueRuntimeException ve)
        {
            // Ignore expected exception
        }
        try
        {
            new Angle3d(DoubleVector.instantiate(new double[] {roll, pitch}, AngleUnit.RADIAN, StorageType.SPARSE));
            fail("Short vector should have thrown an exception");
        }
        catch (ValueRuntimeException ve)
        {
            // Ignore expected exception
        }
        try
        {
            new Angle3d(DoubleVector.instantiate(new double[] {roll, pitch, yaw, pitch}, AngleUnit.RADIAN, StorageType.SPARSE));
            fail("Long vector should have thrown an exception");
        }
        catch (ValueRuntimeException ve)
        {
            // Ignore expected exception
        }
        assertTrue("toString contains class name", a3d.toString().contains("Angle3d"));
    }

    /**
     * Verify the values of the fields in a Direction3d.
     * @param r3da Direction3d; the Direction3d
     * @param roll double; the expected roll value
     * @param pitch double; the expected pitch value
     * @param yaw double; the expected yaw value
     */
    private void checkRotation3d(final Direction3d r3da, final double roll, final double pitch, final double yaw)
    {
        assertEquals("roll", roll, r3da.getRoll().si, 0.00001);
        assertEquals("pitch", pitch, r3da.getPitch().si, 0.00001);
        assertEquals("yaw", yaw, r3da.getYaw().si, 0.00001);
    }

    /**
     * Verify the values of the fields in a Angle3d.
     * @param r3dr Angle3d; the Angle3d
     * @param roll double; the expected roll value
     * @param pitch double; the expected pitch value
     * @param yaw double; the expected yaw value
     */
    private void checkRotation3d(final Angle3d r3dr, final double roll, final double pitch, final double yaw)
    {
        assertEquals("roll", roll, r3dr.getRoll().si, 0.00001);
        assertEquals("pitch", pitch, r3dr.getPitch().si, 0.00001);
        assertEquals("yaw", yaw, r3dr.getYaw().si, 0.00001);
    }

}

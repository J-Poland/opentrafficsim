package org.opentrafficsim.base.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.line.Polygon2d;
import org.djutils.draw.point.OrientedPoint2d;
import org.djutils.draw.point.Point2d;
import org.junit.jupiter.api.Test;

/**
 * Test for OtsLocatable, mainly the static methods.
 * <p>
 * Copyright (c) 2024-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class OtsLocatableTest
{

    /** Contour. */
    private static final Polygon2d CONTOUR =
            new Polygon2d(new double[] {9.0, 9.0, 11.0, 11.0}, new double[] {22.0, 18.0, 18.0, 22.0});

    /**
     * General test.
     */
    @Test
    public void test()
    {
        TestLocatable locatable = new TestLocatable();

        locatable.getShape();

        // bounds and polygon
        Polygon2d relativePolygon = new Polygon2d(new double[] {2.0, -2.0, -2.0, 2.0}, new double[] {1.0, 1.0, -1.0, -1.0});
        pointsEqual(relativePolygon.getPointList(), OtsLocatable.relativeContour(locatable).getPointList());
        pointsEqual(locatable.getBounds().getPointList(), OtsLocatable.contourAsBounds(locatable).getPointList());
        assertEquals(CONTOUR, OtsLocatable.boundsAsContour(locatable));

        // line transformation
        PolyLine2d line = new PolyLine2d(new double[] {7.0, 7.0}, new double[] {0.5, 3.5});
        PolyLine2d transformed = OtsLocatable.transformLine(line, new Point2d(2.0, 2.0));
        assertEquals(transformed.getFirst().x, 5.0, 0.001);
        assertEquals(transformed.getFirst().y, -1.5, 0.001);
        assertEquals(transformed.getLast().x, 5.0, 0.001);
        assertEquals(transformed.getLast().y, 1.5, 0.001);

        transformed = OtsLocatable.transformLine(line, new OrientedPoint2d(2.0, 2.0, Math.PI));
        assertEquals(transformed.getFirst().x, -5.0, 0.001);
        assertEquals(transformed.getFirst().y, 1.5, 0.001);
        assertEquals(transformed.getLast().x, -5.0, 0.001);
        assertEquals(transformed.getLast().y, -1.5, 0.001);
    }

    /**
     * Test locatable class.
     */
    private final class TestLocatable implements OtsLocatable
    {
        /** {@inheritDoc} */
        @Override
        public Polygon2d getContour()
        {
            return CONTOUR;
        }

        /** {@inheritDoc} */
        @Override
        public OrientedPoint2d getLocation()
        {
            return new OrientedPoint2d(10.0, 20.0, 0.5 * Math.PI);
        }

        /** {@inheritDoc} */
        @Override
        public Bounds2d getBounds()
        {
            return new Bounds2d(4.0, 2.0);
        }
    }

    /**
     * Check points are equal.
     * @param points1 points 1
     * @param points2 points 2
     */
    private static void pointsEqual(final List<Point2d> points1, final List<Point2d> points2)
    {
        for (int i = 0; i < points1.size(); i++)
        {
            assertEquals(points2.get(i).x, points1.get(i).x, 0.001);
            assertEquals(points2.get(i).y, points1.get(i).y, 0.001);
        }
    }

}
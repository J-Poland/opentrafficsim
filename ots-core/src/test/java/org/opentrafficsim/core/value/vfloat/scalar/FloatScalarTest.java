package org.opentrafficsim.core.value.vfloat.scalar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.opentrafficsim.core.unit.AreaUnit;
import org.opentrafficsim.core.unit.LengthUnit;
import org.opentrafficsim.core.unit.TemperatureUnit;
import org.opentrafficsim.core.unit.TimeUnit;
import org.opentrafficsim.core.value.Relative;

/**
 * <p>
 * Copyright (c) 2002-2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The OpenTrafficSim project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Jun 25, 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class FloatScalarTest
{
    /**
     * Test creator, verify the various fields in the created objects, test conversions to other units
     */
    @SuppressWarnings("static-method")
    @Test
    public void basics()
    {
        TemperatureUnit tempUnit = TemperatureUnit.DEGREE_CELSIUS;
        float value = 38.0f;
        FloatScalar.Abs<TemperatureUnit> temperatureFS = new FloatScalar.Abs<TemperatureUnit>(value, tempUnit);
        assertEquals("Unit should be Celsius", tempUnit, temperatureFS.getUnit());
        assertEquals("Value is what we put in", value, temperatureFS.getValueInUnit(), 0.0001);
        assertEquals("Value in SI is equivalent in Kelvin", 311.15f, temperatureFS.getValueSI(), 0.05);
        assertEquals("Value in Fahrenheit", 100.4f, temperatureFS.getValueInUnit(TemperatureUnit.DEGREE_FAHRENHEIT),
                0.1);
        FloatScalar.Abs<TemperatureUnit> u2 = new FloatScalar.Abs<TemperatureUnit>(temperatureFS);
        //temperatureFS.setDisplayUnit(TemperatureUnit.DEGREE_FAHRENHEIT);
        //assertEquals("Unit should now be Fahrenheit", TemperatureUnit.DEGREE_FAHRENHEIT, temperatureFS.getUnit());
        //assertEquals("Value in unit is now the equivalent in Fahrenheit", 100.4f, temperatureFS.getValueInUnit(), 0.05);
        assertEquals("Value in SI is equivalent in Kelvin", 311.15f, temperatureFS.getValueSI(), 0.1);
        assertEquals("Value in Fahrenheit", 100.4f, temperatureFS.getValueInUnit(TemperatureUnit.DEGREE_FAHRENHEIT),
                0.1);
        assertTrue("Value is absolute", temperatureFS.isAbsolute());
        assertFalse("Value is absolute", temperatureFS.isRelative());
        assertEquals("Unit of copy made before calling setUnit should be unchanged", tempUnit, u2.getUnit());

        LengthUnit lengthUnit = LengthUnit.INCH;
        value = 12f;
        FloatScalar.Rel<LengthUnit> lengthFS = new FloatScalar.Rel<LengthUnit>(value, lengthUnit);
        //System.out.println("lengthFS is " + lengthFS);
        assertEquals("Unit should be Inch", lengthUnit, lengthFS.getUnit());
        assertEquals("Value is what we put in", value, lengthFS.getValueInUnit(), 0.0001);
        assertEquals("Value in SI is equivalent in Meter", 0.3048f, lengthFS.getValueSI(), 0.0005);
        assertEquals("Value in Foot", 1f, lengthFS.getValueInUnit(LengthUnit.FOOT), 0.0001);
        FloatScalar.Rel<LengthUnit> copy = new FloatScalar.Rel<LengthUnit>(lengthFS);
        //lengthFS.setDisplayUnit(LengthUnit.MILLIMETER);
        //assertEquals("Unit should not be Millimeter", LengthUnit.MILLIMETER, lengthFS.getUnit());
        assertEquals("Unit of copy should still be in Inch", LengthUnit.INCH, copy.getUnit());
    }

    /**
     * Check that copy really performs a deep copy.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testCopyAbs()
    {
        MutableFloatScalar.Abs<TemperatureUnit> value = new MutableFloatScalar.Abs<TemperatureUnit>(10, TemperatureUnit.DEGREE_CELSIUS);
        MutableFloatScalar.Abs<TemperatureUnit> copy = value.copy();
        assertEquals("Copy should have same value", value.getValueSI(), copy.getValueSI(), 0.0001);
        assertTrue("Copy should be equal to value", value.equals(copy));
        assertTrue("Value should be equal to copy", copy.equals(value));
        value.set(new FloatScalar.Abs<TemperatureUnit>(20, TemperatureUnit.DEGREE_CELSIUS));
        assertFalse("Copy should have same value", value.getValueSI() == copy.getValueSI());
        assertFalse("Copy should be equal to value", value.equals(copy));
        assertFalse("Value should be equal to copy", copy.equals(value));        
    }

    /**
     * Check that copy really performs a deep copy.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testCopyRel()
    {
        MutableFloatScalar.Rel<TemperatureUnit> value = new MutableFloatScalar.Rel<TemperatureUnit>(10, TemperatureUnit.DEGREE_CELSIUS);
        MutableFloatScalar.Rel<TemperatureUnit> copy = value.copy();
        assertEquals("Copy should have same value", value.getValueSI(), copy.getValueSI(), 0.0001);
        assertTrue("Copy should be equal to value", value.equals(copy));
        assertTrue("Value should be equal to copy", copy.equals(value));
        value.set(new MutableFloatScalar.Rel<TemperatureUnit>(20, TemperatureUnit.DEGREE_CELSIUS));
        assertFalse("Copy should have same value", value.getValueSI() == copy.getValueSI());
        assertFalse("Copy should be equal to value", value.equals(copy));
        assertFalse("Value should be equal to copy", copy.equals(value));        
    }

    /**
     * Test plus, minus, etc.
     */
    @SuppressWarnings("static-method")
    @Test
    public void simpleArithmetic()
    {
        float leftValue = 123.456f;
        float rightValue = 21.098f;
        float rightValue2 = 1.0987f;
        FloatScalar.Abs<LengthUnit> leftAbs = new FloatScalar.Abs<LengthUnit>(leftValue, LengthUnit.METER);
        FloatScalar.Rel<LengthUnit> right = new FloatScalar.Rel<LengthUnit>(rightValue, LengthUnit.INCH);
        FloatScalar.Rel<LengthUnit> right2 = new FloatScalar.Rel<LengthUnit>(rightValue2, LengthUnit.MILLIMETER);
        MutableFloatScalar.Abs<LengthUnit> sum = MutableFloatScalar.plus(leftAbs, right);
        assertEquals("result should be in METER", LengthUnit.METER, sum.getUnit());
        assertEquals("value of result should be sum of meter equivalent of values", leftValue + rightValue * 0.0254, sum.getValueSI(), 0.0001);
        FloatScalar.Rel<LengthUnit> leftRel = new FloatScalar.Rel<LengthUnit>(leftValue, LengthUnit.METER);
        MutableFloatScalar.Rel<LengthUnit> sum2 = MutableFloatScalar.plus(LengthUnit.MILLIMETER, leftRel, right, right2);
        assertEquals("result should be in MILLIMETER", LengthUnit.MILLIMETER, sum2.getUnit());
        assertEquals("value in SI should be sum of meter equivalent of values", leftValue + rightValue * 0.0254 + rightValue2 / 1000, sum2.getValueSI(), 0.0001);
        assertEquals("value in \"own\" unit should be equivalent in MILLIMETER", 1000 * (leftValue + rightValue * 0.0254) + rightValue2, sum2.getValueInUnit(), 0.1);
        MutableFloatScalar.Abs<LengthUnit> difference = MutableFloatScalar.minus(leftAbs, right, right2);
        assertEquals("result should be in METER", LengthUnit.METER, difference.getUnit());
        assertEquals("value in SI should be sum of meter equivalent of values", leftValue - rightValue * 0.0254 - rightValue2 / 1000, difference.getValueSI(), 0.0001);
        assertEquals("value in \"own\" unit should be equivalent in METER", leftValue - rightValue * 0.0254 - rightValue2 / 1000, difference.getValueInUnit(), 0.1);
        MutableFloatScalar.Rel<LengthUnit> differenceRel = MutableFloatScalar.minus(leftRel, right, right2);
        assertEquals("result should be in METER", LengthUnit.METER, differenceRel.getUnit());
        assertEquals("value in SI should be sum of meter equivalent of values", leftValue - rightValue * 0.0254 - rightValue2 / 1000, differenceRel.getValueSI(), 0.0001);
        assertEquals("value in \"own\" unit should be equivalent in METER", leftValue - rightValue * 0.0254 - rightValue2 / 1000, differenceRel.getValueInUnit(), 0.001);
        differenceRel = MutableFloatScalar.minus(sum.immutable(), leftAbs);
        assertEquals("result should be in METER", LengthUnit.METER, difference.getUnit());
        assertEquals("value of result should be minus leftValue", rightValue * 0.0254, differenceRel.getValueSI(), 0.0001);
               MutableFloatScalar.Abs<?> surface = MutableFloatScalar.multiply(leftAbs, difference.immutable());
        //System.out.println("surface is " + surface);
        assertEquals("Surface should be in square meter", AreaUnit.SQUARE_METER.getSICoefficientsString(), surface.getUnit().getSICoefficientsString());
        assertEquals("Surface should be equal to the product of contributing values", leftAbs.getValueSI() * difference.getValueSI(), surface.getValueSI(), 0.05);
        MutableFloatScalar.Rel<?> relSurface = MutableFloatScalar.multiply(right, right2);
        assertEquals("Surface should be in square meter", AreaUnit.SQUARE_METER.getSICoefficientsString(), relSurface.getUnit().getSICoefficientsString());
        assertEquals("Surface should be equal to the product of contributing values", right.getValueSI() * right2.getValueSI(), relSurface.getValueSI(), 0.00000005);
        assertTrue("FloatScalar should be equal to itself", leftAbs.equals(leftAbs));
        FloatScalar<?> copy = new FloatScalar.Abs<LengthUnit>(leftAbs);
        assertTrue("Copy of FloatScalar should be equal to itself", leftAbs.equals(copy));
        copy = new FloatScalar.Rel<LengthUnit>(leftAbs.getValueInUnit(), leftAbs.getUnit());
        assertTrue("this copy should be relative", copy instanceof Relative);
        assertFalse("Relative can not be equal to (otherwise equal) absolute", leftAbs.equals(copy));
        assertFalse("FloatScalar is not a String", copy.equals("String"));
        FloatScalar.Abs<TimeUnit> timeScalar = new FloatScalar.Abs<TimeUnit>(leftValue, TimeUnit.SECOND);
        assertEquals("Value should match", leftAbs.getValueSI(), timeScalar.getValueSI(), 0.0001);
        assertTrue("Both are absolute", leftAbs.isAbsolute() && timeScalar.isAbsolute());
        assertFalse("Absolute length is not equal to absolute time with same value", leftAbs.equals(timeScalar));
        MutableFloatScalar<LengthUnit> left = new MutableFloatScalar.Abs<LengthUnit>(leftValue, LengthUnit.METER);
        left.add(right);
        assertEquals("after add-and-becomes the type should not be changed", LengthUnit.METER, left.getUnit());
        assertEquals("after add-and-becomes the value should be changed", leftValue + rightValue * 0.0254, left.getValueSI(), 0.0001);
        left = new MutableFloatScalar.Abs<LengthUnit>(leftValue, LengthUnit.METER);
        left.subtract(right);
        assertEquals("after subtract-and-becomes the type should not be changed", LengthUnit.METER, left.getUnit());
        assertEquals("after subtract-and-becomes the value should be changed", leftValue - rightValue * 0.0254, left.getValueSI(), 0.0001);
    }

    /**
     * Test the math operations
     */
    @SuppressWarnings("static-method")
    @Test
    public void mathMethods()
    {
        float[] inputValues = {-10f, -2f, -1f, -0.5f, -0.1f, 0f, 0.1f, 0.5f, 1f, 2f, 10f};
        for (float inputValue : inputValues)
        {
            MutableFloatScalar.Rel<LengthUnit> fs;
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.abs();
            MathTester.tester(inputValue, "abs", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return Math.abs(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.acos();
            MathTester.tester(inputValue, "acos", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.acos(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.asin();
            MathTester.tester(inputValue, "asin", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.asin(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.atan();
            MathTester.tester(inputValue, "atan", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.atan(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.cbrt();
            MathTester.tester(inputValue, "cbrt", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.cbrt(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.ceil();
            MathTester.tester(inputValue, "ceil", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.ceil(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.cos();
            MathTester.tester(inputValue, "cos", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.cos(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.cosh();
            MathTester.tester(inputValue, "cosh", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.cosh(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.exp();
            MathTester.tester(inputValue, "exp", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.exp(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.expm1();
            MathTester.tester(inputValue, "expm1", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.expm1(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.floor();
            MathTester.tester(inputValue, "floor", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.floor(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.log();
            MathTester.tester(inputValue, "log", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.log(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.log10();
            MathTester.tester(inputValue, "log10", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.log10(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.log1p();
            MathTester.tester(inputValue, "log10", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.log1p(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.rint();
            MathTester.tester(inputValue, "rint", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.rint(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.round();
            MathTester.tester(inputValue, "round", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return Math.round(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.signum();
            MathTester.tester(inputValue, "signum", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return Math.signum(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.sin();
            MathTester.tester(inputValue, "sin", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.sin(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.sinh();
            MathTester.tester(inputValue, "sinh", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.sinh(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.sqrt();
            MathTester.tester(inputValue, "sqrt", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.sqrt(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.tan();
            MathTester.tester(inputValue, "tan", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.tan(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.tanh();
            MathTester.tester(inputValue, "tanh", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.tanh(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.toDegrees();
            MathTester.tester(inputValue, "toDegrees", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.toDegrees(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.toRadians();
            MathTester.tester(inputValue, "toRadians", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return (float) Math.toRadians(f);
                }
            });
            fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
            fs.inv();
            MathTester.tester(inputValue, "inv", fs, 0.001, new FloatToFloat()
            {
                public float function(float f)
                {
                    return 1 / f;
                }
            });
            for (int i = -10; i <= 10; i++)
            {
                final float exponent = i * 0.5f;
                fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
                fs.pow(exponent);
                MathTester.tester(inputValue, "pow(" + exponent + ")", fs, 0.001, new FloatToFloat()
                {
                    public float function(float f)
                    {
                        return (float) Math.pow(f, exponent);
                    }
                });
            }
            float[] constants = {-1000, -100, -10, 0, 10, 100, 1000};
            for (final float constant : constants)
            {
                fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
                fs.multiply(constant);
                MathTester.tester(inputValue, "multiply by " + constant, fs, 0.001, new FloatToFloat()
                {
                    public float function(float f)
                    {
                        return f * constant;
                    }
                });
                fs = new MutableFloatScalar.Rel<LengthUnit>(inputValue, LengthUnit.METER);
                fs.divide(constant);
                MathTester.tester(inputValue, "divide by " + constant, fs, 0.001, new FloatToFloat()
                {
                    public float function(float f)
                    {
                        return f / constant;
                    }
                });
            }
        }
    }

    /**
     * Interface encapsulating a function that takes a float and returns a float.
     * <p>
     * Copyright (c) 2002-2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
     * reserved.
     * <p>
     * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
     * <p>
     * The OpenTrafficSim project is distributed under the following BSD-style license:<br>
     * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
     * following conditions are met:
     * <ul>
     * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the
     * following disclaimer.</li>
     * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
     * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
     * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse
     * or promote products derived from this software without specific prior written permission.</li>
     * </ul>
     * This software is provided by the copyright holders and contributors "as is" and any express or implied
     * warranties, including, but not limited to, the implied warranties of merchantability and fitness for a particular
     * purpose are disclaimed. In no event shall the copyright holder or contributors be liable for any direct,
     * indirect, incidental, special, exemplary, or consequential damages (including, but not limited to, procurement of
     * substitute goods or services; loss of use, data, or profits; or business interruption) however caused and on any
     * theory of liability, whether in contract, strict liability, or tort (including negligence or otherwise) arising
     * in any way out of the use of this software, even if advised of the possibility of such damage.
     * @version Jun 23, 2014 <br>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     */
    interface FloatToFloat
    {
        /**
         * @param f float value
         * @return float value
         */
        float function(float f);
    }

    /**
     * <p>
     * Copyright (c) 2002-2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
     * reserved.
     * <p>
     * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
     * <p>
     * The OpenTrafficSim project is distributed under the following BSD-style license:<br>
     * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
     * following conditions are met:
     * <ul>
     * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the
     * following disclaimer.</li>
     * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
     * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
     * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse
     * or promote products derived from this software without specific prior written permission.</li>
     * </ul>
     * This software is provided by the copyright holders and contributors "as is" and any express or implied
     * warranties, including, but not limited to, the implied warranties of merchantability and fitness for a particular
     * purpose are disclaimed. In no event shall the copyright holder or contributors be liable for any direct,
     * indirect, incidental, special, exemplary, or consequential damages (including, but not limited to, procurement of
     * substitute goods or services; loss of use, data, or profits; or business interruption) however caused and on any
     * theory of liability, whether in contract, strict liability, or tort (including negligence or otherwise) arising
     * in any way out of the use of this software, even if advised of the possibility of such damage.
     * @version Jun 23, 2014 <br>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     */
    abstract static class MathTester
    {

        /**
         * @param inputValue Float; unprocessed value
         * @param operation String; description of method that is being tested
         * @param actualResult FloatScalar; the actual result of the operation
         * @param precision double expected accuracy
         * @param function FloatToFloat encapsulating function that converts one value in inputValues to the
         *            corresponding value in resultValues
         */
        public static void tester(final float inputValue, String operation, final MutableFloatScalar<?> actualResult,
                final double precision, final FloatToFloat function)
        {
            float expectedResult = function.function(inputValue);
            String description =
                    String.format("%s(%f)->%f should be equal to %f with precision %f", operation, inputValue,
                            expectedResult, actualResult.getValueSI(), precision);
            // System.out.println(description);
            assertEquals(description, expectedResult, actualResult.getValueSI(), precision);

        }

        /**
         * Function that takes a float value and returns a float value
         * @param in float value
         * @return float value
         */
        public abstract float function(float in);
    }

}

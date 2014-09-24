package org.opentrafficsim.core.value.vfloat.scalar;

import org.opentrafficsim.core.unit.SICoefficients;
import org.opentrafficsim.core.unit.SIUnit;
import org.opentrafficsim.core.unit.Unit;
import org.opentrafficsim.core.value.Absolute;
import org.opentrafficsim.core.value.Relative;
import org.opentrafficsim.core.value.ValueUtil;
import org.opentrafficsim.core.value.vfloat.FloatMathFunctions;

/**
 * <p>
 * Copyright (c) 2013-2014 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/node/13">OpenTrafficSim License</a>.
 * <p>
 * @version Sep 5, 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <U> Unit
 */
public abstract class MutableFloatScalar<U extends Unit<U>> extends FloatScalar<U> implements FloatMathFunctions
{
    /** */
    private static final long serialVersionUID = 20140905L;

    /**
     * Create a new MutableFloatScalar.
     * @param unit Unit; the unit of the new MutableFloatScalar
     */
    public MutableFloatScalar(final U unit)
    {
        super(unit);
    }

    /**
     * @param <U> Unit
     */
    public static class Abs<U extends Unit<U>> extends MutableFloatScalar<U> implements Absolute, Comparable<Abs<U>>
    {
        /** */
        private static final long serialVersionUID = 20140905L;

        /**
         * Create a new Absolute MutableFloatScalar.
         * @param value float; the value of the new Absolute MutableFloatScalar
         * @param unit Unit; the unit of the new Absolute MutableFloatScalar
         */
        protected Abs(final float value, final U unit)
        {
            super(unit);
            // System.out.println("Created Abs");
            initialize(value);
        }

        /**
         * Create a new Absolute MutableFloatScalar from an existing immutable one.
         * @param value FloatScalar.Abs; the reference
         */
        public Abs(final FloatScalar.Abs<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Abs");
            initialize(value);
        }

        /**
         * Create a new Absolute MutableFloatScalar from an existing one.
         * @param value MutableFloatScalar.Abs; the reference
         */
        public Abs(final MutableFloatScalar.Abs<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Abs");
            initialize(value);
        }

        /** {@inheritDoc} */
        @Override
        public final FloatScalar.Abs<U> immutable()
        {
            return new FloatScalar.Abs<U>(this);
        }

        /** {@inheritDoc} */
        @Override
        public final MutableFloatScalar.Abs<U> mutable()
        {
            return new MutableFloatScalar.Abs<U>(this);
        }

        /** {@inheritDoc} */
        @Override
        public final MutableFloatScalar.Abs<U> copy()
        {
            return new MutableFloatScalar.Abs<U>(this);
        }

        /** {@inheritDoc} */
        @Override
        public final int compareTo(final Abs<U> o)
        {
            return new Float(this.getValueSI()).compareTo(o.getValueSI());
        }

    }

    /**
     * @param <U> Unit
     */
    public static class Rel<U extends Unit<U>> extends MutableFloatScalar<U> implements Relative, Comparable<Rel<U>>
    {
        /** */
        private static final long serialVersionUID = 20140905L;

        /**
         * Create a new Relative MutableFloatScalar.
         * @param value float; the value of the new Relative MutableFloatScalar
         * @param unit Unit; the unit of the new MutableFloatScalar
         */
        public Rel(final float value, final U unit)
        {
            super(unit);
            // System.out.println("Created Rel");
            initialize(value);
        }

        /**
         * Create a new Relative MutableFloatScalar from an existing immutable one.
         * @param value FloatScalar.Rel; the reference
         */
        public Rel(final FloatScalar.Rel<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Rel");
            initialize(value);
        }

        /**
         * Create a new Relative MutableFloatScalar from an existing one.
         * @param value MutableFloatScalar.Rel; the reference
         */
        public Rel(final MutableFloatScalar.Rel<U> value)
        {
            super(value.getUnit());
            // System.out.println("Created Rel");
            initialize(value);
        }

        /** {@inheritDoc} */
        @Override
        public final FloatScalar.Rel<U> immutable()
        {
            return new FloatScalar.Rel<U>(this);
        }

        /** {@inheritDoc} */
        @Override
        public final MutableFloatScalar.Rel<U> mutable()
        {
            return new MutableFloatScalar.Rel<U>(this);
        }

        /** {@inheritDoc} */
        @Override
        public final MutableFloatScalar.Rel<U> copy()
        {
            return new MutableFloatScalar.Rel<U>(this);
        }

        /** {@inheritDoc} */
        @Override
        public final int compareTo(final Rel<U> o)
        {
            return new Float(this.getValueSI()).compareTo(o.getValueSI());
        }

    }

    /**
     * Create a mutable version of this FloatScalar. <br>
     * The mutable version is created as a deep copy of this. Delayed copying is not worthwhile for a Scalar.
     * @return MutableFloatScalar; mutable version of this FloatScalar
     */
    public abstract FloatScalar<U> immutable();

    /**
     * @param valueSI the value to store in the cell
     */
    final void setSI(final float valueSI)
    {
        setValueSI(valueSI);
    }

    /**
     * @param value the strongly typed value to store in the cell
     */
    final void set(final FloatScalar<U> value)
    {
        setValueSI(value.getValueSI());
    }

    /**
     * @param value the value to store in the cell
     * @param valueUnit the unit of the value.
     */
    final void setInUnit(final float value, final U valueUnit)
    {
        setValueSI((float) ValueUtil.expressAsSIUnit(value, valueUnit));
    }

    /**********************************************************************************/
    /******************************* NON-STATIC METHODS *******************************/
    /**********************************************************************************/

    /**
     * Add another value to this value. Only relative values are allowed; adding an absolute value to an absolute value is not
     * allowed. Adding an absolute value to an existing relative value would require the result to become absolute, which is a
     * type change that is impossible. For that operation, use a static method.
     * @param value the value to add
     */
    public final void add(final FloatScalar.Rel<U> value)
    {
        setValueSI(getValueSI() + value.getValueSI());
    }

    /**
     * Subtract another value from this value. Only relative values are allowed; subtracting an absolute value from a relative
     * value is not allowed. Subtracting an absolute value from an existing absolute value would require the result to become
     * relative, which is a type change that is impossible. For that operation, use a static method.
     * @param value the value to subtract
     */
    public final void subtract(final FloatScalar.Rel<U> value)
    {
        setValueSI(getValueSI() - value.getValueSI());
    }

    /**********************************************************************************/
    /********************************* STATIC METHODS *********************************/
    /**********************************************************************************/

    /**
     * Increment the stored value by a specified amount.
     * @param increment float; the amount by which to increment the stored value
     * @return this; the modified MutableFloatScalar
     */
    protected final FloatScalar<?> incrementBy(final FloatScalar<?> increment)
    {
        setValueSI(getValueSI() + increment.getValueSI());
        return this;
    }
    
    /**
     * Add a number of relative values to an absolute value. Return a new instance of the value. The unit of the return value
     * will be the unit of the first argument. Because of type erasure of generics, the method cannot check whether an array of
     * arguments submitted to the varargs has a mixed-unit content at runtime.
     * @param valueAbs the absolute base value
     * @param valuesRel zero or more values to add to the absolute value
     * @return the sum of the values as an absolute value
     * @param <U> Unit; the unit of the parameters and the result
     */
    @SafeVarargs
    public static <U extends Unit<U>> MutableFloatScalar.Abs<U> plus(final FloatScalar.Abs<U> valueAbs,
            final FloatScalar.Rel<U>... valuesRel)
    {
        MutableFloatScalar.Abs<U> result = new MutableFloatScalar.Abs<U>(valueAbs);
        for (FloatScalar.Rel<U> v : valuesRel)
        {
            result.incrementBy(v);
        }
        return result;
    }

    /**
     * Add a number of relative values. Return a new instance of the value. Because of type erasure of generics, the method
     * cannot check whether an array of arguments submitted to the varargs has a mixed-unit content at runtime.
     * @param targetUnit the unit of the sum
     * @param valuesRel zero or more values to add
     * @return the sum of the values as a relative value
     * @param <U> Unit; the unit of the parameters and the result
     */
    @SafeVarargs
    public static <U extends Unit<U>> MutableFloatScalar.Rel<U> plus(final U targetUnit, final FloatScalar.Rel<U>... valuesRel)
    {
        MutableFloatScalar.Rel<U> result = new MutableFloatScalar.Rel<U>(0.0f, targetUnit);
        for (FloatScalar.Rel<U> v : valuesRel)
        {
            result.incrementBy(v);
        }
        return result;
    }

    /**
     * Decrement the stored value by a specified amount.
     * @param decrement float; the amount by which to increment the stored value
     * @return this; the modified MutableFloatScalar
     */
    protected final FloatScalar<?> decrementBy(final FloatScalar<?> decrement)
    {
        setValueSI(getValueSI() - decrement.getValueSI());
        return this;
    }
    
    /**
     * Subtract a number of relative values from an absolute value. Return a new instance of the value. The unit of the return
     * value will be the unit of the first argument. Because of type erasure of generics, the method cannot check whether an
     * array of arguments submitted to the varargs has a mixed-unit content at runtime.
     * @param valueAbs the absolute base value
     * @param valuesRel zero or more values to subtract from the absolute value
     * @return the resulting value as an absolute value
     * @param <U> Unit; the unit of the parameters and the result
     */
    @SafeVarargs
    public static <U extends Unit<U>> MutableFloatScalar.Abs<U> minus(final FloatScalar.Abs<U> valueAbs,
            final FloatScalar.Rel<U>... valuesRel)
    {
        MutableFloatScalar.Abs<U> value = new MutableFloatScalar.Abs<U>(valueAbs);
        for (FloatScalar.Rel<U> v : valuesRel)
        {
            value.decrementBy(v);
        }
        return value;
    }

    /**
     * Subtract a number of relative values from a relative value. Return a new instance of the value. The unit of the value
     * will be the unit of the first argument. Because of type erasure of generics, the method cannot check whether an array of
     * arguments submitted to the varargs has a mixed-unit content at runtime.
     * @param valueRel the relative base value
     * @param valuesRel zero or more values to subtract from the first value
     * @return the resulting value as a relative value
     * @param <U> Unit; the unit of the parameters and the result
     */
    @SafeVarargs
    public static <U extends Unit<U>> MutableFloatScalar.Rel<U> minus(final FloatScalar.Rel<U> valueRel,
            final FloatScalar.Rel<U>... valuesRel)
    {
        MutableFloatScalar.Rel<U> value = new MutableFloatScalar.Rel<U>(valueRel);
        for (FloatScalar.Rel<U> v : valuesRel)
        {
            value.decrementBy(v);
        }
        return value;
    }

    /**
     * Subtract two absolute values. Return a new instance of a relative value of the difference. The unit of the value will be
     * the unit of the first argument.
     * @param valueAbs1 value 1
     * @param valueAbs2 value 2
     * @return the difference of the two absolute values as a relative value
     * @param <U> Unit; the unit of the parameters and the result
     */
    public static <U extends Unit<U>> MutableFloatScalar.Rel<U> minus(final FloatScalar.Abs<U> valueAbs1,
            final FloatScalar.Abs<U> valueAbs2)
    {
        MutableFloatScalar.Rel<U> result = new MutableFloatScalar.Rel<U>(valueAbs1.getValueSI(), valueAbs1.getUnit());
        result.setValueSI(valueAbs1.getValueSI() - valueAbs2.getValueSI());
        return result;
    }

    /**
     * Multiply two values; the result is a new instance with a different (existing or generated) SI unit.
     * @param valueAbs1 value 1
     * @param valueAbs2 value 2
     * @return the product of the two absolute values as an absolute value
     */
    public static MutableFloatScalar.Abs<SIUnit> multiply(final FloatScalar.Abs<?> valueAbs1, final FloatScalar.Abs<?> valueAbs2)
    {
        SIUnit targetUnit =
                Unit.lookupOrCreateSIUnitWithSICoefficients(SICoefficients.multiply(valueAbs1.getUnit().getSICoefficients(),
                        valueAbs2.getUnit().getSICoefficients()).toString());
        return new MutableFloatScalar.Abs<SIUnit>(valueAbs1.getValueSI() * valueAbs2.getValueSI(), targetUnit);
    }

    /**
     * Multiply two values; the result is a new instance with a different (existing or generated) SI unit.
     * @param valueRel1 value 1
     * @param valueRel2 value 2
     * @return the product of the two relative values as a relative value
     */
    public static MutableFloatScalar.Rel<SIUnit> multiply(final FloatScalar.Rel<?> valueRel1, final FloatScalar.Rel<?> valueRel2)
    {
        SIUnit targetUnit =
                Unit.lookupOrCreateSIUnitWithSICoefficients(SICoefficients.multiply(valueRel1.getUnit().getSICoefficients(),
                        valueRel2.getUnit().getSICoefficients()).toString());
        return new MutableFloatScalar.Rel<SIUnit>(valueRel1.getValueSI() * valueRel2.getValueSI(), targetUnit);
    }

    /**
     * Divide two values; the result is a new instance with a different (existing or generated) SI unit.
     * @param valueAbs1 value 1
     * @param valueAbs2 value 2
     * @return the division of the two absolute values as an absolute value
     */
    public static FloatScalar.Abs<SIUnit> divide(final FloatScalar.Abs<?> valueAbs1, final FloatScalar.Abs<?> valueAbs2)
    {
        SIUnit targetUnit =
                Unit.lookupOrCreateSIUnitWithSICoefficients(SICoefficients.divide(valueAbs1.getUnit().getSICoefficients(),
                        valueAbs2.getUnit().getSICoefficients()).toString());
        return new FloatScalar.Abs<SIUnit>(valueAbs1.getValueSI() / valueAbs2.getValueSI(), targetUnit);
    }

    /**
     * Divide two values; the result is a new instance with a different (existing or generated) SI unit.
     * @param valueRel1 value 1
     * @param valueRel2 value 2
     * @return the division of the two two relative values as a relative value
     */
    public static FloatScalar.Rel<SIUnit> divide(final FloatScalar.Rel<?> valueRel1, final FloatScalar.Rel<?> valueRel2)
    {
        SIUnit targetUnit =
                Unit.lookupOrCreateSIUnitWithSICoefficients(SICoefficients.divide(valueRel1.getUnit().getSICoefficients(),
                        valueRel2.getUnit().getSICoefficients()).toString());
        return new FloatScalar.Rel<SIUnit>(valueRel1.getValueSI() / valueRel2.getValueSI(), targetUnit);
    }

    /**********************************************************************************/
    /********************************** MATH METHODS **********************************/
    /**********************************************************************************/

    /** {@inheritDoc} */
    @Override
    public final void abs()
    {
        setValueSI(Math.abs(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void acos()
    {
        // TODO: dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.acos(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void asin()
    {
        // TODO: dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.asin(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void atan()
    {
        // TODO: dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.atan(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void cbrt()
    {
        // TODO: dimension for all SI coefficients / 3.
        setValueSI((float) Math.cbrt(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void ceil()
    {
        setValueSI((float) Math.ceil(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void cos()
    {
        // TODO: dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.cos(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void cosh()
    {
        // TODO: dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.cosh(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void exp()
    {
        // TODO: dimensionless result (SIUnit.ONE).
        setValueSI((float) Math.exp(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: dimensionless result (SIUnit.ONE).
    @Override
    public final void expm1()
    {
        setValueSI((float) Math.expm1(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void floor()
    {
        setValueSI((float) Math.floor(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: dimensionless result (SIUnit.ONE).
    @Override
    public final void log()
    {
        setValueSI((float) Math.log(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: dimensionless result (SIUnit.ONE).
    @Override
    public final void log10()
    {
        setValueSI((float) Math.log10(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: dimensionless result (SIUnit.ONE).
    @Override
    public final void log1p()
    {
        setValueSI((float) Math.log1p(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: SI unit with coefficients * x.
    @Override
    public final void pow(final double x)
    {
        setValueSI((float) Math.pow(getValueSI(), x));
    }

    /** {@inheritDoc} */
    @Override
    public final void rint()
    {
        setValueSI((float) Math.rint(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void round()
    {
        setValueSI(Math.round(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: dimensionless result (SIUnit.ONE).
    @Override
    public final void signum()
    {
        setValueSI(Math.signum(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: dimensionless result (SIUnit.ONE).
    @Override
    public final void sin()
    {
        setValueSI((float) Math.sin(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: dimensionless result (SIUnit.ONE).
    @Override
    public final void sinh()
    {
        setValueSI((float) Math.sinh(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: unit coefficients / 2.
    @Override
    public final void sqrt()
    {
        setValueSI((float) Math.sqrt(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: dimensionless result (SIUnit.ONE).
    @Override
    public final void tan()
    {
        setValueSI((float) Math.tan(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: dimensionless result (SIUnit.ONE).
    @Override
    public final void tanh()
    {
        setValueSI((float) Math.tanh(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void toDegrees()
    {
        setValueSI((float) Math.toDegrees(getValueSI()));
    }

    /** {@inheritDoc} */
    @Override
    public final void toRadians()
    {
        setValueSI((float) Math.toRadians(getValueSI()));
    }

    /** {@inheritDoc} */

    // TODO: negate all coefficients in the Unit.
    @Override
    public final void inv()
    {
        setValueSI(1.0f / getValueSI());
    }

    /** {@inheritDoc} */
    @Override
    public final void multiply(final float constant)
    {
        setValueSI(getValueSI() * constant);
    }

    /** {@inheritDoc} */
    @Override
    public final void divide(final float constant)
    {
        setValueSI(getValueSI() / constant);
    }

}

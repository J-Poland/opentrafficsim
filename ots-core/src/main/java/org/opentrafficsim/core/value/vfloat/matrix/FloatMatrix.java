package org.opentrafficsim.core.value.vfloat.matrix;

import java.io.Serializable;

import org.opentrafficsim.core.unit.SICoefficients;
import org.opentrafficsim.core.unit.SIUnit;
import org.opentrafficsim.core.unit.Unit;
import org.opentrafficsim.core.value.Absolute;
import org.opentrafficsim.core.value.AbstractValue;
import org.opentrafficsim.core.value.DenseData;
import org.opentrafficsim.core.value.Format;
import org.opentrafficsim.core.value.Relative;
import org.opentrafficsim.core.value.SparseData;
import org.opentrafficsim.core.value.ValueException;
import org.opentrafficsim.core.value.ValueUtil;
import org.opentrafficsim.core.value.vfloat.scalar.FloatScalar;
import org.opentrafficsim.core.value.vfloat.vector.FloatVector;

import cern.colt.matrix.tfloat.FloatMatrix1D;
import cern.colt.matrix.tfloat.FloatMatrix2D;
import cern.colt.matrix.tfloat.algo.DenseFloatAlgebra;
import cern.colt.matrix.tfloat.algo.SparseFloatAlgebra;
import cern.colt.matrix.tfloat.impl.DenseFloatMatrix1D;
import cern.colt.matrix.tfloat.impl.DenseFloatMatrix2D;
import cern.colt.matrix.tfloat.impl.SparseFloatMatrix1D;
import cern.colt.matrix.tfloat.impl.SparseFloatMatrix2D;

/**
 * Immutable float matrix.
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
 * @version Sep 9, 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <U> Unit
 */
public abstract class FloatMatrix<U extends Unit<U>> extends AbstractValue<U> implements Serializable,
        ReadOnlyFloatMatrixFunctions<U>
{

    /** */
    private static final long serialVersionUID = 20140909L;

    /** the internal storage for the vector; internally they are stored in SI units; can be dense or sparse. */
    protected FloatMatrix2D matrixSI;

    /**
     * @param unit
     */
    protected FloatMatrix(U unit)
    {
        super(unit);
    }

    /**
     * @param <U> Unit
     */
    public abstract static class Abs<U extends Unit<U>> extends FloatMatrix<U> implements Absolute
    {
        /** */
        private static final long serialVersionUID = 20140905L;

        /**
         * Create a Abs.
         * @param unit
         */
        protected Abs(U unit)
        {
            super(unit);
        }

        /**
         * @param <U> Unit
         */
        public static class Dense<U extends Unit<U>> extends Abs<U> implements DenseData
        {
            /** */
            private static final long serialVersionUID = 20140905L;

            /**
             * For package internal use only.
             * @param values
             * @param unit
             */
            protected Dense(final FloatMatrix2D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values); // shallow copy
            }

            /**
             * @param values
             * @param unit
             * @throws ValueException
             */
            public Dense(final float[][] values, final U unit) throws ValueException
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values);
            }

            /**
             * @param values
             * @param unit
             * @throws ValueException
             */
            public Dense(final FloatScalar.Abs<U>[][] values) throws ValueException
            {
                super(checkNonEmpty(values)[0][0].getUnit());
                // System.out.println("Created Dense");
                initialize(values);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.matrix.FloatMatrix#mutable()
             */
            public MutableFloatMatrix.Abs.Dense<U> mutable()
            {
                return new MutableFloatMatrix.Abs.Dense<U>(this.matrixSI, this.unit);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.Matrix.ReadOnlyFloatMatrixFunctions#get(int)
             */
            @Override
            public FloatScalar<U> get(final int row, final int column) throws ValueException
            {
                return new FloatScalar.Abs<U>(getInUnit(row, column, this.unit), this.unit);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.matrix.AbstractFloatMatrix#createMatrix2D(int, int)
             */
            @Override
            protected FloatMatrix2D createMatrix2D(int rows, int columns)
            {
                return new DenseFloatMatrix2D(rows, columns);
            }

        }

        /**
         * @param <U> Unit
         */
        public static class Sparse<U extends Unit<U>> extends Abs<U> implements SparseData
        {
            /** */
            private static final long serialVersionUID = 20140905L;

            /**
             * For package internal use only
             * @param values
             * @param unit
             */
            protected Sparse(final FloatMatrix2D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values); // shallow copy
            }

            /**
             * Create a Dense Relative Immutable FloatMatrix
             * @param values
             * @param unit
             * @throws ValueException
             */
            public Sparse(final float[][] values, final U unit) throws ValueException
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values);
            }

            /**
             * @param values
             * @param unit
             * @throws ValueException
             */
            public Sparse(final FloatScalar.Abs<U>[][] values) throws ValueException
            {
                super(checkNonEmpty(values)[0][0].getUnit());
                // System.out.println("Created Sparse");
                initialize(values);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.matrix.FloatMatrix#mutable()
             */
            public MutableFloatMatrix.Abs.Sparse<U> mutable()
            {
                return new MutableFloatMatrix.Abs.Sparse<U>(this.matrixSI, this.unit);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.Matrix.ReadOnlyFloatMatrixFunctions#get(int)
             */
            @Override
            public FloatScalar<U> get(final int row, final int column) throws ValueException
            {
                return new FloatScalar.Rel<U>(getInUnit(row, column, this.unit), this.unit);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.matrix.AbstractFloatMatrix#createMatrix2D(int, int)
             */
            @Override
            protected FloatMatrix2D createMatrix2D(int rows, int columns)
            {
                return new DenseFloatMatrix2D(rows, columns);
            }
        }

    }

    /**
     * @param <U> Unit
     */
    public abstract static class Rel<U extends Unit<U>> extends FloatMatrix<U> implements Relative
    {
        /** */
        private static final long serialVersionUID = 20140905L;

        /**
         * Create a Sparse
         * @param unit
         */
        protected Rel(U unit)
        {
            super(unit);
        }

        /**
         * @param <U>
         */
        public static class Dense<U extends Unit<U>> extends Rel<U> implements DenseData
        {
            /** */
            private static final long serialVersionUID = 20140905L;

            /**
             * For package internal use only.
             * @param values
             * @param unit
             */
            protected Dense(final FloatMatrix2D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values); // shallow copy
            }

            /**
             * @param values
             * @param unit
             * @throws ValueException
             */
            public Dense(final float[][] values, final U unit) throws ValueException
            {
                super(unit);
                // System.out.println("Created Dense");
                initialize(values); // shallow copy
            }

            /**
             * @param values
             * @param unit
             * @throws ValueException
             */
            public Dense(final FloatScalar.Rel<U>[][] values) throws ValueException
            {
                super(checkNonEmpty(values)[0][0].getUnit());
                // System.out.println("Created Dense");
                initialize(values);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.matrix.FloatMatrix#mutable()
             */
            public MutableFloatMatrix.Rel.Dense<U> mutable()
            {
                return new MutableFloatMatrix.Rel.Dense<U>(this.matrixSI, this.unit);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.Matrix.ReadOnlyFloatMatrixFunctions#get(int)
             */
            @Override
            public FloatScalar<U> get(final int row, final int column) throws ValueException
            {
                return new FloatScalar.Rel<U>(getInUnit(row, column, this.unit), this.unit);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.matrix.AbstractFloatMatrix#createMatrix2D(int, int)
             */
            @Override
            protected FloatMatrix2D createMatrix2D(int rows, int columns)
            {
                return new SparseFloatMatrix2D(rows, columns);
            }

        }

        /**
         * @param <U> Unit
         */
        public static class Sparse<U extends Unit<U>> extends Rel<U> implements SparseData
        {
            /** */
            private static final long serialVersionUID = 20140905L;

            /**
             * For package internal use only
             * @param values
             * @param unit
             */
            protected Sparse(final FloatMatrix2D values, final U unit)
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values); // shallow copy
            }

            /**
             * Create a new Sparse Relative Immutable FloatMatrix
             * @param values
             * @param unit
             * @throws ValueException
             */
            public Sparse(final float[][] values, final U unit) throws ValueException
            {
                super(unit);
                // System.out.println("Created Sparse");
                initialize(values); // shallow copy
            }

            /**
             * @param values
             * @param unit
             * @throws ValueException
             */
            public Sparse(final FloatScalar.Rel<U>[][] values) throws ValueException
            {
                super(checkNonEmpty(values)[0][0].getUnit());
                // System.out.println("Created Sparse");
                initialize(values);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.matrix.FloatMatrix#mutable()
             */
            public MutableFloatMatrix.Rel.Sparse<U> mutable()
            {
                return new MutableFloatMatrix.Rel.Sparse<U>(this.matrixSI, this.unit);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.Matrix.ReadOnlyFloatMatrixFunctions#get(int)
             */
            @Override
            public FloatScalar<U> get(final int row, final int column) throws ValueException
            {
                return new FloatScalar.Rel<U>(getInUnit(row, column, this.unit), this.unit);
            }

            /**
             * @see org.opentrafficsim.core.value.vfloat.matrix.AbstractFloatMatrix#createMatrix2D(int, int)
             */
            @Override
            protected FloatMatrix2D createMatrix2D(int rows, int columns)
            {
                return new SparseFloatMatrix2D(rows, columns);
            }

        }
    }

    /**
     * Import the values and convert them into SI units.
     * @param values an array of values
     * @throws ValueException
     */
    protected void initialize(final float[][] values) throws ValueException
    {
        ensureRectangular(values);
        this.matrixSI = createMatrix2D(values.length, 0 == values.length ? 0 : values[0].length);
        if (this.unit.equals(this.unit.getStandardUnit()))
        {
            this.matrixSI.assign(values);
        }
        else
        {
            for (int row = 0; row < values.length; row++)
            {
                for (int column = 0; column < values[row].length; column++)
                {
                    safeSet(row, column, (float) expressAsSIUnit(values[row][column]));
                }
            }
        }
    }

    /**
     * @param values
     */
    protected void initialize(final FloatMatrix2D values)
    {
        this.matrixSI = values;
    }

    /**
     * Construct the vector and store the values in SI units.
     * @param values float[][] a 2D array of values for the constructor
     * @throws ValueException exception thrown when array with zero elements is offered
     */
    protected void initialize(final FloatScalar<U>[][] values) throws ValueException
    {
        ensureRectangularAndNonEmpty(values);
        this.matrixSI = createMatrix2D(values.length, values[0].length);
        for (int row = 0; row < values.length; row++)
        {
            for (int column = 0; column < values[0].length; column++)
                safeSet(row, column, values[row][column].getValueSI());
        }
    }

    /**
     * This method has to be implemented by each leaf class.
     * @param rows the number of rows in the matrix
     * @param columns the number of columns in the matrix
     * @return an instance of the right type of matrix (absolute / relative, dense / sparse, etc.).
     */
    protected abstract FloatMatrix2D createMatrix2D(final int rows, final int columns);

    /**
     * Create a float[][] array filled with the values in SI unit.
     * @return float[][]; array of values in SI unit
     */
    public float[][] getValuesSI()
    {
        return this.matrixSI.toArray(); // this makes a deep copy
    }

    /**
     * Create a float[][] array filled with the values in the original unit.
     * @return values in original unit
     */
    public float[][] getValuesInUnit()
    {
        return getValuesInUnit(this.unit);
    }

    /**
     * Create a float[][] array filled with the values in the specified unit.
     * @param targetUnit the unit to convert the values to
     * @return values in specific target unit
     */
    public float[][] getValuesInUnit(final U targetUnit)
    {
        float[][] values = this.matrixSI.toArray();
        for (int row = 0; row < values.length; row++)
            for (int column = 0; column < values[0].length; column++)
                values[row][column] = (float) ValueUtil.expressAsUnit(values[row][column], targetUnit);
        return values;
    }

    /**
     * @see org.opentrafficsim.core.value.vfloat.matrix.ReadOnlyFloatMatrixFunctions#rows()
     */
    public int rows()
    {
        return this.matrixSI.rows();
    }

    /**
     * @see org.opentrafficsim.core.value.vfloat.matrix.ReadOnlyFloatMatrixFunctions#columns()
     */
    public int columns()
    {
        return this.matrixSI.columns();
    }

    /**
     * @see org.opentrafficsim.core.value.vfloat.vector.ReadOnlyFloatVectorFunctions#getSI(int)
     */
    public float getSI(final int row, final int column) throws ValueException
    {
        checkIndex(row, column);
        return safeGet(row, column);
    }

    /**
     * @see org.opentrafficsim.core.value.vfloat.vector.ReadOnlyFloatVectorFunctions#getInUnit(int)
     */
    public float getInUnit(final int row, final int column) throws ValueException
    {
        return (float) expressAsSpecifiedUnit(getSI(row, column));
    }

    /**
     * @see org.opentrafficsim.core.value.vfloat.vector.ReadOnlyFloatVectorFunctions#getInUnit(int,
     *      org.opentrafficsim.core.unit.Unit)
     */
    public float getInUnit(final int row, final int column, final U targetUnit) throws ValueException
    {
        return (float) ValueUtil.expressAsUnit(getSI(row, column), targetUnit);
    }

    /**
     * @see org.opentrafficsim.core.value.vfloat.vector.ReadOnlyFloatVectorFunctions#zSum()
     */
    public float zSum()
    {
        return this.matrixSI.zSum();
    }

    /**
     * @see org.opentrafficsim.core.value.vfloat.vectormut.Vectormut#cardinality()
     */
    @Override
    public int cardinality()
    {
        return this.matrixSI.cardinality();
    }

    /**
     * @see org.opentrafficsim.core.value.vfloat.matrix.ReadOnlyFloatMatrixFunctions#det()
     */
    @Override
    public float det() throws ValueException
    {
        try
        {
            if (this instanceof SparseData)
            {
                // System.out.println("calling SparseFloatAlgebra().det(this.matrixSI)");
                return new SparseFloatAlgebra().det(this.matrixSI);
            }
            if (this instanceof DenseData)
            {
                // System.out.println("calling DenseFloatAlgebra().det(this.matrixSI)");
                return new DenseFloatAlgebra().det(this.matrixSI);
            }
            throw new ValueException("FloatMatrix.det -- matrix implements neither Sparse nor Dense");
        }
        catch (IllegalArgumentException exception)
        {
            if (!exception.getMessage().startsWith("Matrix must be square"))
                exception.printStackTrace();
            throw new ValueException(exception.getMessage()); // probably Matrix must be square
        }
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        // unequal if object is of a different type.
        if (!(obj instanceof FloatMatrix<?>))
            return false;
        FloatMatrix<?> fv = (FloatMatrix<?>) obj;

        // unequal if the SI unit type differs (km/h and m/s could have the same content, so that is allowed)
        if (!this.getUnit().getStandardUnit().equals(fv.getUnit().getStandardUnit()))
            return false;

        // unequal if one is absolute and the other is relative
        if (this.isAbsolute() != fv.isAbsolute() || this.isRelative() != fv.isRelative())
            return false;

        // Colt's equals also tests the size of the vector
        return this.matrixSI.equals(fv.matrixSI);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return toString(this.unit);
    }

    /**
     * Print this AbstractFloatVector with the values expressed in the specified unit.
     * @param displayUnit the unit to display the vector in.
     * @return a printable String with the vector contents
     */
    public String toString(final U displayUnit)
    {
        StringBuffer buf = new StringBuffer();
        if (this instanceof MutableFloatMatrix)
        {
            buf.append("Mutable   ");
            if (this instanceof MutableFloatMatrix.Abs.Dense)
                buf.append("Abs Dense  ");
            else if (this instanceof MutableFloatMatrix.Rel.Dense)
                buf.append("Rel Dense  ");
            else if (this instanceof MutableFloatMatrix.Abs.Sparse)
                buf.append("Abs Sparse ");
            else if (this instanceof MutableFloatMatrix.Rel.Sparse)
                buf.append("Rel Sparse ");
            else
                buf.append("??? ");
        }
        else
        {
            buf.append("Immutable ");
            if (this instanceof FloatMatrix.Abs.Dense)
                buf.append("Abs Dense  ");
            else if (this instanceof FloatMatrix.Rel.Dense)
                buf.append("Rel Dense  ");
            else if (this instanceof FloatMatrix.Abs.Sparse)
                buf.append("Abs Sparse ");
            else if (this instanceof FloatMatrix.Rel.Sparse)
                buf.append("Rel Sparse ");
            else
                buf.append("??? ");
        }
        buf.append("[" + displayUnit.getAbbreviation() + "]");
        for (int row = 0; row < this.matrixSI.rows(); row++)
        {
            buf.append("\r\n\t");
            for (int column = 0; column < this.matrixSI.columns(); column++)
            {
                float f = (float) ValueUtil.expressAsUnit(safeGet(row, column), displayUnit);
                buf.append(" " + Format.format(f));
            }
        }
        return buf.toString();
    }

    /**
     * Centralized size equality check.
     * @param other FloatMatrix<U>; other FloatVector
     * @throws ValueException when vectors have unequal size
     */
    protected void checkSize(final FloatMatrix<?> other) throws ValueException
    {
        if (rows() != other.rows() || columns() != other.columns())
            throw new ValueException("The matrices have different sizes: " + rows() + "x" + columns() + " != "
                    + other.rows() + "x" + other.columns());
    }

    /**
     * Centralized size equality check.
     * @param other float[][]; array of float
     * @throws ValueException when vectors have unequal size
     */
    protected void checkSize(final float[][] other) throws ValueException
    {
        if (rows() != other.length || columns() != other[0].length)
            throw new ValueException("The vector and the array have different sizes: " + rows() + " != " + other.length);
        ensureRectangular(other);
    }

    /**
     * Check that a 2D array of double is rectangular; i.e. all rows have the same length.
     * @param values float[][]; the 2D array to check
     * @throws ValueException
     */
    private static void ensureRectangular(final float[][] values) throws ValueException
    {
        for (int row = 1; row < values.length; row++)
            if (values[0].length != values[row].length)
                throw new ValueException("Lengths of rows are not all the same");
    }

    /**
     * Check that a 2D array of double is rectangular; i.e. all rows have the same length.
     * @param values FloatScalar[][]; the 2D array to check
     * @throws ValueException
     */
    private static void ensureRectangularAndNonEmpty(final FloatScalar<?>[][] values) throws ValueException
    {
        if (0 == values.length || 0 == values[0].length)
            throw new ValueException("Cannot determine unit for FloatMatrix from an empty array of FloatScalar");
        for (int row = 1; row < values.length; row++)
            if (values[0].length != values[row].length)
                throw new ValueException("Lengths of rows are not all the same");
    }

    /**
     * Check that provided row and column indices are valid.
     * @param row integer; the row value to check
     * @param column integer; the column value to check
     * @throws ValueException
     */
    protected void checkIndex(final int row, final int column) throws ValueException
    {
        if (row < 0 || row >= this.matrixSI.rows() || column < 0 || column >= this.matrixSI.columns())
            throw new ValueException("index out of range (valid range is 0.." + (this.matrixSI.rows() - 1) + ", 0.."
                    + this.matrixSI.columns() + ", got " + row + ", " + column + ")");
    }

    /**
     * Retrieve a value in vectorSI without checking validity of the index.
     * @param row integer; the row where the value must be retrieved
     * @param column integer; the column where the value must be retrieved
     * @return float; the value stored at the indicated row and column
     */
    protected float safeGet(int row, int column)
    {
        return this.matrixSI.getQuick(row, column);
    }

    /**
     * Modify a value in vectorSI without checking validity of the indices.
     * @param row integer; the row where the value must be stored
     * @param col integer; the column where the value must be stored
     * @param valueSI float; the new value for the entry in vectorSI
     */
    protected void safeSet(final int row, final int column, final float valueSI)
    {
        this.matrixSI.setQuick(row, column, valueSI);
    }

    /**
     * Create a deep copy of the data.
     * @return FloatMatrix2D; deep copy of the data
     */
    protected FloatMatrix2D deepCopyOfData()
    {
        return this.matrixSI.copy();
    }

    /**
     * Check that a provided array can be used to create some descendant of an AbstractFloatMatrix.
     * @param fsArray FloatScalar[][]; the provided array
     * @return FloatScalar[][]; the provided array
     * @throws ValueException
     */
    protected static <U extends Unit<U>> FloatScalar<U>[][] checkNonEmpty(FloatScalar<U>[][] fsArray)
            throws ValueException
    {
        if (0 == fsArray.length || 0 == fsArray[0].length)
            throw new ValueException(
                    "Cannot create a FloatValue or MutableFloatValue from an empty array of FloatScalar");
        return fsArray;
    }

    /**
     * Solve x for A*x = b. According to Colt: x; a new independent matrix; solution if A is square, least squares
     * solution if A.rows() > A.columns(), underdetermined system solution if A.rows() < A.columns().
     * @param A matrix A in A*x = b
     * @param b vector b in A*x = b
     * @return vector x in A*x = b
     * @throws ValueException when Matrix A is neither Sparse nor Dense.
     */
    public static FloatVector<SIUnit> solve(final FloatMatrix<?> A, final FloatVector<?> b) throws ValueException
    {
        // TODO: is this correct? Should lookup matrix algebra to find out unit for x when solving A*x = b ?
        SIUnit targetUnit =
                Unit.lookupOrCreateSIUnitWithSICoefficients(SICoefficients.divide(b.getUnit().getSICoefficients(),
                        A.getUnit().getSICoefficients()).toString());

        // TODO: should the algorithm throw an exception when rows/columns do not match when solving A*x = b ?
        FloatMatrix2D A2D = A.matrixSI;
        if (A instanceof SparseData)
        {
            SparseFloatMatrix1D b1D = new SparseFloatMatrix1D(b.getValuesSI());
            FloatMatrix1D x1D = new SparseFloatAlgebra().solve(A2D, b1D);
            FloatVector.Abs.Sparse<SIUnit> x = new FloatVector.Abs.Sparse<SIUnit>(x1D.toArray(), targetUnit);
            return x;
        }
        if (A instanceof DenseData)
        {
            DenseFloatMatrix1D b1D = new DenseFloatMatrix1D(b.getValuesSI());
            FloatMatrix1D x1D = new DenseFloatAlgebra().solve(A2D, b1D);
            FloatVector.Abs.Dense<SIUnit> x = new FloatVector.Abs.Dense<SIUnit>(x1D.toArray(), targetUnit);
            return x;
        }
        throw new ValueException("FloatMatrix.det -- matrix implements neither Sparse nor Dense");
    }

    /**
     * Create a mutable version of this FloatMatrix. <br />
     * The mutable version is created with a shallow copy of the data and the internal copyOnWrite flag set. The first
     * operation in the mutable version that modifies the data shall trigger a deep copy of the data.
     * @return MutableFloatMatrix; mutable version of this FloatMatrix
     */
    public abstract MutableFloatMatrix<U> mutable();

    /**
     * @see org.opentrafficsim.core.value.Value#copy()
     */
    public FloatMatrix<U> copy()
    {
        return this; // That was easy!
    }

}
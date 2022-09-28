package org.opentrafficsim.road.gtu.lane.perception;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import org.djunits.value.vdouble.scalar.Length;
import org.djutils.exceptions.Try;
import org.opentrafficsim.base.parameters.ParameterException;
import org.opentrafficsim.core.gtu.GTUException;
import org.opentrafficsim.road.gtu.lane.LaneBasedGTU;
import org.opentrafficsim.road.gtu.lane.perception.headway.Headway;

/**
 * This class uses a single primary iterator which a subclass defines, and makes sure that all elements are only looked up and
 * created once. It does so by storing the elements in a linked list. All calls to {@code iterator()} return an iterator which
 * iterates over the linked list. If an iterator runs to the end of the linked list, the primary iterator is requested to add an
 * element if it has one.
 * <p>
 * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <H> headway type
 * @param <U> underlying object type
 */
public abstract class AbstractPerceptionReiterable<H extends Headway, U> implements PerceptionCollectable<H, U>
{

    /** First entry. */
    private SecondaryIteratorEntry first;

    /** Last entry generated by the primary iterator. */
    private SecondaryIteratorEntry last;

    /** Primary iterator. */
    private Iterator<PrimaryIteratorEntry> primaryIterator;

    /** Perceiving GTU. */
    private final LaneBasedGTU gtu;

    /**
     * Constructor.
     * @param perceivingGtu LaneBasedGTU; perceiving GTU
     */
    protected AbstractPerceptionReiterable(final LaneBasedGTU perceivingGtu)
    {
        this.gtu = perceivingGtu;
    }

    /**
     * Returns the GTU.
     * @return LaneBasedGTU; GTU
     */
    protected LaneBasedGTU getGtu()
    {
        return this.gtu;
    }

    /**
     * Returns the primary iterator.
     * @return Iterator; primary iterator
     */
    final Iterator<PrimaryIteratorEntry> getPrimaryIterator()
    {
        if (this.primaryIterator == null)
        {
            this.primaryIterator = primaryIterator();
        }
        return this.primaryIterator;
    }

    /**
     * Returns the primary iterator. This method is called once by AbstractPerceptionReiterable.
     * @return Iterator; primary iterator
     */
    protected abstract Iterator<PrimaryIteratorEntry> primaryIterator();

    /**
     * Returns a perceived version of the underlying object.
     * @param perceivingGtu LaneBasedGTU; perceiving GTU
     * @param object U; underlying object
     * @param distance Length; distance to the object
     * @return H; perceived version of the underlying object
     * @throws GTUException on exception
     * @throws ParameterException on invalid parameter value or missing parameter
     */
    protected abstract H perceive(LaneBasedGTU perceivingGtu, U object, Length distance)
            throws GTUException, ParameterException;

    /** {@inheritDoc} */
    @Override
    public final synchronized H first()
    {
        assureFirst();
        if (this.first == null)
        {
            return null;
        }
        return this.first.getValue();
    }

    /**
     * Assures a first SecondaryIteratorEntry is present, if the primary iterator has any elements.
     */
    private synchronized void assureFirst()
    {
        if (this.first == null && getPrimaryIterator().hasNext())
        {
            addNext(getPrimaryIterator().next());
        }
    }

    /**
     * Adds an iterator entry to the internal linked list.
     * @param next PrimaryIteratorEntry; next object
     */
    @SuppressWarnings("synthetic-access")
    final void addNext(final PrimaryIteratorEntry next)
    {
        SecondaryIteratorEntry entry = new SecondaryIteratorEntry(next.object, next.distance);
        if (AbstractPerceptionReiterable.this.last == null)
        {
            AbstractPerceptionReiterable.this.first = entry;
            AbstractPerceptionReiterable.this.last = entry;
        }
        else
        {
            AbstractPerceptionReiterable.this.last.next = entry;
            AbstractPerceptionReiterable.this.last = entry;
        }
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isEmpty()
    {
        return first() == null;
    }

    /** {@inheritDoc} */
    @Override
    public final Iterator<H> iterator()
    {
        return new PerceptionIterator();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("synthetic-access")
    @Override
    public final <C, I> C collect(final Supplier<I> identity, final PerceptionAccumulator<? super U, I> accumulator,
            final PerceptionFinalizer<C, I> finalizer)
    {
        Intermediate<I> intermediate = new Intermediate<>(identity.get());
        assureFirst();
        if (this.first != null)
        {
            SecondaryIteratorEntry lastReturned = null;
            SecondaryIteratorEntry next = this.first;
            next = assureNext(next, lastReturned);
            while (next != null && !intermediate.isStop())
            {
                intermediate = accumulator.accumulate(intermediate, next.object, next.distance);
                intermediate.step();
                lastReturned = next;
                next = lastReturned.next;
                next = assureNext(next, lastReturned);
            }
        }
        return finalizer.collect(intermediate.getObject());
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<U> underlying()
    {
        assureFirst();
        SecondaryIteratorEntry firstInContext = this.first;
        return new Iterator<U>()
        {
            /** Last returned iterator entry. */
            private SecondaryIteratorEntry lastReturned = null;

            /** Next iterator entry. */
            private SecondaryIteratorEntry next = firstInContext;

            /** {@inheritDoc} */
            @Override
            public boolean hasNext()
            {
                this.next = assureNext(this.next, this.lastReturned);
                return this.next != null;
            }

            /** {@inheritDoc} */
            @SuppressWarnings("synthetic-access")
            @Override
            public U next()
            {
                this.lastReturned = this.next;
                this.next = this.lastReturned.next;
                this.next = assureNext(this.next, this.lastReturned);
                return this.lastReturned.object;
            }
        };
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<UnderlyingDistance<U>> underlyingWithDistance()
    {
        assureFirst();
        SecondaryIteratorEntry firstInContext = this.first;
        return new Iterator<UnderlyingDistance<U>>()
        {
            /** Last returned iterator entry. */
            private SecondaryIteratorEntry lastReturned = null;

            /** Next iterator entry. */
            private SecondaryIteratorEntry next = firstInContext;

            /** {@inheritDoc} */
            @Override
            public boolean hasNext()
            {
                this.next = assureNext(this.next, this.lastReturned);
                return this.next != null;
            }

            /** {@inheritDoc} */
            @SuppressWarnings("synthetic-access")
            @Override
            public UnderlyingDistance<U> next()
            {
                this.lastReturned = this.next;
                this.next = this.lastReturned.next;
                this.next = assureNext(this.next, this.lastReturned);
                return new UnderlyingDistance<>(this.lastReturned.object, this.lastReturned.distance);
            }
        };
    }

    /**
     * This iterator is returned to callers of the {@code iterator()} method. Multiple instances may be returned which use the
     * same linked list of {@code SecondaryIteratorEntry}. Whenever an iterator runs to the end of this list, the primary
     * iterator is requested to find the next object, if it has a next object.
     * <p>
     * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
     * <p>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    public class PerceptionIterator implements Iterator<H>
    {

        /** Last returned entry. */
        private SecondaryIteratorEntry lastReturned;

        /** Next entry. */
        private SecondaryIteratorEntry next;

        /** Constructor. */
        @SuppressWarnings("synthetic-access")
        PerceptionIterator()
        {
            this.next = AbstractPerceptionReiterable.this.first;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext()
        {
            this.next = assureNext(this.next, this.lastReturned);
            return this.next != null;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("synthetic-access")
        @Override
        public H next()
        {
            this.next = assureNext(this.next, this.lastReturned);
            if (this.next == null)
            {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.next;
            this.next = this.lastReturned.next;
            return this.lastReturned.getValue();
        }

    }

    /**
     * Helper method that assures that a next entry is available, if the primary iterator has a next value. This method may be
     * used by any process that derives from the primary iterator.
     * @param next SecondaryIteratorEntry; currently known next entry
     * @param lastReturned SecondaryIteratorEntry; entry of last returned object or value
     * @return IteratorEntry; next entry
     */
    @SuppressWarnings("synthetic-access")
    synchronized SecondaryIteratorEntry assureNext(final SecondaryIteratorEntry next, final SecondaryIteratorEntry lastReturned)
    {
        if (next == null && getPrimaryIterator().hasNext())
        {
            addNext(getPrimaryIterator().next());
            if (lastReturned == null)
            {
                return AbstractPerceptionReiterable.this.first;
            }
            else
            {
                return lastReturned.next;
            }
        }
        return next;
    }

    /**
     * Class for {@code primaryIterator()} to return, implemented in subclasses.
     * <p>
     * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
     * <p>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    protected class PrimaryIteratorEntry implements Comparable<PrimaryIteratorEntry>
    {
        /** Object. */
        private final U object;

        /** Distance to the object. */
        private final Length distance;

        /**
         * Constructor.
         * @param object U; object
         * @param distance Length; distance
         */
        public PrimaryIteratorEntry(final U object, final Length distance)
        {
            this.object = object;
            this.distance = distance;
        }

        /** {@inheritDoc} */
        @Override
        public int compareTo(final PrimaryIteratorEntry o)
        {
            return this.distance.compareTo(o.distance);
        }

        /**
         * Returns the object.
         * @return U; object
         */
        protected U getObject()
        {
            return this.object;
        }
    }

    /**
     * Entries that make up a linked list of values for secondary iterators to iterate over.
     * <p>
     * Copyright (c) 2013-2022 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved.
     * <br>
     * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
     * <p>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    private class SecondaryIteratorEntry
    {
        /** Value. */
        private final U object;

        /** Distance to object. */
        private final Length distance;

        /** Value. */
        private H value;

        /** Next entry. */
        private SecondaryIteratorEntry next;

        /**
         * Constructor.
         * @param object U; object
         * @param distance Length; distance to object
         */
        SecondaryIteratorEntry(final U object, final Length distance)
        {
            this.object = object;
            this.distance = distance;
        }

        /**
         * Returns the perceived version of the object.
         * @return H; perceived version of the object
         */
        H getValue()
        {
            if (this.value == null)
            {
                this.value = Try.assign(() -> perceive(AbstractPerceptionReiterable.this.getGtu(), this.object, this.distance),
                        "Exception during perception of object.");
            }
            return this.value;
        }
    }

}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.commons.lang3.Functions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests "failable" interfaces defined in this package.
 */
public class FailableFunctionsTest {

    public static class CloseableObject {
        private boolean closed;

        public void close() {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }

        public void reset() {
            closed = false;
        }

        public void run(final Throwable pTh) throws Throwable {
            if (pTh != null) {
                throw pTh;
            }
        }
    }
    public static class FailureOnOddInvocations {
        private static int invocations;

        static boolean failingBool() throws SomeException {
            throwOnOdd();
            return true;
        }

        static boolean testDouble(final double value) throws SomeException {
            throwOnOdd();
            return true;
        }

        static boolean testInt(final int value) throws SomeException {
            throwOnOdd();
            return true;
        }

        static boolean testLong(final long value) throws SomeException {
            throwOnOdd();
            return true;
        }

        private static void throwOnOdd() throws SomeException {
            final int i = ++invocations;
            if (i % 2 == 1) {
                throw new SomeException("Odd Invocation: " + i);
            }
        }

        FailureOnOddInvocations() throws SomeException {
            throwOnOdd();
        }

        boolean getAsBoolean() throws SomeException {
            throwOnOdd();
            return true;
        }
    }

    public static class SomeException extends Exception {

        private static final long serialVersionUID = -4965704778119283411L;

        private Throwable t;

        SomeException(final String message) {
            super(message);
        }

        public void setThrowable(final Throwable throwable) {
            t = throwable;
        }

        public void test() throws Throwable {
            if (t != null) {
                throw t;
            }
        }
    }

    public static class Testable<T, P> {
        private T acceptedObject;
        private P acceptedPrimitiveObject1;
        private P acceptedPrimitiveObject2;
        private Throwable throwable;

        Testable(final Throwable throwable) {
            this.throwable = throwable;
        }

        public T getAcceptedObject() {
            return acceptedObject;
        }

        public P getAcceptedPrimitiveObject1() {
            return acceptedPrimitiveObject1;
        }

        public P getAcceptedPrimitiveObject2() {
            return acceptedPrimitiveObject2;
        }

        public void setThrowable(final Throwable throwable) {
            this.throwable = throwable;
        }

        public void test() throws Throwable {
            test(throwable);
        }

        public Object test(final Object input1, final Object input2) throws Throwable {
            test(throwable);
            return acceptedObject;
        }

        public void test(final Throwable throwable) throws Throwable {
            if (throwable != null) {
                throw throwable;
            }
        }

        public boolean testAsBooleanPrimitive() throws Throwable {
            return testAsBooleanPrimitive(throwable);
        }

        public boolean testAsBooleanPrimitive(final Throwable throwable) throws Throwable {
            if (throwable != null) {
                throw throwable;
            }
            return false;
        }

        public double testAsDoublePrimitive() throws Throwable {
            return testAsDoublePrimitive(throwable);
        }

        public double testAsDoublePrimitive(final Throwable throwable) throws Throwable {
            if (throwable != null) {
                throw throwable;
            }
            return 0;
        }

        public Integer testAsInteger() throws Throwable {
            return testAsInteger(throwable);
        }

        public Integer testAsInteger(final Throwable throwable) throws Throwable {
            if (throwable != null) {
                throw throwable;
            }
            return 0;
        }

        public int testAsIntPrimitive() throws Throwable {
            return testAsIntPrimitive(throwable);
        }

        public int testAsIntPrimitive(final Throwable throwable) throws Throwable {
            if (throwable != null) {
                throw throwable;
            }
            return 0;
        }

        public long testAsLongPrimitive() throws Throwable {
            return testAsLongPrimitive(throwable);
        }

        public long testAsLongPrimitive(final Throwable throwable) throws Throwable {
            if (throwable != null) {
                throw throwable;
            }
            return 0;
        }

        public void testDouble(final double i) throws Throwable {
            test(throwable);
            acceptedPrimitiveObject1 = (P) ((Double) i);
        }

        public double testDoubleDouble(final double i, final double j) throws Throwable {
            test(throwable);
            acceptedPrimitiveObject1 = (P) ((Double) i);
            acceptedPrimitiveObject2 = (P) ((Double) j);
            return 3d;
        }

        public void testInt(final int i) throws Throwable {
            test(throwable);
            acceptedPrimitiveObject1 = (P) ((Integer) i);
        }

        public void testLong(final long i) throws Throwable {
            test(throwable);
            acceptedPrimitiveObject1 = (P) ((Long) i);
        }

        public void testObjDouble(final T object, final double i) throws Throwable {
            test(throwable);
            acceptedObject = object;
            acceptedPrimitiveObject1 = (P) ((Double) i);
        }

        public void testObjInt(final T object, final int i) throws Throwable {
            test(throwable);
            acceptedObject = object;
            acceptedPrimitiveObject1 = (P) ((Integer) i);
        }

        public void testObjLong(final T object, final long i) throws Throwable {
            test(throwable);
            acceptedObject = object;
            acceptedPrimitiveObject1 = (P) ((Long) i);
        }
    }

    private static final OutOfMemoryError ERROR = new OutOfMemoryError();

    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException();

    @Test
    public void testAcceptBiConsumer() {
        final Testable<?, ?> testable = new Testable<>(null);
        Throwable e = assertThrows(IllegalStateException.class,
            () -> Failable.accept(Testable::test, testable, ILLEGAL_STATE_EXCEPTION));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        e = assertThrows(OutOfMemoryError.class, () -> Failable.accept(Testable::test, testable, ERROR));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.accept(Testable::test, testable, ioe));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        testable.setThrowable(null);
        Failable.accept(Testable::test, testable, (Throwable) null);
    }

    @Test
    public void testAcceptConsumer() {
        final Testable<?, ?> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class, () -> Failable.accept(Testable::test, testable));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.accept(Testable::test, testable));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.accept(Testable::test, testable));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        testable.setThrowable(null);
        Failable.accept(Testable::test, testable);
    }

    @Test
    public void testAcceptDoubleConsumer() {
        final Testable<?, Double> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class, () -> Failable.accept(testable::testDouble, 1d));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.accept(testable::testDouble, 1d));
        assertSame(ERROR, e);
        assertNull(testable.getAcceptedPrimitiveObject1());

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.accept(testable::testDouble, 1d));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(null);
        Failable.accept(testable::testDouble, 1d);
        assertEquals(1, testable.getAcceptedPrimitiveObject1());
    }

    @Test
    public void testAcceptIntConsumer() {
        final Testable<?, Integer> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class, () -> Failable.accept(testable::testInt, 1));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.accept(testable::testInt, 1));
        assertSame(ERROR, e);
        assertNull(testable.getAcceptedPrimitiveObject1());

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.accept(testable::testInt, 1));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(null);
        Failable.accept(testable::testInt, 1);
        assertEquals(1, testable.getAcceptedPrimitiveObject1());
    }

    @Test
    public void testAcceptLongConsumer() {
        final Testable<?, Long> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class, () -> Failable.accept(testable::testLong, 1L));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.accept(testable::testLong, 1L));
        assertSame(ERROR, e);
        assertNull(testable.getAcceptedPrimitiveObject1());

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.accept(testable::testLong, 1L));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(null);
        Failable.accept(testable::testLong, 1L);
        assertEquals(1, testable.getAcceptedPrimitiveObject1());
    }

    @Test
    public void testAcceptObjDoubleConsumer() {
        final Testable<String, Double> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class,
            () -> Failable.accept(testable::testObjDouble, "X", 1d));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);
        assertNull(testable.getAcceptedObject());
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.accept(testable::testObjDouble, "X", 1d));
        assertSame(ERROR, e);
        assertNull(testable.getAcceptedObject());
        assertNull(testable.getAcceptedPrimitiveObject1());

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.accept(testable::testObjDouble, "X", 1d));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);
        assertNull(testable.getAcceptedObject());
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(null);
        Failable.accept(testable::testObjDouble, "X", 1d);
        assertEquals("X", testable.getAcceptedObject());
        assertEquals(1d, testable.getAcceptedPrimitiveObject1());
    }

    @Test
    public void testAcceptObjIntConsumer() {
        final Testable<String, Integer> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class, () -> Failable.accept(testable::testObjInt, "X", 1));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);
        assertNull(testable.getAcceptedObject());
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.accept(testable::testObjInt, "X", 1));
        assertSame(ERROR, e);
        assertNull(testable.getAcceptedObject());
        assertNull(testable.getAcceptedPrimitiveObject1());

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.accept(testable::testObjInt, "X", 1));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);
        assertNull(testable.getAcceptedObject());
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(null);
        Failable.accept(testable::testObjInt, "X", 1);
        assertEquals("X", testable.getAcceptedObject());
        assertEquals(1, testable.getAcceptedPrimitiveObject1());
    }

    @Test
    public void testAcceptObjLongConsumer() {
        final Testable<String, Long> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class, () -> Failable.accept(testable::testObjLong, "X", 1L));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);
        assertNull(testable.getAcceptedObject());
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.accept(testable::testObjLong, "X", 1L));
        assertSame(ERROR, e);
        assertNull(testable.getAcceptedObject());
        assertNull(testable.getAcceptedPrimitiveObject1());

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.accept(testable::testObjLong, "X", 1L));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);
        assertNull(testable.getAcceptedObject());
        assertNull(testable.getAcceptedPrimitiveObject1());

        testable.setThrowable(null);
        Failable.accept(testable::testObjLong, "X", 1L);
        assertEquals("X", testable.getAcceptedObject());
        assertEquals(1L, testable.getAcceptedPrimitiveObject1());
    }

    @Test
    public void testApplyBiFunction() {
        final Testable<?, ?> testable = new Testable<>(null);
        Throwable e = assertThrows(IllegalStateException.class,
            () -> Failable.apply(Testable::testAsInteger, testable, ILLEGAL_STATE_EXCEPTION));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        e = assertThrows(OutOfMemoryError.class, () -> Failable.apply(Testable::testAsInteger, testable, ERROR));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        e = assertThrows(UncheckedIOException.class, () -> Failable.apply(Testable::testAsInteger, testable, ioe));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        final Integer i = Failable.apply(Testable::testAsInteger, testable, (Throwable) null);
        assertNotNull(i);
        assertEquals(0, i.intValue());
    }

    @Test
    public void testApplyDoubleBinaryOperator() {
        final Testable<?, Double> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        final Throwable e = assertThrows(IllegalStateException.class,
            () -> Failable.applyAsDouble(testable::testDoubleDouble, 1d, 2d));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        final Testable<?, Double> testable2 = new Testable<>(null);
        final double i = Failable.applyAsDouble(testable2::testDoubleDouble, 1d, 2d);
        assertEquals(3d, i);
    }

    @Test
    public void testApplyFunction() {
        final Testable<?, ?> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class,
            () -> Failable.apply(Testable::testAsInteger, testable));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.apply(Testable::testAsInteger, testable));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.apply(Testable::testAsInteger, testable));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        testable.setThrowable(null);
        final Integer i = Failable.apply(Testable::testAsInteger, testable);
        assertNotNull(i);
        assertEquals(0, i.intValue());
    }

    @Test
    public void testAsCallable() {
        FailureOnOddInvocations.invocations = 0;
        final FailableCallable<FailureOnOddInvocations, SomeException> failableCallable = FailureOnOddInvocations::new;
        final Callable<FailureOnOddInvocations> callable = Failable.asCallable(failableCallable);
        final UndeclaredThrowableException e = assertThrows(UndeclaredThrowableException.class, callable::call);
        final Throwable cause = e.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof SomeException);
        assertEquals("Odd Invocation: 1", cause.getMessage());
        final FailureOnOddInvocations instance;
        try {
            instance = callable.call();
        } catch (final Exception ex) {
            throw Failable.rethrow(ex);
        }
        assertNotNull(instance);
    }

    @Test
    public void testAsConsumer() {
        final Testable<?, ?> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        final Consumer<Testable<?, ?>> consumer = Failable.asConsumer(Testable::test);
        Throwable e = assertThrows(IllegalStateException.class, () -> consumer.accept(testable));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> consumer.accept(testable));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> consumer.accept(testable));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        testable.setThrowable(null);
        Failable.accept(Testable::test, testable);
    }

    @Test
    public void testAsRunnable() {
        FailureOnOddInvocations.invocations = 0;
        final Runnable runnable = Failable.asRunnable(FailureOnOddInvocations::new);
        final UndeclaredThrowableException e = assertThrows(UndeclaredThrowableException.class, runnable::run);
        final Throwable cause = e.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof SomeException);
        assertEquals("Odd Invocation: 1", cause.getMessage());

        // Even invocations, should not throw an exception
        runnable.run();
    }

    @Test
    public void testAsSupplier() {
        FailureOnOddInvocations.invocations = 0;
        final FailableSupplier<FailureOnOddInvocations, Throwable> failableSupplier = FailureOnOddInvocations::new;
        final Supplier<FailureOnOddInvocations> supplier = Failable.asSupplier(failableSupplier);
        final UndeclaredThrowableException e = assertThrows(UndeclaredThrowableException.class, supplier::get);
        final Throwable cause = e.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof SomeException);
        assertEquals("Odd Invocation: 1", cause.getMessage());
        assertNotNull(supplier.get());
    }

    @Test
    public void testBiConsumer() throws Throwable {
        final Testable<?, ?> testable = new Testable<>(null);
        final FailableBiConsumer<Testable<?, ?>, Throwable, Throwable> failableBiConsumer = (t, th) -> {
            t.setThrowable(th);
            t.test();
        };
        final BiConsumer<Testable<?, ?>, Throwable> consumer = Failable.asBiConsumer(failableBiConsumer);
        Throwable e = assertThrows(IllegalStateException.class,
            () -> consumer.accept(testable, ILLEGAL_STATE_EXCEPTION));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        e = assertThrows(OutOfMemoryError.class, () -> consumer.accept(testable, ERROR));
        assertSame(ERROR, e);

        e = assertThrows(OutOfMemoryError.class, () -> failableBiConsumer.accept(testable, ERROR));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> consumer.accept(testable, ioe));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        consumer.accept(testable, null);
    }

    @Test
    public void testBiConsumerAndThen() throws Throwable {
        final Testable<?, ?> testable = new Testable<>(null);
        final FailableBiConsumer<Testable<?, ?>, Throwable, Throwable> failableBiConsumer = (t, th) -> {
            t.setThrowable(th);
            t.test();
        };
        final FailableBiConsumer<Testable<?, ?>, Throwable, Throwable> nop = FailableBiConsumer.nop();
        final Throwable e = assertThrows(OutOfMemoryError.class,
            () -> nop.andThen(failableBiConsumer).accept(testable, ERROR));
        assertSame(ERROR, e);
        // Does not throw
        nop.andThen(nop);
        // Documented in Javadoc edge-case.
        assertThrows(NullPointerException.class, () -> failableBiConsumer.andThen(null));
    }

    @Test
    public void testBiFunction() {
        final Testable<?, ?> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        final FailableBiFunction<Testable<?, ?>, Throwable, Integer, Throwable> failableBiFunction = (t, th) -> {
            t.setThrowable(th);
            return t.testAsInteger();
        };
        final BiFunction<Testable<?, ?>, Throwable, Integer> biFunction = Failable.asBiFunction(failableBiFunction);
        Throwable e = assertThrows(IllegalStateException.class,
            () -> biFunction.apply(testable, ILLEGAL_STATE_EXCEPTION));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> biFunction.apply(testable, ERROR));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> biFunction.apply(testable, ioe));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        assertEquals(0, biFunction.apply(testable, null).intValue());
    }

    @Test
    public void testBiFunctionAndThen() throws IOException {
        // Unchecked usage pattern in JRE
        final BiFunction<Object, Integer, Integer> nopBiFunction = (t, u) -> null;
        final Function<Object, Integer> nopFunction = (t) -> null;
        nopBiFunction.andThen(nopFunction);
        // Checked usage pattern
        final FailableBiFunction<Object, Integer, Integer, IOException> failingBiFunctionTest = (t, u) -> {
            throw new IOException();
        };
        final FailableFunction<Object, Integer, IOException> failingFunction = (t) -> { throw new IOException(); };
        final FailableBiFunction<Object, Integer, Integer, IOException> nopFailableBiFunction = FailableBiFunction.nop();
        final FailableFunction<Object, Integer, IOException> nopFailableFunction = FailableFunction.nop();
        //
        assertThrows(IOException.class, () -> failingBiFunctionTest.andThen(failingFunction).apply(null, null));
        assertThrows(IOException.class, () -> failingBiFunctionTest.andThen(nopFailableFunction).apply(null, null));
        //
        assertThrows(IOException.class, () -> nopFailableBiFunction.andThen(failingFunction).apply(null, null));
        nopFailableBiFunction.andThen(nopFailableFunction).apply(null, null);
        // Documented in Javadoc edge-case.
        assertThrows(NullPointerException.class, () -> failingBiFunctionTest.andThen(null));
    }

    @Test
    @DisplayName("Test that asPredicate(FailableBiPredicate) is converted to -> BiPredicate ")
    public void testBiPredicate() {
        FailureOnOddInvocations.invocations = 0;
        final FailableBiPredicate<Object, Object, Throwable> failableBiPredicate = (t1, t2) -> FailureOnOddInvocations
            .failingBool();
        final BiPredicate<?, ?> predicate = Failable.asBiPredicate(failableBiPredicate);
        final UndeclaredThrowableException e = assertThrows(UndeclaredThrowableException.class,
            () -> predicate.test(null, null));
        final Throwable cause = e.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof SomeException);
        assertEquals("Odd Invocation: 1", cause.getMessage());
        final boolean instance = predicate.test(null, null);
        assertNotNull(instance);
    }

    @Test
    public void testCallable() {
        FailureOnOddInvocations.invocations = 0;
        final UndeclaredThrowableException e = assertThrows(UndeclaredThrowableException.class,
            () -> Failable.run(FailureOnOddInvocations::new));
        final Throwable cause = e.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof SomeException);
        assertEquals("Odd Invocation: 1", cause.getMessage());
        final FailureOnOddInvocations instance = Failable.call(FailureOnOddInvocations::new);
        assertNotNull(instance);
    }

    @Test
    public void testConstructor() {
        // We allow this, which must have been an omission to make the ctor private.
        // We could make the ctor private in 4.0.
        new Functions();
    }

    @Test
    public void testConsumerAndThen() throws Throwable {
        final Testable<?, ?> testable = new Testable<>(null);
        final FailableConsumer<Throwable, Throwable> failableConsumer = (th) -> {
            testable.setThrowable(th);
            testable.test();
        };
        final FailableConsumer<Throwable, Throwable> nop = FailableConsumer.nop();
        final Throwable e = assertThrows(OutOfMemoryError.class,
            () -> nop.andThen(failableConsumer).accept(ERROR));
        assertSame(ERROR, e);
        // Does not throw
        nop.andThen(nop);
        // Documented in Javadoc edge-case.
        assertThrows(NullPointerException.class, () -> failableConsumer.andThen(null));
    }

    @Test
    public void testDoublePredicate() throws Throwable {
        FailureOnOddInvocations.invocations = 0;
        final FailableDoublePredicate<Throwable> failablePredicate = t1 -> FailureOnOddInvocations.testDouble(t1);
        assertThrows(SomeException.class, () -> failablePredicate.test(1d));
        failablePredicate.test(1d);
    }

    @Test
    public void testFunction() {
        final Testable<?, ?> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        final FailableFunction<Throwable, Integer, Throwable> failableFunction = th -> {
            testable.setThrowable(th);
            return Integer.valueOf(testable.testAsInteger());
        };
        final Function<Throwable, Integer> function = Failable.asFunction(failableFunction);
        Throwable e = assertThrows(IllegalStateException.class, () -> function.apply(ILLEGAL_STATE_EXCEPTION));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> function.apply(ERROR));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> function.apply(ioe));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        assertEquals(0, function.apply(null).intValue());
    }

    @Test
    public void testGetAsBooleanSupplier() {
        final Testable<?, ?> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class,
            () -> Failable.getAsBoolean(testable::testAsBooleanPrimitive));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.getAsBoolean(testable::testAsBooleanPrimitive));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.getAsBoolean(testable::testAsBooleanPrimitive));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        testable.setThrowable(null);
        assertFalse(Failable.getAsBoolean(testable::testAsBooleanPrimitive));
    }

    @Test
    public void testGetAsDoubleSupplier() {
        final Testable<?, ?> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class,
            () -> Failable.getAsDouble(testable::testAsDoublePrimitive));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.getAsDouble(testable::testAsDoublePrimitive));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.getAsDouble(testable::testAsDoublePrimitive));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        testable.setThrowable(null);
        assertEquals(0, Failable.getAsDouble(testable::testAsDoublePrimitive));
    }

    @Test
    public void testGetAsIntSupplier() {
        final Testable<?, ?> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class, () -> Failable.getAsInt(testable::testAsIntPrimitive));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.getAsInt(testable::testAsIntPrimitive));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.getAsInt(testable::testAsIntPrimitive));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        testable.setThrowable(null);
        final int i = Failable.getAsInt(testable::testAsInteger);
        assertEquals(0, i);
    }

    @Test
    public void testGetAsLongSupplier() {
        final Testable<?, ?> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class,
            () -> Failable.getAsLong(testable::testAsLongPrimitive));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.getAsLong(testable::testAsLongPrimitive));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.getAsLong(testable::testAsLongPrimitive));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        testable.setThrowable(null);
        final long i = Failable.getAsLong(testable::testAsLongPrimitive);
        assertEquals(0, i);
    }

    @Test
    public void testGetFromSupplier() {
        FailureOnOddInvocations.invocations = 0;
        final UndeclaredThrowableException e = assertThrows(UndeclaredThrowableException.class,
            () -> Failable.run(FailureOnOddInvocations::new));
        final Throwable cause = e.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof SomeException);
        assertEquals("Odd Invocation: 1", cause.getMessage());
        final FailureOnOddInvocations instance = Failable.call(FailureOnOddInvocations::new);
        assertNotNull(instance);
    }

    @Test
    public void testGetSupplier() {
        final Testable<?, ?> testable = new Testable<>(ILLEGAL_STATE_EXCEPTION);
        Throwable e = assertThrows(IllegalStateException.class, () -> Failable.get(testable::testAsInteger));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        testable.setThrowable(ERROR);
        e = assertThrows(OutOfMemoryError.class, () -> Failable.get(testable::testAsInteger));
        assertSame(ERROR, e);

        final IOException ioe = new IOException("Unknown I/O error");
        testable.setThrowable(ioe);
        e = assertThrows(UncheckedIOException.class, () -> Failable.get(testable::testAsInteger));
        final Throwable t = e.getCause();
        assertNotNull(t);
        assertSame(ioe, t);

        testable.setThrowable(null);
        final Integer i = Failable.apply(Testable::testAsInteger, testable);
        assertNotNull(i);
        assertEquals(0, i.intValue());
    }

    @Test
    public void testIntPredicate() throws Throwable {
        FailureOnOddInvocations.invocations = 0;
        final FailableIntPredicate<Throwable> failablePredicate = t1 -> FailureOnOddInvocations.testInt(t1);
        assertThrows(SomeException.class, () -> failablePredicate.test(1));
        failablePredicate.test(1);
    }

    @Test
    public void testLongPredicate() throws Throwable {
        FailureOnOddInvocations.invocations = 0;
        final FailableLongPredicate<Throwable> failablePredicate = t1 -> FailureOnOddInvocations.testLong(t1);
        assertThrows(SomeException.class, () -> failablePredicate.test(1L));
        failablePredicate.test(1L);
    }

    @Test
    @DisplayName("Test that asPredicate(FailablePredicate) is converted to -> Predicate ")
    public void testPredicate() {
        FailureOnOddInvocations.invocations = 0;
        final FailablePredicate<Object, Throwable> failablePredicate = t -> FailureOnOddInvocations.failingBool();
        final Predicate<?> predicate = Failable.asPredicate(failablePredicate);
        final UndeclaredThrowableException e = assertThrows(UndeclaredThrowableException.class,
            () -> predicate.test(null));
        final Throwable cause = e.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof SomeException);
        assertEquals("Odd Invocation: 1", cause.getMessage());
        final boolean instance = predicate.test(null);
        assertNotNull(instance);
    }

    @Test
    public void testRunnable() {
        FailureOnOddInvocations.invocations = 0;
        final UndeclaredThrowableException e = assertThrows(UndeclaredThrowableException.class,
            () -> Failable.run(FailureOnOddInvocations::new));
        final Throwable cause = e.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof SomeException);
        assertEquals("Odd Invocation: 1", cause.getMessage());

        // Even invocations, should not throw an exception
        Failable.run(FailureOnOddInvocations::new);
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableBiConsumer_Object_Throwable() {
        new FailableBiConsumer<Object, Object, Throwable>() {

            @Override
            public void accept(final Object object1, final Object object2) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableBiConsumer_String_IOException() {
        new FailableBiConsumer<String, String, IOException>() {

            @Override
            public void accept(final String object1, final String object2) throws IOException {
                throw new IOException("test");

            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableBiFunction_Object_Throwable() {
        new FailableBiFunction<Object, Object, Object, Throwable>() {

            @Override
            public Object apply(final Object input1, final Object input2) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableBiFunction_String_IOException() {
        new FailableBiFunction<String, String, String, IOException>() {

            @Override
            public String apply(final String input1, final String input2) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableBiPredicate_Object_Throwable() {
        new FailableBiPredicate<Object, Object, Throwable>() {

            @Override
            public boolean test(final Object object1, final Object object2) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableBiPredicate_String_IOException() {
        new FailableBiPredicate<String, String, IOException>() {

            @Override
            public boolean test(final String object1, final String object2) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableBooleanSupplier_Object_Throwable() {
        new FailableBooleanSupplier<Throwable>() {

            @Override
            public boolean getAsBoolean() throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableBooleanSupplier_String_IOException() {
        new FailableBooleanSupplier<IOException>() {

            @Override
            public boolean getAsBoolean() throws IOException {
                throw new IOException("test");
            }
        };
    }

    ///////////////////////////////////////////////

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableCallable_Object_Throwable() {
        new FailableCallable<Object, Throwable>() {

            @Override
            public Object call() throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableCallable_String_IOException() {
        new FailableCallable<String, IOException>() {

            @Override
            public String call() throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableConsumer_Object_Throwable() {
        new FailableConsumer<Object, Throwable>() {

            @Override
            public void accept(final Object object) throws Throwable {
                throw new IOException("test");

            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableConsumer_String_IOException() {
        new FailableConsumer<String, IOException>() {

            @Override
            public void accept(final String object) throws IOException {
                throw new IOException("test");

            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableDoubleBinaryOperator_Object_Throwable() {
        new FailableDoubleBinaryOperator<Throwable>() {

            @Override
            public double applyAsDouble(final double left, final double right) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableDoubleBinaryOperator_String_IOException() {
        new FailableDoubleBinaryOperator<IOException>() {

            @Override
            public double applyAsDouble(final double left, final double right) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableDoubleConsumer_Object_Throwable() {
        new FailableDoubleConsumer<Throwable>() {

            @Override
            public void accept(final double value) throws Throwable {
                throw new IOException("test");

            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableDoubleConsumer_String_IOException() {
        new FailableDoubleConsumer<IOException>() {

            @Override
            public void accept(final double value) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableDoubleFunction_Object_Throwable() {
        new FailableDoubleFunction<Object, Throwable>() {

            @Override
            public Object apply(final double input) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableDoubleFunction_String_IOException() {
        new FailableDoubleFunction<String, IOException>() {

            @Override
            public String apply(final double input) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableDoubleSupplier_Object_Throwable() {
        new FailableDoubleSupplier<Throwable>() {

            @Override
            public double getAsDouble() throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableDoubleSupplier_String_IOException() {
        new FailableDoubleSupplier<IOException>() {

            @Override
            public double getAsDouble() throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableDoubleToIntFunction_Object_Throwable() {
        new FailableDoubleToIntFunction<Throwable>() {

            @Override
            public int applyAsInt(final double value) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableDoubleToIntFunction_String_IOException() {
        new FailableDoubleToIntFunction<IOException>() {

            @Override
            public int applyAsInt(final double value) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableDoubleToLongFunction_Object_Throwable() {
        new FailableDoubleToLongFunction<Throwable>() {

            @Override
            public int applyAsLong(final double value) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableDoubleToLongFunction_String_IOException() {
        new FailableDoubleToLongFunction<IOException>() {

            @Override
            public int applyAsLong(final double value) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableFunction_Object_Throwable() {
        new FailableFunction<Object, Object, Throwable>() {

            @Override
            public Object apply(final Object input) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableFunction_String_IOException() {
        new FailableFunction<String, String, IOException>() {

            @Override
            public String apply(final String input) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableIntBinaryOperator_Object_Throwable() {
        new FailableIntBinaryOperator<Throwable>() {

            @Override
            public int applyAsInt(final int left, final int right) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableIntBinaryOperator_String_IOException() {
        new FailableIntBinaryOperator<IOException>() {

            @Override
            public int applyAsInt(final int left, final int right) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableIntConsumer_Object_Throwable() {
        new FailableIntConsumer<Throwable>() {

            @Override
            public void accept(final int value) throws Throwable {
                throw new IOException("test");

            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableIntConsumer_String_IOException() {
        new FailableIntConsumer<IOException>() {

            @Override
            public void accept(final int value) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableIntFunction_Object_Throwable() {
        new FailableIntFunction<Object, Throwable>() {

            @Override
            public Object apply(final int input) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableIntFunction_String_IOException() {
        new FailableIntFunction<String, IOException>() {

            @Override
            public String apply(final int input) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableIntSupplier_Object_Throwable() {
        new FailableIntSupplier<Throwable>() {

            @Override
            public int getAsInt() throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableIntSupplier_String_IOException() {
        new FailableIntSupplier<IOException>() {

            @Override
            public int getAsInt() throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableIntToDoubleFunction_Object_Throwable() {
        new FailableIntToDoubleFunction<Throwable>() {

            @Override
            public double applyAsDouble(final int value) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableIntToDoubleFunction_String_IOException() {
        new FailableIntToDoubleFunction<IOException>() {

            @Override
            public double applyAsDouble(final int value) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableIntToLongFunction_Object_Throwable() {
        new FailableIntToLongFunction<Throwable>() {

            @Override
            public long applyAsLong(final int value) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableIntToLongFunction_String_IOException() {
        new FailableIntToLongFunction<IOException>() {

            @Override
            public long applyAsLong(final int value) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableLongBinaryOperator_Object_Throwable() {
        new FailableLongBinaryOperator<Throwable>() {

            @Override
            public long applyAsLong(final long left, final long right) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableLongBinaryOperator_String_IOException() {
        new FailableLongBinaryOperator<IOException>() {

            @Override
            public long applyAsLong(final long left, final long right) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableLongConsumer_Object_Throwable() {
        new FailableLongConsumer<Throwable>() {

            @Override
            public void accept(final long object) throws Throwable {
                throw new IOException("test");

            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableLongConsumer_String_IOException() {
        new FailableLongConsumer<IOException>() {

            @Override
            public void accept(final long object) throws IOException {
                throw new IOException("test");

            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableLongFunction_Object_Throwable() {
        new FailableLongFunction<Object, Throwable>() {

            @Override
            public Object apply(final long input) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableLongFunction_String_IOException() {
        new FailableLongFunction<String, IOException>() {

            @Override
            public String apply(final long input) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableLongSupplier_Object_Throwable() {
        new FailableLongSupplier<Throwable>() {

            @Override
            public long getAsLong() throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableLongSupplier_String_IOException() {
        new FailableLongSupplier<IOException>() {

            @Override
            public long getAsLong() throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableLongToDoubleFunction_Object_Throwable() {
        new FailableLongToDoubleFunction<Throwable>() {

            @Override
            public double applyAsDouble(final long value) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableLongToDoubleFunction_String_IOException() {
        new FailableLongToDoubleFunction<IOException>() {

            @Override
            public double applyAsDouble(final long value) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableLongToIntFunction_Object_Throwable() {
        new FailableLongToIntFunction<Throwable>() {

            @Override
            public int applyAsInt(final long value) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableLongToIntFunction_String_IOException() {
        new FailableLongToIntFunction<IOException>() {

            @Override
            public int applyAsInt(final long value) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableObjDoubleConsumer_Object_Throwable() {
        new FailableObjDoubleConsumer<Object, Throwable>() {

            @Override
            public void accept(final Object object, final double value) throws Throwable {
                throw new IOException("test");

            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableObjDoubleConsumer_String_IOException() {
        new FailableObjDoubleConsumer<String, IOException>() {

            @Override
            public void accept(final String object, final double value) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableObjIntConsumer_Object_Throwable() {
        new FailableObjIntConsumer<Object, Throwable>() {

            @Override
            public void accept(final Object object, final int value) throws Throwable {
                throw new IOException("test");

            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableObjIntConsumer_String_IOException() {
        new FailableObjIntConsumer<String, IOException>() {

            @Override
            public void accept(final String object, final int value) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableObjLongConsumer_Object_Throwable() {
        new FailableObjLongConsumer<Object, Throwable>() {

            @Override
            public void accept(final Object object, final long value) throws Throwable {
                throw new IOException("test");

            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableObjLongConsumer_String_IOException() {
        new FailableObjLongConsumer<String, IOException>() {

            @Override
            public void accept(final String object, final long value) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailablePredicate_Object_Throwable() {
        new FailablePredicate<Object, Throwable>() {

            @Override
            public boolean test(final Object object) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailablePredicate_String_IOException() {
        new FailablePredicate<String, IOException>() {

            @Override
            public boolean test(final String object) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableRunnable_Object_Throwable() {
        new FailableRunnable<Throwable>() {

            @Override
            public void run() throws Throwable {
                throw new IOException("test");

            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableRunnable_String_IOException() {
        new FailableRunnable<IOException>() {

            @Override
            public void run() throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableSupplier_Object_Throwable() {
        new FailableSupplier<Object, Throwable>() {

            @Override
            public Object get() throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableSupplier_String_IOException() {
        new FailableSupplier<String, IOException>() {

            @Override
            public String get() throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableToDoubleBiFunction_Object_Throwable() {
        new FailableToDoubleBiFunction<Object, Object, Throwable>() {

            @Override
            public double applyAsDouble(final Object t, final Object u) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableToDoubleBiFunction_String_IOException() {
        new FailableToDoubleBiFunction<String, String, IOException>() {

            @Override
            public double applyAsDouble(final String t, final String u) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableToDoubleFunction_Object_Throwable() {
        new FailableToDoubleFunction<Object, Throwable>() {

            @Override
            public double applyAsDouble(final Object t) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableToDoubleFunction_String_IOException() {
        new FailableToDoubleFunction<String, IOException>() {

            @Override
            public double applyAsDouble(final String t) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableToIntBiFunction_Object_Throwable() {
        new FailableToIntBiFunction<Object, Object, Throwable>() {

            @Override
            public int applyAsInt(final Object t, final Object u) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableToIntBiFunction_String_IOException() {
        new FailableToIntBiFunction<String, String, IOException>() {

            @Override
            public int applyAsInt(final String t, final String u) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableToIntFunction_Object_Throwable() {
        new FailableToIntFunction<Object, Throwable>() {

            @Override
            public int applyAsInt(final Object t) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableToIntFunction_String_IOException() {
        new FailableToIntFunction<String, IOException>() {

            @Override
            public int applyAsInt(final String t) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableToLongBiFunction_Object_Throwable() {
        new FailableToLongBiFunction<Object, Object, Throwable>() {

            @Override
            public long applyAsLong(final Object t, final Object u) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableToLongBiFunction_String_IOException() {
        new FailableToLongBiFunction<String, String, IOException>() {

            @Override
            public long applyAsLong(final String t, final String u) throws IOException {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception. using the top level generic types
     * Object and Throwable.
     */
    @Test
    public void testThrows_FailableToLongFunction_Object_Throwable() {
        new FailableToLongFunction<Object, Throwable>() {

            @Override
            public long applyAsLong(final Object t) throws Throwable {
                throw new IOException("test");
            }
        };
    }

    /**
     * Tests that our failable interface is properly defined to throw any exception using String and IOExceptions as
     * generic test types.
     */
    @Test
    public void testThrows_FailableToLongFunction_String_IOException() {
        new FailableToLongFunction<String, IOException>() {

            @Override
            public long applyAsLong(final String t) throws IOException {
                throw new IOException("test");
            }
        };
    }

    @Test
    public void testTryWithResources() {
        final CloseableObject closeable = new CloseableObject();
        final FailableConsumer<Throwable, ? extends Throwable> consumer = closeable::run;
        Throwable e = assertThrows(IllegalStateException.class,
            () -> Failable.tryWithResources(() -> consumer.accept(ILLEGAL_STATE_EXCEPTION), closeable::close));
        assertSame(ILLEGAL_STATE_EXCEPTION, e);

        assertTrue(closeable.isClosed());
        closeable.reset();
        e = assertThrows(OutOfMemoryError.class,
            () -> Failable.tryWithResources(() -> consumer.accept(ERROR), closeable::close));
        assertSame(ERROR, e);

        assertTrue(closeable.isClosed());
        closeable.reset();
        final IOException ioe = new IOException("Unknown I/O error");
        final UncheckedIOException uioe = assertThrows(UncheckedIOException.class,
            () -> Failable.tryWithResources(() -> consumer.accept(ioe), closeable::close));
        final IOException cause = uioe.getCause();
        assertSame(ioe, cause);

        assertTrue(closeable.isClosed());
        closeable.reset();
        Failable.tryWithResources(() -> consumer.accept(null), closeable::close);
        assertTrue(closeable.isClosed());
    }

}
/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package ch.qos.logback.core.appender;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.testUtil.DummyEncoder;
import ch.qos.logback.core.encoder.EchoEncoder;
import ch.qos.logback.core.encoder.NopEncoder;
import ch.qos.logback.core.layout.DummyLayout;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.testUtil.StatusChecker;

import ch.qos.logback.core.testUtil.XTeeOutputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Redirecting System.out is quite messy. Disable this test in Maven but not in
 * Package.class
 */
public class ConsoleAppenderTest extends AbstractAppenderTest<Object> {

    XTeeOutputStream teeOut;
    XTeeOutputStream teeErr;
    PrintStream originalOut;
    PrintStream originalErr;

    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        originalErr = System.err;
        // teeOut will output bytes on System out but it will also
        // collect them so that the output can be compared against
  
        // keep the console quiet
        teeOut = new XTeeOutputStream(null);
        teeErr = new XTeeOutputStream(null);

        // redirect System.out to teeOut and System.err to teeErr
        System.setOut(new PrintStream(teeOut));
        System.setErr(new PrintStream(teeErr));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Override
    public Appender<Object> getAppender() {
        return new ConsoleAppender<>();
    }

    protected Appender<Object> getConfiguredAppender() {
        ConsoleAppender<Object> ca = new ConsoleAppender<>();
        ca.setEncoder(new NopEncoder<>());
        ca.start();
        return ca;
    }

    @Test
    public void smoke() {
        ConsoleAppender<Object> ca = (ConsoleAppender<Object>) getAppender();
        ca.setEncoder(new DummyEncoder<>());
        ca.start();
        ca.doAppend(new Object());
        Assertions.assertEquals(DummyLayout.DUMMY, teeOut.toString());
    }

    @Test
    public void open() {
        ConsoleAppender<Object> ca = (ConsoleAppender<Object>) getAppender();
        DummyEncoder<Object> dummyEncoder = new DummyEncoder<>();
        dummyEncoder.setFileHeader("open");
        ca.setEncoder(dummyEncoder);
        ca.start();
        ca.doAppend(new Object());
        ca.stop();
        Assertions.assertEquals("open" + CoreConstants.LINE_SEPARATOR + DummyLayout.DUMMY, teeOut.toString());
    }

    @Test
    public void testClose() {
        ConsoleAppender<Object> ca = (ConsoleAppender<Object>) getAppender();
        DummyEncoder<Object> dummyEncoder = new DummyEncoder<>();
        dummyEncoder.setFileFooter("CLOSED");
        ca.setEncoder(dummyEncoder);
        ca.start();
        ca.doAppend(new Object());
        ca.stop();
        // ConsoleAppender must keep the underlying stream open.
        // The console is not ours to close.
        Assertions.assertFalse(teeOut.isClosed());
        Assertions.assertEquals(DummyLayout.DUMMY + "CLOSED", teeOut.toString());
    }

    // See http://jira.qos.ch/browse/LBCORE-143
    @Test
    public void changeInConsole() {
        ConsoleAppender<Object> ca = (ConsoleAppender<Object>) getAppender();
        EchoEncoder<Object> encoder = new EchoEncoder<>();
        ca.setEncoder(encoder);
        ca.start();
        ca.doAppend("a");
        Assertions.assertEquals("a" + CoreConstants.LINE_SEPARATOR, teeOut.toString());

        XTeeOutputStream newTee = new XTeeOutputStream(null);
        System.setOut(new PrintStream(newTee));
        ca.doAppend("b");
        Assertions.assertEquals("b" + CoreConstants.LINE_SEPARATOR, newTee.toString());
    }

    @Test
    public void testUTF16BE()  {
        ConsoleAppender<Object> ca = (ConsoleAppender<Object>) getAppender();
        DummyEncoder<Object> dummyEncoder = new DummyEncoder<>();
        Charset utf16BE = StandardCharsets.UTF_16BE;
        dummyEncoder.setCharset(utf16BE);
        ca.setEncoder(dummyEncoder);
        ca.start();
        ca.doAppend(new Object());
        Assertions.assertEquals(DummyLayout.DUMMY, new String(teeOut.toByteArray(), utf16BE));
    }

    @Test
    public void wrongTarget() {
        ConsoleAppender<Object> ca = (ConsoleAppender<Object>) getAppender();
        EchoEncoder<Object> encoder = new EchoEncoder<>();
        encoder.setContext(context);
        ca.setContext(context);
        ca.setTarget("foo");
        ca.setEncoder(encoder);
        ca.start();
        ca.doAppend("a");
        StatusChecker checker = new StatusChecker(context);
        // 21:28:01,246 + WARN in ch.qos.logback.core.ConsoleAppender[null] - [foo]
        // should be one of [System.out,
        // System.err]
        // 21:28:01,246 |-WARN in ch.qos.logback.core.ConsoleAppender[null] - Using
        // previously set target, System.out by
        // default.
        // StatusPrinter.print(context);

        checker.assertContainsMatch(Status.WARN, "\\[foo\\] should be one of \\[System.out, System.err\\]");

    }
}

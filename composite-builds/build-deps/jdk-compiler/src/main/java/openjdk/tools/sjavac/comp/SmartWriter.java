/*
 * Copyright (c) 2012, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package openjdk.tools.sjavac.comp;

import openjdk.tools.sjavac.Log;

import java.io.*;
import jdkx.tools.JavaFileObject;

/**
 * The SmartWriter will cache the written data and when the writer is closed,
 * then it will compare the cached data with the old_content string.
 * If different, then it will write all the new content to the file.
 * If not, the file is not touched.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class SmartWriter extends Writer {

    String name;
    JavaFileObject file;
    String oldContent;
    StringWriter newContent = new StringWriter();
    boolean closed;

    public SmartWriter(JavaFileObject f, String s, String n) {
        name = n;
        file = f;
        oldContent = s;
        newContent = new StringWriter();
        closed = false;
    }

    public void write(char[] chars, int i, int i1) {
        newContent.write(chars, i, i1);
    }

    public void close() throws IOException {
        if (closed) return;
        closed = true;
        String s = newContent.toString();
        if (!oldContent.equals(s)) {
            int p = file.getName().lastIndexOf(File.separatorChar);
            try (Writer writer = file.openWriter()) {
                writer.write(s);
            }
            Log.debug("Writing " + file.getName().substring(p + 1));
        }
    }

    public void flush() throws IOException {
    }
}

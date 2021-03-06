/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.text;

import control.identifiers.Extrema;

import java.io.IOException;
import java.io.Writer;

/**
 * Pushes extreme values to a writer in the appropriate format
 * for the ExtremaReader to handle.
 * <p/>
 * Created by dbborens on 12/11/13.
 */
public class ExtremaHelper {

    private final Writer writer;

    public ExtremaHelper(Writer writer) {
        this.writer = writer;
    }

    /**
     * Format the metadata and push to the writer.
     *
     * @param fieldName
     * @param extrema
     */
    public void push(String fieldName, Extrema extrema) {
        // Format:
        // a>0.0@(0, 0, 0 | 0 | 2.0):7.0@(1, 0, 0 | 0 | 1.0)1,0,1.0

        StringBuilder sb = new StringBuilder();
        sb.append(fieldName);
        sb.append(">");
        sb.append(extrema.toString());

        try {
            writer.append(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Close the writer.
     */
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

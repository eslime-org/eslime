/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package factory.control.arguments;

import control.arguments.Argument;
import control.arguments.ConstantDouble;
import control.arguments.UniformDouble;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by David B Borenstein on 4/7/14.
 */
public abstract class DoubleArgumentFactory {

    public static Argument<Double> instantiate(Element e, String name, double defaultValue, Random random) {
        // No element --> return default
        Element valueElement = e.element(name);
        if (valueElement == null) {
            return new ConstantDouble(defaultValue);
        }

        return instantiate(e, name, random);
    }

    public static Argument<Double> instantiate(Element e, String name, Random random) {

        // No element --> return default
        Element valueElement = e.element(name);
        if (valueElement == null) {
            throw new IllegalArgumentException("No value specified for " +
                    "expected field " + name + " in element " +
                    e.getQualifiedName());
        }

        // No children --> return text
        Iterator children = valueElement.elementIterator();
        if (!children.hasNext()) {
            return getConstant(valueElement);
        }

        // Instantiate child
        Element stochastic = (Element) children.next();
        String className = stochastic.getName();

        if (className.equalsIgnoreCase("constant")) {
            return getConstant(stochastic);
        } else if (className.equalsIgnoreCase("uniform")) {
            return getUniformDouble(stochastic, random);
        } else {
            throw new IllegalArgumentException("Unrecognized argument type '" + className + "'");
        }
    }

    private static Argument<Double> getUniformDouble(Element valueElement, Random random) {
        double min = instantiate(valueElement, "min", random).next();
        double max = instantiate(valueElement, "max", random).next();

        Argument<Double> ret = new UniformDouble(min, max, random);
        return ret;
    }

    private static Argument<Double> getConstant(Element e) {
        String valueText = e.getTextTrim();
        double ret = Double.valueOf(valueText);
        return new ConstantDouble(ret);
    }
}
package de.icw.cui.test.jsf.renderer.util;

import java.io.Serializable;
import java.util.Comparator;

import org.jdom2.Attribute;

/**
 * Compares two {@link Attribute} elements by name
 * 
 * @author Oliver Wolff
 */
public class AttributeComparator implements Comparator<Attribute>, Serializable {

    private static final long serialVersionUID = 2093668555465640881L;

    @Override
    public int compare(final Attribute o1, final Attribute o2) {
        return o1.getName().compareTo(o2.getName());
    }

}

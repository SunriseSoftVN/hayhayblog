/*
 * NumberValue.java
 *
 * Created on April 29, 2006, 5:00 PM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rometools.feed.module.sle.types;

import com.sun.syndication.feed.impl.ObjectBean;
import org.jdom2.Namespace;

import java.math.BigDecimal;

/**
 * An EntryValue implementation for "number" data-type values.
 * 
 * @author <a href="mailto:cooper@screaming-penguin.com">Robert "kebernet" Cooper</a>
 */
public class NumberValue implements EntryValue {
    private static final long serialVersionUID = 8043418996659222922L;
    private BigDecimal value;
    private final ObjectBean obj = new ObjectBean(NumberValue.class, this);
    private String element;
    private String label;
    private Namespace namespace = Namespace.XML_NAMESPACE;

    /** Creates a new instance of NumberValue */
    public NumberValue() {
    }

    public void setElement(final String element) {
        this.element = element;
    }

    @Override
    public String getElement() {
        return element;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public void setValue(final BigDecimal value) {
        this.value = value;
    }

    @Override
    public Comparable<BigDecimal> getValue() {
        return value;
    }

    @Override
    public Object clone() {
        final NumberValue clone = new NumberValue();
        clone.setElement(getElement());
        clone.setLabel(getLabel());
        clone.setValue(value);
        clone.setNamespace(namespace);

        return clone;
    }

    @Override
    public boolean equals(final Object o) {
        return obj.equals(o);
    }

    @Override
    public int hashCode() {
        return obj.hashCode();
    }

    @Override
    public String toString() {
        return "[Element:" + element + " Label:" + label + " Value:" + value + "]";
    }

    @Override
    public Namespace getNamespace() {
        return namespace;
    }

    public void setNamespace(final Namespace namespace) {
        this.namespace = namespace == null ? Namespace.XML_NAMESPACE : namespace;
    }
}

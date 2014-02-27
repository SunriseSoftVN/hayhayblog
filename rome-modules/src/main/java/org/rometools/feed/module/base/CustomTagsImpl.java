/*
 * CustomTagsImpl.java
 *
 * Created on February 6, 2006, 12:26 AM
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.rometools.feed.module.base;

import com.sun.syndication.feed.CopyFrom;

import java.util.ArrayList;
import java.util.List;

/**
 * @version $Revision: 1.1 $
 * @author <a href="mailto:cooper@screaming-penguin.com">Robert "kebernet" Cooper</a>
 */
public class CustomTagsImpl implements CustomTags {

    private List values;

    /** Creates a new instance of CustomTagsImpl */
    public CustomTagsImpl() {
    }

    @Override
    public List getValues() {
        values = values == null ? new ArrayList() : values;
        return values;
    }

    @Override
    public void setValues(final List values) {
        this.values = values;
    }

    @Override
    public void copyFrom(final CopyFrom object) {
        final CustomTags ct = (CustomTags) object;
        values = new ArrayList(ct.getValues());
    }

    @Override
    public Object clone() {
        final CustomTagsImpl cti = new CustomTagsImpl();
        cti.values = new ArrayList(values);
        return cti;
    }

    @Override
    public Class getInterface() {
        return CustomTags.class;
    }

    @Override
    public String getUri() {
        return CustomTags.URI;
    }

}

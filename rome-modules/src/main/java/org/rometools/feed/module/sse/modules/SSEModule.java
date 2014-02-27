package org.rometools.feed.module.sse.modules;

import com.sun.syndication.feed.CopyFrom;
import com.sun.syndication.feed.module.Module;
import org.jdom2.Namespace;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The base module for SSE data synchronization. Defines a namespace, uri, and basic copying operations.
 */
public abstract class SSEModule implements Module {
    private static final long serialVersionUID = -7487775381662291314L;

    public static final String SSE_SCHEMA_URI = "http://www.microsoft.com/schemas/rss/sse";

    // a default prefix to use for sse tags
    public static final String PREFIX = "sx";
    public static final Namespace SSE_NS = Namespace.getNamespace(PREFIX, SSE_SCHEMA_URI);

    public static final Set<Namespace> NAMESPACES;

    static {
        final Set<Namespace> nss = new HashSet<Namespace>();
        nss.add(SSEModule.SSE_NS);
        NAMESPACES = Collections.unmodifiableSet(nss);
    }

    @Override
    public String getUri() {
        return SSE_SCHEMA_URI;
    }

    @Override
    public Class<? extends Module> getInterface() {
        return getClass();
    }

    @Override
    public Object clone() {
        SSEModule clone = null;
        try {
            clone = this.getClass().newInstance();
            clone.copyFrom(this);
        } catch (final InstantiationException e) {
            // TODO: use logging
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            // TODO: use logging
            e.printStackTrace();
        }
        return clone;
    }

    @Override
    public abstract void copyFrom(CopyFrom<? extends Module> obj);
}

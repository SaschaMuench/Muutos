package de.sonnmatt.muutos.rpc.proxy;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.rebind.rpc.ProxyCreator;

import de.sonnmatt.muutos.rpc.proxy.MyRemoteServiceProxy;

/**
 * This proxy creator extends the default GWT {@link ProxyCreator} and replaces {@link RemoteServiceProxy} as base class
 * of proxies with {@link MyRemoteServiceProxy}.
 */
public class MyProxyCreator extends ProxyCreator {
    public MyProxyCreator(JClassType serviceIntf) {
        super(serviceIntf);
    }

    @Override
    protected Class<? extends RemoteServiceProxy> getProxySupertype() {
        return MyRemoteServiceProxy.class;
    }
}
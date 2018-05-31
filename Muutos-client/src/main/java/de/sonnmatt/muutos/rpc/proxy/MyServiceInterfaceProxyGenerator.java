package de.sonnmatt.muutos.rpc.proxy;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.rpc.ProxyCreator;
import com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator;

/**
 * This Generator extends the default GWT {@link ServiceInterfaceProxyGenerator} and replaces it in the
 * co.company.MyModule GWT module for all types that are assignable to
 * {@link com.google.gwt.user.client.rpc.RemoteService}. Instead of the default GWT {@link ProxyCreator} it provides the
 * {@link MyProxyCreator}.
 */
public class MyServiceInterfaceProxyGenerator extends ServiceInterfaceProxyGenerator {
    @Override
    protected ProxyCreator createProxyCreator(JClassType remoteService) {
        return new MyProxyCreator(remoteService);
    }
}
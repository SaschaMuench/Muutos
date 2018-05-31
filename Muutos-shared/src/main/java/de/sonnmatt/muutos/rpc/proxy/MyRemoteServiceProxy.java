package de.sonnmatt.muutos.rpc.proxy;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.client.rpc.impl.Serializer;

/**
 * The remote service proxy extends default GWT {@link RemoteServiceProxy} and proxies the {@link AsyncCallback} with
 * the {@link AsyncCallbackProxy}.
 */
public class MyRemoteServiceProxy extends RemoteServiceProxy {
    public MyRemoteServiceProxy(String moduleBaseURL, String remoteServiceRelativePath, String serializationPolicyName,
                                 Serializer serializer) {
        super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);
    }

    @Override
    protected <T> RequestCallback doCreateRequestCallback(RequestCallbackAdapter.ResponseReader responseReader,
                                                          String methodName, RpcStatsContext statsContext,
                                                          AsyncCallback<T> callback) {
        return super.doCreateRequestCallback(responseReader, methodName, statsContext, new AsyncCallbackProxy<T>(callback));
    }
}
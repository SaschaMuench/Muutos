package de.sonnmatt.muutos.rpc.proxy;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AsyncCallbackProxy<T> implements AsyncCallback<T> {

	private Logger logger = Logger.getLogger("Muutos");
    private AsyncCallback<T> delegate;
    private long startTime;

    public AsyncCallbackProxy(AsyncCallback<T> delegate) {
        this.delegate = delegate;
        this.startTime = System.currentTimeMillis();
        //logger.log(Level.SEVERE, "Start " + this.delegate.getClass().getName().intern() + " : " + this.startTime);
    }

    @Override
    public final void onFailure(Throwable caught) {
    	logger.log(Level.SEVERE, "AsyncCallbackProxy#onFailure() : " + caught.getMessage(), caught);
    	logger.log(Level.SEVERE, "Time (failure) ms: " + (System.currentTimeMillis() - startTime));
    	//ToDo: timeout handling
    	delegate.onFailure(caught);
    }

    @Override
    public final void onSuccess(T result) {
    	//logger.log(Level.SEVERE, "Time (success) ms: " + (System.currentTimeMillis() - startTime));
        delegate.onSuccess(result);
    }
}
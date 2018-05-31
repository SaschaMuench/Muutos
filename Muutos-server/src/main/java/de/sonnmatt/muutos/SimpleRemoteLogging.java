package de.sonnmatt.muutos;

import java.text.SimpleDateFormat;
import java.util.logging.LogRecord;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import com.google.gwt.logging.shared.RemoteLoggingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SimpleRemoteLogging extends RemoteServiceServlet implements RemoteLoggingService {
	
    
    private static final long serialVersionUID = 7044833845984237360L;
    
    static Logger log = LogManager.getLogger("clientLog");
    
	@Override
	public String logOnServer(LogRecord record) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpSession session = httpServletRequest.getSession(true);

        ThreadContext.put("client.datetime", df.format(record.getMillis()));
		ThreadContext.put("client.loggername", record.getLoggerName());
		ThreadContext.put("client.sessionid",session.getId());
		ThreadContext.put("client.sourceclassname", record.getSourceClassName());
		ThreadContext.put("client.sourcemethodname", record.getSourceMethodName());
		
		if (record.getLevel().toString().equals("SEVERE")) { 
			log.log(Level.ERROR, record.getMessage(), record.getThrown());
		} else if (record.getLevel().toString().equals("WARNING")) { 
			log.log(Level.WARN, record.getMessage(), record.getThrown());
		} else if (record.getLevel().toString().equals("INFO")) { 
			log.log(Level.INFO, record.getMessage(), record.getThrown());
		} else if (record.getLevel().toString().equals("CONFIG")) { 
			log.log(Level.DEBUG, record.getMessage(), record.getThrown());
		} else if (record.getLevel().toString().equals("FINE")) { 
			log.log(Level.TRACE, record.getMessage(), record.getThrown());
		} else if (record.getLevel().toString().equals("FINER")) { 
			log.log(Level.TRACE, record.getMessage(), record.getThrown());
		} else if (record.getLevel().toString().equals("FINEST")) { 
			log.log(Level.TRACE, record.getMessage(), record.getThrown());
		} else if (record.getLevel().toString().equals("ALL")) { 
			log.log(Level.ALL, record.getMessage(), record.getThrown());
		}
		ThreadContext.clearAll();
		return null;
	}
}
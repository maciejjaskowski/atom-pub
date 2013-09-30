import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: pragmatists
 * Date: 9/28/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class AtomPubServer {

    private Server server;

    public static void main(String[] args)  {
        AtomPubServer server = new AtomPubServer(8080, new EmployeeProviderServlet(new EmployeeCollectionAdapter(new Employees())));

        server.start();
        server.join();
    }

    public AtomPubServer(int port, EmployeeProviderServlet servlet) {
        Server server1 = new Server(port);


        HandlerCollection handlers = new HandlerCollection();
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        handlers.setHandlers(new Handler[]{contexts,requestLogHandler});


        ServletContextHandler servletHandler = new ServletContextHandler(handlers, "/", ServletContextHandler.SESSIONS);
        ServletHolder servletHolder = new ServletHolder(servlet);
        servletHandler.addServlet(servletHolder, "/*");

        server1.setHandler(handlers);

        requestLogHandler.setRequestLog(new NCSARequestLog() {
            private boolean started;
            private FileWriter fileWriter;
            public boolean stopped;

            @Override
            public void log(Request request, Response response) {
                Enumeration headerNames = request.getHeaderNames();
                String result = "Request:\nHeader: ";
                while(headerNames.hasMoreElements()) {
                    String o = (String) headerNames.nextElement();
                    result += o + ": " + request.getHeader(o) + ";\n";
                }


                try {
                    fileWriter.append(result + "\n");
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


            }

            @Override
            public void doStart() throws Exception {
                File file = new File("./logs/jetty.log");
                fileWriter = new FileWriter(file);
                started = true;
            }

            @Override
            public void doStop() throws Exception {
                fileWriter.close();
                stopped = true;
            }


        });
        server = server1;
    }


    private void join() {
        try {
            server.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

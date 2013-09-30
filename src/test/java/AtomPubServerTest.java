import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Entry;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.abdera.protocol.client.RequestOptions;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import java.util.Date;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;


public class AtomPubServerTest {

    private AtomPubServer atomPubServer;
    private EmployeeProviderServlet servlet;
    private Employees employees;

    private Factory factory;
    private AbderaClient abderaClient;
    private String serverUri;

    @Before
    public void startServer() {
        employees = new Employees();
        servlet = new EmployeeProviderServlet(new EmployeeCollectionAdapter(employees));

        atomPubServer = new AtomPubServer(9091, servlet);
        serverUri = "http://localhost:9091";

        atomPubServer.start();

        Abdera abdera = new Abdera();
        abderaClient = new AbderaClient(abdera);
        factory = abdera.getFactory();
    }

    @Test
    public void shouldPostANewEmployee() {

        Entry entry = factory.newEntry();
        entry.setId("tag:example.org,2011:foo");
        entry.setTitle("This is the title");
        entry.setUpdated(new Date());
        entry.addAuthor("Chad");
        entry.setContent("Hello World");

        ClientResponse resp = post(entry);


        assertThat(employees, hasItem(new Employee("Hello World", 1)));
    }

    private ClientResponse post(Entry entry) {
        RequestOptions opts = new RequestOptions();
        opts.setContentType("application/atom+xml;type=entry");

        return post("employee", entry, opts);
    }

    private ClientResponse post(String resourceName, Entry entry, RequestOptions opts) {
        return abderaClient.post(serverUri +"/" + resourceName, entry, opts);
    }

    private static void report(String title, String message) {
        System.out.println("== " + title + " ==");
        if (message != null)
            System.out.println(message);
        System.out.println();
    }

    @After
    public void stopServer() {
        atomPubServer.stop();
    }

}

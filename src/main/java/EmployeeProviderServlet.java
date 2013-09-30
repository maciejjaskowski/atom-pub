import org.apache.abdera.protocol.server.Provider;
import org.apache.abdera.protocol.server.impl.DefaultProvider;
import org.apache.abdera.protocol.server.impl.SimpleWorkspaceInfo;
import org.apache.abdera.protocol.server.servlet.AbderaServlet;

/**
 * Created with IntelliJ IDEA.
 * User: pragmatists
 * Date: 9/28/13
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeProviderServlet extends AbderaServlet {

    private EmployeeCollectionAdapter employeeCollectionAdapter;

    public EmployeeProviderServlet(EmployeeCollectionAdapter employeeCollectionAdapter) {
        this.employeeCollectionAdapter = employeeCollectionAdapter;
    }


    @Override
    protected Provider createProvider() {
        employeeCollectionAdapter.setHref("employee");

        SimpleWorkspaceInfo wi = new SimpleWorkspaceInfo();
        wi.setTitle("Employee Directory Workspace");
        wi.addCollection(employeeCollectionAdapter);

        DefaultProvider provider = new DefaultProvider("/");
        provider.addWorkspace(wi);

        provider.init(getAbdera(), null);
        return provider;
    }
}

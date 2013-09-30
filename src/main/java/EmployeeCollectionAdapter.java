import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.i18n.text.Sanitizer;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Person;
import org.apache.abdera.protocol.server.RequestContext;
import org.apache.abdera.protocol.server.context.ResponseContextException;
import org.apache.abdera.protocol.server.impl.AbstractEntityCollectionAdapter;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: pragmatists
 * Date: 9/28/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeCollectionAdapter
        extends AbstractEntityCollectionAdapter<Employee> {

    private static final String ID_PREFIX = "ID_PREFIX";
    private Employees employees;
    private AtomicInteger nextId = new AtomicInteger(1);

    public EmployeeCollectionAdapter(Employees employees) {
        this.employees = employees;

    }

    @Override
    public Employee postEntry(String s, IRI iri, String s2, Date date, List<Person> persons, Content content, RequestContext requestContext) throws ResponseContextException {
        Employee employee = new Employee(content.getText().trim(), nextId.getAndIncrement());

        employees.add(employee);

        return employee;
    }

    @Override
    public void putEntry(Employee employee, String s, Date date, List<Person> persons, String s2, Content elements, RequestContext requestContext) throws ResponseContextException {
        employee.setName(elements.getText().trim());
    }

    @Override
    public void deleteEntry(String resourceName, RequestContext requestContext) throws ResponseContextException {
        Integer id = getIdFromResourceName(resourceName);
        employees.remove(id);
    }

    @Override
    public Object getContent(Employee employee, RequestContext requestContext) throws ResponseContextException {
        Content content = requestContext.getAbdera().getFactory().newContent(Content.Type.TEXT);
        content.setText(employee.getName());
        return content;
    }

    @Override
    public Iterable<Employee> getEntries(RequestContext requestContext) throws ResponseContextException {
        return employees;
    }

    @Override
    public Employee getEntry(String resourceName, RequestContext requestContext) throws ResponseContextException {
        Integer id = getIdFromResourceName(resourceName);
        return employees.get(id);

    }

    private Integer getIdFromResourceName(String resourceName) throws ResponseContextException {
        int idx = resourceName.indexOf("-");
        if (idx == -1) {
             throw new ResponseContextException(404);
        }
        return new Integer(resourceName.substring(0, idx));
    }

    @Override
    public String getId(Employee employee) throws ResponseContextException {
        return ID_PREFIX + employee.getId();
    }

    @Override
    public String getName(Employee employee) throws ResponseContextException {
        return employee.getId() + "-" + Sanitizer.sanitize(employee.getName());
    }

    @Override
    public String getTitle(Employee employee) throws ResponseContextException {
        return employee.getName();
    }

    @Override
    public Date getUpdated(Employee employee) throws ResponseContextException {
        return employee.getUpdated();
    }

    @Override
    public String getAuthor(RequestContext requestContext) throws ResponseContextException {
        return "Acme Industries";
    }

    @Override
    public String getId(RequestContext requestContext) {
        return "tag:acme.com,2007:employee:feed";
    }

    @Override
    public String getTitle(RequestContext requestContext) {
        return "Acme Employee Database";
    }
}

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pragmatists
 * Date: 9/28/13
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Employees implements Iterable<Employee> {

    private Map<Integer, Employee> map = new HashMap<Integer, Employee>();

    public Employee get(Integer id) {
        return map.get(id);
    }

    public void add(Employee employee) {
        map.put(employee.getId(), employee);
    }

    public void remove(Integer id) {
        map.remove(id);
    }

    public Iterator<Employee> iterator() {
        return map.values().iterator();
    }
}

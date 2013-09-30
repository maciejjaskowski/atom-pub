import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class Employee {
    private int id;
    private String name;
    private Date updated;

    public Employee(String name, int id) {
        this.id = id;
        this.name = name;
        this.updated = new Date();
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdated() {
        return updated;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "updated");
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, "updated");
    }

}

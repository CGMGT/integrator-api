package gt.com.tigo.integradorhome.dto;

public class ColumnDto {

    private String id;
    private String name;

    public ColumnDto() {
        // no constructor for the moment
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return String.format(
                "{ id: %s, name: %s }",
                this.id,
                this.name
        );
    }
}

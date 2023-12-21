package gt.com.tigo.integradorhome.dto;

public class FilterInfoDto {

    private String id;
    private String value;
    private String operator;

    public FilterInfoDto() {
    }

    public FilterInfoDto(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String toString() {
        return String.format("{id: '%s', value: '%s', operator: '%s'}", this.id, this.value, this.operator);
    }
}

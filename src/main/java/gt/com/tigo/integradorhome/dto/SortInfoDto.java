package gt.com.tigo.integradorhome.dto;

public class SortInfoDto {

    private String id;
    private Boolean desc;

    public SortInfoDto() {
        // no constructor for the moment
    }

    public SortInfoDto(String id, Boolean desc) {
        this.id = id;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getDesc() {
        return desc;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }

    public String toString() {
        return String.format("{id: '%s', desc: '%s'}", this.id, this.desc);
    }
}

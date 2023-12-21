package gt.com.tigo.integradorhome.dto;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

public class ResourceDto {

    private String filename;
    private MediaType mediaType;
    private ByteArrayResource resource;
    private Integer length;

    public ResourceDto() {
        // no constructor for the moment
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public ByteArrayResource getResource() {
        return resource;
    }

    public void setResource(ByteArrayResource resource) {
        this.resource = resource;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}

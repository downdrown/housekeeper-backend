package at.downdrown.housekeeper.api.dto;

import lombok.Data;

/**
 * DTO for the Avatar model.
 *
 * @author Manfred Huber
 */
@Data
public class AvatarDTO {

    private Long id;
    private String originalFileName;
    private String contentType;
    private Long size;
    private byte[] content;

}

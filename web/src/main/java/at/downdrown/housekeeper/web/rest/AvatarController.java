package at.downdrown.housekeeper.web.rest;

import at.downdrown.housekeeper.api.dto.AvatarDTO;
import at.downdrown.housekeeper.api.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * REST Controller for the {@link AvatarDTO}.
 *
 * @author Manfred Huber
 */
@RestController
@RequestMapping("avatar")
public class AvatarController {

    private final AvatarService avatarService;

    @Autowired
    public AvatarController(
        final AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> setAvatarForUser(@PathVariable("username") String username, @RequestParam("file") MultipartFile file) throws IOException {

        AvatarDTO avatar = new AvatarDTO();
        avatar.setOriginalFileName(file.getOriginalFilename());
        avatar.setContentType(file.getContentType());
        avatar.setSize(file.getSize());
        avatar.setContent(file.getBytes());

        try {
            avatarService.setAvatarForUser(username, avatar);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{username}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<ByteArrayResource> getAvatarForUser(@PathVariable("username") String username) {
        AvatarDTO avatar = avatarService.getAvatarForUser(username);
        if (avatar != null) {
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + avatar.getOriginalFileName())
                .contentType(MediaType.parseMediaType(avatar.getContentType()))
                .contentLength(avatar.getSize())
                .body(new ByteArrayResource(avatar.getContent()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{username}")
    public ResponseEntity<Void> deleteAvatarForUser(@PathVariable("username") String username) {
        avatarService.deleteAvatarForUser(username);
        return ResponseEntity.ok().build();
    }
}

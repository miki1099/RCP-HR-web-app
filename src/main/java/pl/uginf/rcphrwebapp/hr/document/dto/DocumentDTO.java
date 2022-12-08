package pl.uginf.rcphrwebapp.hr.document.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.uginf.rcphrwebapp.hr.user.model.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {

    private String filename;

    private String type;

    private User user;

    private byte[] data;
}

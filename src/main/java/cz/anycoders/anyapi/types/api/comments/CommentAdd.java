package cz.anycoders.anyapi.types.api.comments;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommentAdd {
    private String secretKey;
    private Long userId;
    private String text;
}

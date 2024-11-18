package cz.anycoders.anyapi.types.api.posts;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;


@NoArgsConstructor
@Data
public class PostRequest {
    private String secretKey;
    private Long userId;
    //private Long postId;
    private Long typeId;
    private Long stateId;
    private String[] pros;
    private String[] cons;
    private String title;
    private String description;
}

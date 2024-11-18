package cz.anycoders.anyapi.types.api.posts;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PostUpdateRequest {
    private String secretKey;
    private Long userId;
    private Long typeId;
    private Long stateId;
    private String[] pros;
    private String[] cons;
    private String title;
    private String description;
}

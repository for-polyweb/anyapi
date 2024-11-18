package cz.anycoders.anyapi.types.api.rating;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RateUpdate {
    private String secretKey;
    private Long userId;
    private Integer value;
    private String text;
}

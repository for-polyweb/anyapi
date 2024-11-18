package cz.anycoders.anyapi.types.api;

import cz.anycoders.anyapi.entity.APIError;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ApiResponse {
    private String result;
    private Object data;
    private APIError error;
}

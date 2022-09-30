package ski.mashiro.pojo;

import lombok.Data;

/**
 * @author FeczIne
 * 2022-10-01 01:36:38
 */
@Data
public class Result {
    private Integer code;
    private Object data;

    public Result(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }
}

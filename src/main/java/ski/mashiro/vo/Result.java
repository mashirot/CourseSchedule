package ski.mashiro.vo;

public record Result<T>(
        int code,
        T data,
        String msg
) {
    public static <T> Result<T> success(int code, T data) {
        return new Result<>(code, data, null);
    }

    public static <T> Result<T> failed(int code, String msg) {
        return new Result<>(code, null, msg);
    }
}

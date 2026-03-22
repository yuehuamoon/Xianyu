package commontrade.commonyh.pojo.result;

import lombok.Data;

/**
 * 全局统一响应结果类
 * 适用于所有接口的返回格式，包含状态码、提示信息、数据体
 */
@Data
public class Result<T> {

    /**
     * 响应状态码
     * 200：成功
     * 500：服务器内部错误
     * 400：参数错误
     * 401：未登录/权限不足
     * 404：资源不存在
     * 其他可根据业务扩展
     */
    private Integer code;

    /**
     * 响应提示信息
     * 成功时："操作成功"、"登录成功"等
     * 失败时："参数不能为空"、"密码错误"等
     */
    private String msg;

    /**
     * 响应数据体
     * 成功时：返回业务数据（如用户信息、列表、单个对象）
     * 失败时：可返回 null 或具体错误详情
     */
    private T data;

    // ======================== 静态构造方法（简化调用） ========================

    /**
     * 通用成功返回（无数据）
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(null);
        return result;
    }

    /**
     * 通用成功返回（带数据）
     * @param data 业务数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 通用成功返回（自定义提示信息+数据）
     * @param msg 自定义提示语
     * @param data 业务数据
     */
    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * 通用失败返回（默认状态码500+自定义提示）
     * @param msg 失败提示信息
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    /**
     * 通用失败返回（自定义状态码+提示信息）
     * @param code 自定义状态码
     * @param msg 失败提示信息
     */
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    /**
     * 通用失败返回（自定义状态码+提示+数据）
     * @param code 自定义状态码
     * @param msg 失败提示信息
     * @param data 错误详情数据
     */
    public static <T> Result<T> error(Integer code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    // ======================== 常用业务场景快捷方法（可选，简化开发） ========================

    /**
     * 参数错误返回（400）
     * @param msg 参数错误提示
     */
    public static <T> Result<T> paramError(String msg) {
        return error(400, msg);
    }

    /**
     * 权限不足返回（401）
     */
    public static <T> Result<T> unauthorized() {
        return error(401, "未登录或登录已过期，请重新登录");
    }

    /**
     * 资源不存在返回（404）
     * @param msg 资源提示
     */
    public static <T> Result<T> notFound(String msg) {
        return error(404, msg);
    }
}
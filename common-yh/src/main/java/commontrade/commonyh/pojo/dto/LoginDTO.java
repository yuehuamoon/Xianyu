// 包路径建议：commontrade.commonyh.pojo.dto
package commontrade.commonyh.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求 DTO：专门接收前端登录表单数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    // 字段名必须和前端传入的参数名一致（如前端传 phone，这里就叫 phone）
    private String phone;    // 手机号
    private String password; // 密码

}
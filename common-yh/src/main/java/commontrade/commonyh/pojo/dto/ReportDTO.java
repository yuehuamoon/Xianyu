package commontrade.commonyh.pojo.dto;

import lombok.Data;
@Data
public class ReportDTO {
    private String type;   // user, post, comment, good
    private Integer id;
}
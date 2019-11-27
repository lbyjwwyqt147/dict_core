package pers.liujunyi.cloud.dict.domain.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.query.jpa.annotation.MatchType;
import pers.liujunyi.cloud.common.query.jpa.annotation.QueryCondition;
import pers.liujunyi.cloud.common.query.mongo.BaseMongoQuery;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/***
 * 文件名称: DictionariesQueryDto.java
 * 文件描述: 数据字典 query dto
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class DictionariesQueryDto extends BaseMongoQuery {

    /** 字典代码 */
    @ApiModelProperty(value = "字典代码")
    @Length(min = 0, max = 32, message = "字典代码 最大长度为32个字符")
    @QueryCondition()
    private String dictCode;

    /** 字典名称 */
    @ApiModelProperty(value = "字典名称")
    @Length(min = 0, max = 32, message = "字典名称 最大长度为32个字符")
    @QueryCondition(func =  MatchType.like)
    private String dictName;

    /** 上级ID */
    @ApiModelProperty(value = "pid")
    @NotNull(message = "pid 必须填写")
    @Min(value = 0, message = "pid 必须是数字类型")
    @QueryCondition()
    private Long pid;

    /** 标签标注 */
    @ApiModelProperty(value = "标签标注")
    @Length(min = 0, max = 32, message = "标签 最大长度为32个字符")
    private String dictLabel;

}

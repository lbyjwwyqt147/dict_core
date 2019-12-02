package pers.liujunyi.cloud.dict.domain.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;
import pers.liujunyi.cloud.common.util.RegexpUtils;

import javax.validation.constraints.*;

/***
 * 文件名称: DictionariesDto.java
 * 文件描述: 数据字典 dto
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
public class DictionariesDto extends BaseDto {

    /** 字典代码 */
    @ApiModelProperty(value = "字典代码")
    @NotBlank(message = "字典代码必须填写")
    @Length(min = 0, max = 20, message = "字典代码 最多可以输入20个字符")
    @Pattern(regexp = RegexpUtils.CODE_REGEXP, message = "字典代码" + RegexpUtils.CODE_MSG)
    private String dictCode;

    /** 字典名称 */
    @ApiModelProperty(value = "字典名称")
    @NotBlank(message = "字典名称必须填写")
    @Length(min = 0, max = 32, message = "字典名称 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.NAME_REGEXP, message = "字典名称" + RegexpUtils.NAME_MSG)
    private String dictName;

    /** 上级ID */
    @NotNull(message = "pid 必须填写")
    @Min(value = 0, message = "pid 必须是数字类型")
    private Long pid;


    /** 排序值 */
    @ApiModelProperty(value = "排序值")
    @Min(value = 0, message = "排序值 必须大于0")
    @Max(value = 999,  message = "排序值 不能大于999")
    private Integer priority;

    /** 标签标注 */
    @ApiModelProperty(value = "标签标注")
    @Length(min = 0, max = 32, message = "标签 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.NAME_REGEXP, message = "标签" + RegexpUtils.NAME_MSG)
    private String dictLabel;

    /** 描述信息 */
    @ApiModelProperty(value = "描述信息")
    @Length(min = 0, max = 50, message = "描述信息 最多可以输入50个字符")
    @Pattern(regexp = RegexpUtils.ILLEGITMACY_REGEXP, message = "描述信息" + RegexpUtils.ILLEGITMACY_MSG)
    private String description;

    /** 预留字段1 */
    @ApiModelProperty(value = "attributeOne")
    @Length(min = 0, max = 45, message = "attributeOne 最多可以输入45个字符")
    @Pattern(regexp = RegexpUtils.NAME_REGEXP, message = "attributeOne" + RegexpUtils.NAME_MSG)
    private String attributeOne;

    /** 预留字段2 */
    @ApiModelProperty(value = "attributeTwo")
    @Length(min = 0, max = 65, message = "attributeTwo 最多可以输入65个字符")
    @Pattern(regexp = RegexpUtils.NAME_REGEXP, message = "attributeTwo" + RegexpUtils.NAME_MSG)
    private String attributeTwo;

    /** 预留字段3 */
    @ApiModelProperty(value = "attributeThree")
    @Length(min = 0, max = 100, message = "attributeThree 最多可以输入100个字符")
    @Pattern(regexp = RegexpUtils.NAME_REGEXP, message = "attributeThree" + RegexpUtils.NAME_MSG)
    private String attributeThree;


    /** 完整的层级ID */
    @Length(min = 0, max = 50, message = "fullParent 最多可以输入50个字符")
    private String fullDictParent;

    /** 完整的层级代码 */
    @Length(min = 0, max = 150, message = "fullParentCode 最多可以输入150个字符")
    private String fullDictParentCode;

    /** 全字典代码  (包含父级) */
    @Length(min = 0, max = 150, message = "fullDictCode 最多可以输入150个字符")
    private String fullDictCode;

    /** 层次级别 */
    private Byte dictLevel;

}

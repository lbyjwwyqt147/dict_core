package pers.liujunyi.cloud.dict.domain.dict;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/***
 * 文件名称: DictZtreeDto.java
 * 文件描述: 数据字典 ztree 需要的属性 dto
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DictZtreeDto implements Serializable {

    private static final long serialVersionUID = -1230356242808850930L;

    private Long id;

    /** 字典代码 */
    private String dictCode;

    /** 字典名称 */
    private String dictName;

    /** 上级ID */
    private Long pid;

    /** 标签标注 */
    private String dictLabel;

    /** 完整父级代码 */
    private String fullParentCode;

}

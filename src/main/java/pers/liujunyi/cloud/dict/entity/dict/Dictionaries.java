package pers.liujunyi.cloud.dict.entity.dict;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Table;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import pers.liujunyi.cloud.common.annotation.CustomerField;
import pers.liujunyi.cloud.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Version;

/***
 * 文件名称: Dictionaries.java
 * 文件描述: 数据字典实体类
 * 公 司:
 * 内容摘要:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity(name = "dictionaries")
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "dictionaries")
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "dictionaries", comment = "数据字段表")
public class Dictionaries extends BaseEntity {
    private static final long serialVersionUID = 4273145793432055139L;

    /** 字典代码 */
    @CustomerField( desc = "字典代码")
    @Column(length = 20, nullable = false, columnDefinition="varchar(20) NOT NULL COMMENT '字典代码'")
    private String dictCode;

    /** 字典名称 */
    @CustomerField( desc = "字典名称")
    @Column(length = 32, nullable = false, columnDefinition="varchar(32) NOT NULL COMMENT '字典名称'")
    private String dictName;

    /** 上级ID */
    @CustomerField(isLog = false)
    @Column(nullable = false, columnDefinition="bigint(20) DEFAULT '0' NOT NULL COMMENT '上级ID'")
    @Indexed
    private Long pid;

    /** 完整的层级ID */
    @CustomerField(isLog = false)
    @Column(columnDefinition="varchar(50) NOT NULL COMMENT '完整的层级ID'")
    private String fullDictParent;

    /** 完整的层级代码 */
    @CustomerField(isLog = false)
    @Column(columnDefinition="varchar(150) NOT NULL COMMENT '完整的层级代码'")
    private String fullDictParentCode;

    /** 完整的字典代码  (包含父级) */
    @CustomerField(isLog = false)
    @Column(columnDefinition="varchar(150) NOT NULL COMMENT '完整的字典代码  (包含父级)'")
    private String fullDictCode;

    /** 排序值 */
    @Column(columnDefinition="int(11) DEFAULT '10' COMMENT '排序值'")
    @Indexed
    private Integer priority;

    /** 标签标识 */
    @Column(columnDefinition="varchar(32) DEFAULT NULL COMMENT '标签标识'")
    private String dictLabel;

    /** 描述信息 */
    @Column(columnDefinition="varchar(50) DEFAULT NULL COMMENT '描述信息'")
    private String description;

    /** 0: 启动 1：禁用  */
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '状态：0：正常  1：禁用 '")
    @Indexed
    private Byte status;

    /** 层次级别 */
    @CustomerField(isLog = false)
    @Column(columnDefinition="tinyint(4) DEFAULT '1' COMMENT '层次级别'")
    private Byte dictLevel;

    /** 预留字段1 */
    @CustomerField()
    @Column(columnDefinition="varchar(45) DEFAULT NULL COMMENT '预留字段1'")
    private String attributeOne;

    /** 预留字段2 */
    @CustomerField()
    @Column(columnDefinition="varchar(65) DEFAULT NULL COMMENT '预留字段2'")
    private String attributeTwo;

    /** 预留字段3 */
    @CustomerField()
    @Column(columnDefinition="varchar(100) DEFAULT NULL COMMENT '预留字段3'")
    private String attributeThree;

    @Version
    @Override
    public Long getDataVersion() {
        return super.getDataVersion();
    }
}
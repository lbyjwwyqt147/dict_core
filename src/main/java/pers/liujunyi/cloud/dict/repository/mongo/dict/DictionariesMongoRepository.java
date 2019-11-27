package pers.liujunyi.cloud.dict.repository.mongo.dict;

import org.springframework.stereotype.Repository;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.dict.entity.dict.Dictionaries;

import java.util.List;

/***
 * 文件名称: DictionariesMongoRepository.java
 * 文件描述: 数据字典 Mongo Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Repository
public interface DictionariesMongoRepository extends BaseMongoRepository<Dictionaries, Long> {


    /**
     *  根据 pid 获取字典数据
     * @param pid
     * @param status   0: 启动 1：禁用
     * @return
     */
    List<Dictionaries> findByPidAndStatusOrderByPriorityAsc(Long pid, Byte status);

    /**
     * 根据父级代码 获取字典数据
     * @param fullParentCode
     * @param status
     * @return
     */
    List<Dictionaries> findByFullDictParentCodeLikeAndStatusOrderByPriorityAsc(String fullParentCode, Byte status);

    /**
     *  获取 存在 叶子节点的 字典数据
     * @param pid
     */
    List<Dictionaries> findByPidIn(List<Long> pid);

    /**
     *  获取 存在 叶子节点的 字典数据
     * @param pid
     * @param
     */
    List<Dictionaries> findByPid(Long pid);

    /**
     *  根据  pid  dictCode 获取数据
     * @param pid
     * @param dictCode
     * @return
     */
    List<Dictionaries> findByPidAndDictCode(Long pid, String dictCode);


    /**
     * 根据   fullParentCode   获取数据
     * @param fullParentCode
     * @return
     */
    List<Dictionaries> findByFullDictParentCode(String fullParentCode);

    /**
     * 根据   fullParentCode   获取数据
     * @param fullParentCode
     * @param status
     * @return
     */
    List<Dictionaries> findByStatusAndFullDictParentCodeOrderByPriorityAsc(Byte status, String fullParentCode);


    /**
     * 根据   fullParentCode   获取数据
     * @param fullParentCodes
     * @return
     */
    List<Dictionaries> findByFullDictParentCodeIn(List<String> fullParentCodes);


    /**
     * 根据  fullDictCode   获取 数据
     * @param fullDictCode
     * @return
     */
    List<Dictionaries> findByFullDictCode(String fullDictCode);

    /**
     * 根据 fullParentCode dictCode   获取 数据
     * @param fullParentCode
     * @param dictCode
     * @return
     */
    List<Dictionaries> findByFullDictParentCodeAndDictCode(String fullParentCode, String dictCode);


    /**
     * 根据  dictCode status   获取 第一条 数据
     * @param dictCode
     * @param status
     * @return
     */
    List<Dictionaries> findByDictCodeAndStatus(String dictCode, Byte status);
}

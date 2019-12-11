package pers.liujunyi.cloud.dict.service.dict;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseMongoService;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.dict.domain.dict.DictionariesQueryDto;
import pers.liujunyi.cloud.dict.entity.dict.Dictionaries;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: DictionariesMongoService.java
 * 文件描述: 数据字典 Mongo Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface DictionariesMongoService extends BaseMongoService<Dictionaries, Long> {


    /**
     *  获取 存在 叶子节点的 字典数据
     * @param pid
     */
    List<Dictionaries> findByPidIn(List<Long> pid);

    /**
     *  根据  pid  dictCode 获取数据
     * @param pid
     * @param dictCode
     * @return
     */
    List<Dictionaries> findByPidAndDictCode(Long pid, String dictCode);


    /**
     * 根据 pid 获取 符合 ztree 结构的数据
     * @param pid
     * @param status 状态
     * @return
     */
    List<ZtreeNode> dictTree(Long pid, Byte status);

    /**
     * 根据 fullParentCode 获取符合 ztree 结构的数据
     * @param fullParentCode
     * @param status 状态
     * @return
     */
    List<ZtreeNode> dictCodeTree(String fullParentCode, Byte status);

    /**
     *  获取 存在 叶子节点的 字典数据
     * @param pid
     * @return 客户端没有设置分页，es服务端会有默认填充 默认只返回10条
     */
    List<Dictionaries> findByPid(Long pid);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(DictionariesQueryDto query);

    /**
     * 字典 Combox
     * @param parentCode  父级 code
     * @param empty
     * @return
     */
    List<Map<String, String>> dictCombox( String parentCode, Boolean empty);


    /**
     * 根据 fullDictCode 获取字典名称值
     * @param parentCode  父级 code
     * @param dictCode    要取值的 字典 code
     * @return
     */
    String getDictName(String parentCode, String dictCode);


    /**
     * 根据fullParentCode获取字典值 返回map
     * @param fullParentCode  父级 dict code
     * @return  返回 map   key = 字典代码   value = 字典名称
     */
    Map<String, String> getDictNameToMap(String fullParentCode);

    /**
     * 根据fullParentCodes 获取字典值 返回map
     * @param fullParentCodes  父级 dict code
     * @return  返回 map   key = 字典代码   value = map
     */
    Map<String, Map<String, String>> getDictNameToMap(List<String> fullParentCodes);
}

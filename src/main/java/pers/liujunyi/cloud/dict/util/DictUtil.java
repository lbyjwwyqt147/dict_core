package pers.liujunyi.cloud.dict.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.dict.service.dict.DictionariesMongoService;

import java.util.List;
import java.util.Map;

/***
 * 数据字典工具类
 *
 * @author ljy
 */
@Log4j2
@Component
public class DictUtil {

    @Autowired
    private DictionariesMongoService dictionariesMongoService;

    /**
     * 获取业务字典值
     * @param parentDictCode  父级字典代码
     * @param dictCode        需要转换的字典代码
     * @return
     */
    public String getDictName(String parentDictCode, String dictCode) {
        String dictName = "";
        log.info(" * 开始请求获取业务字典值 ..................... ");
        if (StringUtils.isNotBlank(parentDictCode) && StringUtils.isNotBlank(dictCode)) {
            dictName = this.dictionariesMongoService.getDictName(parentDictCode, dictCode);
        } else {
            log.info(" * 获取业务字典值 parentDictCode 参数 或者 dictCode 参数 为 空 ");
        }
        return dictName;
    }

    /**
     * 根据fullParentCode获取字典值 返回map
     * @param fullParentCode  父级 dict code
     * @return  返回 map   key = 字典代码   value = 字典名称
     */
    public Map<String, String> getDictNameToMap(String fullParentCode) {
        Map<String, String> dictNameMap = null;
        log.info(" * 开始请求获取业务字典值 ..................... ");
        if (StringUtils.isNotBlank(fullParentCode)) {
            dictNameMap = this.dictionariesMongoService.getDictNameToMap(fullParentCode);
        } else {
            log.info(" * 获取业务字典值 fullParentCode 参数 为 空 ");
        }
        return dictNameMap;
    }

    /**
     * 根据fullParentCodes 获取字典值 返回map
     * @param parentDictCodes  父级 dict code
     * @return  返回 map   key = 字典代码   value = map
     */
    public Map<String, Map<String, String>> getDictNameToMapList(List<String> parentDictCodes) {
        Map<String, Map<String, String>> dictNameMap = null;
        log.info(" * 开始请求获取业务字典值 ..................... ");
        if (!CollectionUtils.isEmpty(parentDictCodes)) {
            dictNameMap = this.dictionariesMongoService.getDictNameToMap(parentDictCodes);
        } else {
            log.info(" * 获取业务字典值 parentDictCodes 参数 为 空 ");
        }
        return dictNameMap;
    }


}

package pers.liujunyi.cloud.dict.service.dict.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.encrypt.AesEncryptUtils;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseMongoServiceImpl;
import pers.liujunyi.cloud.common.vo.tree.ZtreeBuilder;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.dict.domain.dict.DictZtreeDto;
import pers.liujunyi.cloud.dict.domain.dict.DictionariesQueryDto;
import pers.liujunyi.cloud.dict.entity.dict.Dictionaries;
import pers.liujunyi.cloud.dict.repository.mongo.dict.DictionariesMongoRepository;
import pers.liujunyi.cloud.dict.service.dict.DictionariesMongoService;
import pers.liujunyi.cloud.dict.util.DictConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/***
 * 文件名称: DictionariesMongoServiceImpl.java
 * 文件描述: 数据字典 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class DictionariesMongoServiceImpl extends BaseMongoServiceImpl<Dictionaries, Long> implements DictionariesMongoService {

    @Autowired
    private DictionariesMongoRepository dictionariesMongoRepository;

    public DictionariesMongoServiceImpl(BaseMongoRepository<Dictionaries, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }

    @Override
    public List<Dictionaries> findByPidIn(List<Long> pid) {
        return this.dictionariesMongoRepository.findByPidIn(pid);
    }

    @Override
    public List<Dictionaries> findByPidAndDictCode(Long pid, String dictCode) {
        return this.dictionariesMongoRepository.findByPidAndDictCode(pid, dictCode);
    }

    @Override
    public List<ZtreeNode> dictTree(Long pid, Byte status) {
        List<Dictionaries> list = null;
        if (pid != null) {
            if (status != null) {
                list = this.dictionariesMongoRepository.findByPidAndStatusOrderByPriorityAsc(pid,  status);
            } else {
                list = this.dictionariesMongoRepository.findByPid(pid);
            }
        } else {
            list =  this.dictionariesMongoRepository.findAll(Sort.by(Sort.Direction.ASC, "priority"));
        }
        return this.startBuilderZtree(list);
    }

    @Override
    public List<ZtreeNode> dictCodeTree(String fullParentCode, Byte status) {
        List<Dictionaries> list = this.dictionariesMongoRepository.findByFullDictParentCodeLikeAndStatusOrderByPriorityAsc(fullParentCode, status);
        return this.startBuilderZtree(list);
    }

    @Override
    public List<Dictionaries> findByPid(Long pid) {
        return this.dictionariesMongoRepository.findByPid(pid);
    }


    @Override
    public ResultInfo findPageGird(DictionariesQueryDto query) {
        Sort sort = Sort.by(Sort.Direction.ASC, "priority");
        Pageable pageable = query.toPageable(sort);
        // 查询条件
        Query searchQuery = query.toSpecPageable(pageable);
        // 查询总记录条数
        long totalElements = this.mongoDbTemplate.count(searchQuery, Dictionaries.class);
        // 查询数据
        List<Dictionaries> searchPageResults =  this.mongoDbTemplate.find(searchQuery, Dictionaries.class);
        ResultInfo result = ResultUtil.success(AesEncryptUtils.aesEncrypt(searchPageResults, super.secretKey));
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public List<Map<String, String>> dictCombox(String parentCode, Boolean empty) {
        List<Map<String, String>> result  = new LinkedList<>();
        if (empty != null && empty == true) {
            Map<String, String> emptyMap = new ConcurrentHashMap<>();
            emptyMap.put("id", "");
            emptyMap.put("text", "-请选择-");
            result.add(emptyMap);
        }
        parentCode = parentCode.trim().replace(" ","");
        List<Dictionaries> list = this.dictionariesMongoRepository.findByStatusAndFullDictParentCodeOrderByPriorityAsc(DictConstant.ENABLE_STATUS, parentCode);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(item -> {
                Map<String, String> map = new ConcurrentHashMap<>();
                map.put("id", item.getDictCode());
                map.put("text", item.getDictName());
                result.add(map);
            });
        }
        return result;
    }


    @Override
    public String getDictName(String parentCode, String dictCode) {
        String result = "";
        String fullDictCode = parentCode.trim() + ":" + dictCode.trim().replace(" ","");
        List<Dictionaries> dictionaries = this.dictionariesMongoRepository.findByFullDictCode(fullDictCode);
        if (dictionaries != null && dictionaries.size() > 0) {
                result = dictionaries.get(0).getDictName();
        }
        return result;
    }

    @Override
    public Map<String, String> getDictNameToMap(String fullParentCode) {
        List<Dictionaries> list = this.dictionariesMongoRepository.findByFullDictParentCode(fullParentCode);
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().collect(Collectors.toMap(Dictionaries::getDictCode, Dictionaries::getDictName));
        }
        return null;
    }

    @Override
    public Map<String, Map<String, String>> getDictNameToMap(List<String> fullParentCodes) {
        Map<String, Map<String, String>> dictNameMap = new ConcurrentHashMap<>();
        List<Dictionaries> list = this.dictionariesMongoRepository.findByFullDictParentCodeIn(fullParentCodes);
        if (!CollectionUtils.isEmpty(list)) {
            // 以 fullParentCode 分组
            Map<String, List<Dictionaries>> parentCodeGroup = list.stream().collect(Collectors.groupingBy(Dictionaries::getFullDictParentCode));
            Set<Map.Entry<String, List<Dictionaries>>> entrySet = parentCodeGroup.entrySet();
            Iterator<Map.Entry<String, List<Dictionaries>>> iter = entrySet.iterator();
            while (iter.hasNext()) {
                Map.Entry<String, List<Dictionaries>> entry = iter.next();
                List<Dictionaries> dictionariesList = entry.getValue();
                Map<String, String> tempMap = dictionariesList.stream().collect(Collectors.toMap(Dictionaries::getDictCode, Dictionaries::getDictName));
                dictNameMap.put(entry.getKey(), tempMap);
            }
        }
        return dictNameMap;
    }



    /**
     * 构建 ztree 树
     * @param list
     * @return
     */
    private List<ZtreeNode> startBuilderZtree(List<Dictionaries> list){
        List<ZtreeNode> treeNodes = new LinkedList<>();
        if (!CollectionUtils.isEmpty(list)){
            list.stream().forEach(item -> {
                ZtreeNode zTreeNode = new ZtreeNode(item.getId(), item.getPid(), item.getDictName());
                DictZtreeDto dictZtreeDto = new DictZtreeDto();
                dictZtreeDto.setId(item.getId());
                dictZtreeDto.setDictCode(item.getDictCode());
                dictZtreeDto.setDictName(item.getDictName());
                dictZtreeDto.setDictLabel(item.getDictLabel());
                dictZtreeDto.setPid(item.getPid());
                dictZtreeDto.setFullParentCode(item.getFullDictParentCode());
                zTreeNode.setOtherAttributes(dictZtreeDto);
                treeNodes.add(zTreeNode);
            });
        }
        return ZtreeBuilder.buildListToTree(treeNodes);
    }
}

package pers.liujunyi.cloud.dict.service.dict.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseJpaRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseJpaMongoServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.dict.domain.dict.DictionariesDto;
import pers.liujunyi.cloud.dict.entity.dict.Dictionaries;
import pers.liujunyi.cloud.dict.repository.jpa.dict.DictionariesRepository;
import pers.liujunyi.cloud.dict.service.dict.DictionariesMongoService;
import pers.liujunyi.cloud.dict.service.dict.DictionariesService;
import pers.liujunyi.cloud.dict.util.DictConstant;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: DictionariesServiceImpl.java
 * 文件描述: 数据字典 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class DictionariesServiceImpl extends BaseJpaMongoServiceImpl<Dictionaries, Long> implements DictionariesService {

    @Autowired
    private DictionariesRepository dictionariesRepository;
    @Autowired
    private DictionariesMongoService dictionariesMongoService;

    public DictionariesServiceImpl(BaseJpaRepository<Dictionaries, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(DictionariesDto record) {
        if (this.checkRepetition(record.getId(), record.getPid(), record.getDictCode())){
            return ResultUtil.params("字典代码重复,请重新输入.");
        }
        Dictionaries dictionaries = DozerBeanMapperUtil.copyProperties(record, Dictionaries.class);
        boolean add = record.getId() == null ? true : false;
        if (record.getPid().longValue() > 0) {
            Dictionaries parent = this.selectById(record.getPid());
            dictionaries.setFullDictParent(parent.getFullDictParent() + ":"  + parent.getId());
            String curParentCode = StringUtils.isNotBlank(parent.getFullDictParentCode()) && !parent.getFullDictParentCode().equals("0") ? parent.getFullDictParentCode()   + ":" + parent.getDictCode() : parent.getDictCode();
            dictionaries.setFullDictParentCode(curParentCode);
            String curFullDictCode = StringUtils.isNotBlank(parent.getFullDictCode()) ? parent.getFullDictCode()   + ":" + record.getDictCode() : parent.getDictCode();
            dictionaries.setFullDictCode(curFullDictCode);
            dictionaries.setDictLevel((byte)(parent.getDictLevel() +  1));
        } else {
            dictionaries.setFullDictParent("0");
            dictionaries.setFullDictParentCode("0");
            dictionaries.setDictLevel((byte)1);
            dictionaries.setFullDictCode(record.getDictCode());
        }
        if (record.getPriority() == null) {
            dictionaries.setPriority(10);
        }
        if (record.getStatus() == null) {
            dictionaries.setStatus(DictConstant.ENABLE_STATUS);
        }
        Dictionaries saveObj = this.dictionariesRepository.save(dictionaries);
        if (saveObj == null || saveObj.getId() == null) {
            return ResultUtil.fail();
        }
        if (!add) {
            saveObj.setDataVersion(saveObj.getDataVersion() + 1);
        } else {
            saveObj.setDataVersion(1L);
        }
        this.dictionariesMongoService.save(saveObj);
        return ResultUtil.success(saveObj.getId());
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids, String putParams) {
        if (status.byteValue() == 1) {
            List<Dictionaries> list = this.dictionariesMongoService.findByPidIn(ids);
            if (!CollectionUtils.isEmpty(list)) {
                return ResultUtil.params("无法被禁用.");
            }
        }
        int count = this.dictionariesRepository.setStatusByIds(status, new Date(), ids);
        if (count > 0) {
            JSONArray jsonArray = JSONArray.parseArray(putParams);
            int jsonSize = jsonArray.size();
            Map<Long, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            for(int i = 0; i < jsonSize; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, Object> docDataMap = new HashMap<>();
                docDataMap.put("status", status);
                docDataMap.put("updateTime", System.currentTimeMillis());
                docDataMap.put("dataVersion", jsonObject.getLongValue("dataVersion") + 1);
                sourceMap.put(jsonObject.getLongValue("id"), docDataMap);
            }
            // 更新 Mongo 中的数据
            super.updateMongoDataByIds(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }


    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        List<Dictionaries> list = this.dictionariesMongoService.findByPidIn(ids);
        if (!CollectionUtils.isEmpty(list)) {
            return ResultUtil.params("无法被删除.");
        }
        int count = this.dictionariesRepository.deleteAllByIdIn(ids);
        if (count > 0) {
            this.dictionariesMongoService.deleteAllByIdIn(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo syncDataToMongo() {
       super.syncDataMongoDb();
       return ResultUtil.success();
    }


    /**
     * 检查字典代码是否重复
     * @param id
     * @param pid
     * @param dictCode
     * @return
     */
    private Boolean checkRepetition(Long id, Long pid, String dictCode) {
        if (id == null) {
           return this.checkDictCodeData(pid, dictCode);
        }
        Dictionaries dictionaries = this.selectById(id);
        if (dictionaries != null && !dictionaries.getDictCode().equals(dictCode)) {
            return this.checkDictCodeData(pid, dictCode);
        }
        return false;
    }

    /**
     * 检查中是否存在dictCode 数据
     * @param pid
     * @param dictCode
     * @return
     */
    private Boolean checkDictCodeData (Long pid, String dictCode) {
        List<Dictionaries> exists = this.dictionariesMongoService.findByPidAndDictCode(pid, dictCode);
        if (CollectionUtils.isEmpty(exists)) {
            return false;
        }
        return true;
    }

    /**
     * 根据主键ID 获取数据
     * @param id
     * @return
     */
    private Dictionaries selectById(Long id) {
        return this.dictionariesMongoService.getOne(id);
    }
}

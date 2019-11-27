package pers.liujunyi.cloud.dict.repository.jpa.dict;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.dict.entity.dict.Dictionaries;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: DictionariesRepository.java
 * 文件描述: 数据字典 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface DictionariesRepository extends BaseRepository<Dictionaries, Long> {

    /**
     * 根据ID 批量删除
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying
    @Query("delete from Dictionaries dict where dict.id in (?1)")
    int deleteAllByIdIn(List<Long> ids);

    /**
     * 修改状态
     * @param status  0:启动 1：禁用
     * @param ids
     * @param updateTime
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update dictionaries dict set dict.status = ?1, dict.update_time = ?2, dict.data_version = data_version+1  where dict.id in (?3)", nativeQuery = true)
    int setStatusByIds(Byte status, Date updateTime, List<Long> ids);

    /**
     * 修改状态
     * @param status  0:启动 1：禁用
     * @param id
     * @param updateTime
     * @param version
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update dictionaries dict set dict.status = ?1, dict.update_time = ?2, dict.data_version = data_version+1  where dict.id = ?3 and dict.data_version = ?4", nativeQuery = true)
    int setStatusById(Byte status, Date updateTime, Long id, Long version);

}

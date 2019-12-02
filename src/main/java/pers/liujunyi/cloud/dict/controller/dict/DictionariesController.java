package pers.liujunyi.cloud.dict.controller.dict;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.dto.IdParamDto;
import pers.liujunyi.cloud.common.encrypt.annotation.Decrypt;
import pers.liujunyi.cloud.common.encrypt.annotation.Encrypt;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.util.SystemUtils;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.dict.domain.dict.DictionariesDto;
import pers.liujunyi.cloud.dict.domain.dict.DictionariesQueryDto;
import pers.liujunyi.cloud.dict.service.dict.DictionariesMongoService;
import pers.liujunyi.cloud.dict.service.dict.DictionariesService;
import pers.liujunyi.cloud.dict.util.DictConstant;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/***
 * 文件名称: DictionariesController.java
 * 文件描述: 数据字典 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "数据字典 API")
@RestController
public class DictionariesController extends BaseController {

    @Autowired
    private DictionariesService dictionariesService;
    @Autowired
    private DictionariesMongoService dictionariesMongoService;


    /**
     * 保存数据 (参数已加密)
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "保存数据(数据加密处理)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @Decrypt
    @Encrypt
    @PostMapping(value = "verify/dict/s")
    @ApiVersion(1)
    public ResultInfo encryptSaveDataRecord(@Valid @RequestBody DictionariesDto param) {
        return this.dictionariesService.saveRecord(param);
    }


    /**
     * 单条删除数据(数据加密处理)
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "单条删除数据(数据加密处理)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "String")
    })
    @Encrypt
    @Decrypt
    @DeleteMapping(value = "verify/dict/d")
    @ApiVersion(1)
    public ResultInfo encryptSingleDelete(@Valid @RequestBody IdParamDto param) {
        return this.dictionariesService.deleteSingle(Long.valueOf(param.getId()));
    }



    /**
     * 批量删除(数据加密处理)
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据(数据加密处理)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String")
    })
    @Encrypt
    @Decrypt
    @DeleteMapping(value = "verify/dict/d/b")
    @ApiVersion(1)
    public ResultInfo encryptBatchDelete(@Valid @RequestBody IdParamDto param) {
        return this.dictionariesService.deleteBatch(param.getIdList());
    }

    /**
     * 分页列表数据(数据加密处理)
     *
     * @param query
     * @return
     */

    @ApiOperation(value = "分页列表数据(数据加密处理)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/dict/g")
    @ApiVersion(1)
    public ResultInfo encryptPageGrid(@Valid DictionariesQueryDto query) {
        return this.dictionariesMongoService.findPageGird(query);
    }

    /**
     *  根据pid 获取 字典tree 结构数据 (只包含正常数据  禁用数据不展示)
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "字典tree 结构数据 (只包含正常数据  禁用数据不展示)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/dict/z")
    @ApiVersion(1)
    public List<ZtreeNode> dictZTree(@Valid IdParamDto param ) {
        return this.dictionariesMongoService.dictTree(param.getId(), DictConstant.ENABLE_STATUS);
    }

    /**
     * 根据 fullParentCode 获取 字典tree 结构数据 (只包含正常数据  禁用数据不展示)
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "根据 fullParentCode 获取  字典tree 结构数据 (只包含正常数据  禁用数据不展示)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "codes", value = "codes",  required = true, dataType = "String")
    })
    @GetMapping(value = "tree/dict/p/z")
    @ApiVersion(1)
    public List<ZtreeNode> dictCodeZTree(@Valid IdParamDto param ) {
        return this.dictionariesMongoService.dictCodeTree(param.getCode(), DictConstant.ENABLE_STATUS);
    }

    /**
     * 根据pid 获取 字典tree 结构数据 (包含禁用数据 )
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "字典tree 结构数据 (包含禁用数据 )")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/dict/all/z")
    @ApiVersion(1)
    public List<ZtreeNode> allDictZTree(@Valid IdParamDto param ) {
        return this.dictionariesMongoService.dictTree(param.getId(), null);
    }


    /**
     *  单条修改数据状态(数据加密处理)
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "单条修改数据状态(数据加密处理)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "Long"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer"),
            @ApiImplicitParam(name = "dataVersion", value = "version",  required = true, dataType = "integer")
    })
    @Encrypt
    @Decrypt
    @PutMapping(value = "verify/dict/p")
    @ApiVersion(1)
    public ResultInfo encryptUpdateDataStatus(@Valid @RequestBody IdParamDto param ) {
        return this.dictionariesService.updateStatus(param.getStatus(), param.getId(), param.getDataVersion());
    }


    /**
     *  批量修改数据状态(数据加密处理)
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "批量修改数据状态(数据加密处理)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @Encrypt
    @Decrypt
    @PutMapping(value = "verify/dict/p/b")
    @ApiVersion(1)
    public ResultInfo encryptUpdateStatus(@Valid @RequestBody IdParamDto param ) {
        return this.dictionariesService.updateStatus(param.getStatus(), param.getIdList(), param.getPutParams());
    }

    /**
     *  字典 Combox
     * @param parentCode
     * @param empty
     * @return
     */
    @ApiOperation(value = "字典 Combox")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "parentCode", value = "父级字典代码",  required = true),
            @ApiImplicitParam(name = "empty", value = "是否第一项是空",  required = true),
    })
    @GetMapping(value = "ignore/dict/combox")
    @ApiVersion(1)
    public List<Map<String, String>> dictCombox(@Valid @NotBlank(message = "parentCode 必须填写")
    @RequestParam(name = "parentCode", required = true)  String parentCode, Boolean empty) {
        return this.dictionariesMongoService.dictCombox(parentCode, empty);
    }

    /**
     *  字典 Combox (数据加密处理)
     * @param parentCode
     * @param empty
     * @return
     */
    @ApiOperation(value = "字典 Combox (数据加密处理)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "parentCode", value = "父级字典代码",  required = true),
            @ApiImplicitParam(name = "empty", value = "是否第一项是空",  required = true)
    })
    @Encrypt
    @GetMapping(value = "ignore/dict/selectBox")
    @ApiVersion(1)
    public List<Map<String, String>> encryptDictCombox(@Valid @NotBlank(message = "parentCode 必须填写")
                                                @RequestParam(name = "parentCode", required = true)  String parentCode, Boolean empty) {
        return this.dictionariesMongoService.dictCombox(parentCode, empty);
    }

    /**
     *  字典代码转换为字典值
     * @param dictCode
     * @param  parentCode
     * @return
     */
    @ApiOperation(value = "字典代码转换为字典值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "parentCode", value = "父级字典代码",  required = true),
            @ApiImplicitParam(name = "dictCode", value = "字典代码",  required = true)
    })
    @GetMapping(value = "ignore/dict/name")
    @ApiVersion(1)
    public ResultInfo dictName(@Valid  @NotBlank(message = "parentCode 必须填写")
    @RequestParam(name = "parentCode", required = true) String parentCode, @NotBlank(message = "dictCode 必须填写")
    @RequestParam(name = "dictCode", required = true)  String dictCode) {
        return  ResultUtil.success(this.dictionariesMongoService.getDictName(parentCode, dictCode));
    }



    /**
     *  字典 fullParentCode 父级代码 转换为字典值 map
     * @param fullParentCode
     * @param
     * @return
     */
    @ApiOperation(value = "字典 fullParentCode 父级代码 转换为字典值 map")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "fullParentCode", value = "父级字典代码",  required = true)

    })
    @GetMapping(value = "ignore/dict/map/name")
    @ApiVersion(1)
    public ResultInfo getDictNameToMap(@Valid @NotBlank(message = "fullParentCode 必须填写")
                                       @RequestParam(name = "fullParentCode", required = true) String fullParentCode) {
        return  ResultUtil.success(this.dictionariesMongoService.getDictNameToMap(fullParentCode));
    }

    /**
     *  字典 fullParentCode 父级代码 转换为字典值 map
     * @param fullParentCodes
     * @return
     */
    @ApiOperation(value = "字典 fullParentCode 父级代码 转换为字典值 map")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "fullParentCodes", value = "父级字典代码",  required = true),
            @ApiImplicitParam(name = "dictLevel", value = "层次级别",  required = false)
    })
    @GetMapping(value = "ignore/dict/map/list/name")
    @ApiVersion(1)
    public ResultInfo getDictNameToMapList(@Valid
                                           @NotBlank(message = "fullParentCodes 必须填写")
                                           @RequestParam(name = "fullParentCodes", required = true) String fullParentCodes) {
        return  ResultUtil.success(this.dictionariesMongoService.getDictNameToMap( SystemUtils.stringToList(fullParentCodes)));
    }


    /**
     *  同步数据到es中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "verify/dict/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToMongo() {
        return this.dictionariesService.syncDataToMongo();
    }
}

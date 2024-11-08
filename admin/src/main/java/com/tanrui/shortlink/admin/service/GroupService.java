package com.tanrui.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanrui.shortlink.admin.dao.entity.GroupDO;
import com.tanrui.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.tanrui.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;

import java.util.List;

/**
 * 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO>{


    /**
     * 新增短链接分组
     *
     * @param groupName 短链接分组名
     */
    void saveGroup(String groupName);

    /**
     * 查询用户短链接分组集合
     * @return 用户短链接分组集合
     */
    List<ShortLinkGroupRespDTO> listGroup();

    /**
     * 修改短链接分组名
     * @param reqDTO 修改短链接分组参数
     */
    void updateGroup(ShortLinkGroupUpdateReqDTO reqDTO);

    /**
     * 删除短链接分组
     * @param gid 用户分组标识
     */
    void deleteGroup(String gid);
}

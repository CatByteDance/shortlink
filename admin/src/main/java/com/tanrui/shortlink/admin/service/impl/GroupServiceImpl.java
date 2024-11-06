package com.tanrui.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanrui.shortlink.admin.dao.entity.GroupDO;
import com.tanrui.shortlink.admin.dao.mapper.GroupMapper;
import com.tanrui.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.tanrui.shortlink.admin.service.GroupService;
import com.tanrui.shortlink.admin.toolkit.RandomGenerator;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 短链接接口实现层
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    @Override
    public void saveGroup(String groupName) {
        String gid;
        do {
            gid = RandomGenerator.generateRandom();
        } while (!hasGid(gid));

        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .sortOrder(0)
                .name(groupName)
                .build();

        baseMapper.insert(groupDO);
    }

    @Override
    public List<ShortLinkGroupRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
//              TODO 从当前上下文获取用户名
                .eq(GroupDO::getUsername, "tanrui")
                .orderByDesc(List.of(GroupDO::getSortOrder, GroupDO::getUpdateTime));


        List<GroupDO> groupDOList = baseMapper.selectList(wrapper);
        return BeanUtil.copyToList(groupDOList, ShortLinkGroupRespDTO.class);
    }

    private boolean hasGid(String gid) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                // TODO 设置用户名
                .eq(GroupDO::getUsername, null );

                GroupDO hasGroupFlag = baseMapper.selectOne(queryWrapper);
                return hasGroupFlag == null;
    }
}

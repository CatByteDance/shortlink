package com.tanrui.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanrui.shortlink.project.dao.entity.ShortLinkDO;
import com.tanrui.shortlink.project.dao.mapper.ShortLinkMapper;
import com.tanrui.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.tanrui.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.tanrui.shortlink.project.service.ShortLinkService;
import com.tanrui.shortlink.project.toolkit.HashUtil;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 短链接接口实现层
 */
@Slf4j
@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO reqDTO) {
        String shortLinkSuffix = generateSuffix(reqDTO);
        ShortLinkDO shortLinkDo = BeanUtil.toBean(reqDTO, ShortLinkDO.class);
        shortLinkDo.setShortUri(shortLinkSuffix);
        shortLinkDo.setEnableStatus(0);
        shortLinkDo.setFullShortUrl(reqDTO.getDomain() + "/" + shortLinkSuffix);
        baseMapper.insert(shortLinkDo);

        return ShortLinkCreateRespDTO.builder()
                .fullShortLink(shortLinkDo.getFullShortUrl())
                .originUrl(reqDTO.getOriginUrl())
                .gid(reqDTO.getGid())
                .build();
    }

    private String generateSuffix(ShortLinkCreateReqDTO reqDTO){
        String originUrl = reqDTO.getOriginUrl();
        return HashUtil.hashToBase62(originUrl);
    }
}

package com.tanrui.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanrui.shortlink.project.dao.entity.ShortLinkDO;
import com.tanrui.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.tanrui.shortlink.project.dto.resp.ShortLinkCreateRespDTO;

/**
 * 短链接接口层
 */
public interface ShortLinkService extends IService<ShortLinkDO>{

    /**
     * 创建短链接
     * @param reqDTO 创建短链接请求参数
     * @return 短链接创建信息
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO reqDTO);
}

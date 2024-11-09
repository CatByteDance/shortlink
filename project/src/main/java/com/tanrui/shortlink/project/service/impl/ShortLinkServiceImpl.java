package com.tanrui.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanrui.shortlink.project.common.convention.exception.ServiceException;
import com.tanrui.shortlink.project.dao.entity.ShortLinkDO;
import com.tanrui.shortlink.project.dao.mapper.ShortLinkMapper;
import com.tanrui.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.tanrui.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.tanrui.shortlink.project.service.ShortLinkService;
import com.tanrui.shortlink.project.toolkit.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;



/**
 * 短链接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO reqDTO) {
        String shortLinkSuffix = generateSuffix(reqDTO);
        String fullShortUrl = reqDTO.getDomain() + "/" + shortLinkSuffix;

        ShortLinkDO shortLinkDo = BeanUtil.toBean(reqDTO, ShortLinkDO.class);
        shortLinkDo.setShortUri(shortLinkSuffix);
        shortLinkDo.setEnableStatus(0);
        shortLinkDo.setFullShortUrl(fullShortUrl);

//      解决布隆过滤器出现误判的情况
        try{
            baseMapper.insert(shortLinkDo);
        } catch (DuplicateKeyException e){
//          TODO 已经判误的短链接如何处理
//          第一种情况 短链接确实真实存在缓存中
//          第二种情况 短链接不一定存在缓存中
            log.warn("短链接 {} 重复入库", fullShortUrl);
            throw new ServiceException("短链接生成重复");
        }

//      检查无重复之后，存入布隆过滤器当中
        shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);

        return ShortLinkCreateRespDTO.builder()
                .fullShortLink(shortLinkDo.getFullShortUrl())
                .originUrl(reqDTO.getOriginUrl())
                .gid(reqDTO.getGid())
                .build();
    }

    private String generateSuffix(ShortLinkCreateReqDTO reqDTO){
        int customGenerateCount = 0;
        String shortLinkSuffix;

//      TODO: 没太明白这里的循环是干嘛, 存疑：如果originUrl没发生改变的化，
//       每次生成的shortLinkSuffix都是相同的，个人认为应该在此处实现每次循环都得到不同的shortLinkSuffix
        while (true){
            if (customGenerateCount > 10){
                throw new ServiceException("短链接频繁生成，请稍后再试");
            }

//          只要OriginUrl没有改变，生成的shortLinkSuffix也不会改变，为什么要重复？
            String originUrl = reqDTO.getOriginUrl();
            shortLinkSuffix = HashUtil.hashToBase62(originUrl);

//          判断不存在，则一定不存在, 返回false，然后break
            if(!shortUriCreateCachePenetrationBloomFilter.contains(reqDTO.getDomain() + "/" + shortLinkSuffix)){
                break;
            }
            customGenerateCount++;
        }

        return shortLinkSuffix;
    }
}

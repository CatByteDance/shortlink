package com.tanrui.shortlink.project.service.impl;

import cn.hutool.core.text.StrBuilder;
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

import java.util.UUID;


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

        String fullShortUrl = StrBuilder.create(reqDTO.getDomain())
                .append("/")
                .append(shortLinkSuffix)
                .toString();

        ShortLinkDO shortLinkDo = ShortLinkDO.builder()
                .domain(reqDTO.getDomain())
                .originUrl(reqDTO.getOriginUrl())
                .gid(reqDTO.getGid())
                .createdType(reqDTO.getCreatedType())
                .validDateType(reqDTO.getValidDateType())
                .validDate(reqDTO.getValidDate())
                .describe(reqDTO.getDescribe())
                .shortUri(shortLinkSuffix)
                .enableStatus(0)
                .fullShortUrl(fullShortUrl)
                .build();

        /**
         * 唯一索引的判断，高并发的场景下如果两个相同的短链接请求插入，只有一个能成功，另一个抛出异常
         * 如果使用分布式锁解决的话，性能会得到降低
         */
        try{
            baseMapper.insert(shortLinkDo);
        } catch (DuplicateKeyException e){
            throw new ServiceException(String.format("短链接：%s 生成重复", fullShortUrl));
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

        while (true){
            if (customGenerateCount > 10){
                throw new ServiceException("短链接频繁生成，请稍后再试");
            }

            String originUrl = reqDTO.getOriginUrl();

//          改变originUrl,避免冲突
            originUrl += UUID.randomUUID().toString();
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

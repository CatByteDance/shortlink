package com.tanrui.shortlink.project.controller;

import com.tanrui.shortlink.project.common.convention.result.Result;
import com.tanrui.shortlink.project.common.convention.result.Results;
import com.tanrui.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.tanrui.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.tanrui.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return Results.success(shortLinkService.createShortLink(requestParam));
    }

}

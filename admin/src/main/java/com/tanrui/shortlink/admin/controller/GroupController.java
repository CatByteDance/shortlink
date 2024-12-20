package com.tanrui.shortlink.admin.controller;

import com.tanrui.shortlink.admin.common.convention.result.Result;
import com.tanrui.shortlink.admin.common.convention.result.Results;
import com.tanrui.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.tanrui.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.tanrui.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.tanrui.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.tanrui.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
public class GroupController {
        private final GroupService groupService;

        /**
         * 新增短链接分组
         */
        @PostMapping("/api/short-link/admin/v1/group")
        public Result<Void> save(@RequestBody ShortLinkGroupSaveReqDTO shortLinkGroupSaveReqDTO) {
                groupService.saveGroup(shortLinkGroupSaveReqDTO.getName());
                return Results.success();
        }

        /**
         * 查询短链接分组集合
         */
        @GetMapping("/api/short-link/admin/v1/group")
        public Result<List<ShortLinkGroupRespDTO>> listGroup(){
                return Results.success(groupService.listGroup());
        }

        /**
         * 修改短链接分组名称
         */
        @PutMapping("/api/short-link/admin/v1/group")
        public Result<Void> updateGroup(@RequestBody ShortLinkGroupUpdateReqDTO shortLinkGroupUpdateReqDTO) {
                groupService.updateGroup(shortLinkGroupUpdateReqDTO);
                return Results.success();
        }


        /**
         * 删除短链接分组
         */
        @DeleteMapping("/api/short-link/admin/v1/group")
        public Result<Void> deleteGroup(@RequestParam String gid) {
                groupService.deleteGroup(gid);
                return Results.success();
        }

        /**
         * 短链接分组排序
         */
        @PostMapping("/api/short-link/admin/v1/group/sort")
        public Result<Void> sortGroup(@RequestBody List<ShortLinkGroupSortReqDTO> requestParam) {
                groupService.sortGroup(requestParam);
                return Results.success();
        }

}

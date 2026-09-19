/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.recommendai.controller;

import cn.zhuatech.recommendai.common.ApiResponse;
import cn.zhuatech.recommendai.service.RecommendAnalysisService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/ai/recommend")
@PreAuthorize("hasAnyRole('DOMAIN_USER','DOMAIN_OPERATOR','ADMIN')")
public class RecommendAnalysisController {
    private final RecommendAnalysisService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public RecommendAnalysisController(RecommendAnalysisService service) { this.service = service; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/rank")
    public ApiResponse<RecommendAnalysisService.Result> recommend(@Valid @RequestBody RecommendAnalysisService.Request request) {
        return ApiResponse.ok("商品推荐评分完成", service.recommend(request));
    }
}

/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.recommendai.controller;

import cn.zhuatech.recommendai.common.ApiResponse;
import cn.zhuatech.recommendai.service.RecommendAnalysisService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/recommend")
@PreAuthorize("hasAnyRole('DOMAIN_USER','DOMAIN_OPERATOR','ADMIN')")
public class RecommendAnalysisController {
    private final RecommendAnalysisService service;
    public RecommendAnalysisController(RecommendAnalysisService service) { this.service = service; }
    @PostMapping("/rank")
    public ApiResponse<RecommendAnalysisService.Result> recommend(@Valid @RequestBody RecommendAnalysisService.Request request) {
        return ApiResponse.ok("商品推荐评分完成", service.recommend(request));
    }
}

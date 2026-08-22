/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.recommendai.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** 生成带库存、重复购买和受限品类护栏的可解释推荐。 */
@Service
public class RecommendAnalysisService {
    public Result recommend(Request request) {
        int score = Math.round(request.affinityScore() * 0.55f + request.businessPriority() * 0.20f
            + request.inventoryAvailability() * 0.15f + request.marginRate().movePointRight(2).floatValue() * 0.10f);
        List<String> reasons = new ArrayList<>();
        if (request.affinityScore() >= 70) reasons.add("用户分群与候选商品高度匹配");
        if (request.inventoryAvailability() < 20) reasons.add("可售库存不足，降低推荐优先级");
        if (request.recentlyPurchased()) reasons.add("用户近期已购买同类商品");
        if (request.restrictedCategory()) reasons.add("受限品类不得自动推荐");
        if (request.recentlyPurchased()) score -= 25;
        if (request.inventoryAvailability() < 10) score -= 30;
        if (request.restrictedCategory()) score = 0;
        score = Math.max(0, Math.min(100, score));
        String decision = request.restrictedCategory() || request.inventoryAvailability() == 0 ? "EXCLUDE"
            : score >= 65 ? "RECOMMEND" : score >= 40 ? "EXPLORE" : "HOLD";
        if (reasons.isEmpty()) reasons.add("综合偏好、库存和业务目标生成推荐顺序");
        String placement = "RECOMMEND".equals(decision) ? "PRIMARY" : "EXPLORE".equals(decision) ? "SECONDARY" : "NONE";
        return new Result(request.candidateId(), request.userSegment(), score, decision, placement,
            reasons, request.restrictedCategory());
    }

    public record Request(@NotBlank String userSegment, @NotBlank String candidateId,
                          @Min(0) @Max(100) int affinityScore,
                          @Min(0) @Max(100) int businessPriority,
                          @Min(0) @Max(100) int inventoryAvailability,
                          @DecimalMin("0") @DecimalMax("1") BigDecimal marginRate,
                          boolean recentlyPurchased, boolean restrictedCategory) {}
    public record Result(String candidateId, String userSegment, int recommendationScore,
                         String decision, String placement, List<String> reasons,
                         boolean humanReviewRequired) {}
}

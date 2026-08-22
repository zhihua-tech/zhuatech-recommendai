/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.recommendai;

import cn.zhuatech.recommendai.service.RecommendAnalysisService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class RecommendAnalysisServiceTests {
    private final RecommendAnalysisService service = new RecommendAnalysisService();
    @Test void recommendsStrongAvailableCandidate() {
        var result = service.recommend(new RecommendAnalysisService.Request("新客-家居", "SKU-88", 92, 70, 85, new BigDecimal("0.35"), false, false));
        assertThat(result.decision()).isEqualTo("RECOMMEND");
        assertThat(result.placement()).isEqualTo("PRIMARY");
    }
    @Test void excludesRestrictedCategory() {
        var result = service.recommend(new RecommendAnalysisService.Request("普通客群", "SKU-X", 99, 90, 80, new BigDecimal("0.50"), false, true));
        assertThat(result.decision()).isEqualTo("EXCLUDE");
        assertThat(result.humanReviewRequired()).isTrue();
    }
}

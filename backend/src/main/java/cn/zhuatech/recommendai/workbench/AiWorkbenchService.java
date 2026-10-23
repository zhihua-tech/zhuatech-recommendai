/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.recommendai.workbench;

import static cn.zhuatech.recommendai.workbench.WorkbenchInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);
    keys(r, "k", "ranked", "judgments");
    int k = integer(r, "k", 1, 100);
    var ranked = strings(r, "ranked", 1, 500);
    if (new HashSet<>(ranked).size() != ranked.size())
      throw new IllegalArgumentException("排序候选不能重复");
    Map<String, Integer> grades = new HashMap<>();
    for (var j : rows(r, "judgments", 1, 500)) {
      keys(j, "id", "grade");
      String id = id(j, "id");
      if (grades.putIfAbsent(id, integer(j, "grade", 0, 3)) != null)
        throw new IllegalArgumentException("相关性标注重复");
    }
    if (!grades.keySet().containsAll(ranked))
      throw new IllegalArgumentException("排序中存在未标注候选，不能默认为不相关");
    int positions = Math.min(k, ranked.size()), hits = 0;
    long relevant = grades.values().stream().filter(g -> g > 0).count();
    double dcg = 0, idcg = 0;
    var ideal = grades.values().stream().sorted(Comparator.reverseOrder()).toList();
    List<Map<String, Object>> out = new ArrayList<>();
    for (int i = 0; i < positions; i++) {
      String id = ranked.get(i);
      int grade = grades.get(id);
      if (grade > 0) hits++;
      double gain = (Math.pow(2, grade) - 1) / (Math.log(i + 2) / Math.log(2));
      dcg += gain;
      if (i < ideal.size())
        idcg += (Math.pow(2, ideal.get(i)) - 1) / (Math.log(i + 2) / Math.log(2));
      out.add(Map.of("rank", i + 1, "id", id, "grade", grade, "discountedGain", round(gain)));
    }
    return Map.of(
        "evaluatedPositions",
        positions,
        "precision",
        round(hits / (double) positions),
        "recallDefined",
        relevant > 0,
        "recall",
        relevant == 0 ? BigDecimal.ZERO : round(hits / (double) relevant),
        "ndcgDefined",
        idcg > 0,
        "ndcg",
        idcg == 0 ? BigDecimal.ZERO : round(dcg / idcg),
        "ranked",
        out);
  }
}

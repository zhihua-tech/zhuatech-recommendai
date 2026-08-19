# RecommendAI API

版权所有 © 2026 上海如静知华信息科技有限公司。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录并获取 JWT |
| GET | `/api/admin/dashboard` | 推荐策略运营中心 |
| GET | `/api/admin/work-orders` | 推荐实验任务 |
| GET | `/api/shopfloor/dashboard` | 推荐运营工作台 |
| POST | `/api/shopfloor/work-orders/{id}/reports` | 提交策略反馈 |
| POST | `/api/ai/recommend/rank` | 候选推荐分、位置、原因和护栏结果 |
| POST | `/api/shopfloor/ai-risk-assessment` | AI 功能上线风险初筛 |

除登录外均需 JWT；社区版不处理真实用户画像。

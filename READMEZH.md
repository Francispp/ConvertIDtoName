1. # 如何运行该服务
1.1 运行要求
确保你的环境满足以下条件：
JDK 17+
Maven 3.6+
Spring Boot 3+

1.2 构建与运行
克隆代码仓库，并在项目目录下执行以下命令：
建议使用idea启动项目

1.3 静态资源：
product.csv文件放在resources/static目录下，如需修改，请按照productId,productName进行修改。
# 编译项目
mvn clean package
# 运行 Spring Boot 应用
mvn spring-boot:run
默认情况下，服务会运行在 **http://localhost:8080/**。

2. # 如何使用 API
2.1 上传 trade.csv 进行数据处理，trade.csv格式要求为：date,productId,currency,price

2.2 调用 API 进行数据处理
上传文件：
使用 POST 请求到 /api/trades/enrich 接口，上传 trade.csv 文件。
示例请求
curl -X POST "http://localhost:8080/TradeEnrichmentController/enrich" \
-H "Content-Type: multipart/form-data" \
-F "file=@trade.csv"
2.3 响应结果：
成功：200 OK
错误：400 Bad Request

2.4 AWS 在线版本
AWS 在线版本可直接调用：http://43.201.28.13:8080/TradeEnrichmentController/enrich

3. # 代码的任何限制
3.1 目前的限制
文件大小限制：上传的 trade.csv 文件不能超过 50MB。
数据格式严格：CSV 文件必须包含 4 列 (date, productId, currency, price)，否则请求会被拒绝。
跳过错误Invalid date数据：如果 CSV 文件中的日期格式不正确，该数据行将被跳过。

3.2 如何修改限制
如果需要调整 文件大小限制，请修改 application.properties：
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB


4. # 设计方面的讨论/评论
4.1 代码结构
TradeEnrichmentController.java 负责接收和处理 HTTP 请求。
TradeEnrichmentService.java 负责解析 CSV 并校验数据。
GlobalExceptionHandler.java 统一处理错误并返回 HTTP 响应。
model/Trade.java 定义了 Trade 交易对象。

4.2 设计思路
采用 Spring Boot + REST API，保证扩展性。
全局异常处理，避免在 Controller 层写重复的 try-catch。
数据校验逻辑 放在 TradeEnrichmentService，保持 Controller 简洁。

5. # 如果有更多时间，可以有哪些改进？
数据库存储：
目前数据仅处理后返回，没有存储。如果存入数据库，可支持 历史查询、数据统计等功能。

添加 Swagger 文档：
目前 API 需要手写文档，建议集成 SpringDoc OpenAPI 以自动生成 API 文档。

支持更多数据格式：
目前仅支持 CSV，未来可以支持 JSON、Excel 文件等。

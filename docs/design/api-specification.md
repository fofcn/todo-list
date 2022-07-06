## 响应结构
# 响应内容数据结构
| name       | type    | desc                                                 |
|------------|---------|------------------------------------------------------|
| success    | boolean | 接口请求是否成功，true: 成功，false: 失败，详细可以参考errCode和errMessage | 
| errCode    | string  | 错误码，如果success的值为false，此处为错误的错误码，否则为null              | 
| errMessage | string  | 错误消息，如果success的值为false，此处为错误的消息，否则为null              | 
| data       | object  | 接口如果需要返回数据，则data字段存在。数据类型参考各个接口结构定义                  | 

# 响应内容示例：
1. 无数据成功返回
```json
{
  "success": true,
  "errCode": null,
  "errMessage": null
}
```

2. 无数据失败返回
```json
{
  "success": false,
  "errCode": "401",
  "errMessage": "Authentication failed"
}
```

3. 有数据成功返回
```json
{
  "success": true,
  "errCode": null,
  "errMessage": null,
  "data": "Hello world!"
}
```

# 接口定义

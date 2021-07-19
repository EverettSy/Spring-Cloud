# RestTemplate 使用记录

### 1. RestTemplate 使用简介

  RestTemplate是HTTP客户端库提供了一个更高水平的API。主要用于Rest服务调用。

  RestTemplate方法：

| 方法组          | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| getForObject    | 通过GET检索表示形式。                                        |
| getForEntity    | ResponseEntity通过使用GET 检索（即状态，标头和正文）。       |
| headForHeaders  | 通过使用HEAD检索资源的所有标头。                             |
| postForLocation | 通过使用POST创建新资源，并Location从响应中返回标头。         |
| postForObject   | 通过使用POST创建新资源，并从响应中返回表示形式。             |
| postForEntity   | 通过使用POST创建新资源，并从响应中返回表示形式。             |
| put             | 通过使用PUT创建或更新资源。                                  |
| patchForObject  | 通过使用PATCH更新资源，并从响应中返回表示形式。请注意，JDK HttpURLConnection不支持PATCH，但是Apache HttpComponents和其他支持。 |
| delete          | 使用DELETE删除指定URI处的资源。                              |
| optionsForAllow | 通过使用ALLOW检索资源的允许的HTTP方法。                      |
| exchange        | 前述方法的通用性强（且意见少的版本），在需要时提供了额外的灵活性。它接受RequestEntity（包括HTTP方法，URL，标头和正文作为输入）并返回ResponseEntity。这些方法允许使用ParameterizedTypeReference而不是Class使用泛型来指定响应类型。 |
| execute         | 执行请求的最通用方法，完全控制通过回调接口进行的请求准备和响应提取。 |

### 2. RestTemplate使用示例

* #### POST请求, 表单参数, application/x-www-form-urlencoded



```java
private TokenDTO getTokenDTO(RestTemplate restTemplate, Business businessAuto) {
    //模拟登陆
    //设置请求头
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    // 封装参数，这里是MultiValueMap
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    map.add("username", businessAuto.getMobile());
    map.add("password", "guotai123456");
    map.add("grant_type", "business_password");
    map.add("client_id", "business");
    map.add("client_secret", "business_secret");
    //发送post请求
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
    //使用exchange请求接口
    ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:9999/gongbaojin-uaa/oauth/token",
            HttpMethod.POST,
            request,
            String.class);
    JSONObject object = JSON.parseObject(response.getBody());
    TokenDTO tokenDTO = JSON.parseObject(object.get("data").toString(),TokenDTO.class);
    return tokenDTO;
}
```


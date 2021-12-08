package com.syrobin.cloud.http;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.collect.Maps;
import com.syrobin.cloud.service.OssTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: OssEndpoint
 * @Description: aws 对外提供服务端点
 * @Author syrobin
 * @Date 2021-09-15 9:41 上午
 * @Version V1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/oss")
public class OssEndpoint {

    private final OssTemplate template;

    @SneakyThrows
    @PostMapping("/bucket/{bucketName}")
    public Bucket createBucket(@PathVariable String bucketName){
        template.createBucket(bucketName);
        return template.getBucket(bucketName).get();
    }

    @SneakyThrows
    @GetMapping("/bucket")
    public List<Bucket> getBuckets(){
        return template.getAllBuckets();
    }

    @SneakyThrows
    @GetMapping("/bucket/{bucketName}")
    public Bucket getBucket(@PathVariable String bucketName){
        return template.getBucket(bucketName).orElseThrow(() -> new IllegalArgumentException("Bucket Name not found!"));
    }


    /**
     *
     * @param object
     * @param bucketName
     * @return
     */
    @SneakyThrows
    @PostMapping("/object/{bucketName}")
    public S3Object createObject(@RequestBody MultipartFile object,@PathVariable String bucketName){
        //获取原始文件名
        String name = object.getOriginalFilename();
        template.putObject(bucketName,name,object.getInputStream(), object.getSize(), object.getContentType());
        return  template.getObjectInfo(bucketName,name);

    }

    @SneakyThrows
    @PostMapping("/object/{bucketName}/{objectName}")
    public S3Object createObject(@RequestBody MultipartFile object,@PathVariable String bucketName,
                                 @PathVariable String objectName){
        template.putObject(bucketName,objectName,object.getInputStream(),object.getSize(),object.getContentType());
        return template.getObjectInfo(bucketName,objectName);
    }

    @SneakyThrows
    @GetMapping("/object/{bucketName}/{objectName}")
    public List<S3ObjectSummary> filterObject(@PathVariable String bucketName,@PathVariable String objectName){
        return template.getAllObjectsByPrefix(bucketName,objectName,true);
    }

    @SneakyThrows
    @GetMapping("/object/{bucketName}/{objectName}/{expires}")
    public Map<String, Object> getObject(@PathVariable String bucketName, @PathVariable String objectName,
                                         @PathVariable Integer expires){
        Map<String, Object> responseBody = Maps.newHashMap();
        // Put Object info
        responseBody.put("bucket",bucketName);
        responseBody.put("object",objectName);
        responseBody.put("url",template.getObjectURL(bucketName,objectName,expires));
        responseBody.put("expires",expires);
        return responseBody;
    }

    @SneakyThrows
    @PostMapping("/highLevelMultipartUpload/{bucketName}/{objectName}")
    public S3Object highLevelMultipartUpload(@RequestBody MultipartFile object,@PathVariable String bucketName,
                                         @PathVariable String objectName){
        template.highLevelMultipartUpload(bucketName,objectName,object.getInputStream(),object.getSize(),object.getContentType());
        return template.getObjectInfo(bucketName,objectName);
    }

    @SneakyThrows
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/object/{bucketName}/{objectName}")
    public void deleteObject(@PathVariable String bucketName,@PathVariable String objectName){
        template.removeObject(bucketName,objectName);
    }
}

package com.syraven.cloud.controller;

import ch.hsr.geohash.GeoHash;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.io.GeohashUtils;
import com.syraven.cloud.bo.UserGeohashBo;
import com.syraven.cloud.domain.*;
import com.syraven.cloud.service.UserGeohashService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2019/11/7 22:14
 */
@Api(tags = "基于位置服务")
@RestController
@RequestMapping("/lbs")
public class LocationBasedController {

    public static final Logger LOGGER = LoggerFactory.getLogger(LocationBasedController.class);

    @Autowired
    private UserGeohashService userGeohashService;

    private SpatialContext spatialContext = SpatialContext.GEO;

    @ApiOperation(value = "添加用户")
    @PostMapping("/addUser")
    public CommonResult<Boolean> add(@RequestBody UserGeohash userGeohash) {
        //默认精度12位
        String geoHashCode = GeohashUtils.encodeLatLon(userGeohash.getLatitude(), userGeohash.getLongitude());
        userGeohashService.save(userGeohash.setGeoCode(geoHashCode).setCreateTime(LocalDateTime.now()));
        return new CommonResult<>("操作成功", 200);
    }

    @ApiOperation(value = "获取附近x米的人")
    @PostMapping(value = "/nearBySearch1")
    public CommonResult<List<UserGeohash>> nearBySearch1(@RequestBody UserGeohashBo userGeohashBo) {
        //1.根据要求的范围，确定geoHash码的精度，获取到当前用户坐标的geoHash码
        String geoHashCode = GeohashUtils.encodeLatLon(userGeohashBo.getUserLat(), userGeohashBo.getUserLng(), userGeohashBo.getLen());
        Wrapper<UserGeohash> queryWrapper = new QueryWrapper<UserGeohash>().lambda()
                .likeRight(UserGeohash::getGeoCode, geoHashCode);
        //2.匹配指定精度的geoHash码
        List<UserGeohash> userGeohashList = userGeohashService.list(queryWrapper);
        //3.过滤超出距离的
        userGeohashList = userGeohashList.stream().filter(a -> getDistance(a.getLongitude(), a.getLatitude(), userGeohashBo.getUserLng(), userGeohashBo.getUserLat()) <= userGeohashBo.getDistance())
                .collect(Collectors.toList());
        return new CommonResult<>(userGeohashList);

    }

    @ApiOperation(value = "获取附近x米的人2")
    @PostMapping(value = "/nearby")
    public CommonResult<List<UserGeohash>> nearBySearch(@RequestBody UserGeohashBo userGeohashBo) {
        //1.根据要求的范围，确定geoHash码的精度，获取到当前用户坐标的geoHash码
        GeoHash geoHash = GeoHash.withCharacterPrecision(userGeohashBo.getUserLat(), userGeohashBo.getUserLng(), userGeohashBo.getLen());
        //获取到用户周边8个方位的geoHash码
        GeoHash[] adjacent = geoHash.getAdjacent();
        QueryWrapper<UserGeohash> queryWrapper = new QueryWrapper<UserGeohash>()
                .likeRight("geo_code", geoHash.toBase32());
        Stream.of(adjacent).forEach(a ->
                queryWrapper.or().likeRight("geo_code", a.toBase32())
        );
        //2.匹配指定精度的geoHash码
        List<UserGeohash> userGeohashList = userGeohashService.list(queryWrapper);
        //3.过滤超出距离的
        userGeohashList = userGeohashList.stream().filter(a -> getDistance(a.getLongitude(), a.getLatitude(), userGeohashBo.getUserLng(), userGeohashBo.getUserLat()) <= userGeohashBo.getDistance())
                .collect(Collectors.toList());
        return new CommonResult<>(userGeohashList);

    }


    /**
     * 球面中，两点间的距离
     *
     * @param longitude 经度1
     * @param latitude  纬度1
     * @param userLng   经度2
     * @param userLat   纬度2
     * @return 返回距离，单位km
     */
    private double getDistance(Double longitude, Double latitude, double userLng, double userLat) {
        return spatialContext.calcDistance(spatialContext.makePoint(userLng, userLat),
                spatialContext.makePoint(longitude, latitude)) * DistanceUtils.DEG_TO_KM;
    }

}
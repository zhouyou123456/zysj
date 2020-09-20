package com.xidian.quwanba.bean;

import java.io.Serializable;
import java.util.Date;

public class RouteBean implements Serializable {
    public String routeId; // 线路id，主键(36位UUID)
    public String routeCode; // 线路编号，唯一，未发布的线路为空；编号规则：商家名称首字母简写+年月日+00001（递增），每日清零
    public int publish; // 线路是否发布标志：0表示未发布（草稿），1表示已发布，2表示已添加进微信客户端展示（不能修改）
    public Date updateTime; //  更新时间
    public String title; // 线路标题
    public String region; // 线路所属地域，可以选择多个国家，以逗号隔开
    public String startPlace; // 出发地
    public String keyword; // 地区或者景点名称关键字（至少2个），多个关键字之间以逗号隔开
    public String label; // 线路标签，最多3个标签，多个标签之间以逗号隔开
    public String feature; // 路线特色介绍
    public String trafficDescription; // 交通说明
    public String costDescription; // 费用说明
    public String visa; // 签注签证
    public String remark; // 补充说明（备注）
    public String imagePath;  // 封面照片路径
    //private List<RouteSchedule> routeSchedules; // 线路行程段落（一对多）
    //private List<RoutePrice> routePrices; // 线路报价（一对多）
    private String routeType; // 线路类型（自有、同行）
    private int consumerNum; // 购买人数
    private int shareNum; //  分享次数
    private User user; // 所属用户

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public int getPublish() {
        return publish;
    }

    public void setPublish(int publish) {
        this.publish = publish;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getTrafficDescription() {
        return trafficDescription;
    }

    public void setTrafficDescription(String trafficDescription) {
        this.trafficDescription = trafficDescription;
    }

    public String getCostDescription() {
        return costDescription;
    }

    public void setCostDescription(String costDescription) {
        this.costDescription = costDescription;
    }

    public String getVisa() {
        return visa;
    }

    public void setVisa(String visa) {
        this.visa = visa;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public int getConsumerNum() {
        return consumerNum;
    }

    public void setConsumerNum(int consumerNum) {
        this.consumerNum = consumerNum;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

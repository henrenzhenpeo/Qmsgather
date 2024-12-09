package com.biel.qmsgather.domain;

public class SizeStatusCount {
    public Integer all;
    public Integer overTime;
    public Integer ng;
        public Integer noData;
    public Integer improper;
    public Integer normal;
    public Integer waitMaterial;
    public SizeStatusCount() {
    }

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    public Integer getOverTime() {
        return overTime;
    }

    public void setOverTime(Integer overTime) {
        this.overTime = overTime;
    }

    public Integer getNg() {
        return ng;
    }

    public void setNg(Integer ng) {
        this.ng = ng;
    }

    public Integer getNoData() {
        return noData;
    }

    public void setNoData(Integer noData) {
        this.noData = noData;
    }

    public Integer getImproper() {
        return improper;
    }

    public void setImproper(Integer improper) {
        this.improper = improper;
    }

    public Integer getNormal() {
        return normal;
    }

    public void setNormal(Integer normal) {
        this.normal = normal;
    }

    public Integer getWaitMaterial() {
        return waitMaterial;
    }

    public void setWaitMaterial(Integer waitMaterial) {
        this.waitMaterial = waitMaterial;
    }
}

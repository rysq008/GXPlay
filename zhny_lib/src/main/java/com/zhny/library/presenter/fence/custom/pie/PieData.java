package com.zhny.library.presenter.fence.custom.pie;


public class PieData {
    private int menuType;
    private String name;
    //设置权重，默认为1
    private float weight = 1;
    private float angle = 0;
    //内切bitmap的最大宽/高
    private double max_drawable_size;

    public PieData() {
    }

    public PieData(int menuType, String name, float weight, int drawableId) {
        this.menuType = menuType;
        this.name = name;
        this.weight = weight;
        this.drawableId = drawableId;
    }

    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }

    public double getMax_drawable_size() {
        return max_drawable_size;
    }

    public void setMax_drawable_size(double max_drawable_size) {
        this.max_drawable_size = max_drawable_size;
    }

    private int drawableId;

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }


    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }


}

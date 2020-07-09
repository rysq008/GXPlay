package com.zhny.library.presenter.driver;

public class DriverConstants {

    public static final String Bundle_Driver_Dto = "DriverDto";

    public static final String Intent_Worker_Name = "workerName";

    public static final String Intent_Phone_Number = "phoneName";

    public static final String Intent_Remark = "remark";

    public static final int Request_Code_DriverDetailsActivity = 0;

    public static final int Result_Code_EditDriverActivity = 1;

    //saveDriver json
    public static final String Json_objectVersionNumber = "objectVersionNumber";
    public static final String Json_isDel = "isDel";
    public static final String Json_isEnable = "isEnable";
    public static final String Json_organizationId = "organizationId";

    public static final String Json_phoneNumber = "phoneNumber";
    public static final String Json_remark = "remark";
    public static final String Json_tenantId = "tenantId";
    public static final String Json_type = "type";


    public static final String Json_workerCode = "workerCode";
    public static final String Json_workerId = "workerId";
    public static final String Json_workerName = "workerName";

    //添加：0 编辑：1 删除：2
    public static final int Type_Add = 0;
    public static final int Type_Edit = 1;
    public static final int Type_Delete = 2;

    //isDel 0:不删除 1：删除
    public static final int IsDel_No = 0;
    public static final int IsDel_Yes = 1;

    //isEnable 0:可用 1：不可用
    public static final int IsEnable_Yes = 0;
    public static final int IsEnable_No = 1;
}

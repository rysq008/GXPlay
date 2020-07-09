package com.zhny.library.presenter.operate.helper;

import com.zhny.library.R;

/**
 * created by liming
 */
public enum OperatorEnum {

    OPERATOR_STATISTICS(1, R.drawable.icon_data_statistics, R.string.operator_statistics),
    OPERATOR_TRACK_PLAYBACK(2, R.drawable.icon_replaying, R.string.operator_track_playback),
    OPERATOR_OPERATION_QUALITY(3, R.drawable.icon_working_quality, R.string.operator_operation_quality),
    OPERATOR_ELECTRIC_FENCE(4, R.drawable.icon_electronic_fence, R.string.operator_electric_fence),
    OPERATOR_MACHINE_LIST(5, R.drawable.icon_machines_list, R.string.operator_machine_list),
    OPERATOR_MY_PILOT(6, R.drawable.icon_machinist_list, R.string.operator_my_pilot),
    ;

    public int id;

    public int imgResId;

    public int itemNameId;

    OperatorEnum(int id, int imgResId, int itemNameId) {
        this.id = id;
        this.imgResId = imgResId;
        this.itemNameId = itemNameId;
    }

}

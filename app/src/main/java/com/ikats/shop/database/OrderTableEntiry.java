package com.ikats.shop.database;

import com.ikats.shop.model.BaseModel.XBaseModel;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;

/**
 * Data 类字段可用的注解
 *
 * @Index 如果这个字段经常用来查询，加上 @Index 可以提高查询速度。
 * @Transient 如果字段不需要储存到数据库。
 * @NameInDb 指定字段储存在数据库中的名称。
 * @Id(assignable = true)
 * 手动分配 ID。
 * @Relation 一对一或一对多关系
 * <p>
 * <p>
 * 事务处理
 * <p>
 * <p>
 * <p>
 * API
 * 说明
 * <p>
 * <p>
 * <p>
 * <p>
 * runInTx
 * 在给定的 runnable 中运行的事务。
 * <p>
 * <p>
 * runInReadTx
 * 只读事务，不同于 runInTx，允许并发读取
 * <p>
 * <p>
 * runInTxAsync
 * 运行在一个单独的线程中执行，执行完成后，返回 callback。
 * <p>
 * <p>
 * <p>
 * callInTx    与 runInTx 相似，不同的是可以有返回值。
 * 例子：
 * boxStore.runInTx(new Runnable() {
 * @Override public void run() {
 * for(User user: allUsers) {
 * if(modify(user)) box.put(user);
 * else box.remove(user);
 * }
 * }
 * });
 * <p>
 * <p>
 * 数据库升级
 * <p>
 * 在要修改的字段上添加 @Uid 注解
 * Make Project
 * 修改字段名称
 * <p>
 * 作者：Obadiah
 * 链接：https://www.jianshu.com/p/5a64671c743e
 * 來源：简书
 * 简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
 */
@Entity
public final class OrderTableEntiry extends XBaseModel {

    @Id
    @NameInDb("_ID")
    public long _id;

    public String sell_id;
    public String _order_id;
    public String startTime;
    public String endTime;
    public Date startDate;
    public Date endDate;
    public int streamType;
    public String ip;
    public int prot;
    public String username;
    public String password;
    public int channel;
    public String outBizNo;
    public String payid;
    public int count;
    public String purchaser;//购买人
    public String amount;
    public String phone;
    public String status;
    public String action;
    public String crateTime;

    public String record_ip;
    public String record_prot;
    public String record_channel;

}

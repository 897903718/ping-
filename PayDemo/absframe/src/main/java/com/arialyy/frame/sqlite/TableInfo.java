package com.arialyy.frame.sqlite;

/**
 * Created by AriaLyy on 2015/2/13.
 * 存到数据库里面的表的信息
 */
public class TableInfo {
    private String tableName;
    private boolean isExist;    //表是否存在
    private String belongToDbName;  //所属数据库名

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean isExist) {
        this.isExist = isExist;
    }

    public String getBelongToDbName() {
        return belongToDbName;
    }

    public void setBelongToDbName(String belongToDbName) {
        this.belongToDbName = belongToDbName;
    }
}

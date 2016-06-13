package com.arialyy.frame.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arialyy.frame.annotation.sqlite.Id;
import com.arialyy.frame.annotation.sqlite.Property;
import com.arialyy.frame.annotation.sqlite.Table;
import com.arialyy.frame.application.AbsApplication;
import com.arialyy.frame.util.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by AriaLyy on 2015/2/11.
 * 数据库对象实体，如果要使用ORM,则其实体需要继承该对象
 * 使用：
 * 1，创建表-->new 无参的构造函数
 */
public abstract class AbsDbEntity {
    protected static String TAG = "";
//    protected String mTableName = "";
    protected int mId;

    /**
     * 无参的构造函数表示创建表,子类继承时需要实现无参的构造函数
     */
    public AbsDbEntity() {
        SQLiteDatabase db = AbsApplication.getDbHelper().getWritableDatabase();
        Table table = getClass().getAnnotation(Table.class);
        String tableName = table.name();
        TAG = StringUtil.getClassName(this);
        checkTable(this, tableName, db);
    }

    /**
     * 检查表是否存在，不存在则创建表
     * @param obj
     * @param tableName
     * @param db
     */
    private static void checkTable(Object obj, String tableName, SQLiteDatabase db){
        if (AbsApplication.getInstance().getTablesInfo().get(tableName) == null) {
            DbUtil.createTable(obj, db);
            AbsApplication.getInstance().getTablesInfo().put(tableName, new TableInfo());
            db.close();
        }
    }

    /**
     * 检查表是否存在，不存在则创建表
     * @param clazz
     * @param tableName
     */
    private static <T extends AbsDbEntity> void checkTable(Class<T> clazz, String tableName){
        if (AbsApplication.getInstance().getTablesInfo().get(tableName) == null) {
            try {
                //如果表不存在，则new一个
                clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            AbsApplication.getInstance().getTablesInfo().put(tableName, new TableInfo());
        }
    }

    /**
     * 更新数据
     */
    public void update(){
        SQLiteDatabase db = AbsApplication.getDbHelper().getWritableDatabase();
        DbUtil.update(db, this);
    }

    /**
     * 查找所有数据
     *
     * @param tableClazz 需要查找的表实体
     * @param <T>
     * @return
     */
    public static <T extends AbsDbEntity> List<T> findAllData(Class<T> tableClazz) {
        Table table = tableClazz.getAnnotation(Table.class);
        checkTable(tableClazz, table.name());
        SQLiteDatabase db = AbsApplication.getDbHelper().getReadableDatabase();
        Cursor c = DbUtil.findAllData(db, table.name());
        return c.getCount() > 0 ? newInstanceEntity(tableClazz, c, db) : null;
    }

    /**
     * 通过字段参数进行条件查找数据,如果没有查找到数据，返回null
     *
     * @param tableClazz  需要查找的表实体
     * @param columnNames 表的字段名
     * @param values      字段条件值
     * @param <T>
     * @return
     */
    public static <T extends AbsDbEntity> List<T> findDataByField(Class<T> tableClazz, String[] columnNames, String[] values) {
        Table table = tableClazz.getAnnotation(Table.class);
        checkTable(tableClazz, table.name());
        SQLiteDatabase db = AbsApplication.getDbHelper().getReadableDatabase();
        Cursor c = DbUtil.findDataByField(db, table.name(), columnNames, values);
        return c.getCount() > 0 ? newInstanceEntity(tableClazz, c, db) : null;
    }

    /**
     * 通过Id查找数据,如果没有查找到数据，返回null
     *
     * @param tableClazz 需要查找的表实体
     * @param id         查找的id
     * @param <T>        实体模板
     * @return 表实体
     */
    public static <T extends AbsDbEntity> T findDataById(Class<T> tableClazz, int id) {
        Table table = tableClazz.getAnnotation(Table.class);
        checkTable(tableClazz, table.name());
        SQLiteDatabase db = AbsApplication.getDbHelper().getReadableDatabase();
        Cursor c = DbUtil.findDataById(db, tableClazz, id);
        return c.getCount() > 0 ? newInstanceEntity(tableClazz, c, db).get(0) : null;
    }

    /**
     * 通过Cursor的数据重建一个新的表实体
     *
     * @param tableClazz
     * @param c
     * @param <T>
     * @param db         把数据库传进去的唯一目的就是关闭数据库
     * @return
     */
    private static <T extends AbsDbEntity> List<T> newInstanceEntity(Class<T> tableClazz, Cursor c, SQLiteDatabase db) {
        try {
            List<T> entitys = new ArrayList<>();
            Id idColumn = tableClazz.getAnnotation(Id.class);
            while (c.moveToNext()) {
                T entity = tableClazz.newInstance();
                Field[] fields = entity.getClass().getDeclaredFields();
                //设置所有实体所有注解的字段的值
                for (Field f : fields) {
                    f.setAccessible(true);
                    Property property = f.getAnnotation(Property.class);
                    if(property == null){
                        continue;
                    }
                    Class<?> type = f.getType();
                    int column = c.getColumnIndex(property.column());
                    if (type == String.class) {
                        f.set(entity, c.getString(column));
                    } else if (type == int.class || type == Integer.class) {
                        f.setInt(entity, c.getInt(column));
                    } else if (type == float.class || type == Float.class) {
                        f.setFloat(entity, c.getFloat(column));
                    } else if (type == double.class || type == Double.class) {
                        f.setDouble(entity, c.getDouble(column));
                    } else if (type == long.class || type == Long.class) {
                        f.setLong(entity, c.getLong(column));
                    } else if (type == boolean.class || type == Boolean.class) {
                        f.setBoolean(entity, !c.getString(column).equalsIgnoreCase("false"));
                    } else if (type == java.util.Date.class || type == java.sql.Date.class) {
                        f.set(entity, new Date(c.getString(column)));
                    } else {
                        f.set(entity, c.getBlob(column));
                    }
                }
                entity.mId = c.getInt(c.getColumnIndex(idColumn == null ? "id" : idColumn.column()));
                entitys.add(entity);
            }
            return entitys;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            c.close();
            if (db != null)
                db.close();
        }
        return null;
    }

    /**
     * 插入数据
     */
    public synchronized void insert(){
        SQLiteDatabase db = AbsApplication.getDbHelper().getWritableDatabase();
        DbUtil.insertData(db, this);
    }

    /**
     * 删除数据
     */
    public void delete() {
        SQLiteDatabase db = AbsApplication.getDbHelper().getWritableDatabase();
        DbUtil.deleteData(db, this);
    }

    /**
     * 获取主键Id
     *
     * @return
     */
    public int getId() {
        return mId;
    }

}

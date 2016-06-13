package com.arialyy.frame.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.arialyy.frame.annotation.sqlite.Id;
import com.arialyy.frame.annotation.sqlite.Property;
import com.arialyy.frame.annotation.sqlite.Table;
import com.arialyy.frame.application.AbsApplication;

/**
 * Created by AriaLyy on 2015/2/11.
 * 数据库操作工具
 */
public class DbUtil {
    private static final String TAG = "DbUtil";

    /**
     * 更新表数据
     * @param db
     * @param entity        表实体
     */
    protected static void update(SQLiteDatabase db, AbsDbEntity entity){
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Table table = clazz.getAnnotation(Table.class);
        Id id = clazz.getAnnotation(Id.class);
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(table.name()).append(" SET ");
        int i = 0;
        for(Field field : fields){
            field.setAccessible(true);
            Property property = field.getAnnotation(Property.class);
            if(property == null){
                continue;
            }
            sql.append(i > 0 ? "," : "");
            try {
                sql.append(property.column()).append(" = '").append(field.get(entity).toString()).append("'");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            i++;
        }
        sql.append(" WHERE ").append(id == null ? "Id" : id.column());
        sql.append(" = '").append(entity.getId()).append("'");
        exeSql(db, sql.toString());
        db.close();
    }

    /**
     * 插入数据
     * @param db
     * @param entity
     */
    protected static void insertData(SQLiteDatabase db, AbsDbEntity entity){
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Table table = clazz.getAnnotation(Table.class);
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(table.name()).append("(");
        int i = 0;
        for(Field field : fields){
            field.setAccessible(true);
            Property property = field.getAnnotation(Property.class);
            if(property == null){
                continue;
            }
            sql.append(i > 0 ? "," : "");
            sql.append(property.column());
            i++;
        }
        sql.append(") VALUES (");
        i = 0;
        for(Field field : fields){
            field.setAccessible(true);
            Property property = field.getAnnotation(Property.class);
            if(property == null){
                continue;
            }
            sql.append(i > 0 ? "," : "");
            try {
                sql.append("'").append(field.get(entity).toString()).append("'");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            i++;
        }
        sql.append(")");
        exeSql(db, sql.toString());
    }

    /**
     * 查找数据库里面某张表的所有数据
     *
     * @param db
     * @param tableName 表名
     * @return          Cursor
     */
    protected static Cursor findAllData(SQLiteDatabase db, String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableName);
        debugSql(sql.toString());
        return db.rawQuery(sql.toString(), null);
    }

    /**
     * 通过表的字段参数来查找数据
     *
     * @param db          需要查找的数据库
     * @param tableName   表名
     * @param columnNames 字段名
     * @param values      字段参数
     * @return Cursor
     */
    protected static Cursor findDataByField(SQLiteDatabase db, String tableName, String[] columnNames, String[] values) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableName).append(" WHERE ");
        if (columnNames.length != values.length) {
            Log.e(TAG, "字段参数和值的个数不一致");
            return null;
        }
        int i = 0;
        for (String columnName : columnNames) {
            sql.append(i > 0 ? " AND " : "");
            sql.append(columnName).append(" = '").append(values[i]).append("'");
            i++;
        }
        debugSql(sql.toString());
        return db.rawQuery(sql.toString(), null);
    }

    /**
     * 通过Id查找数据
     *
     * @param db
     * @param tableClazz 表实体的反射
     * @param id
     * @return
     */
    protected static <T extends AbsDbEntity> Cursor findDataById(SQLiteDatabase db, Class<T> tableClazz, int id) {
        StringBuilder sql = new StringBuilder();
        Table column = tableClazz.getAnnotation(Table.class);
        Id idColumn = tableClazz.getAnnotation(Id.class);
        sql.append("SELECT * FROM ").append(column.name());
        sql.append(" WHERE ").append(idColumn == null ? "Id" : idColumn.column());
        sql.append(" = '").append(id).append("'");
        debugSql(sql.toString());
        return db.rawQuery(sql.toString(), null);
    }

    /**
     * 根据实体删除数据
     *
     * @param entity
     */
    protected static void deleteData(SQLiteDatabase db, AbsDbEntity entity) {
        Table tableName = entity.getClass().getAnnotation(Table.class);
        Id id = entity.getClass().getAnnotation(Id.class);
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(tableName.name()).append(" WHERE ").append(id == null ? "Id" : id.column())
                .append("='").append(entity.getId()).append("'");
        exeSql(db, sql.toString());
        db.close();
    }

    /**
     * 创建表
     *
     * @param obj
     * @param db
     */
    protected static void createTable(Object obj, SQLiteDatabase db) {
        exeSql(db, createTable(obj));
        db.close();
    }

    /**
     * 构建创建表的语句
     *
     * @param obj 表的实体
     * @return 返回创建表的语句
     */
    private static String createTable(Object obj) {
        StringBuffer sql = new StringBuffer();
        try {
            Table tableName = obj.getClass().getAnnotation(Table.class);
            Id id = obj.getClass().getAnnotation(Id.class);
            sql.append("CREATE TABLE " + tableName.name() + "(");
            sql.append(id == null ? "Id " : id.column() + " ");
            sql.append("INTEGER PRIMARY KEY,");
            //获取父类申明的所有字段
            Field[] fields = obj.getClass().getDeclaredFields();
            int i = 0;
            for (Field field : fields) {
                Property property = field.getAnnotation(Property.class);
                if(property == null){
                    continue;
                }
                String type = getFieldType(field);
                String str = property.column() + " " + type + " ";  //添加字段
                sql.append(i > 0 ? "," : "");
                sql.append(str);
                sql.append(property.canBeNull() ? "" : "NOT NULL ");//添加判空
                sql.append(property.isUnique() ? "UNIQUE " : "");   //添加唯一标识
//                sql.append(TextUtils.isEmpty(property.defaultValue()) ? "" : )
                i++;
            }
            sql.append(")");
        } catch (NullPointerException e) {
            Log.e("TAG", "没有注解表名");
        }
        return sql.toString();
    }

    /**
     * 删除表
     *
     * @param obj 表实体
     * @return
     */
    protected static String dropTable(Object obj) {
        Table tableName = obj.getClass().getAnnotation(Table.class);
        return "DROP TABLE " + tableName;
    }

    /**
     * 获取字段的属性
     *
     * @param field 字段
     * @return
     */
    private static String getFieldType(Field field) {
        Class<?> type = field.getType();
        if (type == String.class) {
            return "VARCHAR";
        } else if (type == int.class || type == Integer.class) {
            return "INTEGER";
        } else if (type == float.class || type == Float.class) {
            return "FLOAT";
        } else if (type == double.class || type == Double.class) {
            return "DOUBLE";
        } else if (type == long.class || type == Long.class) {
            return "INTERGER";
        } else if (type == boolean.class || type == Boolean.class) {
            return "VARCHAR";
        } else if (type == java.util.Date.class || type == java.sql.Date.class) {
            return "DATE";
        } else {
            return "BLOB";
        }
    }

    /**
     * 检查表是否存在
     *
     * @param db        需要判断的数据库
     * @param tableName 表名
     * @return
     */
    public static boolean tableIsExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = null;
        try {
            String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='"
                    + tableName + "' ";
//            debugSql(sql);
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    /**
     * 获取数据库里面的所有名
     *
     * @param db
     * @return
     */
    public static Set<String> getAllTableName(SQLiteDatabase db) {
        Set<String> tables = new HashSet<>();
        String sql = "SELECT * FROM sqlite_master WHERE type ='table' ORDER BY name";
//        debugSql(sql);
        Cursor cursor = null;
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            tables.add(cursor.getString(cursor.getColumnIndex("name")));
        }
        return tables;
    }

    /**
     * 执行SQL语句
     *
     * @param db  数据库
     * @param sql
     */
    protected static void exeSql(SQLiteDatabase db, String sql) {
        debugSql(sql);
        db.execSQL(sql);
        db.close();
    }

    /**
     * sql语句debug
     *
     * @param sql
     */
    private static void debugSql(String sql) {
        if (AbsApplication.getInstance().isSqlDebug())
            android.util.Log.d("Debug SQL", ">>>>>>  " + sql);
    }

    /**
     * 关闭数据库
     *
     * @param db
     */
    public static void closeDb(SQLiteDatabase db) {
        if (db != null) {
            db.close();
        }
    }

}

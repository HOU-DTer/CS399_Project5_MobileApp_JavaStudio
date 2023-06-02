package com.am.aucklank;

public class TestTable {


    /**
     * Test Record Form (测试记录表)
     */
    //表的名字
    public static final String NAME_TABLE_TEST = "table_text_list";
    //The following are the field names of the table(以下为表的字段名字)
    public static final String NAME_COLUMN_TEST_ID = "id"; // 自增ID
    public static final String NAME_COLUMN_TEST_NAME = "test_name";
    public static final String NAME_COLUMN_TEST_AGE = "test_age";

    /**
     * 创建表
     */
    public static final String SQL_CREATE_TABLE_TEST = "create table IF NOT EXISTS " + NAME_TABLE_TEST + "("
            + NAME_COLUMN_TEST_ID + " integer primary key autoincrement, "
            + NAME_COLUMN_TEST_NAME + " varchar(50) not null, "
            + NAME_COLUMN_TEST_AGE + " long default 0); ";

}

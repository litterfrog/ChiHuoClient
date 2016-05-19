package com.fxp.constants;

import android.net.Uri;

public class ProviderConstant {

    //db name table name and version
    public static final String DBNAME = "chihuodb";
    public static final String TFOOD = "food";
    public static final String TUSERINFO = "userinfo";
    public static final String TACCOUNT = "account";
    public static final String TCOMMENT = "comment";
    public static final String TLIKE = "like";
    public static final String TVISITED = "visited";
    public static final int VERSION = 1;

    //table fields
    public static String TFOOD_ID = "tid";
    public static final String TFOOD_NAME = "name";
    public static final String TFOOD_SUMMARY = "summary";
    public static final String TFOOD_PHONE = "phone";
    //ͬһ���̼ҵ�ͼƬ������ͬһ��Ŀ¼����ļ�����1��2��3...userid_123...
    public static final String TFOOD_PICTRUE_PATH = "pictruepath";
    public static final String TFOOD_ADDRESS="address";
    public static final String TFOOD_LABEL="label";

    public static String TUSERINFO_ID = "tid";
    public static final String TUSERINFO_NAME = "name";
    public static final String TUSERINFO_SEX = "sex";
    public static final String TUSERINFO_PHONE = "phone";
    public static final String TUSERINFO_ACCID = "accid";
    public static final String TUSERINFO_ADDRESS="address";
    public static final int ISMALE = 1;
    public static final int ISFEMALE = 0;

    public static String TACCOUNT_ID = "tid";
    public static final String TACCOUNT_EMAIL = "email";
    public static final String TACCOUNT_PASSWORD = "password";

    public static String TCOMMENT_ID = "tid";
    public static final String TCOMMENT_ACCID = "accid";
    public static final String TCOMMENT_FOODID = "foodid";
    public static final String TCOMMENT_COMMENT = "comment";
    public static final String TCOMMENT_TIME = "time";

    public static String TLIKE_ID = "tid";
    public static final String TLIKE_ACCID = "accid";
    public static final String TLIKE_FOODID = "foodid";

    public static final String TVISITED_ID = "tid";
    public static final String TVISITED_ACCID = "accid";
    public static final String TVISITED_FOODID = "foodid";

    //provider autohority and matcher id
    public static final String AUTOHORITY = "com.fxp.chihuo";
    public static final int ITEM = 1;
    public static final int ITEM_TABLE = 2;
    //content type
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.fxp.chihuo";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.fxp.chihuo";
    //content uri
    public static final Uri FOOD_URI = Uri.parse("content://" + AUTOHORITY + "/food");
    public static final Uri USER_URI = Uri.parse("content://" + AUTOHORITY + "/userinfo");
    public static final Uri ACCOUNT_URI = Uri.parse("content://" + AUTOHORITY + "/account");
    public static final Uri COMMENT_URI = Uri.parse("content://" + AUTOHORITY + "/comment");
    public static final Uri LIKE_URI = Uri.parse("content://" + AUTOHORITY + "/like");
    public static final Uri VISITED_URI = Uri.parse("content://" + AUTOHORITY + "/visited");

}

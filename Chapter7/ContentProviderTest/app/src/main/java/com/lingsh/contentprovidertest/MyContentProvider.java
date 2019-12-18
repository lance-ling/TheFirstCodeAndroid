package com.lingsh.contentprovidertest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    private static final String TAG = "MyContentProvider";

    /**
     * 标识Book表
     */
    public static final int BOOK_DIR = 0;
    /**
     * 标识Book表中数据
     */
    public static final int BOOK_ITEM = 1;
    /**
     * 标识Category表
     */
    public static final int CATEGORY_DIR = 2;
    /**
     * 标识Category表中数据
     */
    public static final int CATEGORY_ITEM = 3;

    public static final String AUTHORITY = "com.lingsh.contentprovidertest.provider";
    /**
     * 表名
     */
    public static final String TBL_BOOK = "Book";
    public static final String TBL_CATEGORY = "Category";

    private static final String CONTENT_BOOK_STRING = String.format("content://%s/%s/", AUTHORITY, TBL_BOOK.toLowerCase());
    private static final String CONTENT_CATEGORY_STRING = String.format("content://%s/%s/", AUTHORITY, TBL_CATEGORY.toLowerCase());

    public static final String ID_SELECTION = "id = ?";

    /**
     * MIME类型 表/数据
     */
    private static final String MIME_BOOK_DIR = "vnd.android.cursor.dir/vnd.com.lingsh.contentprovidertest.provider.book";
    private static final String MIME_BOOK_ITEM = "vnd.android.cursor.item/vnd.com.lingsh.contentprovidertest.provider.book";
    private static final String MIME_CATEGORY_DIR = "vnd.android.cursor.dir/vnd.com.lingsh.contentprovidertest.provider.category";
    private static final String MIME_CATEGORY_ITEM = "vnd.android.cursor.item/vnd.com.lingsh.contentprovidertest.provider.category";

    private static UriMatcher uriMatcher;

    private MyDatabaseHelper mHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        // 创建数据库BookStore
        Log.d(TAG, "onCreate: ");

        mHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // 查询数据
        Log.d(TAG, "query: ");

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = db.query(TBL_BOOK, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query(TBL_BOOK, projection, ID_SELECTION, new String[]{bookId}, null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query(TBL_CATEGORY, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query(TBL_CATEGORY, projection, ID_SELECTION, new String[]{categoryId}, null, null, sortOrder);
                break;
            default:
                break;
        }

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // 插入数据
        Log.d(TAG, "insert: ");

        SQLiteDatabase db = mHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert(TBL_BOOK, null, values);
                uriReturn = Uri.parse(CONTENT_BOOK_STRING + newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert(TBL_CATEGORY, null, values);
                uriReturn = Uri.parse(CONTENT_CATEGORY_STRING + newCategoryId);
                break;
            default:
                break;
        }

        return uriReturn;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // 更新数据
        Log.d(TAG, "update: ");

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updatedRows = db.update(TBL_BOOK, values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updatedRows = db.update(TBL_BOOK, values, ID_SELECTION, new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updatedRows = db.update(TBL_CATEGORY, values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updatedRows = db.update(TBL_CATEGORY, values, ID_SELECTION, new String[]{categoryId});
                break;
            default:
                break;
        }

        return updatedRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // 删除数据
        Log.d(TAG, "delete: ");

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int deleteRows = 0;

        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deleteRows = db.delete(TBL_BOOK, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRows = db.delete(TBL_BOOK, ID_SELECTION, new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows = db.delete(TBL_CATEGORY, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRows = db.delete(TBL_CATEGORY, ID_SELECTION, new String[]{categoryId});
                break;
            default:
                break;
        }

        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "getType: ");

        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return MIME_BOOK_DIR;
            case BOOK_ITEM:
                return MIME_BOOK_ITEM;
            case CATEGORY_DIR:
                return MIME_CATEGORY_DIR;
            case CATEGORY_ITEM:
                return MIME_CATEGORY_ITEM;
        }
        return null;
    }
}

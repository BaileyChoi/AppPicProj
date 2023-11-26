package com.Apic.apic.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TodoDao_Impl implements TodoDao {
    private final RoomDatabase __db;

    private final EntityInsertionAdapter<Todo> __insertionAdapterOfTodo;

    private final EntityDeletionOrUpdateAdapter<Todo> __deletionAdapterOfTodo;

    public TodoDao_Impl(@NonNull final RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfTodo = new EntityInsertionAdapter<Todo>(__db) {
            @Override
            @NonNull
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `Todo` (`title`,`date`,`id`) VALUES (?,?,nullif(?, 0))";
            }

            @Override
            protected void bind(@NonNull final SupportSQLiteStatement statement,
                                @Nullable final Todo entity) {
                if (entity.getTitle() == null) {
                    statement.bindNull(1);
                } else {
                    statement.bindString(1, entity.getTitle());
                }
                statement.bindLong(2, entity.getDate());
                statement.bindLong(3, entity.getId());
            }
        };
        this.__deletionAdapterOfTodo = new EntityDeletionOrUpdateAdapter<Todo>(__db) {
            @Override
            @NonNull
            protected String createQuery() {
                return "DELETE FROM `Todo` WHERE `id` = ?";
            }

            @Override
            protected void bind(@NonNull final SupportSQLiteStatement statement,
                                @Nullable final Todo entity) {
                statement.bindLong(1, entity.getId());
            }
        };
    }

    @Override
    public Object insert(final Todo entity, final Continuation<? super Unit> continuation) {
        return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
            @Override
            @NonNull
            public Unit call() throws Exception {
                __db.beginTransaction();
                try {
                    __insertionAdapterOfTodo.insert(entity);
                    __db.setTransactionSuccessful();
                    return Unit.INSTANCE;
                } finally {
                    __db.endTransaction();
                }
            }
        }, continuation);
    }

    @Override
    public Object delete(final Todo entity, final Continuation<? super Unit> continuation) {
        return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
            @Override
            @NonNull
            public Unit call() throws Exception {
                __db.beginTransaction();
                try {
                    __deletionAdapterOfTodo.handle(entity);
                    __db.setTransactionSuccessful();
                    return Unit.INSTANCE;
                } finally {
                    __db.endTransaction();
                }
            }
        }, continuation);
    }

    @Override
    public Flow<List<Todo>> getAll() {
        final String _sql = "SELECT * FROM todo WHERE date";
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
        return CoroutinesRoom.createFlow(__db, false, new String[] {"todo"}, new Callable<List<Todo>>() {
            @Override
            @NonNull
            public List<Todo> call() throws Exception {
                final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
                try {
                    final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
                    final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
                    final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    final List<Todo> _result = new ArrayList<Todo>(_cursor.getCount());
                    while (_cursor.moveToNext()) {
                        final Todo _item;
                        final String _tmpTitle;
                        if (_cursor.isNull(_cursorIndexOfTitle)) {
                            _tmpTitle = null;
                        } else {
                            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
                        }
                        final long _tmpDate;
                        _tmpDate = _cursor.getLong(_cursorIndexOfDate);
                        _item = new Todo(_tmpTitle,_tmpDate);
                        final long _tmpId;
                        _tmpId = _cursor.getLong(_cursorIndexOfId);
                        _item.setId(_tmpId);
                        _result.add(_item);
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            @Override
            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override
    public List<Todo> getByDate(final long selectedDate) {
        final String _sql = "SELECT * FROM todo WHERE date = ? ORDER BY date DESC";
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
        int _argIndex = 1;
        _statement.bindLong(_argIndex, selectedDate);
        __db.assertNotSuspendingTransaction();
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final List<Todo> _result = new ArrayList<Todo>(_cursor.getCount());
            while (_cursor.moveToNext()) {
                final Todo _item;
                final String _tmpTitle;
                if (_cursor.isNull(_cursorIndexOfTitle)) {
                    _tmpTitle = null;
                } else {
                    _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
                }
                final long _tmpDate;
                _tmpDate = _cursor.getLong(_cursorIndexOfDate);
                _item = new Todo(_tmpTitle,_tmpDate);
                final long _tmpId;
                _tmpId = _cursor.getLong(_cursorIndexOfId);
                _item.setId(_tmpId);
                _result.add(_item);
            }
            return _result;
        } finally {
            _cursor.close();
            _statement.release();
        }
    }

    @Override
    public Flow<List<Todo>> getTodosByDate(final long selectedDate) {
        final String _sql = "SELECT * FROM todo WHERE date = ? ORDER BY date DESC";
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
        int _argIndex = 1;
        _statement.bindLong(_argIndex, selectedDate);
        return CoroutinesRoom.createFlow(__db, false, new String[] {"todo"}, new Callable<List<Todo>>() {
            @Override
            @NonNull
            public List<Todo> call() throws Exception {
                final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
                try {
                    final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
                    final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
                    final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    final List<Todo> _result = new ArrayList<Todo>(_cursor.getCount());
                    while (_cursor.moveToNext()) {
                        final Todo _item;
                        final String _tmpTitle;
                        if (_cursor.isNull(_cursorIndexOfTitle)) {
                            _tmpTitle = null;
                        } else {
                            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
                        }
                        final long _tmpDate;
                        _tmpDate = _cursor.getLong(_cursorIndexOfDate);
                        _item = new Todo(_tmpTitle,_tmpDate);
                        final long _tmpId;
                        _tmpId = _cursor.getLong(_cursorIndexOfId);
                        _item.setId(_tmpId);
                        _result.add(_item);
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            @Override
            protected void finalize() {
                _statement.release();
            }
        });
    }

    @NonNull
    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}

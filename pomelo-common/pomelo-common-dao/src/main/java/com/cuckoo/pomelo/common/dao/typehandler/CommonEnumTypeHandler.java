package com.cuckoo.pomelo.common.dao.typehandler;

import com.cuckoo.pomelo.facade.enums.IEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CommonEnumTypeHandler<E extends Enum<E> & IEnum<T>, T> extends BaseTypeHandler<E> {

    final private Class<E> type;

    public CommonEnumTypeHandler(Class<E> type) {
        super();
        this.type = type;
    }


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.getCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Optional.ofNullable(rs.getObject(columnName)).map(this::getEnum).orElse(null);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Optional.ofNullable(rs.getObject(columnIndex)).map(this::getEnum).orElse(null);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Optional.ofNullable(cs.getObject(columnIndex)).map(this::getEnum).orElse(null);
    }

    @SuppressWarnings("unchecked")
    public E getEnum(Object o) {
        return IEnum.of(type, (T) o);
    }
}

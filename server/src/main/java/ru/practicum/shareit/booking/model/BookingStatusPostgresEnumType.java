package ru.practicum.shareit.booking.model;


import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;


public class BookingStatusPostgresEnumType extends EnumType<BookingStatus> {

    @Override
    public void nullSafeSet(PreparedStatement bookingPreparedStatement, Object value, int index,
                            SharedSessionContractImplementor session) throws HibernateException, SQLException {
        bookingPreparedStatement.setObject(index, value != null ? ((Enum<?>) value).name() : null, Types.OTHER);
    }
}

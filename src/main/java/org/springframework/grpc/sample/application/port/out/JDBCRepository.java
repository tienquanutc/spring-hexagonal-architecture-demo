package org.springframework.grpc.sample.application.port.out;

import java.util.Collection;
import java.util.Map;

public interface JDBCRepository {
    <ID> int updateOne(Class<?> entityClass, ID id, Map<String, Object> updates);

    <ID> int updateMany(Class<?> entityClass, Collection<ID> ids, Map<String, Object> updates);
}

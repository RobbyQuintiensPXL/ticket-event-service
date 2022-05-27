package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.model.QLocation;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationPageRepository extends PagingAndSortingRepository<Location, Long>,
        QuerydslPredicateExecutor<Location>,
        QuerydslBinderCustomizer<QLocation> {

    @Override
    default void customize(QuerydslBindings bindings, QLocation root) {
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::eq);
    }
}
